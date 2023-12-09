package com.microservice.glologic.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "gl_user", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "unique_email_constraint")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private Boolean isActive;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserPhone> phones;

    public User() {
    }

    public User(String name, String email, String password, Date created, List<UserPhone> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.phones = phones;
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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

    public List<UserPhone> getPhones() {
        return phones;
    }
    public void setPhones(List<UserPhone> phones) {
        this.phones = phones;
    }
}
