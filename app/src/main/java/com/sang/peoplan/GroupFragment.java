package com.sang.peoplan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    ListView myGroup_list;
    ArrayList<Group> myGroups;

    public GroupFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
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
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        myGroup_list = view.findViewById(R.id.myGroup);
        myGroups = new ArrayList<>();

        myGroups.add(new Group("111", "비트코인 가족"));
        myGroups.add(new Group("222", "가 족같은 우리회사 ^^"));
        myGroup_list.setAdapter(new MyGroupAdapter(myGroups));

        myGroup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

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

    public class MyGroupAdapter extends BaseAdapter{
        ArrayList<Group> groups;

        public MyGroupAdapter(ArrayList groups){
            this.groups = groups;
        }
        @Override
        public int getCount() {
            return groups.size();
        }

        @Override
        public Group getItem(int i) {
            return groups.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.group_list_item, null);
            ImageView groupImage = v.findViewById(R.id.groupImage);
            TextView groupName = v.findViewById(R.id.groupName);

            groupName.setText(groups.get(i).getGroupName());


            return v;
        }
    }
}