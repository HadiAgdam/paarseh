package com.hadiagdamapps.paarseh.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.register.verify_phone.VerifyPhoneActivity;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

public class RegisterActivity extends AppCompatActivity {

    private TextView nameText, phoneText, passwordText;
    private Button buttonRegister;


    private boolean checkPhone(String text) {

//        if
//        09027856925

        if (text.length() < 11) {
            Toast.makeText(this, "طول شماره تلفن صحیح نمی باشد", Toast.LENGTH_LONG).show();
            return false;
        }

        if (text.charAt(0) != '0' || text.charAt(1) != '9') {
            Toast.makeText(this, "شماره تلفن باید با 09 شروع شود.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!text.matches("[0-9]+")) {
            Toast.makeText(this, "شماره تلفن صحیح نمی باشد", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean checkPassword(String text) {

        if (text.length() < 5) {
            Toast.makeText(this, "تعداد حروف رمز هبور باید بیشتر از 5 باشد", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean checkName(String text) {
        return true;
    }

    private View.OnClickListener submit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = phoneText.getText().toString();
            String password = passwordText.getText().toString();
            String name = nameText.getText().toString();

            if (!checkPhone(phone) || !checkPassword(password) || !checkName(name)) return;

            StringRequest request = new StringRequest(Statics.BASE_URL + "register?phone=" + phone + "&password=" + password + "&name=" + name, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("response of register", response);

                    switch (response) {
                        case "found in temp":
                            Toast.makeText(RegisterActivity.this, "لطفا قبل از امتحان مجدد صبر کنید.", Toast.LENGTH_LONG).show();
                            return;

                        case "user found":
                            Toast.makeText(RegisterActivity.this, "شماره تلفن مورد نظر در سیستم موجود می باشد. لطفا وارد شوید.", Toast.LENGTH_LONG).show();
                            return;
                    }


                    DataManager.writeData(RegisterActivity.this, "t_phone", phone);
                    DataManager.writeData(RegisterActivity.this, "t_password", password);
                    DataManager.writeData(RegisterActivity.this, "t_name", name);

                    startActivity(new Intent(RegisterActivity.this, VerifyPhoneActivity.class));
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("register error", error.toString());
                }
            });
            MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
        }
    };

    private void initialView() {
        nameText = findViewById(R.id.nameText);
        phoneText = findViewById(R.id.phoneText);
        passwordText = findViewById(R.id.passwordText);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(submit);
    }

    private void initialValues() {
        if (DataManager.readData(this, "t_phone").equals("")) return;

        String phone = DataManager.readData(this, "t_phone");
        String password = DataManager.readData(this, "t_password");
        String name = DataManager.readData(this, "t_name");

        phoneText.setText(phone);
        passwordText.setText(password);
        nameText.setText(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialView();
        initialValues();
    }
}