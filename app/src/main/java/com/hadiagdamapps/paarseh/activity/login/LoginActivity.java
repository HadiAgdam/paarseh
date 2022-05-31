package com.hadiagdamapps.paarseh.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.activity.main.MainActivity;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONArray;
import org.json.JSONObject;
import static com.hadiagdamapps.paarseh.helpers.DataManager.Keys.*;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneText;
    private EditText passwordText;
    private Button loginButton;
    private RecyclerView alertRecycler;

    private void notFound(){
        Toast.makeText(this, "incorrect username or password.", Toast.LENGTH_LONG).show();
    }

    private void success(JSONObject data){
        try {
//            SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("name", data.getString("name"));
//            editor.putString("phone", data.getString("phone"));
//            editor.putString("password", data.getString("password"));
//            editor.commit();

            DataManager.writeData(this, USER_NAME_KEY, data.getString("name"));
            DataManager.writeData(this, USER_PHONE_KEY, data.getString("phone"));
            DataManager.writeData(this, USER_PASSWORD_KEY, data.getString("password"));


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception ex){
            Log.e("error", ex.toString());
        }
    }

    private void exception(VolleyError error){
        Toast.makeText(this, "Please try again later.", Toast.LENGTH_LONG).show();
    }

    private void exception(String error, Exception ex){
        Log.e("exception", ex.toString());
        Toast.makeText(this, "Please try again later.", Toast.LENGTH_LONG).show();
    }

    View.OnClickListener buttonLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setEnabled(false);
            StringRequest request = new StringRequest(Statics.BASE_URL + "getuser?phone=" + phoneText.getText() + "&password=" + passwordText.getText(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("response of login", response);
                    if (response.equals("not found")){
                        view.setEnabled(true);
                        notFound();
                        return;
                    }
                    try {
                        JSONObject data = new JSONObject(response);
                        success(data);

                    }catch (Exception ex){
                        exception(response, ex);
                    }
                    view.setEnabled(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error of login", error.toString());
                    exception(error);
                }
            });
            MySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
        }
    };

    private void initialView(){
        phoneText = findViewById(R.id.phoneText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.buttonRegister);
        loginButton.setOnClickListener(buttonLoginListener);
    }

    private void main(){
        initialView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        main();
    }
}