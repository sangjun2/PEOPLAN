package com.sang.peoplan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class EventDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

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

        Button modifyButton = view.findViewById(R.id.event_detail_modify);
        Button removeButton = view.findViewById(R.id.event_detail_remove);

        title.setText(event.getName());
        content.setText(event.getContent());
        SimpleDateFormat format = new SimpleDateFormat("a hh:mm");
        start.setText(format.format(event.getStart()));
        end.setText(format.format(event.getEnd()));

        return view;
    }

}
