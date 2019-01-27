package com.forthecoder.collegeschedule;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
    private DrawerLayout mainLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                mainLayout.openDrawer(GravityCompat.START);
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
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);

        final BaseActivity self = this;

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Log.e("NAVIGATION SELECTED", "HOLY CRAP");

                        menuItem.setChecked(true);

                        Class targetClass = null;
                        switch(menuItem.getItemId()) {
                            case R.id.nav_home:
                                targetClass = MainActivity.class;
                                break;
                            case R.id.nav_current_term:
                                targetClass = TermDetailsActivity.class;
                                break;
                            case R.id.nav_terms:
                                targetClass = TermsActivity.class;
                                break;
                            default:
                                return false;
                        }


                        startActivity(new Intent(self, targetClass));
                        mainLayout.closeDrawers();

                        return true;
                    }
                });

        mainLayout = findViewById(R.id.main_layout);

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
