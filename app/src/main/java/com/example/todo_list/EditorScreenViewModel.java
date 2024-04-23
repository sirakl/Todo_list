package com.example.todo_list;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EditorScreenViewModel extends ViewModel {


    private LiveData<Task> task;

    public EditorScreenViewModel(AppDataBase appDataBase, long id) {

        task = appDataBase.taskDao().getById(id);
        Log.i(" Editor View Model "," Loading a task");

    }

    public LiveData<Task> getTask() {

        return task;
    }
}