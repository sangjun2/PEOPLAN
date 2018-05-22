package com.sang.peoplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText input;
    TextView cancel;

    RecyclerView recyclerView;
    GroupListAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        input = view.findViewById(R.id.input_search);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        cancel = view.findViewById(R.id.input_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                input.clearFocus();
                getActivity().onBackPressed();
            }
        });

        recyclerView = view.findViewById(R.id.search_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));



        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        input.clearFocus();

                        // 검색 동작
                        String searchData = input.getText().toString();
                        if(searchData.equals("")) {
                            Toast.makeText(getContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            SearchGroupAsyncTask task = new SearchGroupAsyncTask();
                            task.execute(searchData);
                        }
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });


        return view;
    }

    public class SearchGroupAsyncTask extends AsyncTask<String, Void, ArrayList<Group>> {
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
        protected void onPostExecute(ArrayList<Group> groups) {
            super.onPostExecute(groups);

            adapter = new GroupListAdapter();
            adapter.groups = groups;
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected ArrayList<Group> doInBackground(String... strings) {
            Call<List<Group>> groups = service.searchGroups(strings[0]);

            try {
                Response<List<Group>> response = groups.execute();
                if(response.code() == 404) {
                    return new ArrayList<>();
                } else if(response.code() == 500) {
                    return new ArrayList<>();
                } else if(response.code() == 200) {
                    ArrayList<Group> list = new ArrayList<>();
                    for(Group group : response.body()) {
                        list.add(group);
                    }

                    return list;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onSearchFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        void onSearchFragmentInteraction();
    }
}
