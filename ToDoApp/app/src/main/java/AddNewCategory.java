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
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.Model.CategoryModel;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewCategory extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewCategory";

    private EditText txt_add_category;
    private Button btn_add_category;

    private DataBaseHelper myDB;

    int categoryId, userId;
    String categoryName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        userId = bundle.getInt("userId");
        categoryId = bundle.getInt("categoryId");
        categoryName = bundle.getString("categoryName");

        txt_add_category = view.findViewById(R.id.txt_add_category);
        btn_add_category = view.findViewById(R.id.btn_add_category);

        myDB = new DataBaseHelper(getActivity());

        //Düzenleme ekranı ilk açıldığında boş yazıyı kabul etmemesi için buton deaktif.
        btn_add_category.setEnabled(false);
        btn_add_category.setBackgroundColor(Color.GRAY);

        //task güncelleniyor mu yoksa 0'dan mı yazılıyor
        //task boş ise 0dan oluşturuluyordur.
        boolean isUpdate = false;

        if(categoryName != null){
            isUpdate = true;

            txt_add_category.setText(categoryName);

            if(categoryName.length() > 0){
                btn_add_category.setEnabled(false);
            }
        }
        txt_add_category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    btn_add_category.setEnabled(false);
                    btn_add_category.setBackgroundColor(Color.GRAY);
                }else{
                    btn_add_category.setEnabled(true);
                    btn_add_category.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = txt_add_category.getText().toString();

                if(finalIsUpdate){
                    myDB.updateCategory(categoryId, text);
                }else{
                    CategoryModel item = new CategoryModel();
                    item.setName(text);
                    item.setUserId(userId);
                    myDB.insertCategory(item);
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
