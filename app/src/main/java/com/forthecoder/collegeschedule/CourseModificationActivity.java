package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Alert;
import com.forthecoder.collegeschedule.entity.AlertRepository;
import com.forthecoder.collegeschedule.entity.Course;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;

public class CourseModificationActivity extends BaseActivity {

    private Course course;
    private Alert start;
    private Alert end;

    private static final String[] STATUSES = new String[] {
            "IN PROGRESS", "COMPLETED", "DROPPED", "PLAN TO TAKE"
    };

    public CourseModificationActivity() {
        super();
        contentLayout = R.layout.activity_course_modification;
        Log.e("ERROR", "COURSE MODIFICATION ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long rowId = getIntent().getLongExtra("rowid", 0L);
        Long parentId = getIntent().getLongExtra("parentid", 0L);

        // If no rowId passed this is a new term.
        if (rowId == 0) {
            course = new Course();
            course.setTermId(parentId);
            course.setStartDate(Calendar.getInstance().getTime());
            course.setAnticipatedEndDate(Calendar.getInstance().getTime());
            start = new Alert();
            end = new Alert();
        } else {
            CourseRepository cr = new CourseRepository(getDatabase());
            try {
                course = cr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));

                AlertRepository alertRepository = new AlertRepository(getDatabase());
                start = alertRepository.findOneByCourseAndType(course.getRowid(), Alert.ALERT_TYPE.START);
                end = alertRepository.findOneByCourseAndType(course.getRowid(), Alert.ALERT_TYPE.END);

                if (start == null) {
                    start = new Alert();
                }

                if (end == null) {
                    end = new Alert();
                }
            } catch (ApplicationException e) {
            }
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ((TextView)findViewById(R.id.courseTitleValue)).setText(course.getTitle());
        ((TextView)findViewById(R.id.courseStartValue)).setText(dateFormat.format(course.getStartDate()));
        ((TextView)findViewById(R.id.courseEndValue)).setText(dateFormat.format(course.getAnticipatedEndDate()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STATUSES);

        Spinner courseStatusSelect = findViewById(R.id.courseStatusValue);
        courseStatusSelect.setAdapter(adapter);

        if (course.getStatus() != null) {
            int statusIndex = Arrays.asList(STATUSES).indexOf(course.getStatus());
            courseStatusSelect.setSelection(statusIndex);
        }

        ((Switch)findViewById(R.id.startAlertEnabledValue)).setChecked(start.getRowid() != 0L);
        ((Switch)findViewById(R.id.endAlertEnabledValue)).setChecked(end.getRowid() != 0L);
    }

    public void save(View view) throws IllegalAccessException, ApplicationException, InvocationTargetException, ParseException {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        course.setTitle(((TextView)findViewById(R.id.courseTitleValue)).getText().toString());
        course.setStartDate(dateFormat.parse(((TextView)findViewById(R.id.courseStartValue)).getText().toString()));
        course.setAnticipatedEndDate(dateFormat.parse(((TextView)findViewById(R.id.courseEndValue)).getText().toString()));
        course.setStatus(((Spinner)findViewById(R.id.courseStatusValue)).getSelectedItem().toString());
        course.setNotes("");

        boolean isInsert = course.getRowid() == 0L;

        CourseRepository courseRepository = new CourseRepository(getDatabase());
        courseRepository.save(course);

        handleAlerts();

        if (isInsert) {
            navigateToTarget(CoursesActivity.class, null, course.getTermId());
        } else {
            navigateToTarget(CourseDetailsActivity.class, course.getRowid());
        }
    }

    private void handleAlerts() throws InvocationTargetException, IllegalAccessException, ApplicationException {

        AlertService alertService = new AlertService(this);

        boolean startAlertEnabled = ((Switch)findViewById(R.id.startAlertEnabledValue)).isChecked();
        boolean endAlertEnabled = ((Switch)findViewById(R.id.endAlertEnabledValue)).isChecked();
        if (startAlertEnabled && start.getRowid() == 0L) {
            start.setTermId(course.getTermId());
            start.setCourseId(course.getRowid());
            start.setType(Alert.ALERT_TYPE.START);
            start.setDate(course.getStartDate());
            start.setText(course.getTitle() + " starts today!");
            alertService.save(start);
        } else if (!startAlertEnabled && start.getRowid() != 0L) {
            alertService.delete(start);
        }

        if (endAlertEnabled && end.getRowid() == 0L) {
            end.setTermId(course.getTermId());
            end.setCourseId(course.getRowid());
            end.setType(Alert.ALERT_TYPE.END);
            end.setDate(course.getAnticipatedEndDate());
            end.setText(course.getTitle() + " ends today!");
            alertService.save(end);
        } else if (!endAlertEnabled && end.getRowid() != 0L) {
            alertService.delete(end);
        }
    }
}
