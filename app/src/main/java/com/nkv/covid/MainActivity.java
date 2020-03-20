package com.nkv.covid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nkv.covid.adapter.CountryCardAdapter;
import com.nkv.covid.adapter.GlobalCardAdapter;
import com.nkv.covid.model.CountryCardModel;
import com.nkv.covid.model.CountryRestModel;
import com.nkv.covid.model.GlobalCardModel;
import com.nkv.covid.model.GlobalRestModel;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_global, R.id.navigation_countries, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    public void outputCountriesData(CountryCardModel[] myListData){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.countries_recycler_view);

        setCountryAdapter(new CountryCardAdapter(myListData));
        CountryCardAdapter mAdapter = getCountryAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fetchCountriesData(){
        RetrofitApiInterface apiService =
                RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<List<CountryRestModel>> call = apiService.getCountriesStats();
        call.enqueue(new Callback<List<CountryRestModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CountryRestModel>> call, @NonNull Response<List<CountryRestModel>> response) {
                List<CountryRestModel> countries = response.body();

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
                outputCountriesData(myListData);
            }

            @Override
            public void onFailure(@NonNull Call<List<CountryRestModel>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Check your Internet connection!", Toast.LENGTH_LONG).show();
                CountryCardModel[] myListData = getCountriesSavedData();
                outputCountriesData(myListData);
            }
        });
    }

    public void outputGlobalData(GlobalCardModel[] myListData){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.global_recycler_view);
        GlobalCardAdapter adapter = new GlobalCardAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
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


                GlobalCardModel[] myListData = new GlobalCardModel[] {
                        new GlobalCardModel(getString(R.string.title_card_1), cases, R.drawable.ic_sick ),
                        new GlobalCardModel(getString(R.string.title_card_2), deaths, R.drawable.ic_crying),
                        new GlobalCardModel(getString(R.string.title_card_3), recovered, R.drawable.ic_smile),
                };

                saveGlobalData(myListData);
                outputGlobalData(myListData);
            }

            @Override
            public void onFailure(@NonNull Call<GlobalRestModel>call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Check your Internet connection!", Toast.LENGTH_LONG).show();
                GlobalCardModel[] myListData = getGlobalSavedData();
                outputGlobalData(myListData);
            }
        });
    }
}
