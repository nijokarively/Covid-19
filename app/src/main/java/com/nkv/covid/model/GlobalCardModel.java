package com.nkv.covid.model;

public class GlobalCardModel {
    private String name;
    private String value;
    private int imgId;

    public GlobalCardModel(String name,  String value, int imgId) {
        this.name = name;
        this.value = value;
        this.imgId = imgId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
