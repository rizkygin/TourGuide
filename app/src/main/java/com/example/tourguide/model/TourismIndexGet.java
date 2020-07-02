package com.example.tourguide.model;

public class TourismIndexGet {
    private int id;
    private int city_id;
    private int gallery_id;
    private String name;
    private String description;
    private String photo;
    private String address;
    private double latitude;
    private double longitude;
    private String created_at;
    private String updated_at;

    public TourismIndexGet(int id, int city_id, int gallery_id, String name, String description, String photo, String address, double latitude, double longitude, String created_at, String updated_at) {
        this.id = id;
        this.city_id = city_id;
        this.gallery_id = gallery_id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public int getCity_id() {
        return city_id;
    }

    public int getGallery_id() {
        return gallery_id;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}

