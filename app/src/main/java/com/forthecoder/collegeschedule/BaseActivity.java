package com.forthecoder.collegeschedule;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

abstract class BaseActivity extends AppCompatActivity {

    @LayoutRes
    protected int layout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigateToTarget(View view) {

        Log.e("ERROR", view.getTag().toString());

        try {
            Log.e("HELLLLL", Class.forName((String) view.getTag()).toString());
            Intent intent = new Intent(this, Class.forName((String) view.getTag()));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            Log.e(this.getLocalClassName(), "Couldn't find class " + view.getTag());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        this.getApplicationContext().deleteDatabase("WGU_Scheduler");
        Log.e("ERROR", "APPLICATION STARTED");
        DB db = new DB(this.getApplicationContext());
        SQLiteDatabase database = db.getWritableDatabase();

//        @todo This can be used as a reference for how to query by id
//        TermRepository tr = new TermRepository(database);
//        try {
//            Term term = tr.findOneByRowid(1);
//            Log.e("ERROR", "TERM IS " + term.getTitle());
//        } catch (ApplicationException e) {
//            Log.e("ERROR", e.toString());
//        }
        String[] vals = new String[0];

        setContentView(R.layout.activity_base);
        ViewStub viewStub = findViewById(R.id.content_view);
        viewStub.setLayoutResource(layout);
        viewStub.inflate();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
