package com.pesem.com.pesaem.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {

    private String username;

    private String password;

    private LocalDateTime lastLoginTime;

    public User(String username, String password, LocalDateTime lastLoginTime) {
        this.username = username;
        this.password = password;
        this.lastLoginTime = lastLoginTime;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "User " + username + "| "+lastLoginTime;
    }
}
