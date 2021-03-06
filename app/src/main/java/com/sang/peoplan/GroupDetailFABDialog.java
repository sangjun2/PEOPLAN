package com.sang.peoplan;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Sangjun on 2018-04-17.
 */

public class GroupDetailFABDialog extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";

    String group_id;

    public GroupDetailFABDialog() {
    }

    public static GroupDetailFABDialog newInstance(String group_id) {
        GroupDetailFABDialog groupDetailFABDialog = new GroupDetailFABDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, group_id);
        groupDetailFABDialog.setArguments(args);

        return groupDetailFABDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            group_id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_group_detail_add, container);

        ConstraintLayout containerLayout = view.findViewById(R.id.dialog_group_detail_container);
        containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        FloatingActionButton addPlanFAB = view.findViewById(R.id.dialog_add_plan_fab);
        FloatingActionButton writeFAB = view.findViewById(R.id.dialog_write_fab);
        FloatingActionButton closeFAB = view.findViewById(R.id.dialog_close_fab);

        addPlanFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateScheduleActivity.class);
                intent.putExtra("group", true);
                intent.putExtra("group_id", group_id);
                startActivity(intent);
            }
        });

        writeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        closeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }
}
