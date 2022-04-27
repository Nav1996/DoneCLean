package com.example.android.employeesmanagementsoftware.SiteDB.SiteRowData;

/**
     * A dummy item representing a piece of content.
     */
    public  class SiteItem {
        public final Long id;
        public final String name;
        public final String details;

        public SiteItem(Long id, String content, String details) {
            this.id = id;
            this.name = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return name;
        }
    }

