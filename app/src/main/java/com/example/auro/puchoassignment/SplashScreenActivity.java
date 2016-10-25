package com.example.auro.puchoassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends Activity {

    private static final int SLEEP_TIME = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SplashScreenActivity.this, SignedInActivity.class);
                    intent.putExtra(getString(R.string.pref_user_name),userName);
                    intent.putExtra(getString(R.string.pref_user_email),userEmail);
                    intent.putExtra(getString(R.string.pref_user_mobile), userMobile);
                    startActivity(intent);
                }
                finish();
            }
        });

        thread.start();
    }

}
