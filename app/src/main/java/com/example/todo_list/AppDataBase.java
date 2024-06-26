package com.example.todo_list;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDataBase extends RoomDatabase {
    public static final String LOG_TAG = AppDataBase.class.getSimpleName();
    public static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "todo_list";
    private static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "creating new database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDataBase.class, AppDataBase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(LOG_TAG, "getting the database instance");

        return sInstance;
    }

    public abstract TaskDataDao taskDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE task ADD COLUMN completed INTEGER NOT NULL DEFAULT 0");
        }
    };
}


