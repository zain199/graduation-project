package com.appz.qrcode.pojo;

import java.util.List;

public class ClientModel  {
    // var
    private String id;
    private String name;
    private String email;
    private String gender;
    private String phone;
    private String address;
    private Double balance;
    private String image_client_url;
    private List<ChildModel> childModels;


    public ClientModel(String id, String name, String email, String gender, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getImage_client_url() {
        return image_client_url;
    }

    public void setImage_client_url(String image_client_url) {
        this.image_client_url = image_client_url;
    }

    public List<ChildModel> getChildModels() {
        return childModels;
    }

    public void setChildModels(List<ChildModel> childModels) {
        this.childModels = childModels;
    }

    public ClientModel(String id, String name, String email, String gender, String phone, String address, Double balance, String image_client_url, List<ChildModel> childModels) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
        this.image_client_url = image_client_url;
        this.childModels = childModels;
    }

    public ClientModel() {
    }




}
