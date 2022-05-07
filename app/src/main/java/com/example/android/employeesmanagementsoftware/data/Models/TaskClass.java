package com.example.android.employeesmanagementsoftware.data.Models;

import java.util.ArrayList;

public class TaskClass {
    private String id, name, description, deadline;
    private String employees;

    private boolean done;
    private int  evaluation;

    public TaskClass() {
        this.done = false;
    }

    public TaskClass(String id, String name, String description, String deadline,
                     String employees, boolean done, int evaluation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.employees = employees;
        this.done = done;
        this.evaluation = evaluation;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

//    public String getSite() {
//        return site;
//    }
//
//    public void setSite(String site) {
//        this.site = site;
//    }

    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
    }
}
