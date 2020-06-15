package com.example.tourguide.Adapter;

public class Recommended {
    private int id;
    private int category_id;
    private String name;
    private String description;
    private String photo;
    private String address;
    private String latitude;
    private String longitude;
    private String status;

    public Recommended() {
    }

    public Recommended(String name, String address, String status, int id,int category_id) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.id = id;
        this.category_id = category_id;
    }

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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
