package com.sang.peoplan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class CreateBusinessCardDialog extends Dialog { // 명함 만들기
    public String day;
    public Context mContext;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private String absolutePath;
    ImageView myPicture;

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
        myPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                ((Activity) mContext).startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }

    public void setImageViewBitmap(Bitmap bitmap){
        myPicture.setImageBitmap(bitmap);
    }

}
