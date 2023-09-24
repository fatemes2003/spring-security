package com.example.springsecuritysample.model.entity;

import com.example.springsecuritysample.model.enums.UserRoles;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Users implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String name;

    private boolean enabled = true;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ElementCollection(targetClass = UserRoles.class)
    @CollectionTable(name = "authorities",
            joinColumns = @JoinColumn(name = "email", referencedColumnName = "email"))
            @Enumerated(EnumType.STRING)
    private List<UserRoles> userRoles;

    public Users(String email, String password, String name, LocalDateTime createdAt, LocalDateTime updatedAt, List<UserRoles> userRoles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userRoles = userRoles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<UserRoles> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoles> userRoles) {
        this.userRoles = userRoles;
    }
}
