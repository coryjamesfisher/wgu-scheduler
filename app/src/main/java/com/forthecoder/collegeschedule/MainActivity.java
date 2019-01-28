package com.forthecoder.collegeschedule;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    public MainActivity() {
        super();
        contentLayout = R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.deleteDatabase(DB.DB_NAME);
        super.onCreate(savedInstanceState);
    }

}
