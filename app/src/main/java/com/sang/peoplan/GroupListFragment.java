package com.sang.peoplan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GroupListFragment extends Fragment {
    public GroupListFragment() {
        // Required empty public constructor
    }

    public static GroupListFragment newInstance() {
        GroupListFragment fragment = new GroupListFragment();
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
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.group_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new GroupListAdapter());

        return view;
    }

    public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            mContext = parent.getContext();

            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(mContext).inflate(R.layout.create_group_item_view, parent, false);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), CreateGroupActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_noanim);
                        }
                    });
                    return new CreateViewHolder(view);
                case 1:
                    view = LayoutInflater.from(mContext).inflate(R.layout.group_list_item_view, parent, false);
                    return new ItemViewHolder(view);
            }

            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(position != 0) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.bind("그룹" + position);
            }
        }

        public class CreateViewHolder extends RecyclerView.ViewHolder {
            public CreateViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            public ItemViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.group_title);
            }

            public void bind(String title) {
                this.title.setText(title);
            }
        }
    }
}
