package com.example.springsecuritysample.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Users {
    private int user_id;
    private String username;
    private String password;
    private String email;
}
