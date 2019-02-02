package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.forthecoder.collegeschedule.entity.Assessment;
import com.forthecoder.collegeschedule.entity.AssessmentRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssessmentsActivity extends BaseActivity {

    private Long courseId;

    public AssessmentsActivity() {
        super();
        contentLayout = R.layout.activity_assessments;
        actionLayout = R.layout.activity_assessments_actions;
        Log.e("ERROR", "ASSESSMENTS ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AssessmentRepository ar = new AssessmentRepository(getDatabase());
        try {
            courseId = getIntent().getLongExtra("parentid", 0L);
            List<Assessment> assessments = ar.findAllByCourseId(courseId);
            final ListView assessmentListView = findViewById(R.id.assessmentsList);

            Map<String, Integer> fieldMap = new HashMap<>();
            fieldMap.put("rowid", R.id.assessmentId);
            fieldMap.put("title", R.id.assessmentTitle);
            assessmentListView.setAdapter(
                    new BaseEntityArrayAdapter<>(
                            Assessment.class,
                            this,
                            assessments,
                            fieldMap,
                            R.layout.assessments_list_item));

            assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    navigateToTarget(view, ((Assessment)assessmentListView.getItemAtPosition(position)).getRowid());
                }
            });

        } catch (ApplicationException e) {
            Log.e("ERROR", e.toString());
        }

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(view, null, courseId);
            }
        });
    }
}
