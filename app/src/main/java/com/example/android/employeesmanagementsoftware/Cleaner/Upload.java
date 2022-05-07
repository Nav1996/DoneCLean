package com.example.android.employeesmanagementsoftware.Cleaner;

public class Upload {
    private String id, start, end;

    public Upload() {

    }

    public Upload(String id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
