package com.forthecoder.collegeschedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.forthecoder.collegeschedule.entity.Alert;
import com.forthecoder.collegeschedule.entity.AlertRepository;
import com.forthecoder.collegeschedule.entity.Assessment;
import com.forthecoder.collegeschedule.entity.AssessmentRepository;
import com.forthecoder.collegeschedule.entity.Course;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.entity.Mentor;
import com.forthecoder.collegeschedule.entity.MentorRepository;
import com.forthecoder.collegeschedule.entity.Term;
import com.forthecoder.collegeschedule.entity.TermRepository;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DB extends SQLiteOpenHelper {

    public static final String DB_NAME = "WGU_Scheduler";
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

            Date now = Calendar.getInstance().getTime();
            termRepository.insert(new Term("TERM 1", now, now));
            termRepository.insert(new Term("TERM 2", now, now));

            mentorRepository.insert(new Mentor("Bryan", "Chun", "555-555-1234", "bryan.chun@wgu.edu"));

            courseRepository.insert(new Course(1,"C196", now, now, "IN PROGRESS", 1, ""));
            courseRepository.insert(new Course(1,"C198", now, now, "IN PROGRESS", 1, ""));
            courseRepository.insert(new Course(2,"C196", now, now, "IN PROGRESS", 1, ""));

            assessmentRepository.insert(new Assessment(1, "OBJECTIVE", "OBJECTIVE ASSESSMENT 1", now));
            assessmentRepository.insert(new Assessment(1, "OBJECTIVE", "PERFORMANCE ASSESSMENT 1", now));

            alertRepository.insert(new Alert(1, 1, now, "REMINDER - TAKE EXAM TODAY"));
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
