package com.example.tourguide.model;

import java.util.List;

public class GalleryResponse {
    private String success;
    private List<GalleryModel> data;

    public String getSuccess() {
        return success;
    }

    public List<GalleryModel> getData() {
        return data;
    }
}
