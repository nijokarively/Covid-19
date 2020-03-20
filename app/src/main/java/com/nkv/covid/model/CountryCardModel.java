package com.nkv.covid.model;

public class CountryCardModel {
    private String name;
    private String cases;
    private String deaths;
    private String recovered;
    private int imgId;

    public CountryCardModel(String name,  String cases, String deaths, String recovered, int imgId) {
        this.name = name;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
        this.imgId = imgId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCases() {
        return cases;
    }
    public void setCases(String value) {
        this.cases = cases;
    }
    public String getDeaths() {
        return deaths;
    }
    public void setDeaths(String recovered) {
        this.deaths = deaths;
    }
    public String getRecovered() {
        return recovered;
    }
    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
