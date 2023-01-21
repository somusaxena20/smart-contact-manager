package com.somu.entity;

import jakarta.persistence.*;

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

    public Contact() {
    }

    public Contact(Integer cId, String name, String secondName, String work, String email, String phone, String imgUrl, String desc, User user) {
        this.cId = cId;
        this.name = name;
        this.secondName = secondName;
        this.work = work;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgUrl;
        this.desc = desc;
        this.user = user;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "cId=" + cId +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", work='" + work + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", desc='" + desc + '\'' +
                ", user=" + user +
                '}';
    }
}
