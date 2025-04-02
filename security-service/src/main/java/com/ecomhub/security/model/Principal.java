package com.ecomhub.security.model;

import java.util.List;

public class Principal {
    private int id;
    private String email;
    private List<?> role;

    public Principal(int id, String email, List<?> role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<?> getRole() {
        return role;
    }

    public void setRole(List<?> role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
