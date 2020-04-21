package com.example.tourguide.model;

public class Result {

    private String name;
    private String email;
    private int id;
    private Roles roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }


    private class Roles {
        private int id;
        private String name;
        private String guard_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGuard_name() {
            return guard_name;
        }

        public void setGuard_name(String guard_name) {
            this.guard_name = guard_name;
        }
    }
}
