package com.sang.peoplan;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Sangjun on 2018-04-12.
 */

public class ColorPickerDialog extends DialogFragment {
    Button refreshButton;
    Button confirmButton;
    RecyclerView recyclerView;
    ColorRecyclerViewAdapter adapter;

    CreateScheduleActivity createScheduleActivity;
    private OnFragmentInteractionListener mListener;

    public static final String DEFAULT_COLOR_CODE = "8CACF9";

    public ColorPickerDialog() {
        super();
    }

    public static ColorPickerDialog newInstance() {
        ColorPickerDialog dialog = new ColorPickerDialog();

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_color_picker, container);

        refreshButton = view.findViewById(R.id.color_picker_refresh_bt);
        confirmButton = view.findViewById(R.id.color_picker_confirm_bt);
        recyclerView = view.findViewById(R.id.color_picker_recyclerview);

        String[] codeList = new Color().getColorList(15);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false));
        adapter = new ColorRecyclerViewAdapter(codeList);
        recyclerView.setAdapter(adapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] newCodeList = new Color().getColorList(15);
                adapter = new ColorRecyclerViewAdapter(newCodeList);
                recyclerView.setAdapter(adapter);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = adapter.code;
                onConfirmSelected(code);
                dismiss();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onConfirmSelected(String code) {
        if (mListener != null) {
            mListener.onFragmentInteraction(code);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (createScheduleActivity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) createScheduleActivity;
        } else {
            throw new RuntimeException(createScheduleActivity.toString()
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
        void onFragmentInteraction(String code);
    }

    public class ColorRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        String[] codeList;
        Button selectedButton;
        String code;

        public ColorRecyclerViewAdapter(String[] codeList) {
            this.codeList = codeList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View view = LayoutInflater.from(mContext).inflate(R.layout.color_item, parent, false);
            ColorViewHolder viewHolder = new ColorViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ColorViewHolder viewHolder = (ColorViewHolder) holder;

            if(position == 0) {
                viewHolder.imageViewButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                viewHolder.imageViewButton.setText("V");
                viewHolder.colorCode = DEFAULT_COLOR_CODE;
                selectedButton = viewHolder.imageViewButton;
                code = DEFAULT_COLOR_CODE;
            } else {
                viewHolder.imageViewButton.setBackgroundColor(android.graphics.Color.parseColor("#" + codeList[position - 1]));
                viewHolder.colorCode = codeList[position - 1];
            }
        }

        @Override
        public int getItemCount() {
            return 16;
        }

        public class ColorViewHolder extends RecyclerView.ViewHolder {
            Button imageViewButton;
            String colorCode;

            public ColorViewHolder(View itemView) {
                super(itemView);

                imageViewButton = itemView.findViewById(R.id.color_item_imageview);
                imageViewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!imageViewButton.getText().toString().equals("V")) {
                            imageViewButton.setText("V");
                            selectedButton.setText("");
                            selectedButton = imageViewButton;
                            code = colorCode;
                        }
                    }
                });
            }
        }
    }
}
