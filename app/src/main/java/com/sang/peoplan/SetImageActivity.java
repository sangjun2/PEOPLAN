package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SetImageActivity extends AppCompatActivity {
    Button defaultImageButton;
    Button setImageButton;
    ImageView image;

    Context mContext;

    public final int REQ_CODE_SELECT_IMAGE = 1;
    public final int REQ_CODE_CROP_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);
        mContext = this;

        defaultImageButton = findViewById(R.id.default_image_bt);
        setImageButton = findViewById(R.id.set_image_bt);
        image = findViewById(R.id.get_image);

        setImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("URI==", data.getDataString());
                Uri imageUri = data.getData();
                Picasso.with(mContext).load(imageUri).into(image);

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("scale", true);
                startActivityForResult(intent, REQ_CODE_CROP_IMAGE);

            }
        } else if(requestCode == REQ_CODE_CROP_IMAGE) {
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                Picasso.with(mContext).load(imageUri).into(image);
            }
        }
    }
}
