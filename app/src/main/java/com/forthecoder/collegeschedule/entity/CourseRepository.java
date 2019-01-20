package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

public class CourseRepository extends BaseRepository<Course> {
    public CourseRepository(SQLiteDatabase dbConnection) {
        super(Course.class, dbConnection);
    }
}
