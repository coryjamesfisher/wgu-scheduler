package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

public class AssessmentRepository extends BaseRepository<Assessment> {
    public AssessmentRepository(SQLiteDatabase dbConnection) {
        super(Assessment.class, dbConnection);
    }
}
