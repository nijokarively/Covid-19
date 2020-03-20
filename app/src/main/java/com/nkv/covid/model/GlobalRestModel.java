package com.nkv.covid.model;

import com.google.gson.annotations.SerializedName;

public class GlobalRestModel {
    @SerializedName("cases")
    public Integer cases;
    @SerializedName("deaths")
    public Integer deaths;
    @SerializedName("recovered")
    public Integer recovered;
}