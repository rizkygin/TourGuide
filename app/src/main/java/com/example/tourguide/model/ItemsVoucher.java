package com.example.tourguide.model;

public class ItemsVoucher {
    private int id;
    private int merchant_id;
    private int price;
    private String name;
    private String description;
    private String photo;

    public int getId() {
        return id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }
}
