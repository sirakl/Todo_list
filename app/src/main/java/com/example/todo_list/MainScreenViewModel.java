package com.example.todo_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainScreenViewModel extends ViewModel {
	private final AppDataBase appDataBase;
	private final MutableLiveData<List<Task>> tasksLiveData;
	private final MutableLiveData<TasksFilter> filterLiveData;

	public MainScreenViewModel(AppDataBase appDataBase) {
		this.appDataBase = appDataBase;
		this.tasksLiveData = new MutableLiveData<>();
		this.filterLiveData = new MutableLiveData<>(TasksFilter.ALL);

		AppExecutor.getInstance().diskIO().execute(() -> {
			List<Task> tasks = appDataBase.taskDao().loadAllTaskSync();
			tasksLiveData.postValue(tasks);
		});
	}

	public LiveData<List<Task>> getTasksLiveData() {
		return tasksLiveData;
	}

	public LiveData<TasksFilter> getFilterLiveData() {
		return filterLiveData;
	}

	public void onFilterSelected(TasksFilter filter) {
		filterLiveData.setValue(filter);
		AppExecutor.getInstance().diskIO().execute(() -> {
			List<Task> filteredTasks = appDataBase.taskDao().loadAllTaskSync(); // TODO - треба реально зробити фільтр тасків по комплітед
			tasksLiveData.postValue(filteredTasks);
		});
	}

	public void onSearchQueryChanged(String searchQuery) {
		AppExecutor.getInstance().diskIO().execute(() -> {
			List<Task> filteredTasks = appDataBase.taskDao().loadAllTaskSync(); // TODO - треба реально зробити фільтр тасків по назві
			tasksLiveData.postValue(filteredTasks);
		});
	}
}
