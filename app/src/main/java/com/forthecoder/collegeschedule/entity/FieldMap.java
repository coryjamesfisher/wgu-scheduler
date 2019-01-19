package com.forthecoder.collegeschedule.entity;

import android.database.Cursor;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class FieldMap {
    private String field;
    private Method method;
    private Class type;

    public FieldMap(String field, Method method, Class type) {
        this.field = field;
        this.method = method;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public void onto(Object obj, Cursor cursor) throws ApplicationException, InvocationTargetException, IllegalAccessException {
        this.getMethod().invoke(obj, getValue(cursor));
    }

    private Object getValue(Cursor cursor) throws ApplicationException {

        if (type == Integer.TYPE) {
            return cursor.getInt(cursor.getColumnIndex(this.getField()));
        } else if (type == String.class) {
            return cursor.getString(cursor.getColumnIndex(this.getField()));
        } else if (type == Date.class) {
            return new Date(cursor.getLong(cursor.getColumnIndex(this.getField())));
        } else {
            throw new ApplicationException("Unrecognized type while mapping database values.", null);
        }
    }
}
