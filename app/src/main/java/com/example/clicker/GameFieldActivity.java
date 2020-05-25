package com.example.clicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GameFieldActivity extends AppCompatActivity {

    TextView textView_level;
    TextView textView_amountClicks;
    TextView textView_hp;
    Button button_click;
    int amount_clicks = 0;
    int level;
    int score;
    int hp = 3;
    boolean check_amount_clicks_zero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field_activity_screen);

        textView_amountClicks = findViewById(R.id.game_field_activity_textview_amount_click);
        textView_level = findViewById(R.id.game_field_activity_textview_level);
        textView_hp = findViewById(R.id.game_field_activity_textview_hp);
        button_click = findViewById(R.id.game_field_activity_button_game_field);

        level = getIntent().getIntExtra("levelInfo_intent", 1);
        score = getIntent().getIntExtra("scoreInfo_intent", 0);


        textView_level.setText("Level: " + level);
        textView_amountClicks.setText("Score:\n0");
        textView_hp.setText("HP: " + hp);

        button_click.setOnClickListener(new View.OnClickListener() {
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

                //score
                // if (amount_clicks > (getIntent().getIntExtra("scoreInfo_intent", 0))) {
                //     Toast.makeText(GameFieldActivity.this, "NEW SCORE", Toast.LENGTH_SHORT).show();
                //}

                //hp
    /*            if (amount_clicks == 0)
                {
                    amount_clicks++;
                    check_amount_clicks_zero = true;
                }
                    if (amount_clicks % 2 == 0)
                    {
                        if(check_amount_clicks_zero)
                        {
                        amount_clicks--;
                        check_amount_clicks_zero = false;
                        }
                        button_click.setBackgroundColor(Color.RED);
                        if (button_click.isClickable())
                        {
                            hp--;
                            Toast.makeText(GameFieldActivity.this, "- 1 HP", Toast.LENGTH_SHORT).show();
                            textView_hp.setText("HP: "+ hp);
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }

                        button_click.setBackgroundColor(getResources().getColor(R.color.button_color));
                    }
     */

            }


        });

        //Переключение цвета поля на запрещённый
        //Задейсвие hp

        final Handler handler_amount_clicks = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {

                if (msg.what == 1) {
                    button_click.setBackgroundColor(Color.RED);
                    Toast.makeText(GameFieldActivity.this, "RED COLOR", Toast.LENGTH_SHORT).show();
                    if (button_click.isActivated()) {
                        hp--;
                        Toast.makeText(GameFieldActivity.this, "- 1 HP", Toast.LENGTH_SHORT).show();
                        textView_hp.setText("HP: " + hp);
                    }


                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    button_click.setBackgroundColor(getResources().getColor(R.color.button_color));
                    Toast.makeText(GameFieldActivity.this, "MAIN COLOR", Toast.LENGTH_SHORT).show();


                }
            }
        };

        Thread t_amount_clicks = new Thread(new Runnable() {
            @Override
            public void run() {
                while (hp > 0) {
                    if (amount_clicks == 0) {
                        amount_clicks++;
                        check_amount_clicks_zero = true;
                    }
                    if (amount_clicks % 10 == 0) {
                        if (check_amount_clicks_zero) {
                            amount_clicks--;
                            check_amount_clicks_zero = false;
                        }
                        handler_amount_clicks.sendEmptyMessageDelayed(1, 500);
                    }


                }
            }
        });
        t_amount_clicks.start();


        //передача данных при закрытии активности
        if (GameFieldActivity.this.isFinishing()) {

            if (score > (getIntent().getIntExtra("scoreInfo_intent", 0))) {
                Intent new_score_intent = new Intent(GameFieldActivity.this, WelcomeActivity.class);
                new_score_intent.putExtra("new_score", score);
                startActivity(new_score_intent);
                Log.e("1", "NEW SCORE");
            }


            if (level > (getIntent().getIntExtra("levelInfo_intent", 0))) {

                Intent new_level_intent = new Intent(GameFieldActivity.this, WelcomeActivity.class);
                new_level_intent.putExtra("new_level", level);
                startActivity(new_level_intent);
                Log.e("1", "NEW LEVEL");
            }
        }
    }
}