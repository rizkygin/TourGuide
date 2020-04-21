package com.example.tourguide.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("user")
    @Expose
    private Value user;

    private String role;
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Value getUser() {
        return user;
    }

    public void setUser(Value user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
