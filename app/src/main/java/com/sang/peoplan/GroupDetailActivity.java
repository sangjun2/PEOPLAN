package com.sang.peoplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupDetailActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout toolbarLayout;
    TabLayout tabLayout;
    NestedScrollView nestedScrollView;
    ViewPager viewPager;
    TabViewPagerAdapter tabViewPagerAdapter;

    Group group;

    Context mContext;

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

    public class TabViewPagerAdapter extends PagerAdapter {
        private ArrayList<TabItem> tabItems;

        public TabViewPagerAdapter() {
            tabItems = new ArrayList<>();
            tabItems.add(new TabItem("공지사항",  R.layout.group_notice));
            tabItems.add(new TabItem("게시판", R.layout.group_board));
            tabItems.add(new TabItem("그룹원", R.layout.group_participants));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(this.tabItems.get(position).getLayout(), null);

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
        public int layout;

        public TabItem() {
            this.title = null;
            this.layout = -1;
        }

        public TabItem(String title, int layout) {
            this.title = title;
            this.layout = layout;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getLayout() {
            return layout;
        }

        public void setLayout(int layout) {
            this.layout = layout;
        }
    }
}
