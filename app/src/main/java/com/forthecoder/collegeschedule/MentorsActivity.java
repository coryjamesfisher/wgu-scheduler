package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.forthecoder.collegeschedule.entity.CourseMentor;
import com.forthecoder.collegeschedule.entity.CourseMentorRepository;
import com.forthecoder.collegeschedule.entity.Mentor;
import com.forthecoder.collegeschedule.entity.MentorRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MentorsActivity extends BaseActivity {

    private Long courseId;

    public MentorsActivity() {
        super();
        contentLayout = R.layout.activity_mentors;
        actionLayout = R.layout.activity_mentors_actions;
        Log.e("ERROR", "ASSESSMENTS ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CourseMentorRepository cmr = new CourseMentorRepository(getDatabase());
        MentorRepository mr = new MentorRepository(getDatabase());
        try {
            final List<Long> mentorIds = new ArrayList<>();

            courseId = getIntent().getLongExtra("parentid", 0L);

            List<CourseMentor> courseMentorRel = cmr.findAllByCourse(courseId);
            for (CourseMentor courseMentor : courseMentorRel) {
                mentorIds.add(courseMentor.getMentorId());
            }

            List<Mentor> mentors = mr.findAllByIdList(mentorIds);
            final ListView mentorsListView = findViewById(R.id.mentorsList);

            Map<String, Integer> fieldMap = new HashMap<>();
            fieldMap.put("rowid", R.id.mentorId);
            fieldMap.put("firstName", R.id.mentorFirstName);
            fieldMap.put("lastName", R.id.mentorLastName);
            mentorsListView.setAdapter(
                    new BaseEntityArrayAdapter<>(
                            Mentor.class,
                            this,
                            mentors,
                            fieldMap,
                            R.layout.mentors_list_item));

            mentorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    navigateToTarget(view, ((Mentor)mentorsListView.getItemAtPosition(position)).getRowid());
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
