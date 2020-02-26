package com.example.tourguide.Adapter;

public class Recommended {
    private String name;
    private String address;
    private boolean recomended;

    public Recommended() {
    }

    public Recommended(String name, String address, boolean recommended) {
        this.name = name;
        this.address = address;
        this.recomended = recommended;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isRecommended() {
        return recomended;
    }

    public void setRecommended(boolean recommended) {
        this.recomended = recommended;
    }
}
