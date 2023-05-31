package com.example.todoapp.Adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    CheckBox myCheckbox;
    RelativeLayout task_layout;
    TextView txt_task_category_tag;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        myCheckbox = itemView.findViewById(R.id.checkbox);
        task_layout = itemView.findViewById(R.id.task_layout);
        txt_task_category_tag = itemView.findViewById(R.id.txt_task_category_tag);
    }
}
