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

            if (field.getName().equals("serialVersionUID") || field.getName().equals("$change")) {
                continue;
            }

            String columnName = field.getName();

            // Get the public setter and getter
            Method setter = findMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
            Method getter = findMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));

            fieldMaps.add(new FieldMap(columnName, setter, getter, field.getType()));
        }

        // Add base fields.
        fieldMaps.add(new FieldMap("rowid", findMethod("setRowid"), findMethod("getRowid"), Long.TYPE));
    }

    private Method findMethod(String methodName) {
        for (Method method : clazz.getDeclaredMethods()) {

            if (Modifier.isPublic(method.getModifiers()) && method.getName().equals(methodName)) {
                return method;
            }
        }

        return null;
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
                map.ontoEntity(obj, cursor);
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

    public T findOneByRowid(Long rowid) throws ApplicationException {
        List<T> entityList = queryList("SELECT rowid, * FROM " + this.clazz.getSimpleName() + " WHERE rowid=?", rowid.toString());

        if (entityList.size() == 0) {
            throw new ApplicationException("Entity not found with rowid " + rowid.toString(), null);
        }

        return entityList.get(0);
    }

    public void save(T obj) throws InvocationTargetException, IllegalAccessException, ApplicationException {

        if (((BaseEntity)obj).getRowid() == 0L) {
            insert(obj);
        } else {
            update(obj);
        }
    }

    private void insert(T obj) throws InvocationTargetException, IllegalAccessException, ApplicationException {

        StringBuilder insertSQL = new StringBuilder("INSERT INTO " + clazz.getSimpleName() + "(");

        List<Object> values = new ArrayList<>();

        for (FieldMap fieldMap : this.fieldMaps) {

            if (fieldMap.getField().equals("rowid")) {
                continue;
            }
            insertSQL.append(fieldMap.getField()).append(",");
            values.add(fieldMap.getGetterMethod().invoke(obj));
        }
        insertSQL.deleteCharAt(insertSQL.length() - 1);
        insertSQL.append(") VALUES (");

        for (int i = 0; i < values.size(); i++) {
            insertSQL.append("?,");
        }
        insertSQL.deleteCharAt(insertSQL.length() - 1);
        insertSQL.append(");");

        try {
            Log.e("ERROR", insertSQL.toString());
            dbConnection.execSQL(insertSQL.toString(), values.toArray());
        } catch (SQLException e) {
            throw new ApplicationException("A system error has occurred.", e);
        }
    }

    private void update(T obj) throws InvocationTargetException, IllegalAccessException, ApplicationException {

        StringBuilder updateSQL = new StringBuilder("UPDATE " + clazz.getSimpleName() + " SET ");

        List<Object> values = new ArrayList<>();

        Long rowid = null;
        for (FieldMap fieldMap : this.fieldMaps) {

            if (fieldMap.getField().equals("rowid")) {
                rowid = (Long) fieldMap.getGetterMethod().invoke(obj);
                continue;
            }
            updateSQL.append(fieldMap.getField()).append(" = ?,");
            values.add(fieldMap.getGetterMethod().invoke(obj));
        }

        if (rowid == null) {
            throw new IllegalArgumentException("Rowid is required for updates.");
        }

        values.add(rowid);

        updateSQL.deleteCharAt(updateSQL.length() - 1);
        updateSQL.append(" WHERE rowid = ?;");

        try {
            Log.e("ERROR", updateSQL.toString());
            dbConnection.execSQL(updateSQL.toString(), values.toArray());
        } catch (SQLException e) {
            throw new ApplicationException("A system error has occurred.", e);
        }
    }

    public void createSchema() throws ApplicationException {

        StringBuilder schemaSQL = new StringBuilder("CREATE TABLE " + clazz.getSimpleName() + " (");

        for (FieldMap map : fieldMaps) {

            if (map.getType() == Integer.class || map.getType() == Long.class) {

                if (!map.getField().equals("rowid")) {
                    schemaSQL.append(map.getField()).append(" INTEGER,");
                }
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

    public List<T> findAll() throws ApplicationException {
        return queryList("SELECT rowid, * FROM " + this.clazz.getSimpleName());
    }
}
