package com.sang.peoplan;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DayScheduleDialog dayScheduleDialog;
    public ArrayList<Event> myEvents;

    public EventListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(myEvents == null) {
            myEvents = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        RecyclerView eventListRecyclerView = view.findViewById(R.id.event_recyclerview);

        eventListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(myEvents);
        eventListRecyclerView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onEventSelected(Event event) {
        if (mListener != null) {
            mListener.onFragmentInteraction(event);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (dayScheduleDialog instanceof OnFragmentInteractionListener) {
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
        void onFragmentInteraction(Event event);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<Event> myEvent;

        Context mContext;

        public RecyclerViewAdapter(ArrayList<Event> myEvent){
            this.myEvent = myEvent;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.event_list_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEventSelected(viewHolder.event);
                }
            });

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Event event = myEvent.get(position);

            viewHolder.view.setBackgroundColor(android.graphics.Color.parseColor("#" + event.getColor()));

            String eventName = event.getName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("a hh:mm");
            String start = dateFormat.format(event.getStart());
            String end = dateFormat.format(event.getEnd());
            String eventTime;
            if(start.equals("오전 12:00") && end.equals("오후 11:59")) {
                eventTime = "하루종일";
            } else {
                eventTime = dateFormat.format(event.getStart()) + " ~ " + dateFormat.format(event.getEnd());
            }

            viewHolder.event = event;
            viewHolder.eventName.setText(eventName);
            viewHolder.eventTime.setText(eventTime);
        }

        @Override
        public int getItemCount() {
            return myEvent.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        TextView eventTime;
        Event event;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            eventName = itemView.findViewById(R.id.event_name);
            eventTime = itemView.findViewById(R.id.event_time);
        }
    }
}
