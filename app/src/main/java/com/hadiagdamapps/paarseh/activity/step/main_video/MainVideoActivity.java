package com.hadiagdamapps.paarseh.activity.step.main_video;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.step.lar_sentences.ListenAndRepeatSentencesActivity;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

public class MainVideoActivity extends AppCompatActivity {

    private WebView webView;

    private void end() {
        String phone = DataManager.readData(this, DataManager.Keys.USER_PHONE_KEY);
        String password = DataManager.readData(this, DataManager.Keys.USER_PASSWORD_KEY);
        String step_id_text = DataManager.readData(this, DataManager.Keys.STEP_ID_KEY);
        if (phone == null || password == null || step_id_text == null) {
            Toast.makeText(this, "NULL data", Toast.LENGTH_LONG).show();
        }
        StringRequest request = new StringRequest(Statics.BASE_URL + "addPassed?phone=" + phone + "&password=" + password + "&step_id=" + step_id_text + "&practice=vid", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("main video response", response);

                if (response.equals("ok")) {
                }

                if (response.equals("invalid practice")) {
                    Log.e("error", "invalid practice");
                }

                if (response.equals("user not found")) {
                    Log.e("error", "user not found");
                    Statics.clearSession(MainVideoActivity.this);

                }

                if (response.equals("step not found")) {
                    Log.e("error", "step not found");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lar_sentences error", error.toString());
            }
        });
        MySingleton.getInstance(MainVideoActivity.this).addToRequestQueue(request);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initialWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        String step_id = DataManager.readData(this, "step_id");
        String phone = DataManager.readData(this, DataManager.Keys.USER_PHONE_KEY);
        String password = DataManager.readData(this, DataManager.Keys.USER_PASSWORD_KEY);

        webView.loadUrl("https://hadiagdam.pythonanywhere.com/playVideo?phone=" + phone + "&password=" + password + "&step_id=" + step_id);
    }

    private void initialView() {
        webView = findViewById(R.id.webView);
    }

    private void main() {
        initialView();
        initialWebView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        main();
        end();
    }
}