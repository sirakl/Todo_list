package com.example.todo_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditorScreenViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final AppDataBase appDataBase;
    private final long id;

    public EditorScreenViewModelFactory(AppDataBase db, long taskId) {

        appDataBase = db;
        id = taskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditorScreenViewModel(appDataBase,id);
    }
}
