package com.sample.multiplechoicequiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String QUESTION_INDEX = "index";
    private static final String SCORE = "score";

    private QuestionBank mQuestionLibrary;
    private TextView mScoreView;   // view for current total score
    private TextView mQuestionView;  //current question to answer
    private Button mButtonChoice1; // multiple choice 1 for mQuestionView
    private Button mButtonChoice2; // multiple choice 2 for mQuestionView
    private Button mButtonChoice3; // multiple choice 3 for mQuestionView
    private Button mButtonChoice4; // multiple choice 4 for mQuestionView

    private String mAnswer;  // correct answer for question in mQuestionView
    private int mScore = 0;  // current total score
    private int mQuestionNumber = 0; // current question number
    private boolean isLast = false; // bool for whether the user has reached the last question
    JDBHelper db;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(QUESTION_INDEX, mQuestionNumber-1);
        savedInstanceState.putInt(SCORE, mScore);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db = new JDBHelper(getApplicationContext());

        // the following four lines of code just inputs 6 question into the database
        // utilize for testing
//        mQuestionLibrary = getBaseQuestions();
//        for (int i=0; i<mQuestionLibrary.getLength();i++){
//            db.addQuestion(mQuestionLibrary.getQuestionObject(i));
//        }

        mQuestionLibrary = db.getAllQuestions();

        if (savedInstanceState != null) {
            mQuestionNumber = savedInstanceState.getInt(QUESTION_INDEX, 0);
            mScore = savedInstanceState.getInt(SCORE, 0);
        }
        // setup screen for the first question with four alternative to answer
        mScoreView = (TextView)findViewById(R.id.score);
        mQuestionView = (TextView)findViewById(R.id.question);
        mButtonChoice1 = (Button)findViewById(R.id.choice1);
        mButtonChoice2 = (Button)findViewById(R.id.choice2);
        mButtonChoice3 = (Button)findViewById(R.id.choice3);
        mButtonChoice4 = (Button)findViewById(R.id.choice4);
        updateQuestion();

        // show current total score for the user
        updateScore(mScore);


    }

    private boolean updateQuestion(){
        // check if we are not outside array bounds for questions
        if(mQuestionNumber < mQuestionLibrary.getLength() ){
            // set the text for new question, and new 4 alternative to answer on four buttons
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
            mButtonChoice1.setText(mQuestionLibrary.getChoice(mQuestionNumber, 0));
            mButtonChoice2.setText(mQuestionLibrary.getChoice(mQuestionNumber, 1));
            mButtonChoice3.setText(mQuestionLibrary.getChoice(mQuestionNumber, 2));
            mButtonChoice4.setText(mQuestionLibrary.getChoice(mQuestionNumber,3));
            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
            mQuestionNumber++;
            return false; // false if hasn't reached end of questions
       }
        else {
            Toast.makeText(QuizActivity.this, "It was the last question!", Toast.LENGTH_SHORT).show();
            return true; // true if user has reached end of questions
        }
    }

    // show current total score for the user
    private void updateScore(int point) {
        mScoreView.setText("" + mScore+"/"+mQuestionLibrary.getLength());
    }

    public void onClick(View view) {
        // checks to see if the user has already answered the last question
        if(!isLast) {
            processAnswer(view);
            isLast = updateQuestion();
        }else{
            int highScore = loadHighScore();
            if(mScore> highScore){
                saveHighScore("highscore", mScore);
                highScore = mScore;
            }
            // direct to highscore menu activity
            Intent intent = new Intent(this, MenuActivity.class);

            intent.putExtra("userscore",mScore);
            intent.putExtra("highscore",highScore);
            startActivity(intent);
        }
    }
    private void processAnswer(View view){
        //all logic for all answers buttons in one method
        Button answer = (Button) view;
        // if the answer is correct, increase the score
        if (answer.getText().equals(mAnswer)) {
            mScore = mScore + 1;
            Toast.makeText(QuizActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(QuizActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
        }
        // show current total score for the user
        updateScore(mScore);
        // once user answer the question, we move on to the next one
    }
    private void saveHighScore(String key, int value){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private int loadHighScore(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        int highscore = sharedPreferences.getInt("highscore", 0);
        return highscore;
    }

    private static String LOG_APP_TAG = "tag";

    public void saveStringToFile(String json){
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("highscore.txt", Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
        } catch(IOException e) {
            Log.e(LOG_APP_TAG, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_APP_TAG, e.getMessage());
                }
            }
        }
    }

    public String loadStringToFile(){
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput("highscore.txt");
            byte[] reader = new byte[inputStream.available()];
            while (inputStream.read(reader) != -1) {}
            return new String(reader);
        } catch(IOException e) {
            Log.e(LOG_APP_TAG, e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_APP_TAG, e.getMessage());
                }
            }
        }
        return "";
    }

    // note: this method isn't used in the build, was used to test and gen. txt w/ q
    // use to set sample questions in text file
    public static QuestionBank getBaseQuestions(){
        QuestionBank bank = new QuestionBank();

        Question q1 = new Question(0,"1. When did Google acquired Android?"
                , new String[]{"2003", "2004", "2005", "2006"}
                ,"2005");

        Question q2 = new Question(1,"2. What is the name of build toolkit for Android Studio?"
                , new String[] {"JVM", "Gradle", "Dalvik", "HAXM"}
                ,"Dalvik");

        Question q3 = new Question(2,"3. What widget can replace any use of radio buttons?"
                , new String[]{"Toggle", "Spinner", "Switch", "CheckBox"}
                ,"Spinner");

        Question q4 = new Question(3,"4. What is the most recent Android OS ?"
                ,new String[]{"Oreo", "Nougat", "Marshmallow", "Octopus"}
                ,"Oreo");

        Question q5 = new Question(4, "5. Which programming language is accepted for android development?"
                , new String[]{"Golang", "JavaScript", "C", "Kotlin"}
                ,"Kotlin");

        Question q6 = new Question( 5,"6. What is DDMS?"
                , new String[]{"Dalvik Debug Monitor Server", "Dijkstra Debug Monitor Server",
                "Distributed Debug Monitor Server","Decentralized Debug Monitor Server"}
                ,"Dalvik Debug Monitor Server");

        bank.addQuestion(q1);
        bank.addQuestion(q2);
        bank.addQuestion(q3);
        bank.addQuestion(q4);
        bank.addQuestion(q5);
        bank.addQuestion(q6);
        return bank;
    }
 }