package com.forthecoder.collegeschedule.entity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BaseRepository<T> {

    private final Class<T> clazz;
    private List<FieldMap> fieldMaps;
    protected SQLiteDatabase dbConnection;

    public BaseRepository(Class<T> clazz, SQLiteDatabase dbConnection) {
        this.clazz = clazz;
        this.fieldMaps = new ArrayList<FieldMap>();
        this.dbConnection = dbConnection;

        // Iterate over the fields to get mappings for.
        Field[] fieldList = clazz.getDeclaredFields();
        for (Field field : fieldList) {

            String columnName = field.getName();

            // Check if there is a public setter
            String search = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            for (Method method : clazz.getDeclaredMethods()) {

                if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(search)) {
                    fieldMaps.add(new FieldMap(columnName, method, field.getType()));
                    break;
                }
            }
        }
    }

    public T fromResultSet(Cursor cursor) throws ApplicationException {

        T obj;

        Class<?>[] empty = {};

        try {
            obj = this.clazz.getConstructor(empty).newInstance();
        } catch (IllegalAccessException|InstantiationException|NoSuchMethodException|InvocationTargetException e) {

            // Should never happen.
            throw new ApplicationException("Failed to instantiate entity object for database mapping.", e);
        }

        for (FieldMap map : this.fieldMaps) {
            try {
                map.onto(obj, cursor);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {

                // Should never happen.
                throw new ApplicationException("Failed to map entity from database record.", e);
            }
        }

        return obj;
    }

    List<T> queryList(String query, String... params) throws ApplicationException {
        List<T> entityList = new ArrayList<>();

        Cursor cursor = dbConnection.rawQuery(query, params);
        while (cursor.moveToNext()) {
            entityList.add(fromResultSet(cursor));
        }

        return entityList;
    }

    public void createSchema() throws ApplicationException {

        StringBuilder schemaSQL = new StringBuilder("CREATE TABLE " + clazz.getSimpleName() + " (");

        for (FieldMap map : fieldMaps) {

            if (map.getType() == Integer.TYPE) {
                schemaSQL.append(map.getField()).append(" INTEGER,");
            } else if (map.getType() == String.class || map.getType() == Date.class) {
                schemaSQL.append(map.getField()).append(" TEXT,");
            }
        }

        schemaSQL.deleteCharAt(schemaSQL.length() - 1);
        schemaSQL.append(");");

        try {
            Log.e("ERROR", schemaSQL.toString());
            dbConnection.execSQL(schemaSQL.toString());
        } catch (SQLException e) {
            throw new ApplicationException("A system error has occurred.", e);
        }
    }

    public void dropSchema() throws ApplicationException {

        String schemaSQL = "DROP TABLE " + clazz.getSimpleName() + ";";
        try {
            Log.e("ERROR", schemaSQL);
            dbConnection.execSQL(schemaSQL);
        } catch (SQLException e) {
            throw new ApplicationException("A system error has occurred.", e);
        }
    }
}
