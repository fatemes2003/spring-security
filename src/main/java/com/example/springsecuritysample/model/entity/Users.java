package com.example.springsecuritysample.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users_tbl")
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


    public Users() {
    }

    public Users(String email, String password, String name, String cover) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

}
