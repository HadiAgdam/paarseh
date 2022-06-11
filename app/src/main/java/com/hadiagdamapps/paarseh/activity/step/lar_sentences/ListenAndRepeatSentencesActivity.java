package com.hadiagdamapps.paarseh.activity.step.lar_sentences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.adapters.TextRecyclerAdapter;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListenAndRepeatSentencesActivity extends AppCompatActivity {

    private TextView backTextView, speechText, micText, counterText;
    private Context self;
    private RecyclerView recycler;
    private int index = -1;
    private ArrayList<TextView> viewList;
    private String text;
    private int correct_count = 0;
    private TextToSpeech speech;
    private ConstraintLayout infoLayout;
    private LottieAnimationView lottieAnimationView;

    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Toast.makeText(ListenAndRepeatSentencesActivity.this, "CLICK", Toast.LENGTH_LONG).show();
            finish();
//            correctAnswer();
        }
    };

    View.OnClickListener speechListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    };

    View.OnClickListener micListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
            startActivityForResult(intent, 100);
        }
    };

    private void end() {
        String phone = DataManager.readData(this, DataManager.Keys.USER_PHONE_KEY);
        String password = DataManager.readData(this, DataManager.Keys.USER_PASSWORD_KEY);
        String step_id_text = DataManager.readData(this, DataManager.Keys.STEP_ID_KEY);
        if (phone == null || password == null || step_id_text == null) {
            toast("NULL data");
        }
        StringRequest request = new StringRequest(Statics.BASE_URL + "addPassed?phone=" + phone + "&password=" + password + "&step_id=" + step_id_text + "&practice=lar_s", new Response.Listener<String>() {
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
                    Statics.clearSession(ListenAndRepeatSentencesActivity.this);

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
        MySingleton.getInstance(ListenAndRepeatSentencesActivity.this).addToRequestQueue(request);
    }

    private String trim(String input) {
        input = input.toLowerCase();
        input = input.trim();
        String[] bad = new String[]{"!", "\"", ".", "'", "?", "(", ")",};

        for (String b : bad) {
            input = input.replace(b, "");
        }
        Log.e("trim", input);
        return input;
    }

    private void toast(String text) {
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void wrongAnimation() {
        int[] animations = new int[]{R.raw.angry_emoji};
        int r = (int) Math.floor(Math.random() * (animations.length + 1));

        infoLayout.setVisibility(View.INVISIBLE);
        lottieAnimationView.setAnimation(animations[r]);
        lottieAnimationView.playAnimation();
    }

    private void wrongAnswer() {
        wrongAnimation();
    }

    private void correctAnimation() {
        int[] animations = new int[]{R.raw.animated_emojis_party_emoji, R.raw.emoji_33, R.raw.grinning_face_emoji, R.raw.happy_emoji, R.raw.happy_emoji_great_work, R.raw.love_emoji, R.raw.smiley_emoji, R.raw.tbd_happyface, R.raw.winking_emoji};
        int r = (int) Math.floor(Math.random() * (animations.length));


        infoLayout.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation(animations[r]);
        lottieAnimationView.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                infoLayout.setVisibility(View.INVISIBLE);
            }
        }, 2000);

    }

    private void correctAnswer() {
        correctAnimation();
        correct_count++;
        String t = String.valueOf(correct_count);
        counterText.setText(t + "/ 10");
        if (correct_count == 10) {
            correct_count = 0;
            next();
        }
    }

    private void initialSpeech() {
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.US);
                }
            }
        });
        speech.setSpeechRate(.5f);
        speech.setPitch(0);
    }

    private void next() {
        Log.e("position", "next");
        if (index != -1) {
            viewList.get(index).setTextColor(getResources().getColor(R.color.black));
        }
        index++;
        if (index == viewList.size()) {
            end();
            return;
        }
        viewList.get(index).setTextColor(getResources().getColor(R.color.link_color));
        text = viewList.get(index).getText().toString();
    }

    private void initialRecycler() {

        String data = DataManager.readData(this, "listen_repeat_sentences");
        if (data == null) {
            Log.e("lar_sentences", "null");
            finish();
            return;
        }
        String[] sentences = data.split("_");

        TextRecyclerAdapter adapter = new TextRecyclerAdapter(sentences, self);
        recycler.setAdapter(adapter);
        viewList = adapter.getItems();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 1000);
    }

    private void initialView() {
        backTextView = findViewById(R.id.back);
        backTextView.setOnClickListener(backListener);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        speechText = findViewById(R.id.speechText);
        speechText.setOnClickListener(speechListener);
        micText = findViewById(R.id.micText);
        micText.setOnClickListener(micListener);
        counterText = findViewById(R.id.counterText);
        infoLayout = findViewById(R.id.infoLayout);
        lottieAnimationView = findViewById(R.id.animation);
    }

    private void main() {
        self = this;
        initialView();
        initialRecycler();
        initialSpeech();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String result = trim(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            if (trim(result).equals(trim(text))) {
                correctAnswer();
            } else {
                wrongAnswer();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_and_repeat_sentences);
        main();
    }
}