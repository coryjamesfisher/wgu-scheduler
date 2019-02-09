package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.forthecoder.collegeschedule.entity.Course;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Requirement A4B: List of Courses
 * This activity will list the courses for a term.
 */
public class CoursesActivity extends BaseActivity {

    private Long termId;

    public CoursesActivity() {
        super();
        contentLayout = R.layout.activity_courses;
        actionLayout = R.layout.activity_courses_actions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CourseRepository cr = new CourseRepository(getDatabase());
        try {
            termId = getIntent().getLongExtra("parentid", 0L);
            final List<Course> courses = cr.findAllByTermId(termId);
            final ListView coursesListView = findViewById(R.id.coursesList);

            Map<String, Integer> fieldMap = new HashMap<>();
            fieldMap.put("rowid", R.id.courseId);
            fieldMap.put("title", R.id.courseTitle);

            coursesListView.setAdapter(
                    new BaseEntityArrayAdapter<>(
                            Course.class,
                            this,
                            courses,
                            fieldMap,
                            R.layout.courses_list_item));

            coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigateToTarget(view, ((Course)coursesListView.getItemAtPosition(position)).getRowid(), termId);
                }
            });

        } catch (ApplicationException e) {
            Log.e("ERROR", e.toString());
        }

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(view, null, termId);
            }
        });

        Button upLevelButton = findViewById(R.id.upLevelButton);
        upLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(TermDetailsActivity.class, termId);
            }
        });
    }
}
