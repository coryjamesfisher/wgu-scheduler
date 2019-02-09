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

            Calendar calZero = Calendar.getInstance();
            calZero.set(2018, 5, 1);
            Date zero = calZero.getTime();
            Calendar calZeroThen = Calendar.getInstance();
            calZeroThen.set(2018, 11, 31);
            Date zeroEnd = calZeroThen.getTime();

            Calendar calOne = Calendar.getInstance();
            calOne.set(2019, 0, 1);
            Date one = calOne.getTime();
            Calendar calOneThen = Calendar.getInstance();
            calOneThen.set(2019, 6, 31);
            Date oneEnd = calOneThen.getTime();

            Calendar calTwo = Calendar.getInstance();
            calTwo.set(2019, 5, 1);
            Date two = calTwo.getTime();
            Calendar calTwoThen = Calendar.getInstance();
            calTwoThen.set(2019, 11, 31);
            Date twoEnd = calTwoThen.getTime();

            termRepository.save(new Term("TERM 0", zero, zeroEnd));
            termRepository.save(new Term("TERM 1", one, oneEnd));
            termRepository.save(new Term("TERM 2", two, twoEnd));

            mentorRepository.save(new Mentor("Bryan", "Chun", "555-555-1234", "bryan.chun@wgu.edu"));


            courseRepository.save(new Course(1L,"C100", now, now, "COMPLETE", ""));
            courseRepository.save(new Course(2L,"C196", now, now, "IN PROGRESS", ""));
            courseRepository.save(new Course(2L,"C198", now, now, "COMPLETE", ""));
            courseRepository.save(new Course(3L,"C196", now, now, "IN PROGRESS", ""));
            courseRepository.save(new Course(3L,"C196", now, now, "IN PROGRESS", ""));

            courseMentorRepository.save(new CourseMentor(2L, 1L));
            courseMentorRepository.save(new CourseMentor(3L, 1L));
            courseMentorRepository.save(new CourseMentor(4L, 1L));

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
