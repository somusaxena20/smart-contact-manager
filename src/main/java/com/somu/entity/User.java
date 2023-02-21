package com.somu.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_email", unique = true)
    private String email;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_role")
    private String role;
    @Column(name = "user_enable")
    private boolean enabled;
    @Column(name = "user_img_url")
    private String imgUrl;
    @Column(name = "user_about")
    private String about;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts;
}
