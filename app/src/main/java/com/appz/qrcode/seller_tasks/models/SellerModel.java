package com.appz.qrcode.seller_tasks.models;

import java.util.List;

public class SellerModel {
    private String id;
    private List<ItemModel> itemModels;

    public SellerModel(String id) {
        this.id = id;
    }

    public SellerModel(String id, List<ItemModel> itemModels) {
        this.id = id;
        this.itemModels = itemModels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemModel> getItemModels() {
        return itemModels;
    }

    public void setItemModels(List<ItemModel> itemModels) {
        this.itemModels = itemModels;
    }

    public SellerModel() {
    }
}
