package com.example.tourguide.model;

public class PromoScanned {

    private int id;
    private int item_id;
    private int value;
    private String description;
    private String category;
    private String start_time;
    private String end_time;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
