package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {

    TextView textView_welcome;
    TextView textView_levelInfo;
    TextView textView_scoreInfo;
    Button button_update_results;
    int level = 1;
    long score;
    boolean check_sound;
    boolean check_vibration;
    boolean check_language; //true = ru --- false = eu
    Button button_sound;
    Button button_vibration;
    Button button_language;


    SharedPreferences SHARED_PREFERENCES_SAVING;

    final int REQUEST_CODE_SCORE = 1;
    SharedPreferences.Editor editor;

    public static final String localeCode = "lang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
        String lang =
                preferences.getString(localeCode, getResources().getConfiguration().locale.getLanguage());

        updateLangForContext(this, lang);
        setContentView(R.layout.welcome_activiy_screen);

        textView_welcome = findViewById(R.id.welcomeActivity_textView_welcome);
        textView_levelInfo = findViewById(R.id.welcomeActivity_textView_levelInfo);
        textView_scoreInfo = findViewById(R.id.welcomeActivity_textView_scoreInfo);
        button_update_results = findViewById(R.id.welcomeActivity_button_update_results);
        button_sound = findViewById(R.id.welcomeActivity_button_sound_on_off);
        button_vibration = findViewById(R.id.welcomeActivity_button_vibration_on_off);
        button_language = findViewById(R.id.welcomeActivity_button_language);

        textView_welcome.setText(getResources().getString(R.string.welcome));
        textView_welcome.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);

        SHARED_PREFERENCES_SAVING = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        editor = SHARED_PREFERENCES_SAVING.edit();


        check_sound = SHARED_PREFERENCES_SAVING.getBoolean("APP_PREFERENCES_SOUND", check_sound);
        check_vibration = SHARED_PREFERENCES_SAVING.getBoolean("APP_PREFERENCES_VIBRATION", check_vibration);
        check_language = SHARED_PREFERENCES_SAVING.getBoolean("APP_PREFERENCES_LANGUAGE", check_language);

        button_update_results.setText(getResources().getString(R.string.start));
        button_update_results.setTextSize(TypedValue.COMPLEX_UNIT_PT, 12);

        //Button language
        button_language.setText(getResources().getString(R.string.gameFieldActivity_language_button));
        button_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor_ = preferences.edit();
                if (check_language) {
                    check_language = false;
                    editor_.putString(localeCode, "en");

                } else {
                    check_language = true;
                    editor_.putString(localeCode, "ru");
                    Toast.makeText(WelcomeActivity.this, "App reboot", Toast.LENGTH_SHORT);
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                editor.putBoolean("APP_PREFERENCES_LANGUAGE", check_language);
                editor.commit();
                editor_.commit();


                System.exit(0);// перезагружает все приложение
            }
        });


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

            }
        });


        if (SHARED_PREFERENCES_SAVING.contains("APP_PREFERENCES_SCORE")) {
            textView_scoreInfo.setText(getResources().getString(R.string.welcomActivity_score) + SHARED_PREFERENCES_SAVING.getLong("APP_PREFERENCES_SCORE", score));
            textView_scoreInfo.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
            score = SHARED_PREFERENCES_SAVING.getLong("APP_PREFERENCES_SCORE", score);
        } else {
            textView_scoreInfo.setText(getResources().getString(R.string.welcomActivity_score) + "0");
            textView_scoreInfo.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
        }
        if (SHARED_PREFERENCES_SAVING.contains("APP_PREFERENCES_LEVEL")) {
            textView_levelInfo.setText(getResources().getString(R.string.welcomActivity_level) + SHARED_PREFERENCES_SAVING.getInt("APP_PREFERENCES_LEVEL", level));
            textView_levelInfo.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
            level = SHARED_PREFERENCES_SAVING.getInt("APP_PREFERENCES_LEVEL", level);
        } else {
            textView_levelInfo.setText(getResources().getString(R.string.welcomActivity_level) + "1");
            textView_levelInfo.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
        }


        button_update_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Alldata = new Intent(WelcomeActivity.this, GameFieldActivity.class);
                intent_Alldata.putExtra("new_level_intent", level);
                intent_Alldata.putExtra("new_score_intent", score);
                intent_Alldata.putExtra("check_vibration", check_vibration);
                intent_Alldata.putExtra("check_sound", check_sound);
                intent_Alldata.putExtra("check_language", check_language);
                startActivityForResult(intent_Alldata, REQUEST_CODE_SCORE);

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {


            level = data.getIntExtra("new_level", level);
            textView_levelInfo.setText(getResources().getString(R.string.welcomActivity_level) + level);
            textView_levelInfo.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);


            score = data.getLongExtra("new_score", 0);
            textView_scoreInfo.setText(getResources().getString(R.string.welcomActivity_score) + score);
            textView_scoreInfo.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);

            check_sound = data.getBooleanExtra("SOUND", check_sound);
            check_vibration = data.getBooleanExtra("VIBRATION", check_vibration);

            editor.putBoolean("APP_PREFERENCES_SOUND", check_sound);
            editor.putBoolean("APP_PREFERENCES_VIBRATION", check_vibration);
            editor.putLong("APP_PREFERENCES_SCORE", score);
            editor.putInt("APP_PREFERENCES_LEVEL", level);
            editor.apply();


        }
    }

    public static void updateLangForContext(Context context, String lang) {
        //здесь из базы данных достаем предыдущее значение локали и ставим его
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }
}
