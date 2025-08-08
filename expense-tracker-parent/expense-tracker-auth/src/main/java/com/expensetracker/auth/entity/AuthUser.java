package com.expensetracker.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AuthUser {

    @Id
    private String email;

    @Column(nullable = false)
    private String name;

    private String roles = "USER";  // Defaults to USER

    // --- Getters and Setters ---
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
}
