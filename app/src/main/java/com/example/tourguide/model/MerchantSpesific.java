package com.example.tourguide.model;

import java.util.List;

public class MerchantSpesific {
    private int id;
    private int category_id;
    private String name;
    private String description;
    private String photo;
    private String address;
    private List<ItemOnMerchant> item;

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
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

    public String getAddress() {
        return address;
    }

    public List<ItemOnMerchant> getItem() {
        return item;
    }


}
