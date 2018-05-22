package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.sang.peoplan.R.drawable.*;

public class BusinessCardFragment extends Fragment {

    private Uri mImageCaptureUri;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static final int CREATE_BUSINESSCARD = 3;
    private static final int MODIFY_BUSINESSCARD = 4;

    private String absolutePath;


    View myBusinessCardsView;
    ViewPager myBusinessCardsViewPager;

    TextView department;
    TextView name;
    TextView tel;
    TextView email;
    TextView address;

    BusinessCardPagerAdapter pagerAdapter;
    ArrayList<BusinessCard> walletList = new ArrayList<>();
    //HorizontalGridView walletListView;
    HorizontalGridView walletListView;
    WalletAdapter walletAdapter;

    Button toolbarNotificationButton;
    NotificationBadge notificationBadge;

    OnFragmentInteractionListener mListener;

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
        pagerAdapter = new BusinessCardPagerAdapter(myBusinessCards);

        toolbarNotificationButton = view.findViewById(R.id.toolbar_notification_bt); // 그룹 알림 버튼
        toolbarNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNotificationButtonClicked();
            }
        });

        notificationBadge = view.findViewById(R.id.toolbar_notification_badge);
        notificationBadge.setNumber(1);


        myBusinessCardsViewPager = view.findViewById(R.id.my_businesscard_view);
        myBusinessCardsViewPager.setAdapter(pagerAdapter);

        walletListView = view.findViewById(R.id.businesscard_wallet_view);
        BusinessCard b = new BusinessCard();
        b.setName("이상인");
        for(int i = 0; i < 7; i++){
            walletList.add(b);
        }
        walletAdapter = new WalletAdapter(walletList);
        walletListView.setAdapter(walletAdapter);

        return view;
    }

    public void onNotificationButtonClicked() {
        if(mListener != null) {
            mListener.onBusinessCardFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBusinessCardFragmentInteraction();
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
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";
                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");

                    storeCropImage(photo, filePath);
                    absolutePath = filePath;
                    break;
                }
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            case MODIFY_BUSINESSCARD:
                if(!data.getBooleanExtra("remove", false)){
                    int index = data.getIntExtra("index", 0);
                    BusinessCard b = SplashActivity.BUSINESSCARD_LIST.get(index);
                    b.setAddress(data.getStringExtra("address"));
                    b.setDepartment(data.getStringExtra("department"));
                    b.setName(data.getStringExtra("name"));
                    SplashActivity.BUSINESSCARD_LIST.set(index, b);
                }
                pagerAdapter.notifyDataSetChanged();
                break;
            case CREATE_BUSINESSCARD:
                pagerAdapter.notifyDataSetChanged();
                break;
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
                createView.setImageResource(ic_add_circle_outline_black_24dp);

                createView.setMaxHeight(60);
                createView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), CreateBusinessCardActivity.class);
                        intent.putExtra("modified", false);
                        intent.putExtra("index", position);
                        startActivityForResult(intent, CREATE_BUSINESSCARD);
                    }
                });
                //removeView
                container.addView(createView);
                return createView;
            }
            else{
                final TextView department = view.findViewById(R.id.department);
                TextView name = view.findViewById(R.id.name);
                TextView tel = view.findViewById(R.id.tel);
                TextView email = view.findViewById(R.id.email_address);
                TextView address = view.findViewById(R.id.address);

                ImageView businessCardSetting = view.findViewById(R.id.businesscard_setting);

                final BusinessCard b = myBusinessCards.get(position);
                businessCardSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), CreateBusinessCardActivity.class);
                        intent.putExtra("modified", true);
                        intent.putExtra("index", position);
                        intent.putExtra("department", b.getDepartment());
                        intent.putExtra("name", b.getName());
                        intent.putExtra("address", b.getAddress());
                        startActivityForResult(intent, MODIFY_BUSINESSCARD);
                    }
                });

                department.setText(department.getText().toString() + b.getDepartment());
                name.setText(name.getText().toString() + b.getName());
                tel.setText(tel.getText().toString() + b.getTel());
                email.setText(email.getText().toString() + SplashActivity.USER.getEmail());
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

    private class WalletAdapter extends BaseAdapter{
        ArrayList<BusinessCard> businessCards = new ArrayList<BusinessCard>();
        public WalletAdapter(ArrayList businessCards){
            this.businessCards = businessCards;
        }
        @Override
        public int getCount() {
            return businessCards.size();
        }

        public void addItems(BusinessCard businessCard){businessCards.add(businessCard);}

        @Override
        public Object getItem(int i) {
            return businessCards.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void delete(int i){
            this.businessCards.remove(i);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int i, View contextView, ViewGroup viewGroup) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.business_card_wallet_item, null);

            ImageView imageView = view.findViewById(R.id.businesscard_image);
            TextView textView = view.findViewById(R.id.businesscard_name);
            textView.setText(businessCards.get(i).getName());
            imageView.setImageResource(R.drawable.alarm_background);
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);

            return view;
        }
    }


}
