package com.hadiagdamapps.paarseh.activity.step;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.login.LoginActivity;
import com.hadiagdamapps.paarseh.activity.step.lar_sentences.ListenAndRepeatSentencesActivity;
import com.hadiagdamapps.paarseh.activity.step.lar_words.ListenAndRepeatWordsActivity;
import com.hadiagdamapps.paarseh.activity.step.law.ListenAndWriteActivity;
import com.hadiagdamapps.paarseh.activity.step.main_video.MainVideoActivity;
import com.hadiagdamapps.paarseh.activity.step.order_sentences.OrderSentencesActivity;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.hadiagdamapps.paarseh.helpers.DataManager.Keys.*;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private int step_id = -1;
    private JSONObject data;
    private ConstraintLayout lar_sentences_container, lar_words_container, law_container, organize_sentences_container, main_video_container;
    private ImageView lar_sentences_check, lar_words_check, law_check, organize_sentences_check, main_video_check;
    private TextView stepNameText;
    private ArrayList<String> passed = new ArrayList<>();


    private void initialPassed() {
        if (passed.contains("lar_s")) {
            lar_sentences_check.setVisibility(View.VISIBLE);
        }

        if (passed.contains("lar_w")) {
            lar_words_check.setVisibility(View.VISIBLE);
        }

        if (passed.contains("law")) {
            law_check.setVisibility(View.VISIBLE);
        }

        if (passed.contains("order_s")) {
            organize_sentences_check.setVisibility(View.VISIBLE);
        }

        if (passed.contains("vid")) {
            main_video_check.setVisibility(View.VISIBLE);
        }
    }

    private void getPassed() {
        String phone = DataManager.readData(this, USER_PHONE_KEY);
        String password = DataManager.readData(this, USER_PASSWORD_KEY);
        Log.e("phone", phone);
        Log.e("password", password);
        StringRequest request = new StringRequest(Statics.BASE_URL + "getPassed?phone=" + phone + "&password=" + password + "&step_id=" + step_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response of passed", response);


                if (response.equals("step not found")) {
                    Log.e("error", "step not found");
//                    finish();
                    Toast.makeText(StepActivity.this, "step not found", Toast.LENGTH_LONG).show();
                } else if (response.equals("user not found")) {
                    Toast.makeText(StepActivity.this, "user not found", Toast.LENGTH_LONG).show();
                    Log.e("user not found error", "error");
                    Statics.clearSession(StepActivity.this);
                }


                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        passed.add(array.getString(i));
                    }
                    initialPassed();
                } catch (Exception ex) {
                    Log.e("error in json of passed", ex.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley step passed", error.toString());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private String get(String key) {
        try {
            return data.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }

    View.OnClickListener lar_sentences_container_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataManager.writeData(StepActivity.this, "listen_repeat_sentences", get("listen_repeat_sentences"));
            startActivity(new Intent(StepActivity.this, ListenAndRepeatSentencesActivity.class));
            getPassed();
        }
    };
    View.OnClickListener lar_words_container_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataManager.writeData(StepActivity.this, "listen_repeat_words", get("listen_repeat_words"));
            startActivity(new Intent(StepActivity.this, ListenAndRepeatWordsActivity.class));
            getPassed();
        }
    };
    View.OnClickListener law_container_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataManager.writeData(StepActivity.this, "law_sentences", get("listen_write"));
            startActivity(new Intent(StepActivity.this, ListenAndWriteActivity.class));
            getPassed();
        }
    };
    View.OnClickListener organize_sentences_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataManager.writeData(StepActivity.this, "order_sentences", get("order_sentences"));
            startActivity(new Intent(StepActivity.this, OrderSentencesActivity.class));
            getPassed();
        }
    };
    View.OnClickListener main_video_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DataManager.writeData(StepActivity.this, "video_src", get("video_src"));
            startActivity(new Intent(StepActivity.this, MainVideoActivity.class));
            getPassed();
        }
    };

    private void initialView() {
        stepNameText = findViewById(R.id.stepNameText);

        // initialize containers
        lar_sentences_container = findViewById(R.id.lar_sentences_container);
        lar_words_container = findViewById(R.id.lar_words_container);
        law_container = findViewById(R.id.law_container);
        organize_sentences_container = findViewById(R.id.organize_sentences_container);
        main_video_container = findViewById(R.id.main_video_container);

        // setOnClickListener
        lar_sentences_container.setOnClickListener(lar_sentences_container_listener);
        lar_words_container.setOnClickListener(lar_words_container_listener);
        law_container.setOnClickListener(law_container_listener);
        organize_sentences_container.setOnClickListener(organize_sentences_listener);
        main_video_container.setOnClickListener(main_video_listener);

        // initialize check
        lar_sentences_check = findViewById(R.id.lar_sentences_check);
        lar_words_check = findViewById(R.id.lar_words_check);
        law_check = findViewById(R.id.law_check);
        organize_sentences_check = findViewById(R.id.organize_sentences_check);
        main_video_check = findViewById(R.id.main_video_check);
    }

    private void initialize(JSONObject data) {
        try {
            stepNameText.setText(data.getString("step_name"));
        } catch (JSONException e) {
            Log.e("error initialize fun", data.toString());
        }
    }

    private void downloadContent() {
        String phone = DataManager.readData(this, USER_PHONE_KEY);
        String password = DataManager.readData(this, USER_PASSWORD_KEY);
        StringRequest request = new StringRequest(Statics.BASE_URL + "getStep?phone=" + phone + "&password=" + password + "&step_id=" + step_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("step response", response);

                if (response.equals("user not found")) {
                    Toast.makeText(StepActivity.this, "user not found", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(StepActivity.this, LoginActivity.class));
                    finish();
                }

                if (response.equals("step not found") || response.equals("access denied")) {
                    Toast.makeText(StepActivity.this, response, Toast.LENGTH_LONG).show();
                    finish();
                }

                try {
                    data = new JSONObject(response);
                    initialize(data);
                } catch (JSONException e) {
                    Log.e("json error step", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error on Step response", error.toString());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void main() {
        Bundle b = this.getIntent().getExtras();
        String d = DataManager.readData(this, DataManager.Keys.STEP_ID_KEY);
        if (d == null) return;
        step_id = Integer.parseInt(d);
        downloadContent();
        initialView();
        getPassed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        main();
    }
}