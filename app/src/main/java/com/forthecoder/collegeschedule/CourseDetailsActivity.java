package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Course;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.text.DateFormat;

public class CourseDetailsActivity extends BaseActivity {

    private Course course;
    private Long termId;

    public CourseDetailsActivity() {
        super();
        contentLayout = R.layout.activity_course_details;
        actionLayout = R.layout.activity_course_details_actions;
        Log.e("ERROR", "COURSE DETAILS ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final CourseRepository cr = new CourseRepository(getDatabase());
        try {
            course = cr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
        } catch (ApplicationException e) {
        }
        termId = getIntent().getLongExtra("parentid", 0L);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ((TextView)findViewById(R.id.courseTitleValue)).setText(course.getTitle());
        ((TextView)findViewById(R.id.courseStartValue)).setText(dateFormat.format(course.getStartDate()));
        ((TextView)findViewById(R.id.courseEndValue)).setText(dateFormat.format(course.getAnticipatedEndDate()));
        ((TextView)findViewById(R.id.courseStatusValue)).setText(course.getStatus());

        /*
         * @todo add course notes list items
         */

        FloatingActionButton fab = findViewById(R.id.edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(view, course.getRowid());
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.remove_button);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cr.delete(course);
                    navigateToTarget(CoursesActivity.class, null, termId);
                } catch (ApplicationException ignored) {
                }
            }
        });

        FloatingActionButton fab3 = findViewById(R.id.list_alerts_button);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void navigateToMentorsList(View view) {
        this.navigateToTarget(view, null, course.getRowid());
    }

    public void navigateToAssessmentsList(View view) {
        this.navigateToTarget(view, null, course.getRowid());
    }
}
