package com.example.tourguide.model;

public class Reward {
    private int id;
    private int point;
    private String name;
    private String photo;
    private String description;
    private String created_at;
    private String updated_at;

    public Reward(int id, int point, String name, String photo, String description, String created_at, String updated_at) {
        this.id = id;
        this.point = point;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public int getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
