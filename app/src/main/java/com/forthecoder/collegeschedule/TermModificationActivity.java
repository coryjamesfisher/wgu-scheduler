package com.forthecoder.collegeschedule;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Alert;
import com.forthecoder.collegeschedule.entity.AlertRepository;
import com.forthecoder.collegeschedule.entity.Term;
import com.forthecoder.collegeschedule.entity.TermRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Requirement A1: Functional Requirements for Terms
 * This activity drives the ability to enter the information for a term.
 * This includes title, start, and end date.
 * It supports both creation and modification of existing
 * terms.
 *
 * Requirement A2: Term Addition Feature
 * Since this is a standalone activity, it can be called an infinite number
 * of times from the list action thus, unlimited terms are allowed.
 */
public class TermModificationActivity extends BaseActivity {

    private Term term;
    private Alert start;
    private Alert end;

    public TermModificationActivity() {
        super();
        contentLayout = R.layout.activity_term_modification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long rowId = getIntent().getLongExtra("rowid", 0L);

        // If no rowId passed this is a new term.
        if (rowId == 0) {
            term = new Term();
            term.setStartDate(Calendar.getInstance().getTime());
            term.setEndDate(Calendar.getInstance().getTime());
            start = new Alert();
            end = new Alert();
        } else {

            TermRepository tr = new TermRepository(getDatabase());
            try {
                term = tr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
                AlertRepository alertRepository = new AlertRepository(getDatabase());
                start = alertRepository.findOneByTermAndType(term.getRowid(), Alert.ALERT_TYPE.START);
                end = alertRepository.findOneByTermAndType(term.getRowid(), Alert.ALERT_TYPE.END);

                if (start == null) {
                    start = new Alert();
                }

                if (end == null) {
                    end = new Alert();
                }
            } catch (ApplicationException e) {
            }
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ((TextView)findViewById(R.id.termTitleValue)).setText(term.getTitle());
        ((TextView)findViewById(R.id.termStartValue)).setText(dateFormat.format(term.getStartDate()));
        ((TextView)findViewById(R.id.termEndValue)).setText(dateFormat.format(term.getEndDate()));

        ((Switch)findViewById(R.id.startAlertEnabledValue)).setChecked(start.getRowid() != 0L);
        ((Switch)findViewById(R.id.endAlertEnabledValue)).setChecked(end.getRowid() != 0L);
    }

    /**
     * This method will save the term and navigate to one of two places.
     * For inserts, navigates to the terms list.
     * For updates, navigates to the term details.
     */
    public void save(View view) throws IllegalAccessException, ApplicationException, InvocationTargetException {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        TextView titleInput = findViewById(R.id.termTitleValue);
        TextView termStartInput = findViewById(R.id.termStartValue);
        TextView termEndInput = findViewById(R.id.termEndValue);

        term.setTitle(titleInput.getText().toString());


        boolean valid = true;
        try {
            term.setStartDate(dateFormat.parse(termStartInput.getText().toString()));
        } catch (ParseException e) {
            valid = false;
            termStartInput.setError("Invalid date format.");
        }

        try {
            term.setEndDate(dateFormat.parse(termEndInput.getText().toString()));
        } catch (ParseException e) {
            valid = false;
            termEndInput.setError("Invalid date format.");
        }

        if (term.getTitle().isEmpty()) {
            valid = false;
            titleInput.setError("Required field!");
        }

        // If the details aren't valid. Do not save/submit.
        if (!valid) {
            return;
        }

        boolean isInsert = term.getRowid() == 0L;
        TermRepository termRepository = new TermRepository(getDatabase());
        termRepository.save(term);

        handleAlerts();

        if (isInsert) {
            navigateToTarget(TermsActivity.class);
        } else {
            navigateToTarget(TermDetailsActivity.class, term.getRowid());
        }
    }

    /**
     * This method will handle creation and deletion of the alerts via the alert service.
     */
    private void handleAlerts() throws IllegalAccessException, ApplicationException, InvocationTargetException {
        boolean startAlertEnabled = ((Switch)findViewById(R.id.startAlertEnabledValue)).isChecked();
        boolean endAlertEnabled = ((Switch)findViewById(R.id.endAlertEnabledValue)).isChecked();

        AlertService alertService = new AlertService(this);
        if (startAlertEnabled && start.getRowid() == 0L) {
            start.setTermId(term.getRowid());
            start.setType(Alert.ALERT_TYPE.START);
            start.setDate(term.getStartDate());
            start.setText(term.getTitle() + " starts today!");
            alertService.save(start);

        } else if (!startAlertEnabled && start.getRowid() != 0L) {
            alertService.delete(start);
        }

        if (endAlertEnabled && end.getRowid() == 0L) {
            end.setTermId(term.getRowid());
            end.setType(Alert.ALERT_TYPE.END);
            end.setDate(term.getEndDate());
            end.setText(term.getTitle() + " ends today!");
            alertService.save(end);
        } else if (!endAlertEnabled && end.getRowid() != 0L) {
            alertService.delete(end);
        }
    }
}
