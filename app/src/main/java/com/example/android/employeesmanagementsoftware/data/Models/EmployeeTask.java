package com.example.android.employeesmanagementsoftware.data.Models;

public class EmployeeTask {
    public EmployeeTask() {
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public EmployeeTask(String empId, String taskId) {
        this.empId = empId;
        this.taskId = taskId;
    }

    private String empId;
    private String taskId;

}
