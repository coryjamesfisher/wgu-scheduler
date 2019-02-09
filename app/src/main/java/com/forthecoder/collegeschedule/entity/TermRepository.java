package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.Date;
import java.util.List;

public class TermRepository extends BaseRepository<Term> {

    public TermRepository(SQLiteDatabase dbConnection) {
        super(Term.class, dbConnection);
    }

    public Term findFirstIncludingDate(Date date) throws ApplicationException {
        Long timestamp = date.getTime();
        List<Term> results = queryList("SELECT rowid,* FROM Term WHERE startDate <= ? AND endDate >= ?", timestamp.toString(), timestamp.toString());

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }
}
