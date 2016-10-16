package com.example.auro.puchoassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    private static final int    SLEEP_TIME = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.pref_name),MODE_PRIVATE);
        final String userName = sharedPreferences.getString(
                getString(R.string.pref_user_name), null);
        final String userEmail = sharedPreferences.getString(
                getString(R.string.pref_user_email), null);
        final String userMobile = sharedPreferences.getString(
                getString(R.string.pref_user_mobile), null);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(userName) &&
                        (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userMobile))) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, SignedInActivity.class);
                    startActivity(intent);
                }

            }
        });

        thread.start();
    }
}
