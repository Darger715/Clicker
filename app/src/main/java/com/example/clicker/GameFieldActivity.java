package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class GameFieldActivity extends AppCompatActivity {

    TextView textView_level;
    TextView textView_amountClicks;
    int amount_clicks = 0;
    int level;
    int score;
    int hp = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field_activity_screen);

        textView_amountClicks = findViewById(R.id.game_field_activity_textview_amount_click);
        textView_level = findViewById(R.id.game_field_activity_textview_level);


        level = getIntent().getIntExtra("levelInfo_intent", 1);
        score = getIntent().getIntExtra("scoreInfo_intent", 0);

        textView_level.setText("Level: " + level);

        (findViewById(R.id.game_field_activity_button_game_field)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_clicks++;
                textView_amountClicks.setText("Score:\n" + (int) amount_clicks);
                if (amount_clicks % 100 == 0) {
                    level++;
                    Toast.makeText(GameFieldActivity.this, "LEVEL UP", Toast.LENGTH_SHORT).show();
                    textView_level.setText("Level: " + level);
                }


            }
        });


        //передача данных при закрытии активности
        if (GameFieldActivity.this.isFinishing())
        {

            if (score > (getIntent().getIntExtra("scoreInfo_intent", 0)))
            {

                Intent new_score_intent = new Intent(GameFieldActivity.this, WelcomeActivity.class);
                new_score_intent.putExtra("new_score", score);
                startActivity(new_score_intent);
                Log.e("1","NEW SCORE");
            }


            if (level > (getIntent().getIntExtra("levelInfo_intent", 0)))
            {

                Intent new_level_intent = new Intent(GameFieldActivity.this, WelcomeActivity.class);
                new_level_intent.putExtra("new_level", level);
                startActivity(new_level_intent);
                Log.e("1","NEW LEVEL");
            }

        }

    }
}