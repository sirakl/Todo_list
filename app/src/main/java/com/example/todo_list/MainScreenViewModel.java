package com.example.todo_list;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainScreenViewModel extends AndroidViewModel {

    private LiveData<List<Task>> taskList;


    public MainScreenViewModel(@NonNull Application application) {
        super(application);

        AppDataBase appDataBase = AppDataBase.getInstance(this.getApplication());
        Log.i("View Model"," Retrieving data from database");
        taskList =  appDataBase.taskDao().loadAllTask();

    }


    public LiveData<List<Task>> getTaskList() {
        return taskList;
    }
}
