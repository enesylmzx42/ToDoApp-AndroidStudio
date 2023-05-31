package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.Model.CategoryModel;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    //widgets
    private EditText txt_add_task;
    private Button btn_add_task;

    private DataBaseHelper myDB;
    
    int userId, categoryId, id;
    String task;
    CategoryModel category;

    Spinner spinner;
    ArrayAdapter<String> adapter;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_task, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        userId = bundle.getInt("userId");
        id = bundle.getInt("id");
        task = bundle.getString("task");

        txt_add_task = view.findViewById(R.id.txt_add_task);
        btn_add_task = view.findViewById(R.id.btn_add_task);

        myDB = new DataBaseHelper(getActivity());

        List<String> categoryNames=new ArrayList<>();
        for (CategoryModel categoryModel : myDB.getAllCategories(userId)){
            categoryNames.add(categoryModel.getName());
        }
        String[] categoryList = new String[categoryNames.size()];
        categoryList = categoryNames.toArray(categoryList);
        spinner = view.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = String.valueOf(adapterView.getItemAtPosition(i));

                for (CategoryModel categoryModel : myDB.getAllCategories(userId)){
                    if(categoryModel.getName().equals(selectedItem)){
                        category = categoryModel;
                    }
                }
                Toast.makeText(getContext(), selectedItem + " " + category.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Düzenleme ekranı ilk açıldığında boş yazıyı kabul etmemesi için buton deaktif.
        btn_add_task.setEnabled(false);
        btn_add_task.setBackgroundColor(Color.GRAY);

        //task güncelleniyor mu yoksa 0'dan mı yazılıyor
        //task boş ise 0dan oluşturuluyordur.
        boolean isUpdate = false;

        
        if(task != null){
            isUpdate = true;

            txt_add_task.setText(task);

            if(task.length() > 0){
                btn_add_task.setEnabled(false);
            }
        }
        txt_add_task.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    btn_add_task.setEnabled(false);
                    btn_add_task.setBackgroundColor(Color.GRAY);
                }else{
                    btn_add_task.setEnabled(true);
                    btn_add_task.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = txt_add_task.getText().toString();

                if(finalIsUpdate){
                    myDB.updateTask(id, text);
                }else{
                    ToDoModel item = new ToDoModel();
                    item.setUserId(userId);
                    item.setCategoryId(category.getId());
                    item.setTask(text);
                    item.setStatus(0);
                    myDB.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
