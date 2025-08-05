package com.dashboard.finance.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String fullName;
    private String email;

    // Constructors
    public AuthResponse(String token, String username, String fullName, String email) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
