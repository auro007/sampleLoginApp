package com.example.auro.puchoassignment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FACEBOOK = "TAG_FACEBOOK==>";
    private static final String TAG_GOOGLE = "TAG_GOOGLE==>";


    private static final ArrayList<String> sPermission = new ArrayList<>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CustomPagerAdapter mCustomPagerAdapter;
    private Button mSignUp, mSignIn;
    private LinearLayout mSignInFacebook, mSignInGoogle;
    private EditText mSignInMobileNo, mSignInPassword;
    private EditText mSignUpMobileNo, mSignUpPassword, mSignUpRePassword;
    private CallbackManager mCallbackManager;
    private String fName, lName, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_main);

        init();
        setOnClickListeners();

      //  generateHashKey();



    }

    private void init() {

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        ViewGroup signInLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_sign_in, mViewPager, false);
        ViewGroup signUpLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_sign_up, mViewPager, false);

        mSignIn = (Button) signInLayout.findViewById(R.id.sign_in_submit);
        mSignUp = (Button) signUpLayout.findViewById(R.id.sign_up_submit);

        mSignInFacebook = (LinearLayout) signInLayout.findViewById(R.id.sign_in_facebook);
        mSignInGoogle = (LinearLayout) signInLayout.findViewById(R.id.sign_in_google);



        mSignInMobileNo = (EditText) signInLayout.findViewById(R.id.sign_in_mobile_no);
        mSignInPassword = (EditText) signInLayout.findViewById(R.id.sign_in_password);
        mSignUpMobileNo = (EditText) signUpLayout.findViewById(R.id.sign_up_mobile_no);
        mSignUpPassword = (EditText) signUpLayout.findViewById(R.id.sign_up_password);
        mSignUpRePassword = (EditText) signUpLayout.findViewById(R.id.sign_up_re_password);

        ArrayList<ViewGroup> layoutArray = new ArrayList<>();
        layoutArray.add(signInLayout);
        layoutArray.add(signUpLayout);

        if (mCustomPagerAdapter == null)
            mCustomPagerAdapter = new CustomPagerAdapter(MainActivity.this, layoutArray);

        mViewPager.setAdapter(mCustomPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(
                ContextCompat.getColor(getApplicationContext(),R.color.button_text_color_not_selected),
                ContextCompat.getColor(getApplicationContext(),R.color.button_text_color_selected));

        sPermission.add("email");
        sPermission.add("public_profile");

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG_FACEBOOK,"Success");
                final String accessToken = loginResult.getAccessToken().getToken();
                Log.i(TAG_FACEBOOK,"access token : " + accessToken);

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i(TAG_FACEBOOK,response.toString());
                        try {
                            extractInfo(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LoginManager.getInstance().logOut();
                    }
                });
                Bundle param = new Bundle();
                param.putString("fields","first_name, last_name, email");
                graphRequest.setParameters(param);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

                Log.d(TAG_FACEBOOK,"Sign In Cancelled");
                Toast.makeText(MainActivity.this,"Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {

                Log.e(TAG_FACEBOOK,error.getCause().toString());
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void extractInfo(final JSONObject object) throws JSONException {

        if (object != null) {
            if (object.has("first_name"))
                fName = object.getString("first_name");
            if (object.has("last_name"))
                lName = object.getString("last_name");
            if (object.has("email"))
                email = object.getString("email");

            Log.i(TAG_FACEBOOK,"FIRST NAME: "+ fName);
            Log.i(TAG_FACEBOOK,"LAST NAME: "+ lName);
            Log.i(TAG_FACEBOOK,"EMAIL: "+ email);

            final String userName = fName + " " + lName;


            SharedPreferences sharedPreferences = getSharedPreferences(
                    getString(R.string.pref_name),MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(
                    getString(R.string.pref_user_name),userName);
            editor.putString(
                    getString(R.string.pref_user_email), email);
            editor.apply();

            moveToSignedInActivity();


        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG_FACEBOOK==>","Reached onActivity");
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void generateHashKey(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.auro.puchoassignment", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void moveToSignedInActivity() {


        Intent intent = new Intent(MainActivity.this,SignedInActivity.class);
        startActivity(intent);


    }


}

