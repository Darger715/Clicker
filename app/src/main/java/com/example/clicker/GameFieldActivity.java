package com.example.clicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
    int amount_clicks = 0;
    int level;
    int score;
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

        level = getIntent().getIntExtra("levelInfo_intent", 1);
        score = getIntent().getIntExtra("scoreInfo_intent", 0);


        textView_level.setText("Level: " + level);
        textView_amountClicks.setText("Score:\n0");

        button_click.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_clicks++;
                textView_amountClicks.setText("Score:\n" + (int) amount_clicks);


                //level
                if (amount_clicks % 100 == 0) {
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
                        check_new_score = false;
                    }
                }
            }
        };

        Thread t_score = new Thread(new Runnable() {
            @Override
            public void run() {
                while (1 == 1) {
                    if (!(amount_clicks == 0 && score == 0)) {
                        if (amount_clicks > score) {
                            score = amount_clicks;
                            handler_score.sendEmptyMessage(1);
                        }
                    }
                }
            }

        });
        t_score.start();

        /*
        final Handler handler_level = new Handler() {

            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {

                if (msg.what == 1) {
                    Toast.makeText(GameFieldActivity.this, "LEVEL UP", Toast.LENGTH_SHORT).show();
                    textView_level.setText("Level: " + level);
                }
            }
        };

        Thread t_level = new Thread(new Runnable() {
            @Override
            public void run() {
                while (level > 0) {
                    if (amount_clicks % 100 == 0) {
                        level++;
                        handler_score.sendEmptyMessageDelayed(1, 500);
                    }


                }
            }
        });
        t_level.start();
        */


        //передача данных при закрытии активности
        button_save_exit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                    if (amount_clicks > (getIntent().getIntExtra("scoreInfo_intent", 0)))
                    {
                        Intent new_score_intent = new Intent(GameFieldActivity.this, WelcomeActivity.class);
                        new_score_intent.putExtra("new_score", score);
                        Log.e("RESULT","SCORE_RESULT_OK");
                        setResult(RESULT_OK, new_score_intent);
                    }


                    if (level > (getIntent().getIntExtra("levelInfo_intent", 0)))
                    {
                        Intent new_level_intent = new Intent(GameFieldActivity.this, WelcomeActivity.class);
                        new_level_intent.putExtra("new_level", level);
                        setResult(RESULT_OK, new_level_intent);
                    }

            finish();}
        });


    }
}