package com.example.clicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
    boolean check_vibration;
    boolean check_sound;

    private AudioManager audioManager;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field_activity_screen);

        textView_amountClicks = findViewById(R.id.game_field_activity_textview_amount_click);
        textView_level = findViewById(R.id.game_field_activity_textview_level);
        textView_hp = findViewById(R.id.game_field_activity_textview_hp);
        button_click = findViewById(R.id.game_field_activity_button_game_field);
        button_save_exit = findViewById(R.id.game_field_activity_button_save_and_exit);

        //Данные с первой Activity
        level = getIntent().getIntExtra("new_level_intent", 1);
        score = getIntent().getLongExtra("new_score_intent", 0);
        check_vibration = getIntent().getBooleanExtra("check_vibration", true);
        check_sound = getIntent().getBooleanExtra("check_sound", true);


        textView_amountClicks.setText("Score:\n0");

        if (level > 1) {
            textView_level.setText("Level: " + level);
        } else {
            textView_level.setText("Level: 1");

        }


        // Получаем доступ к менеджеру звуков
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

//sound
        if (check_sound) {
            button_click.setSoundEffectsEnabled(true);
            button_save_exit.setSoundEffectsEnabled(true);
        } else {
            button_click.setSoundEffectsEnabled(false);
            button_save_exit.setSoundEffectsEnabled(false);
        }

        button_click.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_clicks++;
                textView_amountClicks.setText("Score:\n" + (long) amount_clicks);
                //audioManager.loadSoundEffects();

//vibration
                final Handler handler_vibration = new Handler() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void handleMessage(@NonNull Message msg) {

                        if (msg.what == 1) {
                            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(50);
                        }
                    }
                };

                Thread t_vibration = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (button_click.isClickable()) {
                            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                                if (check_vibration) {
                                    handler_vibration.sendEmptyMessage(1);
                                }
                            }
                        }
                    }

                });
                t_vibration.start();


                //level
                if (amount_clicks % 50 == 0) {
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

                vibrator.cancel();


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