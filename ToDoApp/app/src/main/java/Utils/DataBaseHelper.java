package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.CategoryModel;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db = null;

    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_TODO = "TODO_TABLE";
    private static final String TABLE_USER = "TODO_USER";
    private static final String TABLE_CATEGORY = "TODO_CATEGORY";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TODO + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER, CATEGORYID INTEGER, TASK TEXT, STATUS INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, EMAIL TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER, NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        if (newVersion > oldVersion) {
//            db.execSQL("ALTER TABLE " + TABLE_TODO + " ADD COLUMN CATEGORYID INTEGER");
//            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER, NAME TEXT)");
//        }

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
    
    public List<UserModel> getAllUsers(){
        db = this.getReadableDatabase();
        List<UserModel> userList = new ArrayList<>();
        Cursor b = db.rawQuery("SELECT * FROM TODO_USER", null);
        int idIndex = b.getColumnIndex("ID");
        int nameIndex = b.getColumnIndex("NAME");
        int surnameIndex = b.getColumnIndex("SURNAME");
        int mailIndex = b.getColumnIndex("EMAIL");
        int passwordIndex = b.getColumnIndex("PASSWORD");
        UserModel user = new UserModel();
        
        while (b.moveToNext()){
            user.setId(b.getInt(idIndex));
            user.setName(b.getString(nameIndex));
            user.setSurname(b.getString(surnameIndex));
            user.setEmail(b.getString(mailIndex));
            user.setPassword(b.getString(passwordIndex));
            userList.add(user);
        }

        return userList;
    }

    
    public UserModel getUser(int userId){
        db = this.getReadableDatabase();
        UserModel user = new UserModel();
        Cursor b = db.rawQuery("SELECT * FROM TODO_USER", null);
        int idIndex = b.getColumnIndex("ID");
        int nameIndex = b.getColumnIndex("NAME");
        int surnameIndex = b.getColumnIndex("SURNAME");
        int mailIndex = b.getColumnIndex("EMAIL");
        int passwordIndex = b.getColumnIndex("PASSWORD");


        while (b.moveToNext()){
            if (userId == b.getInt(idIndex)){
                user.setId(b.getInt(idIndex));
                user.setName(b.getString(nameIndex));
                user.setSurname(b.getString(surnameIndex));
                user.setEmail(b.getString(mailIndex));
                user.setPassword(b.getString(passwordIndex));
            }
        }

        return user;
    }

    public UserModel getUserFromEmail(String email){
        db = this.getReadableDatabase();
        UserModel user = new UserModel();
        Cursor b = db.rawQuery("SELECT * FROM TODO_USER", null);
        int idIndex = b.getColumnIndex("ID");
        int nameIndex = b.getColumnIndex("NAME");
        int surnameIndex = b.getColumnIndex("SURNAME");
        int mailIndex = b.getColumnIndex("EMAIL");
        int passwordIndex = b.getColumnIndex("PASSWORD");


        while (b.moveToNext()){
            if (email.equals(b.getString(mailIndex))){
                user.setId(b.getInt(idIndex));
                user.setName(b.getString(nameIndex));
                user.setSurname(b.getString(surnameIndex));
                user.setEmail(b.getString(mailIndex));
                user.setPassword(b.getString(passwordIndex));
            }
        }

        return user;
    }
    
    public boolean findUserFromEmail(String email){
        db = this.getWritableDatabase();
        Cursor b = db.rawQuery("SELECT * FROM TODO_USER", null);
        int mailIndex = b.getColumnIndex("EMAIL");

        while(b.moveToNext()){
            if(email.equals(b.getString(mailIndex))){
                return true;
            }
        }

        return false;

    }

    public void insertUser(UserModel user){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", user.getName());
        values.put("SURNAME", user.getSurname());
        values.put("EMAIL", user.getEmail());
        values.put("PASSWORD", user.getPassword());
        db.insert(TABLE_USER, null, values);
    }

    public void updateUser(UserModel user){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", user.getName());
        values.put("SURNAME", user.getSurname());
        values.put("EMAIL", user.getEmail());
        values.put("PASSWORD", user.getPassword());
        db.update(TABLE_USER, values, "ID=?", new String[]{String.valueOf(user.getId())});
    }

    public void updateUserPassword(UserModel user){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PASSWORD",user.getPassword());
        db.update(TABLE_USER, values, "ID=?", new String[]{String.valueOf(user.getId())});
    }

    public void insertTask(ToDoModel todo){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CATEGORYID", todo.getCategoryId());
        values.put("USERID", todo.getUserId());
        values.put(COL_2, todo.getTask());
        values.put(COL_3, 0);
        db.insert(TABLE_TODO, null, values);
    }

    public void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        db.update(TABLE_TODO, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);
        db.update(TABLE_TODO, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_TODO, "ID=?", new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(int userId){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> todoList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_TODO, null, null, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    ToDoModel todo = new ToDoModel();
                    if(cursor.getInt(cursor.getColumnIndex("USERID")) == userId){
                        todo.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        todo.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        todo.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        todo.setUserId(userId);
                        todo.setCategoryId(cursor.getInt(cursor.getColumnIndex("CATEGORYID")));
                        todoList.add(todo);
                    }

                }while(cursor.moveToNext());
            }
        }finally {
            db.endTransaction();
            //cursor.close();
        }
        return todoList;
    }

    public void insertCategory(CategoryModel category){
        System.out.println("Çalıştı");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERID", category.getUserId());
        values.put("NAME", category.getName());
        db.insert(TABLE_CATEGORY, null, values);
    }

    public void updateCategory(int id, String categoryName){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", categoryName);
        db.update(TABLE_CATEGORY, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteCategory(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<CategoryModel> getAllCategories(int userId){
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<CategoryModel> categoryList = new ArrayList<>();

        db.beginTransaction();
        try{
            cursor = db.query(TABLE_CATEGORY, null, null, null, null, null, null);
            if(cursor.moveToFirst()){

                int idIndex = cursor.getColumnIndex("ID");
                int userIdIndex = cursor.getColumnIndex("USERID");
                int nameIndex = cursor.getColumnIndex("NAME");
                do{
                    CategoryModel category = new CategoryModel();
                    if (cursor.getInt(userIdIndex) == userId){
                        category.setId(cursor.getInt(idIndex));
                        category.setUserId(cursor.getInt(userIdIndex));
                        category.setName(cursor.getString(nameIndex));
                        categoryList.add(category);
                    }


                }while(cursor.moveToNext());
            }
        }finally {
            db.endTransaction();
            //cursor.close();
        }
        System.out.println(categoryList.size());
        for(CategoryModel categoryModel : categoryList){

            System.out.println(categoryModel.getName());
        }

        return categoryList;
    }

}
