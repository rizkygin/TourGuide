package com.example.tourguide.model;

import com.example.tourguide.Adapter.Recommended;

import java.util.List;

public class MerchantIndex {
    private List<Recommended> data;

    private String links;

    public List<Recommended> getData() {
        return data;
    }

    public void setData(List<Recommended> data) {
        this.data = data;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }
}
