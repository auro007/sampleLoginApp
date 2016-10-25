package com.example.auro.puchoassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SignedInActivity extends AppCompatActivity {

    private int exitCount = 1;
    private String userName = null;
    private String userEmail = null;
    private String userMobile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        Intent intent = getIntent();

        userName = intent.getStringExtra(getString(R.string.pref_user_name));

        if (intent.hasExtra(getString(R.string.pref_user_email)))
            userEmail = intent.getStringExtra(getString(R.string.pref_user_email));
        if (intent.hasExtra(getString(R.string.pref_user_mobile)))
            userMobile = intent.getStringExtra(getString(R.string.pref_user_mobile));


        Button button = (Button) findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_name),MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(SignedInActivity.this, userName + " " + userEmail + " " + userMobile + " Logged In", Toast.LENGTH_SHORT ).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if  (exitCount == 2)
            finish();
        else {
            Toast.makeText(SignedInActivity.this,"Press again to exit",Toast.LENGTH_SHORT).show();
            exitCount++;
        }
    }
}
