package com.example.tourguide.service;

public class CityHelper {
    private String city = "Search here";

    public CityHelper() {
    }

    public CityHelper(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
