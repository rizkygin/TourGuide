package com.example.tourguide.model;

public class MerchantItemStore {

    private String success;
    private DataItemStore data;

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setData(DataItemStore data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public DataItemStore getData() {
        return data;
    }
}
