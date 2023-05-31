package com.example.todoapp.Model;

import java.util.List;

public class CategoryModel {

    int id, userId;
    String name;
    List<ToDoModel> toDoModelList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ToDoModel> getToDoModelList() {
        return toDoModelList;
    }

    public void setToDoModelList(List<ToDoModel> toDoModelList) {
        this.toDoModelList = toDoModelList;
    }
}
