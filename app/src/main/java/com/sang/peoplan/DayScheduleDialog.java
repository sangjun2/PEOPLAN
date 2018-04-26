package com.sang.peoplan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by sanginLee on 2018-01-10.
 */
// dialog : 알림창
public class DayScheduleDialog extends DialogFragment implements EventListFragment.OnFragmentInteractionListener {
    public String day;

    ArrayList<Event> myEvent;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ImageView backButton;
    FloatingActionButton fab;

    public DayScheduleDialog() {
        super();

        myEvent = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_day_schedule, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView dayText = view.findViewById(R.id.dialog_day_text);
        dayText.setText(day);

        fab = view.findViewById(R.id.dialog_floating_bt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Activity activity = (Activity) getContext();

                Intent intent = new Intent(getContext(), CreateScheduleActivity.class); // 스케줄 추가 액티비티 실행
                intent.putExtra("day", day);
                getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_noanim);
            }
        });

        backButton = view.findViewById(R.id.event_detail_back_bt);

        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        EventListFragment eventListFragment = EventListFragment.newInstance();
        eventListFragment.dayScheduleDialog = this;
        eventListFragment.myEvents = myEvent;
        fragmentTransaction.add(R.id.event_frame, eventListFragment);
        fragmentTransaction.commit();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onFragmentInteraction(Event event) {
        backButton.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);

        final Dialog dialog = getDialog();

        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        final EventDetailFragment eventDetailFragment = EventDetailFragment.newInstance(event);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();

                backButton.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);

                if(dialog != null) {
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                }
            }
        });

        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        fragmentTransaction.replace(R.id.event_frame, eventDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
