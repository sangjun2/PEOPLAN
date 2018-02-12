package com.sang.peoplan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.ToggleButtonGroup;
import com.nex3z.togglebuttongroup.button.CircularToggle;

/**
 * Created by Sangjun on 2018-02-06.
 */

public class WeekdaysGroup extends LinearLayout {
    MultiSelectToggleGroup group;
    CircularToggle sunToggle;
    CircularToggle monToggle;
    CircularToggle tueToggle;
    CircularToggle wedToggle;
    CircularToggle thuToggle;
    CircularToggle friToggle;
    CircularToggle satToggle;
    LinearLayout isCheckedLayout;


    public WeekdaysGroup(Context context) {
        super(context);
    }

    public WeekdaysGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        final View view = inflater.inflate(R.layout.group_weekdays, this, false);
        addView(view);

        group = view.findViewById(R.id.group_weekdays);

        sunToggle = view.findViewById(R.id.weekdays_sun);
        monToggle = view.findViewById(R.id.weekdays_mon);
        tueToggle = view.findViewById(R.id.weekdays_tue);
        wedToggle = view.findViewById(R.id.weekdays_wed);
        thuToggle = view.findViewById(R.id.weekdays_thu);
        friToggle = view.findViewById(R.id.weekdays_fri);
        satToggle = view.findViewById(R.id.weekdays_sat);
        isCheckedLayout = view.findViewById(R.id.weekdays_check_layout);

        group.setOnCheckedChangeListener(new MultiSelectToggleGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiSelectToggleGroup group, int checkedId, boolean isChecked) {
                View addView = inflater.inflate(R.layout.checked_weekday, null, false);
                CircularToggle toggle = addView.findViewById(R.id.checked_day);

                if(isChecked) {
                    if(checkedId == sunToggle.getId()) {
                        addView.setTag("sun");
                        toggle.setText(sunToggle.getText().toString());
                        isCheckedLayout.addView(addView);
                    } else if(checkedId == monToggle.getId()) {
                        addView.setTag("mon");
                        toggle.setText(monToggle.getText().toString());
                        isCheckedLayout.addView(addView);
                    } else if(checkedId == tueToggle.getId()) {
                        addView.setTag("tue");
                        toggle.setText(tueToggle.getText().toString());
                        isCheckedLayout.addView(addView);
                    } else if(checkedId == wedToggle.getId()) {
                        addView.setTag("wed");
                        toggle.setText(wedToggle.getText().toString());
                        isCheckedLayout.addView(addView);
                    } else if(checkedId == thuToggle.getId()) {
                        addView.setTag("thu");
                        toggle.setText(thuToggle.getText().toString());
                        isCheckedLayout.addView(addView);
                    } else if(checkedId == friToggle.getId()) {
                        addView.setTag("fri");
                        toggle.setText(friToggle.getText().toString());
                        isCheckedLayout.addView(addView);
                    } else if(checkedId == satToggle.getId()) {
                        addView.setTag("sat");
                        toggle.setText(satToggle.getText().toString());
                        isCheckedLayout.addView(addView);
                    }
                } else {
                    if(checkedId == sunToggle.getId()) {
                        isCheckedLayout.removeView(isCheckedLayout.findViewWithTag("sun"));
                    } else if(checkedId == monToggle.getId()) {
                        isCheckedLayout.removeView(isCheckedLayout.findViewWithTag("mon"));
                    } else if(checkedId == tueToggle.getId()) {
                        isCheckedLayout.removeView(isCheckedLayout.findViewWithTag("tue"));
                    } else if(checkedId == wedToggle.getId()) {
                        isCheckedLayout.removeView(isCheckedLayout.findViewWithTag("wed"));
                    } else if(checkedId == thuToggle.getId()) {
                        isCheckedLayout.removeView(isCheckedLayout.findViewWithTag("thu"));
                    } else if(checkedId == friToggle.getId()) {
                        isCheckedLayout.removeView(isCheckedLayout.findViewWithTag("fri"));
                    } else if(checkedId == satToggle.getId()) {
                        isCheckedLayout.removeView(isCheckedLayout.findViewWithTag("sat"));
                    }
                }
            }
        });

        for(int i = 0; i < isCheckedLayout.getChildCount(); i++){
            isCheckedLayout.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
