package com.nkv.covid;


import android.content.Intent;
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
    }

    private void launchWizard(){
        final Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onResume(){
        super.onResume();
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
}