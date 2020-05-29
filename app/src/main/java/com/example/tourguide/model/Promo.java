package com.example.tourguide.model;

public class Promo {
    private int id;
    private int item_id;
    private int value;
    private String end_time;
    private String start_time;

    public Promo(int id, int item_id, int value,String end_time,String start_time) {
        this.id = id;
        this.item_id = item_id;
        this.value = value;
        this.end_time = end_time;
        this.start_time = start_time;
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
