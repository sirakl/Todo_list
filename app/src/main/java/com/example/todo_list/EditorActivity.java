package com.example.todo_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;


public class EditorActivity extends AppCompatActivity {

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;


    Button buttonAdd;
    EditText etTask;

    RadioButton radioButtonHigh;
    RadioButton radioButtonMedium;
    RadioButton radioButtonLow;


    AppDataBase mdb;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        buttonAdd = findViewById(R.id.saveButton);

        etTask = findViewById(R.id.editTextTaskDescription);


        radioButtonHigh = findViewById(R.id.radButton1);
        radioButtonMedium = findViewById(R.id.radButton2);
        radioButtonLow = findViewById(R.id.radButton3);
        calendarView=findViewById(R.id.calendar);


        mdb = AppDataBase.getInstance(getApplicationContext());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                calendarView.setDate(calendar.getTimeInMillis());
            }
        });

        final Intent intent = getIntent();



        if (intent != null && intent.hasExtra("id")) {
            buttonAdd.setText("Update");


            final LiveData<Task> task = mdb.taskDao().getById(intent.getIntExtra("id", 0));

            EditorScreenViewModel viewModel = new EditorScreenViewModel(mdb,intent.getIntExtra("id",0));

            viewModel.getTask().observe(EditorActivity.this, new Observer<Task>() {
                @Override
                public void onChanged(@Nullable Task task) {
                    final int priority = task.getPriority();

                    etTask.setText(task.getDescription());
                    Toast.makeText(EditorActivity.this, task.getDescription().toString(), Toast.LENGTH_SHORT).show();
                    setPriority(priority);

                }
            });



        }



        buttonAdd.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String text = etTask.getText().toString().trim();
                int priority = getPriorityFromViews();
                Date date = new Date(calendarView.getDate());

                final Task task = new Task(text, priority, date);


                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        if (intent != null && intent.hasExtra("id")) {


                            task.setId(intent.getIntExtra("id", 0));
                            mdb.taskDao().updateTask(task);
                        } else {
                            mdb.taskDao().insertTask(task);


                        }

                        finish();


                    }
                });


            }
        });


    }


    public int getPriorityFromViews() {
        int priority = PRIORITY_HIGH; // По умолчанию устанавливаем высший приоритет

        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        if (checkedId == R.id.radButton1) {
            priority = PRIORITY_HIGH;
        } else if (checkedId == R.id.radButton2) {
            priority = PRIORITY_MEDIUM;
        } else if (checkedId == R.id.radButton3) {
            priority = PRIORITY_LOW;
        }

        return priority;
    }


    public void setPriority(int priority) {

        switch (priority) {

            case PRIORITY_HIGH:
                radioButtonHigh.setChecked(true);
                break;
            case PRIORITY_MEDIUM:
                radioButtonMedium.setChecked(true);
                break;
            case PRIORITY_LOW:
                radioButtonLow.setChecked(true);
                break;

        }

    }
}
