package com.sample.multiplechoicequiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private static String LOG_APP_TAG = "tag";

    private int highscore = 0;
    private int userScore = 0;

    private TextView textView_highScore;
    private TextView textView_userScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        highscore = getIntent().getIntExtra("highscore",0);
        userScore = getIntent().getIntExtra("userscore",0);
        textView_highScore = (TextView) findViewById(R.id.textView_highScore);
        textView_userScore = (TextView) findViewById(R.id.textView_userScore);
        textView_highScore.setText(highscore+"");
        textView_userScore.setText(userScore+"");
    }

    public void tryAgain(View view){
        // direct to quiz activity
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }
}
