package com.microservice.glologic.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

public class UserDto extends SignUpUserDto {
    private Long id;
    private Boolean isActive;
    private Date created;
    private Date lastLogin;
    private String token;

    public UserDto() {
    }

    public UserDto(Long id, String name, String email, String password, Boolean isActive, Date created, Date lastLogin, List<SignUpUserPhoneDto> phones, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.created = created;
        this.lastLogin = lastLogin;
        this.token = token;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }
    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastLogin() {
        return lastLogin;
    }
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
