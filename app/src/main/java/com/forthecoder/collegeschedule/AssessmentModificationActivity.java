package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Alert;
import com.forthecoder.collegeschedule.entity.AlertRepository;
import com.forthecoder.collegeschedule.entity.Assessment;
import com.forthecoder.collegeschedule.entity.AssessmentRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Requirement A6A: Assessment Addition
 * Since this is a standalone activity, it can be called an infinite number
 * of times from the list action thus 1-n assessments can be added for a course.
 *
 * Requirement A7A: Assessments For Each Course
 * This activity allows the entering of all fields for the assessment
 * including title, type, goal date, and status.
 *
 * Requirement A7B: Assessment Information
 * This activity allows the user to create or modify any assessment.
 * The delete is handled in the assessment details activity as an action handler.
 */
public class AssessmentModificationActivity extends BaseActivity {

    private static final String[] STATUSES = {"NOT TAKEN", "PASSED", "FAILED"};
    private static final String[] TYPES = {"OBJECTIVE ASSESSMENT", "PERFORMANCE ASSESSMENT"};
    private Assessment assessment;
    private Alert end;

    public AssessmentModificationActivity() {
        super();
        contentLayout = R.layout.activity_assessment_modification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long rowId = getIntent().getLongExtra("rowid", 0L);
        Long parentId = getIntent().getLongExtra("parentid", 0L);

        // If no rowId passed this is a new term.
        if (rowId == 0) {
            assessment = new Assessment();
            assessment.setCourseId(parentId);
            assessment.setGoalDate(Calendar.getInstance().getTime());
            end = new Alert();
        } else {
            AssessmentRepository ar = new AssessmentRepository(getDatabase());
            try {
                assessment = ar.findOneByRowid(getIntent().getLongExtra("rowid", 0L));

                AlertRepository alertRepository = new AlertRepository(getDatabase());
                end = alertRepository.findOneByAssessmentAndType(assessment.getRowid(), Alert.ALERT_TYPE.END);
                if (end == null) {
                    end = new Alert();
                }
            } catch (ApplicationException e) {
            }
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ((TextView)findViewById(R.id.assessmentTitleValue)).setText(assessment.getTitle());
        ((TextView)findViewById(R.id.assessmentGoalDateValue)).setText(dateFormat.format(assessment.getGoalDate()));

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STATUSES);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TYPES);

        Spinner assessmentStatusSelect = findViewById(R.id.assessmentStatusValue);
        assessmentStatusSelect.setAdapter(statusAdapter);

        Spinner assessmentTypeSelect = findViewById(R.id.assessmentTypeValue);
        assessmentTypeSelect.setAdapter(typeAdapter);

        if (assessment.getStatus() != null) {
            int statusIndex = Arrays.asList(STATUSES).indexOf(assessment.getStatus());
            assessmentStatusSelect.setSelection(statusIndex);
        }

        if (assessment.getType() != null) {
            int typeIndex = Arrays.asList(TYPES).indexOf(assessment.getType());
            assessmentTypeSelect.setSelection(typeIndex);
        }

        ((Switch)findViewById(R.id.endAlertEnabledValue)).setChecked(end.getRowid() != 0L);
    }

    /**
     * This method will save the assessment and navigate to one of two places.
     * For inserts, navigates to the assessment list for the course assigned to this assessment.
     * For updates, navigates to the assessment details.
     */
    public void save(View view) throws IllegalAccessException, ApplicationException, InvocationTargetException, ParseException {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        assessment.setTitle(((TextView)findViewById(R.id.assessmentTitleValue)).getText().toString());
        assessment.setGoalDate(dateFormat.parse(((TextView)findViewById(R.id.assessmentGoalDateValue)).getText().toString()));
        assessment.setType(((Spinner)findViewById(R.id.assessmentTypeValue)).getSelectedItem().toString());
        assessment.setStatus(((Spinner)findViewById(R.id.assessmentStatusValue)).getSelectedItem().toString());

        boolean isInsert = assessment.getRowid() == 0L;

        handleAlerts();

        if (isInsert) {
            navigateToTarget(AssessmentsActivity.class, null, assessment.getCourseId());
        } else {
            navigateToTarget(AssessmentDetailsActivity.class, assessment.getRowid());
        }
    }

    /**
     * Requirement A7C: Alerts for Goal Dates
     * This method will handle creation and deletion of the alerts for assessment goal dates via the alert service.
     */
    private void handleAlerts() throws IllegalAccessException, ApplicationException, InvocationTargetException {
        AlertService alertService = new AlertService(this);
        boolean endAlertEnabled = ((Switch)findViewById(R.id.endAlertEnabledValue)).isChecked();
        if (endAlertEnabled && end.getRowid() == 0L) {
            end.setCourseId(assessment.getCourseId());
            end.setAssessmentId(assessment.getRowid());
            end.setType(Alert.ALERT_TYPE.END);
            end.setDate(assessment.getGoalDate());
            end.setText(assessment.getTitle() + " starts today!");
            alertService.save(end);
        } else if (!endAlertEnabled && end.getRowid() != 0L) {
            alertService.delete(end);
        }
    }
}
