package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Assessment;
import com.forthecoder.collegeschedule.entity.AssessmentRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.text.DateFormat;

public class AssessmentDetailsActivity extends BaseActivity {

    private Assessment assessment;
    private Long courseId;

    public AssessmentDetailsActivity() {
        super();
        contentLayout = R.layout.activity_assessment_details;
        actionLayout = R.layout.activity_assessment_details_actions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AssessmentRepository ar = new AssessmentRepository(getDatabase());
        try {
            assessment = ar.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
        } catch (ApplicationException e) {
        }
        courseId = getIntent().getLongExtra("parentid", 0L);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ((TextView)findViewById(R.id.assessmentTitleValue)).setText(assessment.getTitle());
        ((TextView)findViewById(R.id.assessmentTypeValue)).setText(assessment.getType());
        ((TextView)findViewById(R.id.assessmentStatusValue)).setText(assessment.getStatus());
        ((TextView)findViewById(R.id.assessmentGoalDateValue)).setText(dateFormat.format(assessment.getGoalDate()));

        /*
         * @todo add course notes list items
         */

        FloatingActionButton fab = findViewById(R.id.edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(view, assessment.getRowid());
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.remove_button);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ar.delete(assessment);
                    navigateToTarget(AssessmentsActivity.class, null, courseId);
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
}
