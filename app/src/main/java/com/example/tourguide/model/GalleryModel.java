package com.example.tourguide.model;

public class GalleryModel {
    private int id;
    private int tourism_id;
    private String description;
    private String photo;
    private String created_at;
    private String updated_at;

    public GalleryModel(int id, int tourism_id, String description, String photo, String created_at, String updated_at) {
        this.id = id;
        this.tourism_id = tourism_id;
        this.description = description;
        this.photo = photo;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public int getTourism_id() {
        return tourism_id;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
