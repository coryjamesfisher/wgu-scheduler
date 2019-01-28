package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.forthecoder.collegeschedule.entity.Course;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursesActivity extends BaseActivity {

    public CoursesActivity() {
        super();
        contentLayout = R.layout.activity_courses;
        Log.e("ERROR", "COURSES ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CourseRepository cr = new CourseRepository(getDatabase());
        try {
            List<Course> courses = cr.findAll();
            ListView coursesListView = findViewById(R.id.coursesList);

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
                    navigateToTarget(view);
                }
            });

        } catch (ApplicationException e) {
            Log.e("ERROR", e.toString());
        }
    }
}
