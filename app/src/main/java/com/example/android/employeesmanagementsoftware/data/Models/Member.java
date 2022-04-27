package com.example.android.employeesmanagementsoftware.data.Models;

public class Member {
    private String name;
    private String email;
    private String password;
    private String location;
    private String phone;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;

    public Member() {
    }

    public Member(String name, String email, String password, String location, String phone,String userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.phone = phone;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
