package com.forthecoder.collegeschedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.forthecoder.collegeschedule.entity.Term;
import com.forthecoder.collegeschedule.entity.TermRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermsActivity extends BaseActivity {

    public TermsActivity() {
        super();
        contentLayout = R.layout.activity_terms;
        Log.e("ERROR", "TERMS ACTIVITY STARTED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        TermRepository tr = new TermRepository(getDatabase());
        try {
            List<Term> terms = tr.findAll();
            final ListView termListView = findViewById(R.id.termsList);

            Map<String, Integer> fieldMap = new HashMap<>();
            fieldMap.put("rowid", R.id.termId);
            fieldMap.put("title", R.id.termTitle);

            termListView.setAdapter(
                    new BaseEntityArrayAdapter<>(
                            Term.class,
                            this,
                            terms,
                            fieldMap,
                            R.layout.terms_list_item));

            termListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    navigateToTarget(view, ((Term)termListView.getItemAtPosition(position)).getRowid());
                }
            });

        } catch (ApplicationException e) {
            Log.e("ERROR", e.toString());
        }
    }
}
