package com.example.todo_list;

import android.room.ColumnInfo;
import android.room.Entity;
import android.room.Ignore;
import android.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long id;
}
