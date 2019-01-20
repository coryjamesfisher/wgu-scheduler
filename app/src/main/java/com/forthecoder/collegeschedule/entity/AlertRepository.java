package com.forthecoder.collegeschedule.entity;

import android.database.sqlite.SQLiteDatabase;

public class AlertRepository extends BaseRepository<Alert> {
    public AlertRepository(SQLiteDatabase dbConnection) {
        super(Alert.class, dbConnection);
    }
}
