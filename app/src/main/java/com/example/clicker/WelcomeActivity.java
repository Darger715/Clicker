package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    TextView textView_levelInfo;
    TextView textView_scoreInfo;
    Button button_update_results;
    int level = 1;
    long score;
    boolean check_sound;
    boolean check_vibration;
    Button button_sound;
    Button button_vibration;

    SharedPreferences SHARED_PREFERENCES_SAVING;

    final int REQUEST_CODE_SCORE = 1;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activiy_screen);

        textView_levelInfo = findViewById(R.id.welcomeActivity_textView_levelInfo);
        textView_scoreInfo = findViewById(R.id.welcomeActivity_textView_scoreInfo);
        button_update_results = findViewById(R.id.welcomeActivity_button_update_results);
        button_sound = findViewById(R.id.welcomeActivity_button_sound_on_off);
        button_vibration = findViewById(R.id.welcomeActivity_button_vibration_on_off);


        SHARED_PREFERENCES_SAVING = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        editor = SHARED_PREFERENCES_SAVING.edit();


        check_sound = SHARED_PREFERENCES_SAVING.getBoolean("APP_PREFERENCES_SOUND", check_sound);
        check_vibration = SHARED_PREFERENCES_SAVING.getBoolean("APP_PREFERENCES_VIBRATION", check_vibration);
        Log.e("vibration_image", "vibration_mode_load: " + check_vibration);
        Log.e("sound_image", "sound_mode_load: " + check_sound);

        //Button sound
        if (check_sound) {
            button_sound.setBackgroundResource(R.drawable.image_sound_on);
            button_sound.setSoundEffectsEnabled(true);
            button_vibration.setSoundEffectsEnabled(true);
            button_update_results.setSoundEffectsEnabled(true);
        } else {
            button_sound.setBackgroundResource(R.drawable.image_sound_off);
            button_sound.setSoundEffectsEnabled(false);
            button_vibration.setSoundEffectsEnabled(false);
            button_update_results.setSoundEffectsEnabled(false);
        }
        button_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check_sound) {
                    check_sound = false;
                    button_sound.setBackgroundResource(R.drawable.image_sound_off);

                    button_sound.setSoundEffectsEnabled(false);
                    button_vibration.setSoundEffectsEnabled(false);
                    button_update_results.setSoundEffectsEnabled(false);
                } else {
                    check_sound = true;
                    button_sound.setBackgroundResource(R.drawable.image_sound_on);

                    button_sound.setSoundEffectsEnabled(true);
                    button_vibration.setSoundEffectsEnabled(true);
                    button_update_results.setSoundEffectsEnabled(true);
                }
                editor.putBoolean("APP_PREFERENCES_SOUND", check_sound);
                Log.e("sound_image", "sound_mode_save: " + check_sound);
            }
        });


//Button vibration
        if (check_vibration) {
            button_vibration.setBackgroundResource(R.drawable.image_vibration_on);
        } else {
            button_vibration.setBackgroundResource(R.drawable.image_vibration_off);
        }
        button_vibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check_vibration) {
                    check_vibration = false;
                    button_vibration.setBackgroundResource(R.drawable.image_vibration_off);
                } else {
                    check_vibration = true;
                    button_vibration.setBackgroundResource(R.drawable.image_vibration_on);
                }
                editor.putBoolean("APP_PREFERENCES_VIBRATION", check_vibration);
                Log.e("vibration_image", "vibration_mode_save: " + check_vibration);
            }
        });


        if (SHARED_PREFERENCES_SAVING.contains("APP_PREFERENCES_SCORE")) {
            textView_scoreInfo.setText("Score: " + SHARED_PREFERENCES_SAVING.getLong("APP_PREFERENCES_SCORE", score));
            score = SHARED_PREFERENCES_SAVING.getLong("APP_PREFERENCES_SCORE", score);
        } else {
            textView_scoreInfo.setText("Score: 0");
        }
        if (SHARED_PREFERENCES_SAVING.contains("APP_PREFERENCES_LEVEL")) {
            textView_levelInfo.setText("Level: " + SHARED_PREFERENCES_SAVING.getInt("APP_PREFERENCES_LEVEL", level));
            level = SHARED_PREFERENCES_SAVING.getInt("APP_PREFERENCES_LEVEL", level);
        } else {
            textView_levelInfo.setText("Level: 1");
        }


        button_update_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Alldata = new Intent(WelcomeActivity.this, GameFieldActivity.class);
                intent_Alldata.putExtra("new_level_intent", level);
                intent_Alldata.putExtra("new_score_intent", score);
                intent_Alldata.putExtra("check_vibration", check_vibration);
                intent_Alldata.putExtra("check_sound", check_sound);
                startActivityForResult(intent_Alldata, REQUEST_CODE_SCORE);

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {


            level = data.getIntExtra("new_level", level);
            textView_levelInfo.setText("Level: " + level);


            score = data.getLongExtra("new_score", 0);
            textView_scoreInfo.setText("Score: " + score);

            check_sound = data.getBooleanExtra("SOUND",check_sound);
            check_vibration = data.getBooleanExtra("VIBRATION", check_vibration);

            editor.putBoolean("APP_PREFERENCES_SOUND", check_sound);
            editor.putBoolean("APP_PREFERENCES_VIBRATION", check_vibration);
            editor.putLong("APP_PREFERENCES_SCORE", score);
            editor.putInt("APP_PREFERENCES_LEVEL", level);
            editor.apply();


        }
    }
}
