package com.sample.multiplechoicequiz;

// This file contains questions from QuestionBank

import java.util.LinkedList;
import java.util.List;

public class QuestionBank{

    List<Question> mQuestionBank = new LinkedList<>();

    // method returns number of questions
    public int getLength(){
        return mQuestionBank.size();
    }

    public void addQuestion(Question question){
        mQuestionBank.add(question);
    }
    public Question getQuestionObject(int index){
        return mQuestionBank.get(index);
    }
    // method returns question from array textQuestions[] based on array index
    public String getQuestion(int index) {
        return mQuestionBank.get(index).getQuestion();
    }

    // method return a single multiple choice item for question based on array index,
    // based on number of multiple choice item in the list - 1, 2, 3 or 4 as an argument
    public String getChoice(int index, int num) {
        return mQuestionBank.get(index).getMultipleChoice()[num];
    }

    //  method returns correct answer for the question based on array index
    public String getCorrectAnswer(int index) {
        return mQuestionBank.get(index).getAnswer();
    }



}