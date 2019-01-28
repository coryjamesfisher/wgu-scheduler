package com.forthecoder.collegeschedule;

import android.os.Bundle;
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

    public AssessmentsActivity() {
        super();
        contentLayout = R.layout.activity_assessments;
        Log.e("ERROR", "ASSESSMENTS ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AssessmentRepository ar = new AssessmentRepository(getDatabase());
        try {
            List<Assessment> courses = ar.findAll();
            ListView coursesListView = findViewById(R.id.assessmentsList);

            Map<String, Integer> fieldMap = new HashMap<>();
            fieldMap.put("rowid", R.id.assessmentId);
            fieldMap.put("title", R.id.assessmentTitle);
            coursesListView.setAdapter(
                    new BaseEntityArrayAdapter<>(
                            Assessment.class,
                            this,
                            courses,
                            fieldMap,
                            R.layout.assessments_list_item));


            coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    navigateToTarget(view);
                }
            });

        } catch (ApplicationException e) {
            Log.e("ERROR", e.toString());
        }
    }
}
