package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.List;

public class CourseRepository extends BaseRepository<Course> {
    public CourseRepository(SQLiteDatabase dbConnection) {
        super(Course.class, dbConnection);
    }

    public List<Course> findAllByTermId(Long parentid) throws ApplicationException {
        return queryList("SELECT rowid,* FROM Course WHERE termId=?", parentid.toString());
    }
}
