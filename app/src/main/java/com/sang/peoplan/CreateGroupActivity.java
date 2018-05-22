package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.kakao.usermgmt.response.model.UserProfile;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateGroupActivity extends AppCompatActivity { // 그룹 추가
    TextView category; // 선택된 카테고리 보이는 텍스트, 그룹 카테고리
    LinearLayout categoryView; // 카테고리 종류 띄우는 뷰
    Switch bPublic; // 공개.비공개 그룹 선택 뷰
    private RecyclerView friend_list_recycler_view; // 연락처 띄우는 뷰
    private RecyclerView.Adapter adapter; // 어댑터
    private RecyclerView.LayoutManager layoutManager; // 레이아웃 매니저

    // ArrayList, 많은 양의 자료를 다루는 경우 이게 최선책인지 검토
    private ArrayList<User> friends; // test 값, 친구 목록
    private ArrayList<String> invitationList; // 친구 id

    final int REQUESTCODE_CATEGORY = 500;

    /*
     * - 친구 목록 외부에서 가져오기
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

         // 그룹 만드는 본인 정보 수집
        final User userProfile = SplashActivity.USER;
        // 본인의 친구들 정보 수집, 초대목록 초기화
        friends = new ArrayList<>();
        invitationList = new ArrayList<>();

        /*
        User f1 = new User("01","정태표","1-2","a");
        User f2 = new User("02","이상인","2-2","b");
        User f3 = new User("03","이상준","3-2","c");
        User f4 = new User("04","전철민","4-2","d");
        User f5 = new User("05","정태표","1-2","a");
        User f6 = new User("06","이상인","2-2","b");
        User f7 = new User("07","이상준","3-2","c");
        User f8 = new User("08","전철민","4-2","d");
        friends.add(f1);
        friends.add(f2);
        friends.add(f3);
        friends.add(f4);
        friends.add(f5);
        friends.add(f6);
        friends.add(f7);
        friends.add(f8);
        */
        //화면 상위 확인 버튼
        Toolbar toolbar = findViewById(R.id.group_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);

        Button confirm_toolbar_bt = findViewById(R.id.confirm_toolbar_bt); // 그룹 생성 버튼
        final EditText group_name = findViewById(R.id.group_name); // 그룹 이름 EditText

        bPublic = findViewById(R.id.bPublic);
        final boolean isPublic = true;
        final TextView groupState = findViewById(R.id.groupState);

        toolbarTitle.setText("그룹 만들기");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 생성 버튼 리스너 설정
        confirm_toolbar_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // 생성 버튼 클릭시, 데이터 입력 확인
                // 입력 받는 텍스트의 안전성 확인.. 쿼리문상
                if( group_name.getText().toString().length() == 0 ){
                    Toast.makeText(getApplicationContext(), "그룹 명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if( category.getText().toString().equals("없음")){ // 카테로리 미선택 예외처리
                    Toast.makeText(getApplicationContext(), "그룹 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    // 그룹 멤버 데이터 추가
                    Group newGroup;
                    if(groupState.getText().equals("공개그룹"))
                        newGroup = new Group(userProfile.get_id(), group_name.getText().toString(), category.getText().toString(), true);
                    else
                        newGroup = new Group(userProfile.get_id(), group_name.getText().toString(), category.getText().toString(), false);

                    addGroupMembers(newGroup);
                    // DB에만 저장
                    CreateGroupAsyncTask task = new CreateGroupAsyncTask();
                    task.execute(newGroup);
                }
            }
        });
        //공개 그룹 설정
        bPublic.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){ // 공개 그룹
                    groupState.setText("공개그룹");
                }
                else{ // 비 공개 그룹
                    groupState.setText("비공개그룹");
                }
            }
        });

        // 그룹 카테고리 설정
        categoryView = findViewById(R.id.category_view);
        category = findViewById(R.id.category_confirm);
        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupActivity.this, SetGroupCategoryActivity.class);
                startActivityForResult(intent, REQUESTCODE_CATEGORY);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        //그룹 구성원 목록
        friend_list_recycler_view = findViewById(R.id.group_friendlist_recycler_view);
        friend_list_recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        friend_list_recycler_view.setLayoutManager(layoutManager);
        adapter = new FriendsAdapter(friends);
        friend_list_recycler_view.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUESTCODE_CATEGORY){
            if(resultCode == Activity.RESULT_OK){
                String repeat = data.getStringExtra("category");
                category.setText(repeat);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_noanim, R.anim.anim_slide_out_bottom);
    }

    private void addGroupMembers(Group newGroup){
        for (String i: invitationList ) {
            newGroup.addMember(i);
        }
    }

    // 스레드 이용한 DB에 그룹 데이터 저장
    public class CreateGroupAsyncTask extends AsyncTask<Group, Void, Boolean> {
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
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Group... groups) {
            // 새로운 그룹 추가
            // DB 그룹 추가는 possible, but 그룹 클래스 아직 완성 안되있음
            Call<Group> group = service.createGroup(groups[0]);
            try {
                if(group.execute().code() == 200) { // 추가 성공
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ItemViewHolder>{
        private ArrayList<User> friendlist; // 친구목록

        public FriendsAdapter(ArrayList<User> friendlist) {
            this.friendlist = friendlist;
        }

        // 새로운 뷰 홀더 생성
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // xml 상에서 커스텀 된 레이아웃 사용 /?!!!
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_friendlist_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            // 아이템 클릭시 반응
            holder.itemView.setOnClickListener(holder);

            return holder;
        }

        // View의 내용을 해당 포지션의 데이터로 바꿉니다.
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            // 일단 이름만 변경
            holder._name.setText(friendlist.get(position).getName());
            holder._position = position;

        }

        @Override
        public int getItemCount() {
            return friendlist.size();
        }

        // 커스텀 뷰홀더, 이미지, 이름, 체크박스
        // 바인딩 완료
        class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
            private ImageView _img;
            private TextView _name;
            private CheckBox _choicecheckbox; // 임시로 ID
            private int _position; // 위치
            public ItemViewHolder(View itemView){
                super(itemView);
                _img = itemView.findViewById(R.id.friend_item_img);
                _name = itemView.findViewById(R.id.friend_item_name);
                _choicecheckbox = itemView.findViewById(R.id.friend_choice_cb);
                _choicecheckbox.setOnCheckedChangeListener(this);
                _position = 0;
            }

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch(compoundButton.getId()){
                    case R.id.friend_choice_cb:
                        if(b){
                            _choicecheckbox.setChecked(true);
                            invitationList.add(friendlist.get(_position).getKakaoUID()); // 초대목록에 선택된 유저 id 추가
                        }else{
                            _choicecheckbox.setChecked(false);
                            invitationList.remove(friendlist.get(_position).getKakaoUID()); // 초대목록에 선택된 유저 id 삭제
                        }
                }

            }

            @Override
            public void onClick(View view) {
                if(_choicecheckbox.isChecked()){
                    _choicecheckbox.setChecked(false);
                }else{
                    _choicecheckbox.setChecked(true);
                }
            }
        }
    }
}
