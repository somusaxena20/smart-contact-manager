package com.somu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contact_id")
    private Integer cId;
    @Column(name = "contact_name")
    private String name;
    @Column(name = "contact_second_name")
    private String secondName;
    @Column(name = "contact_work")
    private String work;
    @Column(name = "contact_email", unique = true)
    private String email;
    @Column(name = "contact_phone")
    private String phone;
    @Column(name = "contact_img_url")
    private String imgUrl;
    @Column(name = "contact_desc",length = 1000)
    private String desc;
    @ManyToOne
    @JsonIgnore
    private User user;
    
    @Override
    public boolean equals(Object obj) {
    	return this.cId == ((Contact)obj).getCId();
    }
    
}
