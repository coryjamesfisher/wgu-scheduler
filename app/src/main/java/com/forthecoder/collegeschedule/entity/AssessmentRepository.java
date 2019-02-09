package com.forthecoder.collegeschedule.entity;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.List;

public class AssessmentRepository extends BaseRepository<Assessment> {
    public AssessmentRepository(SQLiteDatabase dbConnection) {
        super(Assessment.class, dbConnection);
    }

    public List<Assessment> findAllByCourseId(Long parentid) throws ApplicationException {
        return queryList("SELECT rowid,* FROM Assessment WHERE courseId=?", parentid.toString());
    }

    public void deleteAllForCourse(Long rowid) throws ApplicationException {

        String deleteSQL = "DELETE FROM Assessment WHERE courseId = ?;";
        try {
            dbConnection.execSQL(deleteSQL, new Object[]{rowid});
        } catch (SQLException e) {
            Log.e("ERROR", deleteSQL);
            throw new ApplicationException("A system error has occurred.", e);
        }
    }
}
