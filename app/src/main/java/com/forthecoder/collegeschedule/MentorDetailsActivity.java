package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Assessment;
import com.forthecoder.collegeschedule.entity.AssessmentRepository;
import com.forthecoder.collegeschedule.entity.Mentor;
import com.forthecoder.collegeschedule.entity.MentorRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.text.DateFormat;

public class MentorDetailsActivity extends BaseActivity {

    private Mentor mentor;

    public MentorDetailsActivity() {
        super();
        contentLayout = R.layout.activity_mentor_details;
        actionLayout = R.layout.activity_mentor_details_actions;
        Log.e("ERROR", "MENTOR DETAILS ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MentorRepository mr = new MentorRepository(getDatabase());
        try {
            mentor = mr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
        } catch (ApplicationException e) {
        }

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.remove_button);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab3 = findViewById(R.id.list_alerts_button);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
