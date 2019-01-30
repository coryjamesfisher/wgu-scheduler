package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.List;

public class CourseMentorRepository extends BaseRepository<CourseMentor> {
    public CourseMentorRepository(SQLiteDatabase dbConnection) {
        super(CourseMentor.class, dbConnection);
    }

    public List<CourseMentor> findAllByCourse(Long parentid) throws ApplicationException {
        return queryList("SELECT rowid,* FROM CourseMentor WHERE courseId=?", parentid.toString());
    }
}
