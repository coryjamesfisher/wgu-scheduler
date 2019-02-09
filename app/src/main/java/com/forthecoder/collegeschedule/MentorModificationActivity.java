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


        TextView firstNameInput = findViewById(R.id.mentorFirstNameValue);
        TextView lastNameInput = findViewById(R.id.mentorLastNameValue);
        TextView emailInput = findViewById(R.id.mentorEmailValue);
        TextView phoneInput = findViewById(R.id.mentorPhoneValue);

        mentor.setFirstName(firstNameInput.getText().toString());
        mentor.setLastName(lastNameInput.getText().toString());
        mentor.setEmail(emailInput.getText().toString());
        mentor.setPhoneNumber(phoneInput.getText().toString());

        boolean valid = true;

        if (mentor.getFirstName().isEmpty()) {
            valid = false;
            firstNameInput.setError("Required field!");
        }

        if (mentor.getLastName().isEmpty()) {
            valid = false;
            lastNameInput.setError("Required field!");
        }

        if (mentor.getEmail().isEmpty()) {
            valid = false;
            emailInput.setError("Required field!");
        } else if (!mentor.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            valid = false;
            emailInput.setError("Invalid email format!");
        }

        if (mentor.getPhoneNumber().isEmpty()) {
            valid = false;
            phoneInput.setError("Required field!");
        } else if (!mentor.getPhoneNumber().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
            valid = false;
            phoneInput.setError("Invalid phone format!");
        }

        if (!valid) {
            return;
        }

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
            navigateToTarget(MentorDetailsActivity.class, mentor.getRowid(), courseMentor.getCourseId());
        }
    }
}
