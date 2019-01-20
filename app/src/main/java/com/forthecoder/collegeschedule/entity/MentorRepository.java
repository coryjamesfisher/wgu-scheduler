package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

public class MentorRepository extends BaseRepository<Mentor> {
    public MentorRepository(SQLiteDatabase dbConnection) {
        super(Mentor.class, dbConnection);
    }
}
