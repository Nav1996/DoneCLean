package com.example.android.employeesmanagementsoftware.data.Models;

public class Site {
    private String id;

    public Site(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Site() {
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

    private String name;
    private String description;

}
