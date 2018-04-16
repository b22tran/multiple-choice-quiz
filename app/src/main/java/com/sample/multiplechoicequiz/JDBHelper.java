package com.sample.multiplechoicequiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CRISPR on 4/9/2018.
 */

public class JDBHelper extends SQLiteOpenHelper {

    // database name
    public static String DATABASE_NAME = "quiz_database";
    // current db version
    private static final int DATABASE_VERSION = 2;
    // table name
    private static final String TABLE_QUIZ = "quiz";
    // table fields
    private static final String QUESTION_ID = "id";
    private static final String QUESTION = "question";
    private static final String MC_0 = "multiple_choice_0";
    private static final String MC_1 = "multiple_choice_1";
    private static final String MC_2 = "multiple_choice_2";
    private static final String MC_3 = "multiple_choice_3";
    private static final String ANSWER = "answer";
    public static String TAG = "JDBH";

    public JDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Client Table Create Query in this string
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE "
            + TABLE_QUIZ + "(" + QUESTION_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + QUESTION + " TEXT,"
            + MC_0 + " TEXT," + MC_1 + " TEXT," + MC_2 + " TEXT," + MC_3 + " TEXT,"
            + ANSWER + " TEXT );";

    /**
     * This method is called by system if the database is accessed but not yet
     * created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTION); // create client table
    }

    /**
     * This method is called when any modifications in database are done like
     * version is updated or database schema is changed
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ); // drop table if exists
        onCreate(db);
    }

    public long addQuestion(Question q) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(QUESTION_ID, q.getID());
        values.put(QUESTION, q.getQuestion());
        values.put(MC_0, q.getMultipleChoice()[0]);
        values.put(MC_1, q.getMultipleChoice()[1]);
        values.put(MC_2, q.getMultipleChoice()[2]);
        values.put(MC_3, q.getMultipleChoice()[3]);
        values.put(ANSWER, q.getAnswer());

        // insert row in client table
        long insert = db.insert(TABLE_QUIZ, null, values);
        return insert;
    }

    public int updateEntry(Question q) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_ID, q.getID());
        values.put(QUESTION, q.getQuestion());
        values.put(MC_0, q.getMultipleChoice()[0]);
        values.put(MC_1, q.getMultipleChoice()[1]);
        values.put(MC_2, q.getMultipleChoice()[2]);
        values.put(MC_3, q.getMultipleChoice()[3]);
        values.put(ANSWER, q.getAnswer());

        // update row in client table base on client.is value
        return db.update(TABLE_QUIZ, values, QUESTION_ID + " = ?",
                new String[]{String.valueOf(q)});
    }

    public void deleteEntry(long id) {
        // delete row in client table based on id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZ, QUESTION_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public Question getQuestion(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM client WHERE id = ?;
        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ + " WHERE "
                + QUESTION_ID + " = " + id;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

            String[] mc = new String[4];
            Question question = new Question();
            question.setID(c.getInt(c.getColumnIndex(QUESTION_ID)));
            question.setQuestion(c.getString(c.getColumnIndex(QUESTION)));
            mc[0] = c.getString(c.getColumnIndex(MC_0));
            mc[1] = c.getString(c.getColumnIndex(MC_1));
            mc[2] = c.getString(c.getColumnIndex(MC_2));
            mc[3] = c.getString(c.getColumnIndex(MC_3));
            question.setMultipleChoice(mc);
            question.setAnswer(c.getString(c.getColumnIndex(ANSWER)));

        return question;
    }

    public QuestionBank getAllQuestions() {
        QuestionBank bank = new QuestionBank();

        String selectQuery = "SELECT  * FROM " + TABLE_QUIZ;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String[] mc = new String[4];
                Question question = new Question();
                question.setID(c.getInt(c.getColumnIndex(QUESTION_ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QUESTION)));
                mc[0] = c.getString(c.getColumnIndex(MC_0));
                mc[1] = c.getString(c.getColumnIndex(MC_1));
                mc[2] = c.getString(c.getColumnIndex(MC_2));
                mc[3] = c.getString(c.getColumnIndex(MC_3));
                question.setMultipleChoice(mc);
                question.setAnswer(c.getString(c.getColumnIndex(ANSWER)));

                // adding to question list
                bank.addQuestion(question);
            } while (c.moveToNext());
        }
        return bank;
    }
}
