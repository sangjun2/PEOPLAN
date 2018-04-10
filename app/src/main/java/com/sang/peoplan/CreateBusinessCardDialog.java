package com.sang.peoplan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateBusinessCardDialog extends Dialog { // 명함 만들기
    public String day;
    public Context mContext;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;


    private String absolutePath;
    ImageView myPicture;
    EditText name;
    EditText department;
    TextView phoneNumber;
    TextView emailAddress;
    EditText address;
    Button confirm;

    String nameText;
    String departmentText;
    String addressText;
    Boolean modified;

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public CreateBusinessCardDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public CreateBusinessCardDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected CreateBusinessCardDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_business_card);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 자기 얼굴 고르기
        myPicture = findViewById(R.id.my_picture);
        confirm = findViewById(R.id.confirm_toolbar_bt);
        name = findViewById(R.id.name);
        department = findViewById(R.id.departmant);
        phoneNumber = findViewById(R.id.phone_number);
        emailAddress = findViewById(R.id.email_address);
        address = findViewById(R.id.address);

        myPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                ((Activity) mContext).startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        if(modified){
           department.setText(departmentText);
           name.setText(nameText);
           address.setText(addressText);
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")){
                    Toast.makeText(getContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT);
                }
                else if(department.getText().toString().equals("")){
                    Toast.makeText(getContext(), "소속을 입력해주세요.", Toast.LENGTH_SHORT);
                }
                else if(address.getText().toString().equals("")){
                    Toast.makeText(getContext(), "주소를 입력해주세요.", Toast.LENGTH_SHORT);
                }
                else{
                    //db저장
                    BusinessCard businessCard = new BusinessCard();
                    businessCard.setKakaoid(String.valueOf(SplashActivity.USER_PROFILE.getId()));
                    businessCard.setName(name.getText().toString());
                    businessCard.setDepartment(department.getText().toString());
                    businessCard.setTel(SplashActivity.USER_TEL);
                    businessCard.setAddress(address.getText().toString());

                    CreateBusinessCardAsyncTask businessCardAsyncTask = new CreateBusinessCardAsyncTask();
                    if(modified){
                        businessCardAsyncTask.setModified(modified);
                    }
                    businessCardAsyncTask.execute(businessCard);

                }
            }
        });

        phoneNumber.setText("Phone: " + SplashActivity.USER_TEL);
        emailAddress.setText("e-mail: " + SplashActivity.USER_PROFILE.getEmail());

    }

    public void setImageViewBitmap(Bitmap bitmap){
        myPicture.setImageBitmap(bitmap);
    }

    public class CreateBusinessCardAsyncTask extends AsyncTask<BusinessCard, Void, Boolean> {
        Retrofit retrofit;
        APIService service;
        boolean modified;

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
                Toast.makeText(getContext(), "명함이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                //프레그먼트에 직접 반응시킬 방법
            }
            dismiss();
        }

        @Override
        protected Boolean doInBackground(BusinessCard... businessCards) {
            // 유저 정보 추가
            Call<BusinessCard> businessCard;
            if(!modified){
                businessCard = service.createBusinessCard(String.valueOf(SplashActivity.USER_PROFILE.getId()), businessCards[0]);
                try {
                    if(businessCard.execute().code() == 200) { // 추가 성공
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
                    if(businessCard.execute().code() == 200) { // 추가 성공
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
}
