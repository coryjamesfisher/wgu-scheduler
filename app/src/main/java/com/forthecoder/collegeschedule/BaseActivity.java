package com.forthecoder.collegeschedule;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

    private SQLiteDatabase database;

    /**
     * Wrapper contentLayout that all activity layouts will nest inside.
     */
    private DrawerLayout mainLayout;

    /**
     * Activity specific layout holding that activity's content.
     */
    @LayoutRes
    protected int contentLayout;

    /**
     * Activity specific layout holding that activities floating actions.
     */
    @LayoutRes
    protected int actionLayout = 0;

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
        navigateToTarget(view, null, null);
    }

    public void navigateToTarget(View view, Long rowid) {
        navigateToTarget(view, rowid, null);
    }

    /**
     * This method is designed for use with UI elements that
     * when clicked start a new activity. The android:tag property
     * must contain a valid fully qualified class name.
     *
     * @param view - the clicked UI element
     */
    public void navigateToTarget(View view, Long rowid, Long parentid) {

        try {
            Intent intent = new Intent(this, Class.forName((String) view.getTag()));

            if (rowid != null) {
                intent.putExtra("rowid", rowid);
            }

            if (parentid != null) {
                intent.putExtra("parentid", parentid);
            }

            startActivity(intent);
        } catch (ClassNotFoundException e) {
            Log.e(this.getLocalClassName(), "Couldn't find class " + view.getTag());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB db = new DB(this.getApplicationContext());
        database = db.getWritableDatabase();

        setContentView(R.layout.activity_base);
        ViewStub viewStub = findViewById(R.id.content_view);
        viewStub.setLayoutResource(contentLayout);
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
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

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
                            case R.id.nav_alerts:
                                targetClass = AlertsActivity.class;
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

        // If an action contentLayout has been specified inflate it and replace the stub.
        if (actionLayout != 0) {
            ViewStub actionStub = findViewById(R.id.floating_action_view);
            actionStub.setLayoutResource(actionLayout);
            actionStub.inflate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
