package com.nkv.covid;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nkv.covid.adapter.CountryCardAdapter;
import com.nkv.covid.model.CountryCardModel;
import com.nkv.covid.model.CountryRestModel;
import com.nkv.covid.model.GlobalCardModel;
import com.nkv.covid.model.GlobalRestModel;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    private CountryCardAdapter mAdapter;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getCountryCode(String countryName) {
        String[] isoCountryCodes = Locale.getISOCountries();
        for (String code : isoCountryCodes) {
            Locale locale = new Locale("", code);
            if (countryName.equalsIgnoreCase(locale.getDisplayCountry())) {
                return code;
            }
        }

        // Fallback for ISO code not found
        switch (countryName) {
            case "USA":
                return "us";
            case "UK":
                return "gb";
            case "S. Korea":
                return "kr";
            case "UAE":
                return "ae";
            case "Faeroe Islands":
                return "fo";
            case "Bosnia and Herzegovina":
                return "ba";
            case "North Macedonia":
                return "mk";
            case "Macao":
                return "mo";
            case "DRC":
                return "cd";
            case "Ivory Coast":
                return "ci";
            case "Trinidad and Tobago":
                return "tt";
            case "St. Barth":
                return "bl";
            case "Saint Martin":
                return "mf";
            case "Saint Lucia":
                return "lc";
            case "Antigua and Barbuda":
                return "ag";
            case "CAR":
                return "cf";
            case "Congo":
                return "cg";
            case "St. Vincent Grenadines":
                return "vc";
            case "Eswatini":
                return "sz";
            case "Channel Islands":
                return "je";
            case "U.S. Virgin Islands":
                return "vi";
            case "Diamond Princess":
                return "bm";
            case "Cabo Verde":
                return "cv";
        }

        return "";
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public CountryCardModel[] getCountriesSavedData(){
        try{
            SharedPreferences mPrefs =  getSharedPreferences("covidData", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("countriesData", "");
            Type type = new TypeToken<CountryCardModel[]>(){}.getType();
            CountryCardModel[] countriesData = gson.fromJson(json, type);
            return countriesData;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void saveCountriesData(CountryCardModel[] myListData){
        mPrefs = getSharedPreferences("covidData", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myListData);
        prefsEditor.putString("countriesData", json);
        prefsEditor.apply();
    }

    public CountryCardAdapter getCountryAdapter(){
        return mAdapter;
    }

    public void setCountryAdapter(CountryCardAdapter newAdapter){
        this.mAdapter = newAdapter;
    }

    public void fetchCountriesData(){
        RetrofitApiInterface apiService =
                RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<List<CountryRestModel>> call = apiService.getCountriesStats();
        call.enqueue(new Callback<List<CountryRestModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CountryRestModel>> call, @NonNull Response<List<CountryRestModel>> response) {
                List<CountryRestModel> countries = response.body();

                assert countries != null;
                CountryCardModel[] myListData = new CountryCardModel[countries.size()];
                for (int i = 0; i < countries.size(); i++) {

                    String countryName = countries.get(i).country;

                    String countryCode = getCountryCode(countryName).toLowerCase();

                    int countryImgId = getResId("flag_" + countryCode, R.drawable.class);

                    if (countryImgId == -1) {
                        countryImgId = R.drawable.ic_globe_europe_solid;
                    }

                    String cases = String.format(Locale.US, "%,d", Long.parseLong(countries.get(i).cases.toString()));
                    String todayCases = String.format(Locale.US, "%,d", Long.parseLong(countries.get(i).todayCases.toString()));
                    String deaths = String.format(Locale.US, "%,d", Long.parseLong(countries.get(i).deaths.toString()));
                    String todayDeaths = String.format(Locale.US, "%,d", Long.parseLong(countries.get(i).todayDeaths.toString()));
                    String recovered = String.format(Locale.US, "%,d", Long.parseLong(countries.get(i).recovered.toString()));
                    String critical = String.format(Locale.US, "%,d", Long.parseLong(countries.get(i).critical.toString()));

                    String strCases = "Cases: " + cases + "  |  Today: " + todayCases;
                    String strDeaths = "Deaths: " + deaths + "  |  Today: " + todayDeaths;
                    String strRecovered = "Recovered: " + recovered + "  |  Critical: " + critical;

                    myListData[i] = new CountryCardModel(countryName, strCases, strDeaths, strRecovered, countryImgId);
                }
                saveCountriesData(myListData);
            }

            @Override
            public void onFailure(@NonNull Call<List<CountryRestModel>> call, @NonNull Throwable t) {

            }
        });
    }

    public GlobalCardModel[] getGlobalSavedData(){
        try {
            SharedPreferences mPrefs =  getSharedPreferences("covidData", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("globalData", "");
            Type type = new TypeToken<GlobalCardModel[]>(){}.getType();
            GlobalCardModel[] globalData = gson.fromJson(json, type);
            return globalData;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void saveGlobalData(GlobalCardModel[] globalData){
        mPrefs = getSharedPreferences("covidData", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(globalData);
        prefsEditor.putString("globalData", json);
        prefsEditor.apply();
    }

    public void fetchGlobalData(){
        RetrofitApiInterface apiService =
                RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<GlobalRestModel> call = apiService.getGlobalStats();
        call.enqueue(new Callback<GlobalRestModel>() {
            @Override
            public void onResponse(@NonNull Call<GlobalRestModel>call, @NonNull Response<GlobalRestModel> response) {

                GlobalRestModel globalData = response.body();
                String cases =  String.format(Locale.US, "%,d" ,Long.parseLong(globalData.cases.toString()));
                String deaths =  String.format(Locale.US, "%,d", Long.parseLong(globalData.deaths.toString()));
                String recovered =  String.format(Locale.US, "%,d", Long.parseLong(globalData.recovered.toString()));
                int closedCases = globalData.deaths + globalData.recovered;
                int activeCases = globalData.cases - closedCases;
                String active =  String.format(Locale.US, "%,d", Long.parseLong(Integer.toString(activeCases)));
                String closed =  String.format(Locale.US, "%,d", Long.parseLong(Integer.toString(closedCases)));

                GlobalCardModel[] myListData = new GlobalCardModel[] {
                        new GlobalCardModel(getString(R.string.title_card_1), cases, R.drawable.ic_sick),
                        new GlobalCardModel(getString(R.string.title_card_2), deaths, R.drawable.ic_crying),
                        new GlobalCardModel(getString(R.string.title_card_3), recovered, R.drawable.ic_smile),
                        new GlobalCardModel(getString(R.string.title_card_4), active, R.drawable.ic_injury),
                        new GlobalCardModel(getString(R.string.title_card_5), closed, R.drawable.ic_monocle),

                };
                saveGlobalData(myListData);
            }

            @Override
            public void onFailure(@NonNull Call<GlobalRestModel>call, @NonNull Throwable t) {

            }
        });
    }
}
