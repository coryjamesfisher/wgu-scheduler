package com.forthecoder.collegeschedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseEntityArrayAdapter<T> extends ArrayAdapter<T> {

    private final Class<T> clazz;
    private int rowTemplate;
    private Map<Method, Integer> fieldMap = new HashMap<>();

    public BaseEntityArrayAdapter(Class<T> clazz, Context context, List<T> entities, Map<String, Integer> fieldMap, int rowTemplate) throws ApplicationException {
        super(context, 0, entities);

        this.clazz = clazz;
        this.rowTemplate = rowTemplate;

        for (String field : fieldMap.keySet()) {
            try {
                this.fieldMap.put(clazz.getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1)), fieldMap.get(field));
            } catch (NoSuchMethodException e) {
                throw new ApplicationException("Failed to map object field to adapter.", e);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        T entity = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(rowTemplate, parent, false);
        }

        for (Map.Entry entry : fieldMap.entrySet()) {
            TextView listColumn = convertView.findViewById((int) entry.getValue());

            try {
                listColumn.setText(((Method) entry.getKey()).invoke(entity).toString());
            } catch (IllegalAccessException e) {
                Log.e("Entity getter failure", e.toString());
            } catch (InvocationTargetException e) {
                Log.e("Entity getter failure", e.toString());
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
