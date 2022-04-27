package com.example.android.employeesmanagementsoftware.data.Models;

import java.sql.Date;

public class Task {
    private String id;

    public Task(){}

    public Task(String id, String name, String description, Date deadline, Date taskDate, int taskCompleted, int taskEvaluation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.taskDate = taskDate;
        this.taskCompleted = taskCompleted;
        this.taskEvaluation = taskEvaluation;
    }

    private String name;
    private String description;
    private Date deadline;
    private Date taskDate;
    private int taskCompleted;
    private int taskEvaluation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public int getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(int taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public int getTaskEvaluation() {
        return taskEvaluation;
    }

    public void setTaskEvaluation(int taskEvaluation) {
        this.taskEvaluation = taskEvaluation;
    }



}
