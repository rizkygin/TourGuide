package com.example.tourguide.Adapter;

import android.accessibilityservice.GestureDescription;

import com.google.gson.annotations.SerializedName;

public class Voucher {
    private int id;
    private int item_id;
    private int value;
    private String description;
    private String category;
    private String start_time;
    @SerializedName("point")
    private int point;
    private int merchant_id;
    private String end_time;
    private String nameStore;
    private Integer nominal;
    private Boolean status;

    public Voucher() {
    }

    public Voucher(String description, int value, Boolean status,String end_time,int point,int id,int merchant_id) {
        this.description = description;
        this.value = value;
        this.status = status;
        this.end_time = end_time;
        this.point = point;
        this.id = id;
        this.merchant_id = merchant_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
}
