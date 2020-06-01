package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    TextView textView_levelInfo;
    TextView textView_scoreInfo;
    Button update_results;
    int level = 1;
    long score;

    SharedPreferences PREFERENCES_score_level_save;

    final int REQUEST_CODE_SCORE = 1;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activiy_screen);

        textView_levelInfo = findViewById(R.id.welcomeActivity_textView_levelInfo);
        textView_scoreInfo = findViewById(R.id.welcomeActivity_textView_scoreInfo);
        update_results = findViewById(R.id.welcomeActivity_button_update_results);

        PREFERENCES_score_level_save = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        editor = PREFERENCES_score_level_save.edit();


        if (PREFERENCES_score_level_save.contains("APP_PREFERENCES_SCORE")) {
            textView_scoreInfo.setText("Score: " + PREFERENCES_score_level_save.getLong("APP_PREFERENCES_SCORE", score));
            score = PREFERENCES_score_level_save.getLong("APP_PREFERENCES_SCORE",score);
        } else {
            textView_scoreInfo.setText("Score: 0");
        }
        if (PREFERENCES_score_level_save.contains("APP_PREFERENCES_LEVEL")) {
            textView_levelInfo.setText("Level: " + PREFERENCES_score_level_save.getInt("APP_PREFERENCES_LEVEL", level));
            level = PREFERENCES_score_level_save.getInt("APP_PREFERENCES_LEVEL",level);
        } else {
            textView_levelInfo.setText("Level: 1");
        }


        update_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_score_level_intent = new Intent(WelcomeActivity.this, GameFieldActivity.class);
                new_score_level_intent.putExtra("new_level_intent", level);
                new_score_level_intent.putExtra("new_score_intent", score);
                startActivityForResult(new_score_level_intent, REQUEST_CODE_SCORE);

            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {


            level = data.getIntExtra("new_level", level);
            textView_levelInfo.setText("Level: " + level);


            score = data.getLongExtra("new_score", 0);
            textView_scoreInfo.setText("Score: " + score);

            editor.putLong("APP_PREFERENCES_SCORE", score);
            editor.putInt("APP_PREFERENCES_LEVEL", level);
            editor.apply();
            Log.e("PREFERENCES", "score: " + score + "\nlevel: " + level);

        } else {
            Toast.makeText(WelcomeActivity.this, "saving wrong", Toast.LENGTH_SHORT).show();
            textView_scoreInfo.setText("Score: 0");
        }
    }
}
