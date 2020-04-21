package com.example.tourguide.Adapter;

public class Voucher {
    private String nameStore;
    private Integer nominal;
    private Boolean status;

    public Voucher() {
    }

    public Voucher(String nameStore, Integer nominal, Boolean status) {
        this.nameStore = nameStore;
        this.nominal = nominal;
        this.status = status;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        status = status;
    }
}
