package com.hadiagdamapps.paarseh.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.login.LoginActivity;
import com.hadiagdamapps.paarseh.adapters.LevelRecyclerAdapter;
import com.hadiagdamapps.paarseh.adapters.StepRecyclerAdapter;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;
import com.hadiagdamapps.paarseh.models.LevelRecyclerModel;
import com.hadiagdamapps.paarseh.models.StepRecyclerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.hadiagdamapps.paarseh.helpers.DataManager.Keys.*;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private void backToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void initialRecycler() {
        String phone = DataManager.readData(this, USER_PHONE_KEY);
        String password = DataManager.readData(this, USER_PASSWORD_KEY);
        if (phone == null || password == null) backToLogin();

        StringRequest request = new StringRequest(Statics.BASE_URL + "getData?phone=" + phone + "&password=" + password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response of main", response);

                if (response.equals("invalid data")) {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.equals("user not found")) {
                    backToLogin();
                }


                try {
                    JSONArray result = new JSONArray(response);
                    ArrayList<LevelRecyclerModel> levelsList = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject data = result.getJSONObject(i);
                        JSONArray steps = data.getJSONArray("steps");

                        ArrayList<StepRecyclerModel> stepsList = new ArrayList<>();
                        for (int j = 0; j < steps.length(); j++) {
                            JSONObject step = steps.getJSONObject(j);
                            StepRecyclerModel model = new StepRecyclerModel(step.getInt("id"), step.getString("name"), step.getString("description"), !step.getBoolean("access"), step.getInt("progress"));
                            stepsList.add(model);
                        }

                        LevelRecyclerModel model = new LevelRecyclerModel(data.getInt("id"), data.getString("name"), new StepRecyclerAdapter(stepsList, MainActivity.this));
                        levelsList.add(model);
                    }
                    LevelRecyclerAdapter adapter = new LevelRecyclerAdapter(MainActivity.this, levelsList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);

    }


    private void initialView() {
        recyclerView = findViewById(R.id.levelRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void main() {
        initialView();
        initialRecycler();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main();
    }
}