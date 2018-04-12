package com.sang.peoplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

    /*
     * - 그룹 삭제 부분
     */

public class GroupDetailActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout toolbarLayout;
    TabLayout tabLayout;
    NestedScrollView nestedScrollView;
    ViewPager viewPager;
    TabViewPagerAdapter tabViewPagerAdapter;
    ImageView groupImage;

    Group group;

    Context mContext;
    final static int REQUEST_DELETE_GROUP = 100;
    final static int REQUEST_LEAVE_GROUP = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mContext = this;

        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("Group");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if(state == State.COLLAPSED) {
                    getSupportActionBar().setTitle(group.getName());
                } else if(state == State.EXPANDED) {
                    getSupportActionBar().setTitle("");

                } else {

                }
            }
        });


        tabLayout = findViewById(R.id.group_detail_tab);
        viewPager = findViewById(R.id.group_viewpager);
        nestedScrollView = findViewById(R.id.group_nestedscrollview);
        nestedScrollView.setFillViewport(true);
        tabViewPagerAdapter = new TabViewPagerAdapter();
        viewPager.setAdapter(tabViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        TextView groupTitle = findViewById(R.id.group_title_text);
        groupTitle.setText(group.getName());
        TextView groupContent = findViewById(R.id.group_data);
        groupContent.setText("멤버 : " + group.getMembers().size());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        groupImage = findViewById(R.id.group_image);
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
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ // 그룹 삭제나 탈퇴시
            if(resultCode == RESULT_OK){  // 삭제나 탈퇴 성공시
                finish();
            }
            if(resultCode == RESULT_CANCELED){ // createGroupActivity에서 추가 실패시, 물론 실패시 구현 안됨
            }
        }
    }


    public class TabViewPagerAdapter extends PagerAdapter {
        private ArrayList<TabItem> tabItems;

        public TabViewPagerAdapter() {
            tabItems = new ArrayList<>();
            tabItems.add(new TabItem("공지사항",  new GroupNoticeView().initView()));
            tabItems.add(new TabItem("일정", new GroupPlanView().initView()));
            tabItems.add(new TabItem("게시판", new GroupBoardView().initView()));
            tabItems.add(new TabItem("그룹원", new GroupParticipantsView().initView()));
            tabItems.add(new TabItem("설정", new GroupSettingView().initView()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = tabItems.get(position).getView();

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.tabItems.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return this.tabItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class TabItem {
        public String title;
        public View view;

        public TabItem() {
            this.title = null;
            this.view = null;
        }

        public TabItem(String title, View view) {
            this.title = title;
            this.view = view;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }

    public class GroupNoticeView {
        private int layout;

        public GroupNoticeView() {
            this.layout = R.layout.group_notice;
        }

        public View initView() {
            View view = LayoutInflater.from(mContext).inflate(layout, null);

            return view;
        }
    }

    public class GroupPlanView {
        private int layout;

        public GroupPlanView() {
            this.layout = R.layout.group_plan;
        }

        public View initView() {
            View view = LayoutInflater.from(mContext).inflate(layout, null);

            return view;
        }
    }

    public class GroupBoardView {
        private int layout;

        public GroupBoardView() {
            this.layout = R.layout.group_board;
        }

        public View initView() {
            View view = LayoutInflater.from(mContext).inflate(layout, null);

            return view;
        }
    }

    public class GroupParticipantsView {
        private int layout;

        public GroupParticipantsView() {
            this.layout = R.layout.group_participants;
        }

        public View initView() {
            View view = LayoutInflater.from(mContext).inflate(layout, null);

            return view;
        }
    }


    public class GroupSettingView {
        private int layout;

        public GroupSettingView() {
            this.layout = R.layout.group_setting;
        }

        public View initView() {
            View view = LayoutInflater.from(mContext).inflate(layout, null);

            // 그룹 이미지 선택
            LinearLayout groupPicture = view.findViewById(R.id.group_setting_picture);
            groupPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GroupDetailActivity.this, SetImageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });

            // 그룹 삭제
            LinearLayout groupDelete = view.findViewById(R.id.group_delete);
            groupDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    // 클릭시 admin 맞은지 재확인
                    // 클릭시 삭제 여부 확인 후
                    // 삭제 -> 쓰레드로 서버에게 삭제 메세지 전달
                    Intent intent = new Intent(GroupDetailActivity.this, GroupDeleteOrLeave.class);
                    intent.putExtra("Group", group);
                    intent.putExtra("WhatToDo", REQUEST_DELETE_GROUP);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });

            // 그룹 탈퇴
            LinearLayout groupLeave = view.findViewById(R.id.group_leave);
            groupLeave.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    // 팝업창으로 삭제 여부 확인
                    Intent intent = new Intent(GroupDetailActivity.this, GroupDeleteOrLeave.class);
                    intent.putExtra("Group", group);
                    intent.putExtra("WhatToDo", REQUEST_LEAVE_GROUP);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

                }
            });
            return view;
        }
    }


}
