package com.example.todo_list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    AppDataBase appDataBase;
    ToDoListAdapter toDoListAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                //
                final List<Task> tasks = toDoListAdapter.getTasks();


                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        appDataBase.taskDao().deleteTask(tasks.get(position));

                    }
                });




            }
        }).attachToRecyclerView(recyclerView);


        getTasks("onCreate");


    }

    private void getTasks(final String s) {



        MainScreenViewModel viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainScreenViewModel.class);

        viewModel.getTaskList().observe(MainActivity.this, new Observer<List<Task>>() {

            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                toDoListAdapter.setTasks(tasks);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

}