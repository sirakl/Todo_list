package com.example.todo_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.Calendar;
import java.util.Date;

public class EditorActivity extends AppCompatActivity {

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    private CheckBox checkBoxCompleted;

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
        calendarView = findViewById(R.id.calendar);
        checkBoxCompleted = findViewById(R.id.checkboxCompleted);

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
            checkBoxCompleted.setVisibility(View.VISIBLE);

            final LiveData<Task> taskLiveData = mdb.taskDao().getById(intent.getLongExtra("id", 0));
            taskLiveData.observe(this, new Observer<Task>() {
                @Override
                public void onChanged(@Nullable Task task) {
                    taskLiveData.removeObserver(this);
                    if (task != null) {
                        etTask.setText(task.getDescription());
                        setPriority(task.getPriority());
                        calendarView.setDate(task.getUpdatedAt().getTime());
                        checkBoxCompleted.setChecked(task.isCompleted());
                    } else {
                        Toast.makeText(EditorActivity.this, "Error loading task", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
        else {
            checkBoxCompleted.setVisibility(View.GONE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etTask.getText().toString().trim();
                int priority = getPriorityFromViews();
                Date date = new Date(calendarView.getDate());
                boolean completed = checkBoxCompleted.isChecked();

                if (text.isEmpty()) {
                    Toast.makeText(EditorActivity.this, "Please enter a task description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (priority == -1) {
                    Toast.makeText(EditorActivity.this, "Please select a priority", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Task task = new Task(text, priority, date, completed);

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (intent != null && intent.hasExtra("id")) {
                            task.setId(intent.getLongExtra("id", 0));
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
        int priority = PRIORITY_HIGH;
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
