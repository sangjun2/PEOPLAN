package com.sang.peoplan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupFragment extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

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

        Toolbar toolbar = view.findViewById(R.id.group_toolbar);
        TextView title = view.findViewById(R.id.toolbar_title);
        title.setText("그룹");

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.group_frame, GroupListFragment.newInstance());
        fragmentTransaction.commit();

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
