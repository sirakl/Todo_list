package com.example.todo_list;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String description;
    private int priority;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;
    private boolean completed;
    public Task(String description, int priority, Date updatedAt, boolean completed) {
        this.description = description;
        this.priority = priority;
        this.updatedAt = updatedAt;
        this.completed=completed;
    }


    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public boolean isCompleted(){return completed;}


    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setCompleted(boolean completed){this.completed=completed;}
}

