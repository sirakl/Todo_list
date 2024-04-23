package com.example.todo_list;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Task.class},version=1,exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDataBase extends RoomDatabase {
    public static final String LOG_TAG =AppDataBase.class.getSimpleName();
    public static final Object LOCK = new Object();
    public static final String DATABASE_NAME="todo_list";
    private static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK) {
                Log.d(LOG_TAG, "creating new database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, AppDataBase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG,"geting the database instance");

        return sInstance;

            }
            public abstract TaskDataDao taskDao();
        }


