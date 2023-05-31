package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.Model.UserModel;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


    //Variable definitions
    Button btn_login, btn_register;
    EditText txt_email, txt_password;
    private SQLiteDatabase db;
    DataBaseHelper myDB;
    UserModel user = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Variable assignments
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);

        myDB = new DataBaseHelper(getApplicationContext());
        db = this.openOrCreateDatabase("TODO_DATABASE", MODE_PRIVATE, null);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){


                    user = myDB.getUserFromEmail(email);

                    if(email.equals(user.getEmail()) && password.equals(user.getPassword())){
                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       Toast.makeText(SplashActivity.this, "Giriş Yapıldı. " + user.getPassword(), Toast.LENGTH_SHORT).show();
                       intent.putExtra("userMail", user.getEmail());
                       startActivity(intent);
                       txt_email.setText("");
                       txt_password.setText("");

                    }else{
                        Toast.makeText(SplashActivity.this, "E-Posta veya şifre hatalı!", Toast.LENGTH_SHORT).show();

//                        db.execSQL("INSERT INTO TODO_USER (EMAIL, PASSWORD) VALUES ('"+ email +"','"+ password +"')");
//                        Cursor b = db.rawQuery("SELECT * FROM TODO_USER WHERE EMAIL=('" + email + "')", null);
//                        b.moveToNext();
//                        int id = b.getColumnIndex("ID");
//                        user.setId(b.getInt(id));
//                        Toast.makeText(SplashActivity.this, "Yeni Kullanıcı Oluşturuldu!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.putExtra("a", user.getId());
//                        startActivity(intent);
                    }

                }else{
                    Toast.makeText(SplashActivity.this, "Tüm Alanları Doldurmalısınız!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

    }
}