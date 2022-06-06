package com.hadiagdamapps.paarseh.helpers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class DataManager {

    public class Keys {
        public static final String USER_PHONE_KEY = "user_phone";
        public static final String USER_PASSWORD_KEY = "user_password";
        public static final String USER_NAME_KEY = "user_name";
        public static final String STEP_ID_KEY = "step_id";
    }


    public static void writeData(Context context, String key, String data) {
        try {
            FileOutputStream stream = context.openFileOutput(key, Context.MODE_PRIVATE);
            stream.write(data.getBytes(StandardCharsets.UTF_8));
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }

    public static String readData(Context context, String key) {

        try {

            FileInputStream stream = context.openFileInput(key);
            StringBuilder result = new StringBuilder();
            int content;
            while ((content = stream.read()) != -1) {
                result.append((char) content);
            }
            stream.close();
            return result.toString();

        } catch (FileNotFoundException ex) {
            return "";
        } catch (Exception ex) {
            Log.e("error", ex.toString());
        }

        return null;
    }

    public static void clearData(Context context, String key) {
        try {
            if (!context.getFileStreamPath(key).delete()) {
                Log.e("clear file error", "result is False");
            }


        } catch (Exception ex) {
            Log.e("error on clear file", ex.toString());
        }
    }

}
