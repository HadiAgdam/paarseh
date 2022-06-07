package com.hadiagdamapps.paarseh.activity.step.main_video;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

    private VideoView videoView;

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

    private void initialVideoPlayer() {
        String video_src = DataManager.readData(this, "video_src");
        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);
        videoView.setMediaController(controller);

        videoView.setVideoURI(Uri.parse(video_src));
        videoView.start();
    }

    private void initialView() {
        videoView = findViewById(R.id.videoView);
    }

    private void main() {
        initialView();
        initialVideoPlayer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        main();
        end();
    }
}