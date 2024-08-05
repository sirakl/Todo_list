package com.example.todo_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainScreenViewModelFactory extends ViewModelProvider.NewInstanceFactory {
	private final AppDataBase appDataBase;

	public MainScreenViewModelFactory(AppDataBase appDataBase) {
		this.appDataBase = appDataBase;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		return (T) new MainScreenViewModel(appDataBase);
	}
}
