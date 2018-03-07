package com.sang.peoplan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by sanginLee on 2018-01-10.
 */
// dialog : 알림창
public class DayScheduleDialog extends Dialog {
    public String day;
    public Context mContext;
    RecyclerView eventList;


    public DayScheduleDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public DayScheduleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected DayScheduleDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public void setDay(String day) {
        this.day = day;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 일정추가 물음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_day_schedule);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView dayText = findViewById(R.id.dialog_day_text);

        FloatingActionButton fab = findViewById(R.id.dialog_floating_bt);
        eventList = findViewById(R.id.event_recyclerview);
        ArrayList<Event> myEvent = new ArrayList<>();
        //서버에서 해당 유저에 관한 eventlist 불러오기

        dayText.setText(this.day);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Activity activity = (Activity) mContext;

                Intent intent = new Intent(getContext(), CreateScheduleActivity.class); // 스케줄 추가 액티비티 실행
                intent.putExtra("day", day);
                getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_noanim);
            }
        });

        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList.setAdapter(new RecyclerViewAdapter(myEvent));
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{
        private ArrayList<Event> myEvent;

        Context mContext;

        public RecyclerViewAdapter(ArrayList<Event> myEvent){
            this.myEvent = myEvent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.planner_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Event event = myEvent.get(position);
            String eventName = event.getName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            String eventTime = dateFormat.format(event.getStart()) + " ~ " + dateFormat.format(event.getEnd());

            viewHolder.setEventName(eventName);
            viewHolder.setEventTime(eventTime);
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        TextView eventTime;

        public ViewHolder(View itemView) {
            super(itemView);

            eventName = findViewById(R.id.event_name);
            eventTime = findViewById(R.id.event_time);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        public void setEventName(String eventName){
            this.eventTime.setText(eventName);
        }

        public void setEventTime(String eventTime){
            this.eventTime.setText(eventTime);
        }
    }
}
