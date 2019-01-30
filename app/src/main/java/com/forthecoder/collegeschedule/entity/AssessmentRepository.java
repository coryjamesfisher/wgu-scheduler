package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.List;

public class AssessmentRepository extends BaseRepository<Assessment> {
    public AssessmentRepository(SQLiteDatabase dbConnection) {
        super(Assessment.class, dbConnection);
    }

    public List<Assessment> findAllByCourseId(Long parentid) throws ApplicationException {
        return queryList("SELECT rowid,* FROM Assessment WHERE courseId=?", parentid.toString());
    }
}
