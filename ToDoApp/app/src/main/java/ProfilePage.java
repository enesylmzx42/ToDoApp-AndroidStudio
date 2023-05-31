package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.Model.UserModel;
import com.example.todoapp.Utils.DataBaseHelper;

/*
 *  Bu activity kullanıcı profil sayfası hakkındadır.
 */


public class ProfilePage extends AppCompatActivity {

    Button btn_profile_back_to_home, btn_profile_update_user_settings, btn_profile_update_user_password;
    EditText edittxt_profile_user_name, edittxt_profile_user_surname, edittxt_profile_user_mail, edittxt_profile_user_old_password, edittxt_profile_user_new_password, edittxt_profile_user_new_password_repeat;
    TextView txt_profile_password_feedback;
    
    int userId;
    
    SQLiteDatabase db;
    DataBaseHelper myDB;

    UserModel user1 = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        
        /*
         * buttonların eşitlenmesi
         */
        btn_profile_back_to_home = findViewById(R.id.btn_profile_back_to_home);
        btn_profile_update_user_settings = findViewById(R.id.btn_profile_update_user_settings);
        btn_profile_update_user_password = findViewById(R.id.btn_profile_update_user_password);


        /*
         * editTxtlerin eşitlenmesi
         */
        edittxt_profile_user_name = findViewById(R.id.edittxt_profile_user_name);
        edittxt_profile_user_surname = findViewById(R.id.edittxt_profile_user_surname);
        edittxt_profile_user_mail = findViewById(R.id.edittxt_profile_user_mail);
        edittxt_profile_user_old_password = findViewById(R.id.edittxt_profile_user_old_password);
        edittxt_profile_user_new_password = findViewById(R.id.edittxt_profile_user_new_password);
        edittxt_profile_user_new_password_repeat = findViewById(R.id.edittxt_profile_user_new_password_repeat);
        txt_profile_password_feedback = findViewById(R.id.txt_profile_password_feedback);

        //veritabanı eşitlenmesi
        db = this.openOrCreateDatabase("TODO_DATABASE", MODE_PRIVATE, null);
        myDB = new DataBaseHelper(ProfilePage.this);
        
        
        /*
         * Intent ile gönderilen userId'nin yakalanması
         */
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        
        
        /*
         * alınan userId ile database'den kullanıcının çekilmesi
         */
        user1 = myDB.getUser(userId);
        
        
        /*
         * Uygun boşluklara kullanıcının ilgili verilerinin yazılması
         */
        edittxt_profile_user_name.setText(user1.getName());
        edittxt_profile_user_surname.setText(user1.getSurname());
        edittxt_profile_user_mail.setText(user1.getEmail());


        /*
         * Kullanıcı ayarları güncelleme butonu ile ilgili işlemler
         */
        btn_profile_update_user_settings.setEnabled(false);
        btn_profile_update_user_settings.setBackgroundColor(Color.GRAY);
        
        edittxt_profile_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_profile_update_user_settings.setEnabled(true);
                btn_profile_update_user_settings.setBackgroundColor(getResources().getColor(R.color.light_blue));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edittxt_profile_user_name.getText().toString().equals(user1.getName()) && edittxt_profile_user_surname.getText().toString().equals(user1.getSurname()) && edittxt_profile_user_mail.getText().toString().equals(user1.getEmail())){
                    btn_profile_update_user_settings.setEnabled(false);
                    btn_profile_update_user_settings.setBackgroundColor(Color.GRAY);
                }
            }
        });

        edittxt_profile_user_surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_profile_update_user_settings.setEnabled(true);
                btn_profile_update_user_settings.setBackgroundColor(getResources().getColor(R.color.light_blue));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edittxt_profile_user_name.getText().toString().equals(user1.getName()) && edittxt_profile_user_surname.getText().toString().equals(user1.getSurname()) && edittxt_profile_user_mail.getText().toString().equals(user1.getEmail())){
                    btn_profile_update_user_settings.setEnabled(false);
                    btn_profile_update_user_settings.setBackgroundColor(Color.GRAY);
                }
            }
        });

        edittxt_profile_user_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_profile_update_user_settings.setEnabled(true);
                btn_profile_update_user_settings.setBackgroundColor(getResources().getColor(R.color.light_blue));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edittxt_profile_user_name.getText().toString().equals(user1.getName()) && edittxt_profile_user_surname.getText().toString().equals(user1.getSurname()) && edittxt_profile_user_mail.getText().toString().equals(user1.getEmail())){
                    btn_profile_update_user_settings.setEnabled(false);
                    btn_profile_update_user_settings.setBackgroundColor(Color.GRAY);
                }
            }
        });

        btn_profile_update_user_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfilePage.this);
                dialogBuilder.setTitle("Bilgileri Güncelle");
                dialogBuilder.setMessage("Bilgileriniz güncellenecek, emin misiniz?");
                dialogBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user1.setName(edittxt_profile_user_name.getText().toString());
                        user1.setSurname(edittxt_profile_user_surname.getText().toString());
                        user1.setEmail(edittxt_profile_user_mail.getText().toString());
                        myDB.updateUser(user1);
                        Toast.makeText(ProfilePage.this, "Bilgileriniz güncellendi!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("userMail", user1.getEmail());
                        startActivity(intent);
                    }
                });
                dialogBuilder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        
                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                
            }
        });
        
        
        /*
         * Şifre güncelleme alanı ile ilgili işlemler
         */
        btn_profile_update_user_password.setEnabled(false);
        btn_profile_update_user_password.setBackgroundColor(Color.GRAY);
        
        edittxt_profile_user_old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_profile_update_user_password.setEnabled(true);
                btn_profile_update_user_password.setBackgroundColor(getResources().getColor(R.color.light_blue));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edittxt_profile_user_old_password.getText().toString().isEmpty() || edittxt_profile_user_new_password.getText().toString().isEmpty() || edittxt_profile_user_new_password_repeat.getText().toString().isEmpty()){
                    btn_profile_update_user_password.setEnabled(false);
                    btn_profile_update_user_password.setBackgroundColor(Color.GRAY);
                }
            }
        });

        edittxt_profile_user_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_profile_update_user_password.setEnabled(true);
                btn_profile_update_user_password.setBackgroundColor(getResources().getColor(R.color.light_blue));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edittxt_profile_user_old_password.getText().toString().isEmpty() || edittxt_profile_user_new_password.getText().toString().isEmpty() || edittxt_profile_user_new_password_repeat.getText().toString().isEmpty()){
                    btn_profile_update_user_password.setEnabled(false);
                    btn_profile_update_user_password.setBackgroundColor(Color.GRAY);
                }
            }
        });

        edittxt_profile_user_new_password_repeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_profile_update_user_password.setEnabled(true);
                btn_profile_update_user_password.setBackgroundColor(getResources().getColor(R.color.light_blue));
                if (edittxt_profile_user_new_password.getText().toString().equals(edittxt_profile_user_new_password_repeat.getText().toString())){
                    txt_profile_password_feedback.setTextColor(getResources().getColor(R.color.green));
                    txt_profile_password_feedback.setText("Şifreler eşit.");
                }else{
                    txt_profile_password_feedback.setTextColor(getResources().getColor(R.color.RED));
                    txt_profile_password_feedback.setText("Şifreler uyuşmuyor!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edittxt_profile_user_old_password.getText().toString().isEmpty() || edittxt_profile_user_new_password.getText().toString().isEmpty() || edittxt_profile_user_new_password_repeat.getText().toString().isEmpty()){
                    btn_profile_update_user_password.setEnabled(false);
                    btn_profile_update_user_password.setBackgroundColor(Color.GRAY);
                }
            }
        });

        btn_profile_update_user_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edittxt_profile_user_old_password.getText().toString().equals(user1.getPassword())){
                    if (edittxt_profile_user_new_password.getText().toString().equals(edittxt_profile_user_new_password_repeat.getText().toString())){
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfilePage.this);
                        dialogBuilder.setTitle("Şifre Güncelle");
                        dialogBuilder.setMessage("Şifreniz güncellenecek, emin misiniz?");
                        dialogBuilder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user1.setPassword(edittxt_profile_user_new_password.getText().toString());
                                myDB.updateUserPassword(user1);
                                Toast.makeText(ProfilePage.this, "Şifreniz güncellendi!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("userMail", user1.getEmail());
                                startActivity(intent);
                                txt_profile_password_feedback.setText("");
                            }
                        });
                        dialogBuilder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog dialog = dialogBuilder.create();
                        dialog.show();
                    }else{
                        Toast.makeText(ProfilePage.this, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show();
                        edittxt_profile_user_old_password.setText("");
                        edittxt_profile_user_new_password.setText("");
                        edittxt_profile_user_new_password_repeat.setText("");
                        txt_profile_password_feedback.setText("");
                    }
                }else{
                    Toast.makeText(ProfilePage.this, "Hatalı şifre! " + user1.getPassword() + user1.getName(), Toast.LENGTH_SHORT).show();
                    edittxt_profile_user_old_password.setText("");
                    edittxt_profile_user_new_password.setText("");
                    edittxt_profile_user_new_password_repeat.setText("");
                    txt_profile_password_feedback.setText("");
                }
            }
        });

        
        /*
         * Anasayfaya dönüş butonu ile ilgili işlemler
         */
        btn_profile_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userMail", user1.getEmail());
                startActivity(intent);
            }
        });
        
        
    }
}