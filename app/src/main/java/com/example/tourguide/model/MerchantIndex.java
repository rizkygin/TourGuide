package com.example.tourguide.model;

import com.example.tourguide.Adapter.Recommended;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MerchantIndex {
    @SerializedName("data")
    private List<Recommended> data;


    public List<Recommended> getData() {
        return data;
    }

    public void setData(List<Recommended> data) {
        this.data = data;
    }


}
