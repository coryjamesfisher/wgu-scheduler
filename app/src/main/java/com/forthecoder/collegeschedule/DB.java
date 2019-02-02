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
import com.forthecoder.collegeschedule.entity.CourseMentor;
import com.forthecoder.collegeschedule.entity.CourseMentorRepository;
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
        CourseMentorRepository courseMentorRepository = new CourseMentorRepository(db);
        AssessmentRepository assessmentRepository = new AssessmentRepository(db);
        AlertRepository alertRepository = new AlertRepository(db);

        try {
            termRepository.createSchema();
            courseRepository.createSchema();
            mentorRepository.createSchema();
            courseMentorRepository.createSchema();
            assessmentRepository.createSchema();
            alertRepository.createSchema();

            Date now = Calendar.getInstance().getTime();
            termRepository.save(new Term("TERM 1", now, now));
            termRepository.save(new Term("TERM 2", now, now));

            mentorRepository.save(new Mentor("Bryan", "Chun", "555-555-1234", "bryan.chun@wgu.edu"));

            courseRepository.save(new Course(1L,"C196", now, now, "IN PROGRESS", ""));
            courseRepository.save(new Course(1L,"C198", now, now, "IN PROGRESS", ""));
            courseRepository.save(new Course(2L,"C196", now, now, "IN PROGRESS", ""));

            courseMentorRepository.save(new CourseMentor(1L, 1L));
            courseMentorRepository.save(new CourseMentor(2L, 1L));
            courseMentorRepository.save(new CourseMentor(3L, 1L));

            assessmentRepository.save(new Assessment(1L, "OBJECTIVE", "OBJECTIVE ASSESSMENT 1", now, "NOT TAKEN"));
            assessmentRepository.save(new Assessment(1L, "OBJECTIVE", "PERFORMANCE ASSESSMENT 1", now, "NOT TAKEN"));

//            alertRepository.save(new Alert(1L, 1L, 1L, now, Alert.ALERT_TYPE.START, "BOGUS ALERT"));
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
        CourseMentorRepository courseMentorRepository = new CourseMentorRepository(db);
        AssessmentRepository assessmentRepository = new AssessmentRepository(db);
        AlertRepository alertRepository = new AlertRepository(db);

        try {
            termRepository.dropSchema();
            courseRepository.dropSchema();
            mentorRepository.dropSchema();
            courseMentorRepository.dropSchema();
            assessmentRepository.dropSchema();
            alertRepository.dropSchema();

            termRepository.createSchema();
            courseRepository.createSchema();
            mentorRepository.createSchema();
            courseMentorRepository.createSchema();
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
