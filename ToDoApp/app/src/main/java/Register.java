package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.UserModel;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.List;

public class Register extends AppCompatActivity {

    EditText txt_name, txt_surname, txt_mail, txt_password;
    Button btn_register;
    private SQLiteDatabase db;
    UserModel user = new UserModel();
    List<UserModel> userList;
    DataBaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_name = findViewById(R.id.txt_name);
        txt_surname = findViewById(R.id.txt_surname);
        txt_mail = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);

        btn_register = findViewById(R.id.btn_register);

        myDB = new DataBaseHelper(Register.this);
        db = this.openOrCreateDatabase("TODO_TABLE", MODE_PRIVATE, null);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_name.getText().toString().isEmpty() && !txt_surname.getText().toString().isEmpty() && !txt_mail.getText().toString().isEmpty() && !txt_password.getText().toString().isEmpty()){

//                    Cursor c = db.rawQuery("SELECT * FROM TODO_USER", null);
//                    int mailIndex = c.getColumnIndex("EMAIL");
//                    boolean findUser = false;
//                    while(c.moveToNext()){
//
//                        if (c.getString(mailIndex).equals(txt_mail.getText().toString())){
//                            findUser = true;
//                        }
//                    }


                    if(myDB.findUserFromEmail(txt_mail.getText().toString())){
                        Toast.makeText(Register.this, "Bu E-Postaya Kayıtlı Kullanıcı Bulunmaktadır!", Toast.LENGTH_SHORT).show();
                    }else{
                        //db.execSQL("INSERT INTO TODO_USER(NAME, SURNAME, EMAIL, PASSWORD) VALUES ('"+ txt_name.getText().toString() +"','"+ txt_surname.getText().toString() +"' ,'"+ txt_mail.getText().toString() +"','"+ txt_password.getText().toString() +"')");
                        user.setName(txt_name.getText().toString());
                        user.setSurname(txt_surname.getText().toString());
                        user.setEmail(txt_mail.getText().toString());
                        user.setPassword(txt_password.getText().toString());
                        myDB.insertUser(user);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(Register.this, "Kayıt Olundu, Anasayfaya aktarılıyorsunuz! " + user.getName(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("userMail", user.getEmail());
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(Register.this, "Lütfen Tüm Alanları Doldurunuz!", Toast.LENGTH_SHORT).show();
                }
                
            }
        });



    }


}