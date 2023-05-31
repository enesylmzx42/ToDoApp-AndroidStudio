package com.example.todoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.Adapter.CategoryAdapter;
import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.CategoryModel;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.UserModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener, PopupMenu.OnMenuItemClickListener {

    TextView txt_welcome_message, txt_user_name, txt_user_surname;
    RelativeLayout layout_menu, layout_home;
    
    private RecyclerView todosRecyclerView;
    private RecyclerView categoryRecyclerView;
    private FloatingActionButton btn_floating_action;
    private DataBaseHelper myDB;
    private List<ToDoModel> todoList;
    private List<CategoryModel> categoryList;
    private ToDoAdapter todoAdapter;
    private CategoryAdapter categoryAdapter;

    private String userMail;
    private String name, surname;

    Button btn_add_category, btn_menu, btn_menu_back, btn_profile_page, btn_logout;
    
    UserModel user = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation zoom_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        Animation zoom_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        txt_welcome_message = findViewById(R.id.txt_welcome_message);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_user_surname = findViewById(R.id.txt_user_surname);

        btn_floating_action = findViewById(R.id.btn_floating_action);
        myDB = new DataBaseHelper(MainActivity.this);
        todoList = new ArrayList<>();
        categoryList = new ArrayList<>();
        todoAdapter = new ToDoAdapter(myDB, MainActivity.this);
        categoryAdapter = new CategoryAdapter(myDB, MainActivity.this);

        
        todosRecyclerView = findViewById(R.id.recyclerview);
        todosRecyclerView.setHasFixedSize(true);
        todosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todosRecyclerView.setAdapter(todoAdapter);

        categoryRecyclerView = findViewById(R.id.recyclerview_category);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        Intent intent = getIntent();
        userMail = intent.getStringExtra("userMail");
        user = myDB.getUserFromEmail(userMail);

        txt_welcome_message.setText("HOŞ GELDİN, " + user.getName().toUpperCase());
        txt_user_name.setText(user.getName().toUpperCase());
        txt_user_surname.setText(user.getSurname().toUpperCase());
        
        layout_menu = findViewById(R.id.layout_menu);
        layout_home = findViewById(R.id.layout_home);

        categoryList = myDB.getAllCategories(user.getId());
        Collections.reverse(categoryList);
        categoryAdapter.setCategories(categoryList);

        todoList = myDB.getAllTasks(user.getId());
        Collections.reverse(todoList);
        todoAdapter.setTasks(todoList);

        /*
         * Profil butonu ile ilgili işlemler
         */
        btn_profile_page = findViewById(R.id.btn_profile_page);
        btn_profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), ProfilePage.class);
                intent2.putExtra("userId", user.getId());
                startActivity(intent2);
            }
        });


        /*
         * çıkış yap butonu ile ilgili işlemler
         */
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent1);
            }
        });

        /*
         * Menu Animasyonu
         */
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_menu.setVisibility(View.VISIBLE);
                layout_home.setVisibility(View.INVISIBLE);
                layout_home.startAnimation(zoom_in);
            }
        });
        btn_menu_back = findViewById(R.id.btn_menu_back);
        btn_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_home.startAnimation(zoom_out);
                layout_menu.setVisibility(View.INVISIBLE);
            }
        });


        /*
         * Kategori Ekleme Eventi
         */
        btn_add_category = findViewById(R.id.btn_add_category);
        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewCategory category = new AddNewCategory();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", user.getId());
                category.setArguments(bundle);
                category.show(getSupportFragmentManager(), category.getTag());
            }
        });

        /*
         * Todo Ekleme Eventi
         */
        btn_floating_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask task = new AddNewTask();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", user.getId());
                task.setArguments(bundle);
                task.show(getSupportFragmentManager(), task.getTag());


                //AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


        /*
         * Todolarda sağa, sola kaydırma efekti
         */
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(todoAdapter));
        itemTouchHelper.attachToRecyclerView(todosRecyclerView);

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        todoList = myDB.getAllTasks(user.getId());
        Collections.reverse(todoList);
        todoAdapter.setTasks(todoList);

        categoryList = myDB.getAllCategories(user.getId());
        Collections.reverse(categoryList);
        categoryAdapter.setCategories(categoryList);

        todoAdapter.notifyDataSetChanged();
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }
}