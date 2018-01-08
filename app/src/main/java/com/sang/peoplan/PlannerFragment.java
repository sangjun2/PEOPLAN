package com.sang.peoplan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

public class PlannerFragment extends Fragment {

    public PlannerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlannerFragment newInstance() {
        PlannerFragment fragment = new PlannerFragment();
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
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        PlannerView plannerView = view.findViewById(R.id.planner);
        plannerView.initView(new LocalDate());

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
}
