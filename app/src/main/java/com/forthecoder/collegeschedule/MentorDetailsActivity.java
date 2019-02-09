package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Mentor;
import com.forthecoder.collegeschedule.entity.MentorRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

public class MentorDetailsActivity extends BaseActivity {

    private Mentor mentor;
    private Long courseId;

    public MentorDetailsActivity() {
        super();
        contentLayout = R.layout.activity_mentor_details;
        actionLayout = R.layout.activity_mentor_details_actions;
        Log.e("ERROR", "MENTOR DETAILS ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MentorRepository mr = new MentorRepository(getDatabase());
        try {
            mentor = mr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
        } catch (ApplicationException e) {
        }
        courseId = getIntent().getLongExtra("parentid", 0L);

        ((TextView)findViewById(R.id.mentorFirstNameValue)).setText(mentor.getFirstName());
        ((TextView)findViewById(R.id.mentorLastNameValue)).setText(mentor.getLastName());
        ((TextView)findViewById(R.id.mentorPhoneValue)).setText(mentor.getPhoneNumber());
        ((TextView)findViewById(R.id.mentorEmailValue)).setText(mentor.getEmail());

        /*
         * @todo add course notes list items
         */

        FloatingActionButton fab = findViewById(R.id.edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(view, mentor.getRowid(), courseId);
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.remove_button);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mr.delete(mentor);
                    navigateToTarget(MentorsActivity.class, null, courseId);
                } catch (ApplicationException ignored) {
                }
            }
        });

        Button upLevelButton = findViewById(R.id.upLevelButton);
        upLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(MentorsActivity.class, null, courseId);
            }
        });
    }
}
