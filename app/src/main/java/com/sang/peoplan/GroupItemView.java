package com.sang.peoplan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sangjun on 2018-03-26.
 */

public class GroupItemView extends ConstraintLayout {
    public SquareImageView imageView;
    public LinearLayout textLayout;
    public GroupItemViewHolder viewHolder;
    ViewGroup viewGroup;

    public GroupItemView(Context context) {
        super(context);
        initView();
    }

    public GroupItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GroupItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public GroupItemView(Context context, ViewGroup viewGroup) {
        super(context);
        this.viewGroup = viewGroup;
        initView();
    }

    public void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        final View view = inflater.inflate(R.layout.group_list_item_view, this.viewGroup, false);
        viewHolder = new GroupItemViewHolder(view);

        imageView = view.findViewById(R.id.group_item_image);
        textLayout = view.findViewById(R.id.group_item_textlayout);
        textLayout.getLayoutParams().height = imageView.getHeight() / 3;
    }

    public class GroupItemViewHolder extends RecyclerView.ViewHolder {
        Group group;

        TextView title;
        TextView category;
        TextView member;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            group = null;

            title = itemView.findViewById(R.id.group_list_item_title);
            category = itemView.findViewById(R.id.group_list_item_category);
            member = itemView.findViewById(R.id.group_list_item_member);
        }

        public void bind() {
            title.setText(group.getName());
            category.setText(group.getCategory());
            member.setText("멤버 " + group.getMembers().size() + "명");
        }
    }
}
