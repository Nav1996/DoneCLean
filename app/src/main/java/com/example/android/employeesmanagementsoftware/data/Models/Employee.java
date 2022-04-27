package com.example.android.employeesmanagementsoftware.data.Models;

import java.sql.Date;

public class Employee {
    private String id;

    public Employee(String id, String name, String birthDate, String departmentId, String job, String phone, String email, String photo, String notes) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.departmentId = departmentId;
        this.job = job;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
        this.notes = notes;
    }

    private String name;

    public Employee() {
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private String birthDate;
    private String departmentId;
    private String job;
    private String phone;
    private String email;
    private String photo;
    private String notes;
    
}
