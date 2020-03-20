package com.nkv.covid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

import com.google.gson.Gson;
import com.nkv.covid.model.CountryRestModel;
import com.nkv.covid.model.GlobalRestModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            MainActivity main = new MainActivity();
//            main.fetchGlobalData(getWindow().getDecorView());
//            main.fetchCountriesData(getWindow().getDecorView());
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    launchWizard();
                }
            }, SPLASH_DISPLAY_LENGTH);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void launchWizard(){
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                //  Launch app MAIN
                final Intent i = new Intent(SplashActivity.this, MainActivity.class);

                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        startActivity(i);
                        finish();
                    }
                });
            }
        });

        // Start the thread
        t.start();
    }

//    private void fetchGlobalData(){
//        RetrofitApiInterface apiService =
//                RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
//        Call<GlobalRestModel> call = apiService.getGlobalStats();
//        call.enqueue(new Callback<GlobalRestModel>() {
//            @Override
//            public void onResponse(Call<GlobalRestModel>call, Response<GlobalRestModel> response) {
//                GlobalRestModel globalData = response.body();
//                mPrefs = getSharedPreferences("covidData", MODE_PRIVATE);
//                prefsEditor = mPrefs.edit();
//                Gson gson = new Gson();
//                String json = gson.toJson(globalData);
//                prefsEditor.putString("globalData", json);
//                prefsEditor.apply();
//            }
//
//            @Override
//            public void onFailure(Call<GlobalRestModel>call, Throwable t) {
//                Toast.makeText(SplashActivity.this, "Failed loading data!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}