package com.microservice.glologic.dto;

import java.util.Date;

public class CreatedUserDto {
    private Long id;
    private String name;
    private String email;
    private String token;
    private Boolean isActive;
    private Date created;
    private Date lastLogin;

    public CreatedUserDto(Long id, String name, String email, Boolean isActive, Date created, Date lastLogin, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isActive = isActive;
        this.created = created;
        this.lastLogin = lastLogin;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Date getCreated() {
        return created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }
}
