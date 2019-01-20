package com.forthecoder.collegeschedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.forthecoder.collegeschedule.entity.AlertRepository;
import com.forthecoder.collegeschedule.entity.AssessmentRepository;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.entity.MentorRepository;
import com.forthecoder.collegeschedule.entity.Term;
import com.forthecoder.collegeschedule.entity.TermRepository;

import java.util.Date;

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "WGU_Scheduler";
    private static final int DB_VERSION = 4;

    public DB(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TermRepository termRepository = new TermRepository(db);
        CourseRepository courseRepository = new CourseRepository(db);
        MentorRepository mentorRepository = new MentorRepository(db);
        AssessmentRepository assessmentRepository = new AssessmentRepository(db);
        AlertRepository alertRepository = new AlertRepository(db);

        try {
            termRepository.createSchema();
            courseRepository.createSchema();
            mentorRepository.createSchema();
            assessmentRepository.createSchema();
            alertRepository.createSchema();
        } catch (Exception e) {
            // ignore
            Log.e("ERROR", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        TermRepository termRepository = new TermRepository(db);
        CourseRepository courseRepository = new CourseRepository(db);
        MentorRepository mentorRepository = new MentorRepository(db);
        AssessmentRepository assessmentRepository = new AssessmentRepository(db);
        AlertRepository alertRepository = new AlertRepository(db);

        try {
            termRepository.dropSchema();
            courseRepository.dropSchema();
            mentorRepository.dropSchema();
            assessmentRepository.dropSchema();
            alertRepository.dropSchema();

            termRepository.createSchema();
            courseRepository.createSchema();
            mentorRepository.createSchema();
            assessmentRepository.createSchema();
            alertRepository.createSchema();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
