package com.example.clicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    boolean check_amount_clicks;
    int hp = 3;
    boolean check_red_button_push;
    boolean check_game_over = true;
    boolean check_change_color = true;
    boolean isDialogShow;
    boolean isDialogShow_backPressed;

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
        textView_hp.setText("HP: " + hp);

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
                check_amount_clicks = true;
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


        final Handler handler_change_color = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {

                if (msg.what == 0) {
                    check_red_button_push = true;
                    check_change_color = true;
                    button_click.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                if (msg.what == 1) {
                    check_amount_clicks = false;
                    check_red_button_push = false;
                    button_click.setBackgroundColor(getResources().getColor(R.color.button_color));
                    Log.e("MSG.WHAT", "" + msg.what);
                }
            }
        };
        Thread t_change_color = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                if (hp > 0) {
                    if (amount_clicks != 0) {
                        if (check_amount_clicks) {
                            if (amount_clicks % 3 == 0) {
                                check_amount_clicks = false;
                                handler_change_color.sendEmptyMessage(0);
                            }

                        } else if (check_change_color) {
                            check_change_color = false;
                            handler_change_color.sendEmptyMessageDelayed(1, 1000);
                        }
                    }
                }}
            }

        });
        t_change_color.start();


        final Handler handler_hp = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {

                if (msg.what == 0) {
                    check_red_button_push = false;
                    hp--;
                    Toast.makeText(GameFieldActivity.this, "-1 HP", Toast.LENGTH_SHORT).show();
                    textView_hp.setText("HP: " + hp);
                }
                if (msg.what == 1) {
                    button_click.setClickable(false);
                    GameOverDialog();
                }
            }
        };
        Thread t_hp = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (hp > 0) {
                        if (check_red_button_push) {
                            if (button_click.isPressed()) {
                                check_red_button_push = false;
                                handler_hp.sendEmptyMessage(0);

                            }
                        }


                    } else if (check_game_over) {
                        check_game_over = false;
                        handler_hp.sendEmptyMessage(1);
                    }
                }
            }
        });
        t_hp.start();


        //передача данных при закрытии активности
        button_save_exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_new_data = new Intent(GameFieldActivity.this, WelcomeActivity.class);

                if (level > (getIntent().getIntExtra("new_level_intent", 0))) {

                    intent_new_data.putExtra("new_level", level);

                }

                if (amount_clicks > score) {
                    intent_new_data.putExtra("new_score", amount_clicks);
                }
                if (amount_clicks <= score) {
                    intent_new_data.putExtra("new_score", score);
                }

                intent_new_data.putExtra("VIBRATION", check_vibration);
                intent_new_data.putExtra("SOUND", check_sound);


                setResult(RESULT_OK, intent_new_data);
                finish();
            }
        });


    }

    //GameOver DialogFragment
    public void GameOverDialog() {
        if (hp < 1) {
            if (!isDialogShow) {
                GameOver_DialogFragment gameOver_DialogFragment = new GameOver_DialogFragment();
                gameOver_DialogFragment.show(getSupportFragmentManager(), "gameOver_Dialog");
                isDialogShow = true;
                Log.e("GameOver_Dialog", "" + isDialogShow);
            }


        } else
            super.onBackPressed();
    }


    void gameOver_DialogYes(String a) {
        GameOverDialog();
finish();
    }

    void gameOver_DialogNo() {
        //button_click.setClickable(false);
        isDialogShow = false;

        amount_clicks = 0;
        hp = 3;
        textView_amountClicks.setText("Score:\n" + amount_clicks);
        textView_hp.setText("HP: " + hp);
        button_click.setClickable(true);




    }

    //Back DialogFragment
    public void onBackPressed() {

        if (!isDialogShow_backPressed) {
            Back_DialogFragment backDialogFragment = new Back_DialogFragment();
            backDialogFragment.show(getSupportFragmentManager(), "backDialog");
            isDialogShow_backPressed = true;
            Log.e("onBackPressed_Dialog", "" + isDialogShow_backPressed);

        } else
            super.onBackPressed();
    }


    void backDialogYes(String a) {
        onBackPressed();
    }

    void backDialogNo() {
        isDialogShow_backPressed = false;
    }
}