package com.example.tourguide.model;

import java.util.List;

public class ItemOnMerchant {
    private int id;
    private int merchant_id;
    private String name;
    private String description;
    private String photo;
    private int price;
    private List<Promo> promo;

    public ItemOnMerchant(int id, int merchant_id, String name, String description, String photo, int price,List<Promo> promo) {
        this.id = id;
        this.merchant_id = merchant_id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.promo = promo;
    }

    public List<Promo> getPromo() {
        return promo;
    }

    public int getId() {
        return id;
    }

    public int getMerchant_id() {
        return merchant_id;
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

    public int getPrice() {
        return price;
    }
}
