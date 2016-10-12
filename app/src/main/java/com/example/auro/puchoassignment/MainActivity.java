package com.example.auro.puchoassignment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button mSignIn, mSignUp;
    private ImageView mDialogClose;
    private LinearLayout mSignInFacebook, mSignInGoogle;
    private static final ArrayList<String> sPermission = new ArrayList<>();
    private Dialog mSignUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppEventsLogger.activateApp(getApplication());

        init();
        setOnClickListeners();

    }

    private void init() {

        mSignIn = (Button) findViewById(R.id.button_sign_in);
        mSignUp = (Button) findViewById(R.id.button_sign_up);
        mSignInFacebook = (LinearLayout) findViewById(R.id.button_sign_in_facebook);
        mSignInGoogle = (LinearLayout) findViewById(R.id.button_sign_in_google);
        mSignUpDialog = new Dialog(MainActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar);

        mSignUpDialog.setContentView(R.layout.layout_dialog_sign_up);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (mSignUpDialog.getWindow() != null) {
            layoutParams.copyFrom(mSignUpDialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mSignUpDialog.getWindow().setAttributes(layoutParams);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_sign_up_dialog);
            mSignUpDialog.getWindow().setBackgroundDrawable(drawable);
        }

        mDialogClose = (ImageView) mSignUpDialog.findViewById(R.id.button_dialog_close);

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

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkConnection()) {

                } else
                    Toast.makeText(getApplicationContext(),"Not Connected to Internet",Toast.LENGTH_SHORT).show();

            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSignUpDialog.show();
            }
        });

        mSignInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    LoginManager.
                            getInstance().
                            logInWithReadPermissions(MainActivity.this, sPermission);
                } else
                      Toast.makeText(getApplicationContext(),"Not Connected to Internet",Toast.LENGTH_SHORT).show();
              }
          });

        mSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSignUpDialog != null)
                    mSignUpDialog.cancel();
            }
        });
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return ((networkInfo != null) && networkInfo.isConnectedOrConnecting());
    }
}
