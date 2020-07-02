package com.example.tourguide.model;

public class UserScanned {
    private int id;
    private int points;
    private int status;
    private int merchant_id;
    private String name;
    private String email;
    private String email_verified_at;
    private String updated_at;
    private String created_at;

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public int getStatus() {
        return status;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }
}
