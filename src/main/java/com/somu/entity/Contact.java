package com.somu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
    @Column(name = "contact_email")
    private String email;
    @Column(name = "contact_phone")
    private String phone;
    @Column(name = "contact_img_url")
    private String imgUrl;
    @Column(name = "contact_desc",length = 1000)
    private String desc;
    @ManyToOne
    private User user;
}
