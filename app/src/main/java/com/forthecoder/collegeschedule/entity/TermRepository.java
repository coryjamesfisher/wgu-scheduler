package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

public class TermRepository extends BaseRepository<Term> {

    public TermRepository(SQLiteDatabase dbConnection) {
        super(Term.class, dbConnection);
    }
}
