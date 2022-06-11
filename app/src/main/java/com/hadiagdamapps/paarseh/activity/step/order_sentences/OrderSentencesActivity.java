package com.hadiagdamapps.paarseh.activity.step.order_sentences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.step.lar_sentences.ListenAndRepeatSentencesActivity;
import com.hadiagdamapps.paarseh.adapters.OrganizeRecyclerAdapter;
import com.hadiagdamapps.paarseh.adapters.OrganizeSelectedRecyclerAdapter;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;
import com.hadiagdamapps.paarseh.models.OrganizeRecyclerModel;
import com.hadiagdamapps.paarseh.models.OrganizeSelectedRecyclerModel;

import java.util.ArrayList;
import java.util.Random;

public class OrderSentencesActivity extends AppCompatActivity {

    private RecyclerView selectedRecycler;
    private RecyclerView wordsRecycler;
    private final ArrayList<OrganizeRecyclerModel> wordsList = new ArrayList<>();
    private final ArrayList<OrganizeSelectedRecyclerModel> selectedList = new ArrayList<>();
    private Button checkButton;

    private void end() {
        String phone = DataManager.readData(this, DataManager.Keys.USER_PHONE_KEY);
        String password = DataManager.readData(this, DataManager.Keys.USER_PASSWORD_KEY);
        String step_id_text = DataManager.readData(this, DataManager.Keys.STEP_ID_KEY);
        if (phone == null || password == null || step_id_text == null) {
            Toast.makeText(this,"NULL data", Toast.LENGTH_LONG).show();
        }
        StringRequest request = new StringRequest(Statics.BASE_URL + "addPassed?phone=" + phone + "&password=" + password + "&step_id=" + step_id_text + "&practice=order_sentences", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("lar_sentences response", response);

                if (response.equals("ok")) {
                }

                if (response.equals("invalid practice")) {
                    Log.e("error", "invalid practice");
                }

                if (response.equals("user not found")) {
                    Log.e("error", "user not found");
                    Statics.clearSession(OrderSentencesActivity.this);

                }

                if (response.equals("step not found")) {
                    Log.e("error", "step not found");
                }

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lar_sentences error", error.toString());
            }
        });
        MySingleton.getInstance(OrderSentencesActivity.this).addToRequestQueue(request);
    }

    private View.OnClickListener checkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StringBuilder text = new StringBuilder();
            for (OrganizeSelectedRecyclerModel s : selectedList) {
                text.append(s).append("_");
            }
            text.deleteCharAt(text.length() - 1);

            if (text.toString().equals(getData())) {
                end();
            }

        }
    };

    private void commit() {
        wordsRecycler.setAdapter(new OrganizeRecyclerAdapter(this, wordsList));
        selectedRecycler.setAdapter(new OrganizeSelectedRecyclerAdapter(selectedList, this));
    }

    private void selectedWordOnclick(OrganizeSelectedRecyclerModel model) {
        for (int i = 0; i < selectedList.size(); i++) {
            if (model.id == selectedList.get(i).id) {
                selectedList.remove(i);
                commit();
                return;
            }
        }
    }

    private void removeSelected(int index) {
        selectedList.remove(index);
        commit();
    }

    private void wordOnclick(OrganizeRecyclerModel model) {

        for (int i = 0; i < selectedList.size(); i++) {
            if (model.id == selectedList.get(i).id) {
                removeSelected(i);
                return;
            }
        }
        selectedList.add(new OrganizeSelectedRecyclerModel(model) {
            @Override
            public void onClick(OrganizeSelectedRecyclerModel self, TextView textView) {
                selectedWordOnclick(self);
            }
        });
        commit();
    }

    private String getData() {
        return DataManager.readData(this, "order_sentences");
    }

    private void initialWordsRecycler() {
        String[] data_before = getData().split("_");

        String[] data = new String[data_before.length];

        Random r = new Random();

        for (String d : data_before) {
            int index = r.nextInt(data_before.length);
            while (data[index] != null) {
                index = r.nextInt(data_before.length);
            }
            data[index] = d;
        }



        for (int i = 0; i < data.length; i++) {
            wordsList.add(new OrganizeRecyclerModel(i, data[i]) {
                @Override
                public void onClick(OrganizeRecyclerModel self, ToggleButton toggle) {
                    wordOnclick(self);
                }
            });
        }

        wordsRecycler.setAdapter(new OrganizeRecyclerAdapter(this, wordsList));

    }

    private void initialView() {
        selectedRecycler = findViewById(R.id.selectedRecycler);
        selectedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        wordsRecycler = findViewById(R.id.wordsRecycler);
        wordsRecycler.setLayoutManager(new FlexboxLayoutManager(this));
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(checkListener);
    }

    private void main() {
        initialView();
        initialWordsRecycler();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sentences);
        main();
    }
}