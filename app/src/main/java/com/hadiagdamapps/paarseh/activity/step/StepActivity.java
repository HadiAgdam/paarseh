package com.hadiagdamapps.paarseh.activity.step;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.login.LoginActivity;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONException;
import org.json.JSONObject;
import static com.hadiagdamapps.paarseh.helpers.DataManager.Keys.*;

public class StepActivity extends AppCompatActivity {


    private int step_id = -1;
    private JSONObject data;


    private void initialView() {

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
    }


    private void main() {
        Bundle b = this.getIntent().getExtras();
        step_id = b.getInt("step_id");
        downloadContent();
        initialView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        main();
    }
}