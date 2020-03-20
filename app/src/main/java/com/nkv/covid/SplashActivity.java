package com.nkv.covid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            Log.d("Tag", String.valueOf(isNetworkAvailable()));
            if (isNetworkAvailable()){
                fetchGlobalData();
                fetchCountriesData();
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        launchWizard();
                    }
                }, SPLASH_DISPLAY_LENGTH);
            } else{
                Toast.makeText(SplashActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                if(getGlobalSavedData()!= null || getCountriesSavedData() != null){
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, "Loading offline data...", Toast.LENGTH_SHORT).show();
                            launchWizard();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                } else {
                    Toast.makeText(SplashActivity.this, "Connect to the Internet and restart!", Toast.LENGTH_LONG).show();
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void launchWizard(){
        final Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}