package com.spring.companytaskmanager.enums;

public enum Roles {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;
    Roles(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
