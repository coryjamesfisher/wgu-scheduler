package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.List;

public class AlertRepository extends BaseRepository<Alert> {
    public AlertRepository(SQLiteDatabase dbConnection) {
        super(Alert.class, dbConnection);
    }

    public Alert findOneByTermAndType(Long termId, Alert.ALERT_TYPE type) throws ApplicationException {
        List<Alert> alerts = queryList("SELECT rowid, * FROM Alert WHERE termId=? AND type=?", termId.toString(), type.toString());

        if (alerts.size() == 0) {
            return null;
        }

        return alerts.get(0);
    }

    public Alert findOneByCourseAndType(Long courseId, Alert.ALERT_TYPE type) throws ApplicationException {
        List<Alert> alerts = queryList("SELECT rowid, * FROM Alert WHERE courseId=? AND type=?", courseId.toString(), type.toString());

        if (alerts.size() == 0) {
            return null;
        }

        return alerts.get(0);
    }

    public Alert findOneByAssessmentAndType(Long assessmentId, Alert.ALERT_TYPE type) throws ApplicationException {
        List<Alert> alerts = queryList("SELECT rowid, * FROM Alert WHERE assessmentId=? AND type=?", assessmentId.toString(), type.toString());

        if (alerts.size() == 0) {
            return null;
        }

        return alerts.get(0);
    }
}
