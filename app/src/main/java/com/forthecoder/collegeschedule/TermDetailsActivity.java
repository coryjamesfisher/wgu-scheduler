package com.forthecoder.collegeschedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Course;
import com.forthecoder.collegeschedule.entity.CourseRepository;
import com.forthecoder.collegeschedule.entity.Term;
import com.forthecoder.collegeschedule.entity.TermRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.text.DateFormat;
import java.util.List;

/**
 * Requirement A4C: Term Details
 * This activity shows the details of a particular term including
 * all of the term information title, start date, and end date.
 */
public class TermDetailsActivity extends BaseActivity {

    private Term term;
    public TermDetailsActivity() {
        super();
        contentLayout = R.layout.activity_term_details;
        actionLayout = R.layout.activity_term_details_actions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final TermRepository tr = new TermRepository(getDatabase());
        try {
            term = tr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
        } catch (ApplicationException e) {
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ((TextView)findViewById(R.id.termTitleValue)).setText(term.getTitle());
        ((TextView)findViewById(R.id.termStartValue)).setText(dateFormat.format(term.getStartDate()));
        ((TextView)findViewById(R.id.termEndValue)).setText(dateFormat.format(term.getEndDate()));

        FloatingActionButton fab = findViewById(R.id.edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(view, term.getRowid());
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.remove_button);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    /*
                     * Requirement A3: Validation Implementation
                     * The following validation prevents a term from being deleted if it
                     * has courses assigned to it.
                     */
                    CourseRepository cr = new CourseRepository(getDatabase());
                    List<Course> courses = cr.findAllByTermId(term.getRowid());

                    if (courses.size() != 0) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TermDetailsActivity.this)
                                .setPositiveButton("OK", null)
                                .setTitle("Error")
                                .setMessage("Unable to delete term. Courses are associated.");

                        alertDialog.show();
                        return;
                    }
                    tr.delete(term);
                    navigateToTarget(TermsActivity.class);
                } catch (ApplicationException ignored) {
                }
            }
        });

        Button upLevelButton = findViewById(R.id.upLevelButton);
        upLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTarget(TermsActivity.class);
            }
        });
    }

    public void navigateToCourses(View view) {
        navigateToTarget(view, null, term.getRowid());
    }
}
