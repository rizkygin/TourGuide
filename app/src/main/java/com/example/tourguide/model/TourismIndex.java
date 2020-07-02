package com.example.tourguide.model;

import java.util.List;

public class TourismIndex {
    private String success;
    private List<TourismIndexGet> data;

    public String getSuccess() {
        return success;
    }

    public List<TourismIndexGet> getData() {
        return data;
    }
}
