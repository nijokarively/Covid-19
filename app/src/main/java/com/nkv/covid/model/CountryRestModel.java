package com.nkv.covid.model;

import com.google.gson.annotations.SerializedName;

public class CountryRestModel {
    @SerializedName("country")
    public String country;
    @SerializedName("cases")
    public Integer cases;
    @SerializedName("todayCases")
    public Integer todayCases;
    @SerializedName("deaths")
    public Integer deaths;
    @SerializedName("todayDeaths")
    public Integer todayDeaths;
    @SerializedName("recovered")
    public Integer recovered;
    @SerializedName("active")
    public Integer active;
    @SerializedName("critical")
    public Integer critical;
    @SerializedName("casesPerOneMillion")
    public Integer casesPerOneMillion;
}