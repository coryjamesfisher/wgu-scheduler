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

/**
 * Requirement A4A: Course Addition
 * Since this is a standalone activity, it can be called an infinite number
 * of times from the list action thus, unlimited courses are allowed for
 * each term.
 *
 * Requirement A5: Course Details
 * This activity drives the ability to enter the information for a course.
 * This includes title, start date, anticipated end date, status, and notes.
 * It supports both creation and modification of existing courses.
 */
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

    /**
     * This method will save the course and navigate to one of two places.
     * For inserts, navigates to the course list for the term assigned to this course.
     * For updates, navigates to the course details.
     */
    public void save(View view) throws IllegalAccessException, ApplicationException, InvocationTargetException {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        TextView titleInput = findViewById(R.id.courseTitleValue);
        TextView startDateInput = findViewById(R.id.courseStartValue);
        TextView endDateInput = findViewById(R.id.courseEndValue);

        course.setTitle(titleInput.getText().toString());
        course.setStatus(((Spinner)findViewById(R.id.courseStatusValue)).getSelectedItem().toString());

        boolean valid = true;
        try {
            course.setStartDate(dateFormat.parse(startDateInput.getText().toString()));
        } catch (ParseException e) {
            valid = false;
            startDateInput.setError("Invalid date format.");
        }

        try {
            course.setAnticipatedEndDate(dateFormat.parse(endDateInput.getText().toString()));
        } catch (ParseException e) {
            valid = false;
            endDateInput.setError("Invalid date format.");
        }

        if (course.getTitle().isEmpty()) {
            valid = false;
            titleInput.setError("Required field!");
        }

        /*
         * Requirement A6B: Optional Notes
         * The application allows a user to add optional notes for each course.
         * @todo finish implementing.
         */
        course.setNotes("");

        if (!valid) {
            return;
        }

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

    /**
     * Requirement A6F: Alerts for Courses
     * This method will handle creation and deletion of the alerts for a course via the alert service.
     */
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
