package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.forthecoder.collegeschedule.entity.Course;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.entity.Term;
import com.forthecoder.collegeschedule.entity.TermRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.Calendar;
import java.util.List;

/**
 * Requirement C: Student Scheduler and Progress Tracking Application
 * This activity shows progress meters for the term and the degree program overall.
 * Scheduling is done via the creation of terms, courses, and assessments which
 * each have dates associated.
 */
public class MainActivity extends BaseActivity {

    public MainActivity() {
        super();
        contentLayout = R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.deleteDatabase(DB.DB_NAME);
        super.onCreate(savedInstanceState);

        ((ProgressBar)findViewById(R.id.degreeProgressIndicator))
                .setProgress(getDegreeProgressPercentage());

        ((ProgressBar)findViewById(R.id.termProgressIndicator))
                .setProgress(getTermProgressPercentage());
    }

    private int getTermProgressPercentage() {

        final TermRepository termRepository = new TermRepository(getDatabase());
        final CourseRepository courseRepository = new CourseRepository(getDatabase());

        try {
            Term term = termRepository.findFirstIncludingDate(Calendar.getInstance().getTime());

            if (term == null) {
                return 0;
            }

            List<Course> courses = courseRepository.findAllByTermId(term.getRowid());

            Integer countComplete = 0;
            Integer count = 0;
            for (Course course : courses) {
                count++;

                if (course.getStatus().equals("COMPLETE")) {
                    countComplete++;
                }
            }

            Double ratio = Math.floor(countComplete.doubleValue()/count.doubleValue() * 100) ;
            return ratio.intValue();

        } catch (ApplicationException e) {
            Log.e("Term Progress Error", "Unable to get term or courses.");
            return 0;
        }
    }

    private int getDegreeProgressPercentage() {
        final TermRepository termRepository = new TermRepository(getDatabase());

        try {
            List<Term> terms = termRepository.findAll();
            Integer countComplete = 0;
            Integer count = 0;
            for (Term term : terms) {
                count++;

                if (term.getEndDate().before(Calendar.getInstance().getTime())) {
                    countComplete++;
                }
            }

            Double ratio = Math.floor(countComplete.doubleValue()/count.doubleValue() * 100) ;
            return ratio.intValue();
        } catch(Exception e ) {
            Log.e("Degree Progress Error", "Unable to get terms.");
            return 0;
        }
    }

}
