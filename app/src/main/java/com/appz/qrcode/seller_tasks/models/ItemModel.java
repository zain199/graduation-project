package com.appz.qrcode.seller_tasks.models;

public class ItemModel {
    private String id;
    private String name;
    private String img_url;
    private double point;
    private int number_units;

    public ItemModel(String name, String img_url, double point, int number_units) {
        this.name = name;
        this.img_url = img_url;
        this.point = point;
        this.number_units = number_units;
    }

    public ItemModel(String id, String name, String img_url, double point, int number_units) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.point = point;
        this.number_units = number_units;
    }

    public ItemModel() {
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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public int getNumber_units() {
        return number_units;
    }

    public void setNumber_units(int number_units) {
        this.number_units = number_units;
    }
}
