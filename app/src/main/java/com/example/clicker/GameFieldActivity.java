package com.example.clicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GameFieldActivity extends AppCompatActivity {

    TextView textView_level;
    TextView textView_amountClicks;
    TextView textView_hp;
    Button button_click;
    Button button_save_exit;
    long amount_clicks = 0;
    int level;
    long score;
    boolean check_new_score = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field_activity_screen);

        textView_amountClicks = findViewById(R.id.game_field_activity_textview_amount_click);
        textView_level = findViewById(R.id.game_field_activity_textview_level);
        textView_hp = findViewById(R.id.game_field_activity_textview_hp);
        button_click = findViewById(R.id.game_field_activity_button_game_field);
        button_save_exit = findViewById(R.id.game_field_activity_button_save_and_exit);

        level = getIntent().getIntExtra("new_level_intent", 1);
        score = getIntent().getLongExtra("new_score_intent", 0);
        Log.e("SCORE","score___ "+score);


        textView_amountClicks.setText("Score:\n0");

        if (level > 1) {
            textView_level.setText("Level: " + level);
        } else {
            textView_level.setText("Level: 1");
        }

        button_click.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_clicks++;
                textView_amountClicks.setText("Score:\n" + (long) amount_clicks);


                //level
                if (amount_clicks % 10 == 0) {
                    level++;
                    Toast.makeText(GameFieldActivity.this, "LEVEL UP", Toast.LENGTH_SHORT).show();
                    textView_level.setText("Level: " + level);
                }
                {

                }

            }


        });


        final Handler handler_score = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {

                if (msg.what == 1) {
                    if (check_new_score) {
                        Toast.makeText(GameFieldActivity.this, "NEW SCORE", Toast.LENGTH_SHORT).show();
                        Log.e("SCORE","score: "+score);
                        check_new_score = false;
                    }
                }
            }
        };

        Thread t_score = new Thread(new Runnable() {
            @Override
            public void run() {
                while (check_new_score) {
                    if (!(amount_clicks == 0 && score == 0)) {
                        if (amount_clicks > score) {
                            handler_score.sendEmptyMessage(1);
                        }
                    }
                }
            }

        });
        t_score.start();


        //передача данных при закрытии активности
        button_save_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent new_level_score_intent = new Intent(GameFieldActivity.this, WelcomeActivity.class);

                if (level > (getIntent().getIntExtra("new_level_intent", 0))) {

                    new_level_score_intent.putExtra("new_level", level);

                }

                if (amount_clicks > score) {
                    new_level_score_intent.putExtra("new_score", amount_clicks);
                }
                if (amount_clicks <= score) {
                    new_level_score_intent.putExtra("new_score", score);
                }


                setResult(RESULT_OK, new_level_score_intent);
                finish();
            }
        });


    }
}