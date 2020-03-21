package com.nkv.covid;

import android.os.Bundle;
import android.view.MenuItem;

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
        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // Do nothing to ignore the reselection
            }
        });
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

    public void outputGlobalData(GlobalCardModel[] myListData){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.global_recycler_view);
        GlobalCardAdapter adapter = new GlobalCardAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
    }


    public void reloadGlobalData(){
        fetchGlobalData();
        GlobalCardModel[] myListData = getGlobalSavedData();
        outputGlobalData(myListData);
    }

    public void reloadCountriesData(){
        fetchCountriesData();
        CountryCardModel[] myListData = getCountriesSavedData();
        outputCountriesData(myListData);
    }
}
