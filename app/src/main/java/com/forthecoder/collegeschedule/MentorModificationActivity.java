package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.CourseMentor;
import com.forthecoder.collegeschedule.entity.CourseMentorRepository;
import com.forthecoder.collegeschedule.entity.Mentor;
import com.forthecoder.collegeschedule.entity.MentorRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MentorModificationActivity extends BaseActivity {

    private Mentor mentor;
    private CourseMentor courseMentor;

    public MentorModificationActivity() {
        super();
        contentLayout = R.layout.activity_mentor_modification;
        Log.e("ERROR", "MENTOR MODIFICATION ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long rowId = getIntent().getLongExtra("rowid", 0L);
        Long parentId = getIntent().getLongExtra("parentid", 0L);

        courseMentor = new CourseMentor();

        // If no rowId passed this is a new term.
        if (rowId == 0) {
            mentor = new Mentor();
        } else {
            MentorRepository mr = new MentorRepository(getDatabase());
            try {
                mentor = mr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
            } catch (ApplicationException e) {
            }
        }

        if (parentId != 0L) {
            courseMentor.setCourseId(parentId);
        }

        ((TextView)findViewById(R.id.mentorFirstNameValue)).setText(mentor.getFirstName());
        ((TextView)findViewById(R.id.mentorLastNameValue)).setText(mentor.getLastName());
        ((TextView)findViewById(R.id.mentorPhoneValue)).setText(mentor.getPhoneNumber());
        ((TextView)findViewById(R.id.mentorEmailValue)).setText(mentor.getEmail());
    }

    public void save(View view) throws IllegalAccessException, ApplicationException, InvocationTargetException {

        mentor.setFirstName(((TextView)findViewById(R.id.mentorFirstNameValue)).getText().toString());
        mentor.setLastName(((TextView)findViewById(R.id.mentorLastNameValue)).getText().toString());
        mentor.setEmail(((TextView)findViewById(R.id.mentorEmailValue)).getText().toString());
        mentor.setPhoneNumber(((TextView)findViewById(R.id.mentorPhoneValue)).getText().toString());

        boolean isInsert = mentor.getRowid() == 0L;
        boolean isAddingForCourse = courseMentor.getCourseId() != 0L;

        MentorRepository mentorRepository = new MentorRepository(getDatabase());
        CourseMentorRepository courseMentorRepository = new CourseMentorRepository(getDatabase());
        mentorRepository.save(mentor);

        // Mentors can be added with or without simultaneous assignment to a course.
        if (isAddingForCourse) {

            // Check if the mentor is already assigned to the course.
            // Avoid double assignment.
            List<CourseMentor> courseMentors = courseMentorRepository.findAllByCourse(courseMentor.getCourseId());
            boolean found = false;
            for (CourseMentor cm : courseMentors) {
                if (cm.getMentorId().equals(mentor.getRowid())) {
                    found = true;
                }
            }

            if (!found) {
                courseMentor.setMentorId(mentor.getRowid());
                courseMentorRepository.save(courseMentor);
            }
        }

        if (isInsert && isAddingForCourse) {
            navigateToTarget(MentorsActivity.class, null, courseMentor.getCourseId());
        } else if (isInsert) {
            navigateToTarget(MentorsActivity.class);
        }
        else {
            navigateToTarget(MentorDetailsActivity.class, mentor.getRowid());
        }
    }
}
