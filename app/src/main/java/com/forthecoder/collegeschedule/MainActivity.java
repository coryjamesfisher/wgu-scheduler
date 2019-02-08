package com.forthecoder.collegeschedule;

import android.os.Bundle;

/**
 * Requirement C: Student Scheduler and Progress Tracking Application
 * This activity shows progress meters for the term and the degree program overall.
 * Scheduling is done via the creation of terms, courses, and assessments which
 * each have dates associated.
 */
public class MainActivity extends BaseActivity {

    public MainActivity() {
        super();
        contentLayout = R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.deleteDatabase(DB.DB_NAME);
        super.onCreate(savedInstanceState);
    }

}
