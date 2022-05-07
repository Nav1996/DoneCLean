package com.example.android.employeesmanagementsoftware.SiteDB.SiteRowData;

/**
     * A dummy item representing a piece of content.
     */
    public  class SiteItem {
        public String id;
        public String name;
        public String details;

        public SiteItem(String id, String content, String details) {
            this.id = id;
            this.name = content;
            this.details = details;
        }

    public SiteItem() {
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
        public String toString() {
            return name;
        }

    }

