package com.sang.peoplan;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    private OnFragmentInteractionListener mListener;
    public DayScheduleDialog dayScheduleDialog;

    private Event event;


    public EventDetailFragment() {
        // Required empty public constructor
    }

    public static EventDetailFragment newInstance(Event event) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);

        TextView title = view.findViewById(R.id.event_detail_title);
        TextView type = view.findViewById(R.id.event_detail_type);
        TextView content = view.findViewById(R.id.event_detail_content);
        TextView start = view.findViewById(R.id.event_detail_start_text);
        TextView end = view.findViewById(R.id.event_detail_end_text);

        if(event.getParticipants().length == 0) {
            type.setText("개인일정");
        } else {
            type.setText("그룹일정");
        }

        Button modifyButton = view.findViewById(R.id.event_detail_modify);
        Button removeButton = view.findViewById(R.id.event_detail_remove);

        title.setText(event.getName());
        content.setText(event.getContent());
        SimpleDateFormat format = new SimpleDateFormat("a hh:mm");
        start.setText(format.format(event.getStart()));
        end.setText(format.format(event.getEnd()));

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveEventAsyncTask task = new RemoveEventAsyncTask();
                task.execute(event.get_id());
            }
        });

        return view;
    }

    public void removeButtonClicked() {
        if(mListener != null) {
            mListener.onEventDetailFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (dayScheduleDialog instanceof EventListFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) dayScheduleDialog;
        } else {
            throw new RuntimeException(dayScheduleDialog.toString()
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
        void onEventDetailFragmentInteraction();
    }

    public class RemoveEventAsyncTask extends AsyncTask<String, Void, Boolean> {
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
                removeButtonClicked();
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Call<Void> call = service.removeEvent(strings[0]);

            try {
                Response<Void> response = call.execute();
                if(response.code() == 204) {
                    SplashActivity.EVENT_LIST.remove(strings[0]);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

}
