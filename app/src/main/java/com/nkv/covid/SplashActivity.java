package com.nkv.covid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {

            fetchGlobalData();
            fetchCountriesData();
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

}