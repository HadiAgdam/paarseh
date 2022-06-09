package com.hadiagdamapps.paarseh.activity.register.verify_phone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.main.MainActivity;
import com.hadiagdamapps.paarseh.activity.register.RegisterActivity;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

public class VerifyPhoneActivity extends AppCompatActivity {

    private TextView changePhoneText;
    private Button checkButton;
    private EditText codeEditText;

    View.OnClickListener changePhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(VerifyPhoneActivity.this, RegisterActivity.class));
        }
    };

    View.OnClickListener checkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = DataManager.readData(VerifyPhoneActivity.this, "t_phone");
            String code = codeEditText.getText().toString();
            if (code.length() != 5) {
                Toast.makeText(VerifyPhoneActivity.this, "کد اشتباه است", Toast.LENGTH_LONG).show();
                Log.e("user error verifyPhone", "invalid code length");
                return;
            }
            if (phone.equals("")) {
                Log.e("verify phones phone", "is \"\"");
                finish();
            }
            checkButton.setEnabled(false);
            StringRequest request = new StringRequest(Statics.BASE_URL + "verifyPhone?phone=" + phone + "&code=" + code, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    checkButton.setEnabled(true);
                    Log.e("response", response);

                    switch (response) {
                        case "invalid phone":
                            Toast.makeText(VerifyPhoneActivity.this, "لطفا از اول ثبت نام کنید", Toast.LENGTH_LONG).show();
                            finish();

                        case "invalid code":
                            Toast.makeText(VerifyPhoneActivity.this, "کد وارد شده اشتباه است", Toast.LENGTH_LONG).show();
                            break;

                        case "ok":
                            ok();
                            break;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(VerifyPhoneActivity.this).addToRequestQueue(request);
        }
    };

    private void ok() {
        DataManager.writeData(this, DataManager.Keys.USER_PHONE_KEY ,DataManager.readData(this, "t_phone"));
        DataManager.writeData(this, DataManager.Keys.USER_PASSWORD_KEY, DataManager.readData(this, "t_password"));
        DataManager.writeData(this, DataManager.Keys.USER_NAME_KEY, DataManager.readData(this, "t_name"));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void initialView() {
        changePhoneText = findViewById(R.id.changePhoneText);
        changePhoneText.setOnClickListener(changePhoneListener);
        checkButton = findViewById(R.id.checkCodeButton);
        checkButton.setOnClickListener(checkListener);
        codeEditText = findViewById(R.id.codeText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        initialView();
    }
}