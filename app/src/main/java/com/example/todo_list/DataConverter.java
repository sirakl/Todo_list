package com.example.todo_list;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

public class DataConverter {
    @TypeConverter
    public static Date toDate(Long timeStamp){
        return timeStamp == null ? null : new Date(timeStamp);
        
    }
    @TypeConverter
    public static Long toTimeStamp(Date date){
        return date == null ? null : date.getTime();
    }
}
