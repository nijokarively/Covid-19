package com.nkv.covid;

import com.nkv.covid.model.CountryRestModel;
import com.nkv.covid.model.GlobalRestModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApiInterface {
    @GET("/all")
    Call<GlobalRestModel> getGlobalStats();

    @GET("/countries")
    Call<List<CountryRestModel>> getCountriesStats();
}
