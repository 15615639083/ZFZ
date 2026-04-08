package com.gametech.platform.modules.user.dto;

public class UserProfileResponse {

    private Long id;
    private String username;
    private String phone;
    private String role;
    private boolean engineer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEngineer() {
        return engineer;
    }

    public void setEngineer(boolean engineer) {
        this.engineer = engineer;
    }
}
