package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
    Button toolbar_notification_bt; // 그룹 알림 버튼
    ArrayList<Group> groups;
    GroupListAdapter adapter;

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

        groups = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        // 그룹 내용 동기화 //
        GetGroupsAsyncTask task = new GetGroupsAsyncTask();
        task.execute(String.valueOf(SplashActivity.USER_PROFILE.getId()));
        // 화면 구성
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        Toolbar toolbar = view.findViewById(R.id.group_toolbar); //화면 상위 모습
        TextView title = view.findViewById(R.id.toolbar_title);
        title.setText("그룹");

        toolbar_notification_bt = view.findViewById(R.id.toolbar_notification_bt); // 그룹 알림 버튼

        //??
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        RecyclerView recyclerView = view.findViewById(R.id.group_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new GroupListAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ // 그룹만들기 버튼 클릭시 requestCode 1 로 날라감
            if(resultCode == Activity.RESULT_OK){  // createGroupActivity에서 추가 성공시, 리프레쉬
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this);
                ft.attach(this);
                ft.commit();
            }
            if(resultCode == Activity.RESULT_CANCELED){ // createGroupActivity에서 추가 실패시, 물론 실패시 구현 안됨
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class GetGroupsAsyncTask extends AsyncTask<String, Void, Boolean> {
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
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
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

    public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        ArrayList<Group> groups;

        public GroupListAdapter() {
            this.groups = new ArrayList<>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            mContext = parent.getContext();

            switch (viewType) { // ?? viewType이 어떤식으로 잡히는 지 ?.. => getItemViewType method
                case 0:
                    view = LayoutInflater.from(mContext).inflate(R.layout.create_group_item_view, parent, false);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { // 그룹 생성
                            Intent intent = new Intent(getContext(), CreateGroupActivity.class);
                            startActivityForResult(intent,1);
                            getActivity().overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_noanim);
                        }
                    });
                    return new CreateViewHolder(view);
                case 1:
                    view = LayoutInflater.from(mContext).inflate(R.layout.group_list_item_view, parent, false);
                    final ItemViewHolder viewHolder = new ItemViewHolder(view);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), GroupDetailActivity.class);
                            intent.putExtra("Group", viewHolder.group);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                        }
                    });
                    return viewHolder;
            }

            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getItemCount() {
            return 1 + groups.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(position != 0) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.group = groups.get(position - 1);
                itemViewHolder.bind();
            }
        }

        public class CreateViewHolder extends RecyclerView.ViewHolder {
            public CreateViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            Group group;
            TextView title;

            public ItemViewHolder(View itemView) {
                super(itemView);
                group = null;
                title = itemView.findViewById(R.id.group_title);
            }

            public void bind() {
                this.title.setText(group.getName());
            }
        }
    }
}
