package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

    /*
     *
     * - 공개그룹인지, 비공개그룹인지 알려주기
     */


public class GroupFragment extends Fragment {
    ArrayList<Group> groups;
    GroupListAdapter adapter;
    FloatingActionButton floatingActionButton;

    OnFragmentInteractionListener mListener;

    Button notificationButton;
    NotificationBadge notificationBadge;

    ConstraintLayout groupSearchLayout;

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
        // 그룹 내용 동기화 //
        GetGroupsAsyncTask task = new GetGroupsAsyncTask();
        task.execute(SplashActivity.USER.get_id());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // 화면 구성
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        Toolbar toolbar = view.findViewById(R.id.group_toolbar); //화면 상위 모습
        TextView title = view.findViewById(R.id.toolbar_title);
        title.setText("그룹");

        notificationButton = view.findViewById(R.id.toolbar_notification_bt);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNotificationButtonClicked();
            }
        });
        notificationBadge = view.findViewById(R.id.toolbar_notification_badge);
        notificationBadge.setNumber(1);

        //??
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        floatingActionButton = view.findViewById(R.id.group_floating_bt);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateGroupActivity.class);
                startActivityForResult(intent,1);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_noanim);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.group_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        adapter = new GroupListAdapter();
        recyclerView.setAdapter(adapter);

        groupSearchLayout = view.findViewById(R.id.group_search_layout);
        groupSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ // 그룹만들기 버튼 클릭시 requestCode 1 로 날라감
            if(resultCode == Activity.RESULT_OK){  // createGroupActivity에서 추가 성공시, 리프레쉬
                GetGroupsAsyncTask task = new GetGroupsAsyncTask();
                task.execute(String.valueOf(SplashActivity.USER.get_id()));
            }
            if(resultCode == Activity.RESULT_CANCELED){ // createGroupActivity에서 추가 실패시, 물론 실패시 구현 안됨
            }
        }
    }

    public void onNotificationButtonClicked() {
        if(mListener != null) {
            mListener.onGroupFragmentNotificationInteraction();
        }
    }

    public void onSearchButtonClicked() {
        if(mListener != null) {
            mListener.onGroupFragmentSearchInteraction();
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
        void onGroupFragmentNotificationInteraction();
        void onGroupFragmentSearchInteraction();
    }

    public class GetGroupsAsyncTask extends AsyncTask<String, Void, Boolean> {
        Retrofit retrofit;
        APIService service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            groups = new ArrayList<>();

            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalApplication.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(APIService.class);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                SplashActivity.GROUP_LIST = groups;
                adapter.groups = groups;
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected Boolean doInBackground(String... userUID) {
            Call<List<Group>> group = service.getGroups(userUID[0]);
            try {
                Response<List<Group>> response = group.execute();
                if(response.code() == 200) { // 데이터 받아옴
                    List<Group> groupList = response.body();
                    groups.clear();
                    for(int i = 0; i < groupList.size(); i++) {
                            groups.add(groupList.get(i));
                    }
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public class GroupListAdapter extends RecyclerView.Adapter<GroupItemView.GroupItemViewHolder> {
        Context mContext;
        ArrayList<Group> groups;

        public GroupListAdapter() {
            this.groups = new ArrayList<>();
        }

        @Override
        public GroupItemView.GroupItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GroupItemView view;
            mContext = parent.getContext();
            view = new GroupItemView(mContext, parent);

            GroupItemView.GroupItemViewHolder viewHolder = view.viewHolder;
            return viewHolder;

        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        @Override
        public void onBindViewHolder(GroupItemView.GroupItemViewHolder holder, int position) {
            final GroupItemView.GroupItemViewHolder itemViewHolder = holder;
            itemViewHolder.group = groups.get(position);
            itemViewHolder.bind();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), GroupDetailActivity.class);
                    intent.putExtra("Group", itemViewHolder.group);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });
        }
    }


}
