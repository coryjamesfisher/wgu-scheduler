package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.forthecoder.collegeschedule.entity.Term;
import com.forthecoder.collegeschedule.entity.TermRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TermModificationActivity extends BaseActivity {

    private Term term;
    public TermModificationActivity() {
        super();
        contentLayout = R.layout.activity_term_modification;

        Log.e("ERROR", "TERM MODIFICATION ACTIVITY STARTED");
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
        } else {
            TermRepository tr = new TermRepository(getDatabase());
            try {
                term = tr.findOneByRowid(getIntent().getLongExtra("rowid", 0L));
            } catch (ApplicationException e) {
            }
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        ((TextView)findViewById(R.id.termTitleValue)).setText(term.getTitle());
        ((TextView)findViewById(R.id.termStartValue)).setText(dateFormat.format(term.getStartDate()));
        ((TextView)findViewById(R.id.termEndValue)).setText(dateFormat.format(term.getEndDate()));
    }

    public void save(View view) throws IllegalAccessException, ApplicationException, InvocationTargetException, ParseException {

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        term.setTitle(((TextView)findViewById(R.id.termTitleValue)).getText().toString());
        term.setStartDate(dateFormat.parse(((TextView)findViewById(R.id.termStartValue)).getText().toString()));
        term.setEndDate(dateFormat.parse(((TextView)findViewById(R.id.termEndValue)).getText().toString()));

        boolean isInsert = term.getRowid() == 0L;
        TermRepository termRepository = new TermRepository(getDatabase());
        termRepository.save(term);

        if (isInsert) {
            navigateToTarget(TermsActivity.class);
        } else {
            navigateToTarget(TermDetailsActivity.class, term.getRowid());
        }
    }
}
