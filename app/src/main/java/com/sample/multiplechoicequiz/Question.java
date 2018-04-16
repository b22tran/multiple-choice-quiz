package com.sample.multiplechoicequiz;

/**
 * Created by CRISPR on 4/11/2018.
 */

public class Question {
    private int mID;
    private String mQuestion;
    private String[] mMultipleChoice;
    private String mAnswer;

    Question(){}
    Question(int id, String question, String[] mc, String ans){
        mID = id;
        mQuestion = question;
        mMultipleChoice = mc;
        mAnswer = ans;
    }
    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String[] getMultipleChoice() {
        return mMultipleChoice;
    }

    public void setMultipleChoice(String[] mMultipleChoice) {
        this.mMultipleChoice = mMultipleChoice;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }




}
