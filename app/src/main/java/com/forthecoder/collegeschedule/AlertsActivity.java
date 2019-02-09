package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.forthecoder.collegeschedule.entity.Alert;
import com.forthecoder.collegeschedule.entity.AlertRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertsActivity extends BaseActivity {

    public AlertsActivity() {
        super();
        contentLayout = R.layout.activity_alerts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AlertRepository ar = new AlertRepository(getDatabase());
        try {
            List<Alert> courses = ar.findAll();
            ListView coursesListView = findViewById(R.id.alertsList);

            Map<String, Integer> fieldMap = new HashMap<>();
            fieldMap.put("rowid", R.id.alertId);
            fieldMap.put("text", R.id.alertText);
            coursesListView.setAdapter(
                    new BaseEntityArrayAdapter<>(
                            Alert.class,
                            this,
                            courses,
                            fieldMap,
                            R.layout.alerts_list_item));


            coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    navigateToTarget(view);
                }
            });

        } catch (ApplicationException e) {
            Log.e("ERROR", e.toString());
        }
    }
}
