package com.example.todo_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    AppDataBase appDataBase;
    ToDoListAdapter toDoListAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private int filter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        toDoListAdapter = new ToDoListAdapter(this);
        appDataBase = AppDataBase.getInstance(getApplicationContext());
        recyclerView.setAdapter(toDoListAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                final List<Task> tasks = toDoListAdapter.getTasks();

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDataBase.taskDao().deleteTask(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

        getTasks();
    }

    private void getTasks() {
        MainScreenViewModel viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainScreenViewModel.class);
        viewModel.getTaskList().observe(MainActivity.this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                if (tasks != null) {
                    List<Task> filteredTasks = filterTasks(tasks);
                    toDoListAdapter.setTasks(filteredTasks);
                }
            }
        });
    }

    private List<Task> filterTasks(List<Task> tasks) {
        return tasks.stream().filter(task -> {
            switch (filter) {
                case 1:
                    return task.isCompleted();
                case 2:
                    return !task.isCompleted();
                default:
                    return true;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        final String[] options = {"All", "Completed", "Not Completed"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter Tasks");
        builder.setSingleChoiceItems(options, filter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filter = which;
                getTasks();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
    }
}
