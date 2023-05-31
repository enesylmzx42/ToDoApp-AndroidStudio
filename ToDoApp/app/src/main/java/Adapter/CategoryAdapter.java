package com.example.todoapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewCategory;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.CategoryModel;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.OnDialogCloseListener;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.Collections;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> implements OnDialogCloseListener{

    List<CategoryModel> categoryList;
    MainActivity activity;
    private DataBaseHelper myDB;


    public CategoryAdapter(DataBaseHelper myDB, MainActivity activity) {
        this.myDB = myDB;
        this.activity = activity;
    }

    public CategoryAdapter(DataBaseHelper myDB){
        this.myDB = myDB;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_category_name;
        RelativeLayout category_layout;
        ImageButton btn_category_more_events;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_category_name = itemView.findViewById(R.id.txt_category_name);
            category_layout = itemView.findViewById(R.id.category_layout);
            btn_category_more_events = itemView.findViewById(R.id.btn_category_more_events);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final CategoryModel category = categoryList.get(position);
        holder.txt_category_name.setText(category.getName().toString().toUpperCase());
//        int total = 0;
//        for (ToDoModel todo : myDB.getAllTasks(category.getUserId())){
//            if (todo.getCategoryId() == category.getId()){
//                total++;
//            }
//        }
//        holder.txt_category_count.setText(String.valueOf(total));

        //Category menu (edit ,delete)
        holder.btn_category_more_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity, holder.itemView);
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_edit_category:
                            Toast.makeText(activity, "edit", Toast.LENGTH_SHORT).show();
                            editCategory(holder.getPosition());
                            return true;
                        case R.id.menu_delete_category:
                            Toast.makeText(activity, "delete", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                            dialogBuilder.setTitle("Delete Category");
                            dialogBuilder.setMessage("Are You Sure?");
                            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (ToDoModel toDoModel : myDB.getAllTasks(category.getUserId())){
                                        if (toDoModel.getCategoryId() == category.getId()){
                                            myDB.deleteTask(toDoModel.getId());
                                        }
                                    }
                                    activity.onDialogClose(dialogInterface);
                                    deleteCategory(holder.getPosition());
                                }
                            });
                            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    notifyItemChanged(holder.getPosition());
                                }
                            });
                            //Show the alert window
                            AlertDialog dialog = dialogBuilder.create();
                            dialog.show();
                            return true;
                        default:
                            return false;
                    }
                });
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public Context getContext(){
        return activity;
    }

    public void setCategories(List<CategoryModel> categoryList){
        this.categoryList = categoryList;

        //notifies the adapter reload data
        notifyDataSetChanged();
    }

    public void deleteCategory(int position){
        CategoryModel item = categoryList.get(position);
        myDB.deleteCategory(item.getId());
        categoryList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateCategories(int userId){
        categoryList = myDB.getAllCategories(userId);
        Collections.reverse(categoryList);
        this.setCategories(categoryList);
        this.notifyDataSetChanged();
    }

    public void editCategory(int position){
        CategoryModel item = categoryList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", item.getId());
        bundle.putString("categoryName", item.getName());

        AddNewCategory category = new AddNewCategory();
        category.setArguments(bundle);
        category.show(activity.getSupportFragmentManager(), category.getTag());
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        activity.onDialogClose(dialogInterface);
    }
}
