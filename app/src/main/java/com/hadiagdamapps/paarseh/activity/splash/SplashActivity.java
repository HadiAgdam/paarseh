package com.hadiagdamapps.paarseh.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SharedMemory;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.login.LoginActivity;
import com.hadiagdamapps.paarseh.activity.main.MainActivity;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import java.io.InterruptedIOException;
import java.util.Objects;

import static com.hadiagdamapps.paarseh.helpers.DataManager.Keys.*;

public class SplashActivity extends AppCompatActivity {

    private void go(Class<?> target) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, target));
                finish();
            }
        }, 1500);
    }


    private void getUser(String phone, String password) {
        StringRequest request = new StringRequest(Statics.BASE_URL + "/getuser?phone=" + phone + "&password=" + password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response of getUser", response);

                if (response.equals("not found")) {
                    go(LoginActivity.class);
                } else {
                    go(MainActivity.class);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error on splash request", error.toString());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void checkUser() {
        String phone = DataManager.readData(this, USER_PHONE_KEY);
        String password = DataManager.readData(this, USER_PASSWORD_KEY);

        Log.e("phone", phone);
        Log.e("password", password);


        if (phone == null || password == null) {
            go(LoginActivity.class);
            return;
        }

        getUser(phone, password);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkUser();
    }
}