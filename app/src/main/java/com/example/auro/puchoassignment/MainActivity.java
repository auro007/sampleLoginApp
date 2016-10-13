package com.example.auro.puchoassignment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.auro.puchoassignment.Fragments.SignInFragment;
import com.example.auro.puchoassignment.Fragments.SignUpFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final ArrayList<String> sPermission = new ArrayList<>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CustomPagerAdapter mCustomPagerAdapter;
    private Button mSignUp, mSignIn;
    private LinearLayout mSignInFacebook, mSignInGoogle;
    //private ViewGroup
    private EditText mSignInMobileNo, mSignInPassword;
    private EditText mSignUpMobileNo, mSignUpPassword, mSignUpRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_main);

        init();
        //setOnClickListeners();


    }

    private void init() {

        mSignUp = (Button) findViewById(R.id.sign_up_submit);
        mSignIn = (Button) findViewById(R.id.sign_in_submit);

        mSignInFacebook = (LinearLayout) findViewById(R.id.sign_in_facebook);
        mSignInGoogle = (LinearLayout) findViewById(R.id.sign_in_google);

        mSignInMobileNo = (EditText) findViewById(R.id.sign_in_mobile_no);
        mSignInPassword = (EditText) findViewById(R.id.sign_in_password);
        mSignUpMobileNo = (EditText) findViewById(R.id.sign_up_mobile_no);
        mSignUpPassword = (EditText) findViewById(R.id.sign_up_password);
        mSignUpRePassword = (EditText) findViewById(R.id.sign_up_re_password);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        if (mCustomPagerAdapter == null)
            mCustomPagerAdapter = new CustomPagerAdapter(MainActivity.this);

        mViewPager.setAdapter(mCustomPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(
                ContextCompat.getColor(getApplicationContext(),R.color.button_text_color_not_selected),
                ContextCompat.getColor(getApplicationContext(),R.color.button_text_color_selected));

        sPermission.add("email");
        CallbackManager callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG_FACEBOOK==>","Success");
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(),"Login Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setOnClickListeners() {

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });

        if (mSignIn != null)
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    Toast.makeText(getApplicationContext(),"YAY!!!!",Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(),"NO INTERNET!!",Toast.LENGTH_SHORT).show();
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    Toast.makeText(getApplicationContext(),"YAY!!!!",Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(),"NO INTERNET!!",Toast.LENGTH_SHORT).show();
            }
        });

        mSignInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,sPermission);
                } else
                    Toast.makeText(getApplicationContext(),"NO INTERNET!!",Toast.LENGTH_SHORT).show();
            }
        });

        mSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    Toast.makeText(getApplicationContext(),"YAY!!!!",Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(),"NO INTERNET!!",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return ((networkInfo != null) && networkInfo.isConnectedOrConnecting());
    }

}

