package com.forthecoder.collegeschedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.forthecoder.collegeschedule.entity.TermRepository;

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "WGU_Scheduler";
    private static final int DB_VERSION = 6;

    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TermRepository termRepository = new TermRepository(db);

        try {
            termRepository.createSchema();
        } catch (Exception e) {
            // ignore
            Log.e("ERROR", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            TermRepository termRepository = new TermRepository(db);
            termRepository.dropSchema();
            termRepository.createSchema();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }
}
