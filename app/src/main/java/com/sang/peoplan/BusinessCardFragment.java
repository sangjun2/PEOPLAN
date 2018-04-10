package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BusinessCardFragment extends Fragment {

    private Uri mImageCaptureUri;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private String absolutePath;

    CreateBusinessCardDialog dialog;

    View myBusinessCardsView;
    ViewPager myBusinessCardsViewPager;

    TextView department;
    TextView name;
    TextView tel;
    TextView email;
    TextView address;

    public BusinessCardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BusinessCardFragment newInstance() {
        BusinessCardFragment fragment = new BusinessCardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_card, container, false);

        ArrayList<BusinessCard> myBusinessCards = SplashActivity.BUSINESSCARD_LIST;

        myBusinessCardsViewPager = view.findViewById(R.id.my_businesscard_view);
        myBusinessCardsViewPager.setAdapter(new BusinessCardPagerAdapter(myBusinessCards));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case PICK_FROM_ALBUM:
                mImageCaptureUri = data.getData();
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 120);
                intent.putExtra("outputY", 160);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            case CROP_FROM_IMAGE:
                if(resultCode != Activity.RESULT_OK){
                    return;
                }

                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";
                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");
                    dialog.setImageViewBitmap(photo);

                    storeCropImage(photo, filePath);
                    absolutePath = filePath;
                    break;
                }

                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists()){
            directory_SmartWheel.mkdir();
        }
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public class BusinessCardPagerAdapter extends PagerAdapter {
        ArrayList<BusinessCard> myBusinessCards;


        public BusinessCardPagerAdapter(ArrayList<BusinessCard> myBusinessCards) {
            this.myBusinessCards = myBusinessCards;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.business_card_item, null);
            if(position == myBusinessCards.size()){

                ImageView createView = new ImageView(getContext());
                createView.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);

                createView.setMaxHeight(60);
                createView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = new CreateBusinessCardDialog(getContext());
                        dialog.setTitle("명함 만들기");
                        dialog.setModified(false);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                notifyDataSetChanged();

                            }
                        });
                    }
                });
                //removeView
                container.addView(createView);
                return createView;
            }
            else{
                TextView department = view.findViewById(R.id.department);
                TextView name = view.findViewById(R.id.name);
                TextView tel = view.findViewById(R.id.tel);
                TextView email = view.findViewById(R.id.email_address);
                TextView address = view.findViewById(R.id.address);

                ImageView businessCardSetting = view.findViewById(R.id.businesscard_setting);

                final BusinessCard b = myBusinessCards.get(position);
                businessCardSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = new CreateBusinessCardDialog(getContext());
                        dialog.setTitle("명함 수정");
                        dialog.setNameText(b.getName());
                        dialog.setDepartmentText(b.getDepartment());
                        dialog.setAddressText(b.getAddress());
                        dialog.setModified(true);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                notifyDataSetChanged();
                            }
                        });
                        dialog.show();
                    }
                });

                department.setText(department.getText().toString() + b.getDepartment());
                name.setText(name.getText().toString() + b.getName());
                tel.setText(tel.getText().toString() + b.getTel());
                email.setText(email.getText().toString() + SplashActivity.USER_PROFILE.getEmail());
                address.setText(address.getText().toString() + b.getAddress());

                container.addView(view);
                return view;
            }

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            if(position == myBusinessCards.size()){
//                container.removeView((ImageView) object);
//            }
//            else{
//                container.removeView((ConstraintLayout) object);
//            }
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return this.myBusinessCards.size() + 1;
        }
    }


}
