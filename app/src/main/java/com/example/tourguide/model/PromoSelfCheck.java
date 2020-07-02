package com.example.tourguide.model;

public class PromoSelfCheck {
    private int id;
    private int item_id;
    private int value;
    private int max_cut;
    private String description;
    private String start_time;
    private String end_time;

    public PromoSelfCheck(int id, int item_id, int value, int max_cut, String description, String start_time, String end_time) {
        this.id = id;
        this.item_id = item_id;
        this.value = value;
        this.max_cut = max_cut;
        this.description = description;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public int getId() {
        return id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getValue() {
        return value;
    }

    public int getMax_cut() {
        return max_cut;
    }

    public String getDescription() {
        return description;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }
}
