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

public class CreateBusinessCardDialog extends Dialog {
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

                    CreateBusinessCardAsyncTask businessCardAsyncTask = new CreateBusinessCardAsyncTask();
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
                Toast.makeText(getContext(), "저장완료", Toast.LENGTH_SHORT);
            }
        }

        @Override
        protected Boolean doInBackground(BusinessCard... businessCards) {
            // 유저 정보 추가
            Call<BusinessCard> businessCard = service.createBusinessCard(String.valueOf(SplashActivity.USER_PROFILE.getId()), businessCards[0]);
            try {
                if(businessCard.execute().code() == 200) { // 추가 성공
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
