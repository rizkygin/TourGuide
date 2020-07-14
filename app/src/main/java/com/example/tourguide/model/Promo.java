package com.example.tourguide.model;

public class Promo {
    private int id;
    private int item_id;
    private int value;
    private int max_cut;
    private String description;
    private String end_time;
    private String start_time;

    public Promo(int id, int item_id, int value,String end_time,String start_time,int max_cut,String description) {
        this.id = id;
        this.item_id = item_id;
        this.value = value;
        this.end_time = end_time;
        this.start_time = start_time;
        this.max_cut = max_cut;
        this.description = description;
    }

    public int getMax_cut() {
        return max_cut;
    }

    public String getDescription() {
        return description;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEndDate() {
        return end_time;
    }

    public String getStartDate() {
        return start_time;
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
}
