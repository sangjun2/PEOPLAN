package com.sang.peoplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateBusinessCardActivity extends AppCompatActivity {

    public Context mContext;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    ImageView myPicture;
    EditText name;
    EditText department;
    TextView phoneNumber;
    TextView emailAddress;
    EditText address;
    Button confirm;
    Button remove;

    Boolean modified;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_business_card);

        myPicture = findViewById(R.id.my_picture);
        confirm = findViewById(R.id.confirm_toolbar_bt);
        remove = findViewById(R.id.remove_toolbar_bt);
        name = findViewById(R.id.name);
        department = findViewById(R.id.departmant);
        phoneNumber = findViewById(R.id.phone_number);
        emailAddress = findViewById(R.id.email_address);
        address = findViewById(R.id.address);
        modified = getIntent().getBooleanExtra("modified", false);
        index = getIntent().getIntExtra("index", 0);




        myPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                ((Activity) mContext).startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        if(modified){//수정일 경우 원래값 입력
            name.setText(getIntent().getStringExtra("name"));
            department.setText(getIntent().getStringExtra("department"));
            address.setText(getIntent().getStringExtra("address"));
        }
        //수정할 수 없는 부분 고정
        phoneNumber.setText("Phone: " + SplashActivity.USER.getTel());
        emailAddress.setText("e-mail: " + SplashActivity.USER.getEmail());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(department.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "소속을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(address.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //db저장
                    if(modified){//명함 수정
                        modifyBusinessCard(name.getText().toString(), department.getText().toString(), address.getText().toString(), null);
                    }
                    else{//명함 생성
                        createBusinessCard(name.getText().toString(), department.getText().toString(), address.getText().toString(), null);
                    }

                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateBusinessCardActivity.this);

                alertDialog.setTitle("명함 삭제");
                alertDialog.setMessage("해당 명함을 삭제하시겠습니까?");

                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RemoveBusinessCardAsyncTask removeBusinessCardAsyncTask = new RemoveBusinessCardAsyncTask();
                        BusinessCard businessCard = SplashActivity.BUSINESSCARD_LIST.get(index);
                        removeBusinessCardAsyncTask.execute(businessCard);
                    }
                });
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
                alert.setTitle("명함 삭제");



            }
        });


    }

    public void createBusinessCard(String name, String department, String address, String imgSrc){
        BusinessCard businessCard = new BusinessCard(String.valueOf(SplashActivity.USER.get_id()), name, department, SplashActivity.USER.getTel(), address, imgSrc);
        CreateBusinessCardAsyncTask businessCardAsyncTask = new CreateBusinessCardAsyncTask();
        businessCardAsyncTask.execute(businessCard);
    }

    public void modifyBusinessCard(String name, String department, String address, String imgSrc){
        BusinessCard businessCard;
        businessCard = SplashActivity.BUSINESSCARD_LIST.get(index);//수정할 명함
        businessCard.setName(name);
        businessCard.setDepartment(department);
        businessCard.setAddress(address);
        businessCard.setImg(imgSrc);
        CreateBusinessCardAsyncTask businessCardAsyncTask = new CreateBusinessCardAsyncTask();
        businessCardAsyncTask.setModified(modified);
        businessCardAsyncTask.setIndex(index);
        businessCardAsyncTask.execute(businessCard);

    }





    public class CreateBusinessCardAsyncTask extends AsyncTask<BusinessCard, Void, Boolean> {
        Retrofit retrofit;
        APIService service;
        boolean modified;
        int index;
        BusinessCard b;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public boolean isModified() {
            return modified;
        }

        public void setModified(boolean modified) {
            this.modified = modified;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalApplication.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(APIService.class);
        }

        @Override
        protected void onPostExecute(Boolean isSuccessed) {
            super.onPostExecute(isSuccessed);
            if(isSuccessed) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);

                if(modified) {
//                    BusinessCard b = SplashActivity.BUSINESSCARD_LIST.get(index);
//                    b.setAddress(address.getText().toString());
//                    b.setDepartment(department.getText().toString());
//                    b.setName(name.getText().toString());
//                    SplashActivity.BUSINESSCARD_LIST.set(index, b);
                    returnIntent.putExtra("index", this.index);
                    returnIntent.putExtra("address", this.b.getAddress());
                    returnIntent.putExtra("name", this.b.getName());
                    returnIntent.putExtra("department", this.b.getDepartment());
                }

            }
            finish();
        }

        @Override
        protected Boolean doInBackground(BusinessCard... businessCards) {
            // 유저 정보 추가
            this.b = businessCards[0];
            Call<BusinessCard> businessCard;
            if(!modified){
                businessCard = service.createBusinessCard(String.valueOf(SplashActivity.USER.get_id()), businessCards[0]);
                Log.d("owner==", businessCards[0].getOwner());
                Log.d("name==", businessCards[0].getName());
                try {
                    if(businessCard.execute().code() == 201) { // 추가 성공
                        SplashActivity.BUSINESSCARD_LIST.add(businessCards[0]);
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Log.d("id==", businessCards[0].get_id());
                businessCard = service.updateBusinessCard(businessCards[0].get_id(), businessCards[0]);
                    try {
                    if(businessCard.execute().code() == 201) { // 추가 성공
                        //splash에서 수정하는 부분 필요

                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }
    public class RemoveBusinessCardAsyncTask extends AsyncTask<BusinessCard, Void, Boolean> {
        Retrofit retrofit;
        APIService service;
        int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalApplication.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(APIService.class);
        }

        @Override
        protected void onPostExecute(Boolean isSuccessed) {
            super.onPostExecute(isSuccessed);
            if(isSuccessed) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("remove", true);
                setResult(Activity.RESULT_OK,returnIntent);

                SplashActivity.BUSINESSCARD_LIST.remove(index);
                }
            finish();
            }


        @Override
        protected Boolean doInBackground(BusinessCard... businessCards) {
            // 유저 정보 추가
            Call<BusinessCard> businessCard;

                Log.d("id==", businessCards[0].get_id());
                businessCard = service.removeBusinessCard(businessCards[0].get_id());
                try {
                    if(businessCard.execute().code() == 200) { // 삭제 성공
                        //splash에서 수정하는 부분 필요
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return false;
        }
    }
}
