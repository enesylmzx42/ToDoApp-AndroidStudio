package com.example.todoapp.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.CategoryModel;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.Collections;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB, MainActivity activity){
        this.myDB = myDB;
        this.activity = activity;
    }

    public ToDoAdapter(DataBaseHelper myDB){
        this.myDB = myDB;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = todoList.get(position);
        holder.myCheckbox.setText(item.getTask());
        holder.myCheckbox.setChecked(toBoolean(item.getStatus()));
        for (CategoryModel categoryModel : myDB.getAllCategories(item.getUserId())){
            if (categoryModel.getId() == item.getCategoryId()){
                holder.txt_task_category_tag.setText(categoryModel.getName());
            }
        }


        //veritabanındaki todonun status değeri 1(true) ise görünümü değiştirir.
        if(toBoolean(item.getStatus())){
            holder.task_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.task_checked));
            holder.myCheckbox.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.btn_checked));
        }
        holder.myCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    myDB.updateStatus(item.getId(), 1);
                    holder.task_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.task_checked));
                    holder.myCheckbox.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.btn_checked));
                }else{
                    myDB.updateStatus(item.getId(), 0);
                    holder.task_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_blue));
                    holder.myCheckbox.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.purple));
                }
            }
        });
    }
    
    //num == 0 --> return false
    public boolean toBoolean(int num){
        return num != 0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;

        //notifies the adapter reload data
        notifyDataSetChanged();
    }

    public List<ToDoModel> getTodoList(int userId){
        setTasks(myDB.getAllTasks(userId));
        return todoList;
    }

    public void deleteTask(int position, int userId){
        ToDoModel item = getTodoList(userId).get(position);
        myDB.deleteTask(item.getId());
        todoList.remove(position);
        this.setTasks(todoList);
        //this.updateTasks(userId);
        notifyItemRemoved(position);
    }

    public void deleteTask(int position){
        ToDoModel item = todoList.get(position);
        myDB.deleteTask(item.getId());
        todoList.remove(position);
        CategoryAdapter adapter = new CategoryAdapter(myDB);
        adapter.updateCategories(item.getUserId());

        notifyItemRemoved(position);
    }

    public void updateTasks(int userId){
        todoList = myDB.getAllTasks(userId);
        Collections.reverse(todoList);
        this.setTasks(todoList);
        this.notifyDataSetChanged();
    }
    
    public void editTask(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putInt("userId", item.getUserId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
