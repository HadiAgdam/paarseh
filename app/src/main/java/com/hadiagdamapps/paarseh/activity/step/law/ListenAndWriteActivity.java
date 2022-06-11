package com.hadiagdamapps.paarseh.activity.step.law;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.step.lar_words.ListenAndRepeatWordsActivity;
import com.hadiagdamapps.paarseh.helpers.DataManager;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class ListenAndWriteActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    private TextView playText;
    private EditText valueText;
    private Button checkButton;
    private TextToSpeech speech;

    private String val;
    private int index = 0;
    private final ArrayList<String> sentences = new ArrayList<>();

    private String getData() {
        return DataManager.readData(this, "law_sentences");
    }

    private void wrongAnswer() {
        int[] animations = new int[]{R.raw.angry_emoji};
        int r = (int) Math.floor(Math.random() * (animations.length));

        Toast.makeText(this, "incorrect", Toast.LENGTH_SHORT).show();

        Log.e("random-----------", r + "");

        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation(animations[r]);
        lottieAnimationView.playAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    private void correctAnswer() {
        int[] animations = new int[]{R.raw.animated_emojis_party_emoji, R.raw.emoji_33, R.raw.grinning_face_emoji, R.raw.happy_emoji, R.raw.happy_emoji_great_work, R.raw.love_emoji, R.raw.smiley_emoji, R.raw.tbd_happyface, R.raw.winking_emoji};
        int r = (int) Math.floor(Math.random() * (animations.length));

        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation(animations[r]);
        lottieAnimationView.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        }, 2000);


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

    private void end() {
        String phone = DataManager.readData(this, DataManager.Keys.USER_PHONE_KEY);
        String password = DataManager.readData(this, DataManager.Keys.USER_PASSWORD_KEY);
        String step_id_text = DataManager.readData(this, DataManager.Keys.STEP_ID_KEY);
        if (phone == null || password == null || step_id_text == null) {
            Toast.makeText(this, "NULL data", Toast.LENGTH_LONG).show();
            Log.e("NULL data","");
        }
        StringRequest request = new StringRequest(Statics.BASE_URL + "addPassed?phone=" + phone + "&password=" + password + "&step_id=" + step_id_text + "&practice=law", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("law response", response);

                if (response.equals("ok")) {
                }

                if (response.equals("invalid practice")) {
                    Log.e("error", "invalid practice");
                }

                if (response.equals("user not found")) {
                    Log.e("error", "user not found");
                    Statics.clearSession(ListenAndWriteActivity.this);

                }

                if (response.equals("step not found")) {
                    Log.e("error", "step not found");
                }

                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("law error", error.toString());
            }
        });
        MySingleton.getInstance(ListenAndWriteActivity.this).addToRequestQueue(request);
    }

    private void check() {
        if (trim(valueText.getText().toString()).equals(trim(val))) {
            correctAnswer();
            index++;
            if (index < sentences.size()) {
                val = sentences.get(index);
            } else {
                end();
            }
        } else {
            wrongAnswer();
        }
    }

    View.OnClickListener checkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            check();
            valueText.setText("");
        }
    };

    View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            speech.speak(val, TextToSpeech.QUEUE_FLUSH, null);
        }
    };

    private void initialView() {
        lottieAnimationView = findViewById(R.id.animation);
        playText = findViewById(R.id.speechText);
        playText.setOnClickListener(playListener);
        valueText = findViewById(R.id.valueText);
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(checkListener);
    }

    private void main() {
        initialView();
        initialSpeech();
        String data = getData();
        String[] dataSentences = data.split("_");
        Collections.addAll(sentences, dataSentences);
        val = sentences.get(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_and_write);
        main();
    }
}