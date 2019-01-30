package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.List;

public class MentorRepository extends BaseRepository<Mentor> {
    public MentorRepository(SQLiteDatabase dbConnection) {
        super(Mentor.class, dbConnection);
    }

    public List<Mentor> findAllByIdList(List<Long> ids) throws ApplicationException {

        String placeholders = "(";
        placeholders += String.format("%0" + ids.size() + "d", 0).replace("0", "?,");
        placeholders = placeholders.substring(0, placeholders.length() - 1) + ")";

        String[] idsArray = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            idsArray[i] = ids.get(i).toString();
        }

        return queryList("SELECT rowid, * FROM Mentor WHERE rowid IN " + placeholders, idsArray);
    }
}
