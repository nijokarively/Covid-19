package com.nkv.covid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nkv.covid.adapter.CountryCardAdapter;
import com.nkv.covid.adapter.GlobalCardAdapter;
import com.nkv.covid.model.CountryCardModel;
import com.nkv.covid.model.CountryRestModel;
import com.nkv.covid.model.GlobalCardModel;
import com.nkv.covid.model.GlobalRestModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private CountryCardAdapter mAdapter;
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
        getSupportActionBar().setTitle(title);
    }

    public String getCountryCode(String countryName) {
        String[] isoCountryCodes = Locale.getISOCountries();
        for (String code : isoCountryCodes) {
            Locale locale = new Locale("", code);
            if (countryName.equalsIgnoreCase(locale.getDisplayCountry())) {
                return code;
            }
        }

        // Fallback for ISO code issues
        if (countryName.equals("USA")) {
            return "us";
        } else if (countryName.equals("UK")) {
            return "gb";
        } else if (countryName.equals("S. Korea")) {
            return "kr";
        } else if (countryName.equals("UAE")) {
            return "ae";
        } else if (countryName.equals("Faeroe Islands")) {
            return "fo";
        } else if (countryName.equals("Bosnia and Herzegovina")) {
            return "ba";
        } else if (countryName.equals("North Macedonia")) {
            return "mk";
        } else if (countryName.equals("Macao")) {
            return "mo";
        } else if (countryName.equals("DRC")) {
            return "cd";
        } else if (countryName.equals("Ivory Coast")) {
            return "ci";
        } else if (countryName.equals("Trinidad and Tobago")) {
            return "tt";
        } else if (countryName.equals("St. Barth")) {
            return "bl";
        } else if (countryName.equals("Saint Martin")) {
            return "mf";
        } else if (countryName.equals("Saint Lucia")) {
            return "lc";
        } else if (countryName.equals("Antigua and Barbuda")) {
            return "ag";
        } else if (countryName.equals("CAR")) {
            return "cf";
        } else if (countryName.equals("Congo")) {
            return "cg";
        } else if (countryName.equals("St. Vincent Grenadines")) {
            return "vc";
        } else if (countryName.equals("Eswatini")) {
            return "sz";
        } else if (countryName.equals("Channel Islands")) {
            return "je";
        } else if (countryName.equals("U.S. Virgin Islands")) {
            return "vi";
        } else if (countryName.equals("Diamond Princess")) {
            return "bm";
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

    public CountryCardModel[] getCountriesSavedData(View view){
        try{
            SharedPreferences mPrefs =  view.getContext().getSharedPreferences("covidData", MODE_PRIVATE);
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

    public void outputCountriesData(View view, CountryCardModel[] myListData){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.countries_recycler_view);
        mAdapter = new CountryCardAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(mAdapter);
    }

    private void saveCountriesData(View view, CountryCardModel[] myListData){
        mPrefs = view.getContext().getSharedPreferences("covidData", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myListData);
        prefsEditor.putString("countriesData", json);
        prefsEditor.apply();
    }

    public CountryCardAdapter getCountryAdapter(){
        return mAdapter;
    }

    public void fetchCountriesData(View view){
        RetrofitApiInterface apiService =
                RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<List<CountryRestModel>> call = apiService.getCountriesStats();
        call.enqueue(new Callback<List<CountryRestModel>>() {
            @Override
            public void onResponse(Call<List<CountryRestModel>> call, Response<List<CountryRestModel>> response) {
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

                saveCountriesData(view, myListData);

                outputCountriesData(view, myListData);

                Toast.makeText(MainActivity.this, "Data loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<CountryRestModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed loading data!", Toast.LENGTH_SHORT).show();
                CountryCardModel[] myListData = getCountriesSavedData(view);
                outputCountriesData(view, myListData);
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

    public void outputGlobalData(GlobalCardModel[] myListData){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.global_recycler_view);
        GlobalCardAdapter adapter = new GlobalCardAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
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
            public void onResponse(Call<GlobalRestModel>call, Response<GlobalRestModel> response) {

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
            public void onFailure(Call<GlobalRestModel>call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed loading data!", Toast.LENGTH_SHORT).show();
                GlobalCardModel[] myListData = getGlobalSavedData();
                outputGlobalData(myListData);
            }
        });
    }
}
