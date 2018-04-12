package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class SetGroupActivity extends AppCompatActivity {
    Button confirm;
    RecyclerView recyclerView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_group);
        mContext = this;

        Toolbar toolbar = findViewById(R.id.set_group_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);
        toolbarTitle.setText("그룹 선택");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.set_group_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        final GroupItemViewAdapter adapter = new GroupItemViewAdapter(SplashActivity.GROUP_LIST);
        recyclerView.setAdapter(adapter);

        confirm = findViewById(R.id.confirm_toolbar_bt);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(adapter.getIndex() == -1) {
                    intent.putExtra("group", "없음");
                } else {
                    intent.putExtra("group", adapter.groups.get(adapter.getIndex()).getName());
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    public class GroupItemViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<Group> groups;
        private Context mContext;
        private RadioButton selectedIndexButton;
        private int index;

        public GroupItemViewAdapter(ArrayList<Group> groups) {
            this.groups = groups;
            this.index = -1;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.set_group_item, parent, false);
            GroupItemViewHolder viewHolder = new GroupItemViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final GroupItemViewHolder viewHolder = (GroupItemViewHolder) holder;

            if(position == 0) {
                viewHolder.groupText.setText("없음");
                viewHolder.radioButton.setChecked(true);
                selectedIndexButton = viewHolder.radioButton;
                viewHolder.position = -1;
            } else {
                viewHolder.groupText.setText(this.groups.get(position - 1).getName());
                viewHolder.position = position - 1;
            }
        }

        @Override
        public int getItemCount() {
            return this.groups.size() + 1;
        }

        private class GroupItemViewHolder extends RecyclerView.ViewHolder {
            TextView groupText;
            RadioButton radioButton;
            int position;

            public GroupItemViewHolder(View itemView) {
                super(itemView);
                groupText = itemView.findViewById(R.id.set_group_item_text);
                radioButton = itemView.findViewById(R.id.set_group_item_radiobt);
                radioButton.setClickable(false);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedIndexButton.setChecked(false);
                        radioButton.setChecked(true);
                        selectedIndexButton = radioButton;
                        index = position;
                    }
                });
            }
        }
    }
}
