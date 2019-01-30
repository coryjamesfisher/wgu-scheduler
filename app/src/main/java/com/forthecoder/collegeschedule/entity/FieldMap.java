package com.forthecoder.collegeschedule.entity;

import android.database.Cursor;

import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class FieldMap {
    private String field;
    private Method setterMethod;
    private Method getterMethod;
    private Class type;

    public FieldMap(String field, Method setterMethod, Method getterMethod, Class type) {
        this.field = field;
        this.setterMethod = setterMethod;
        this.getterMethod = getterMethod;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Method getSetterMethod() {
        return setterMethod;
    }

    public void setSetterMethod(Method setterMethod) {
        this.setterMethod = setterMethod;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public void setGetterMethod(Method getterMethod) {
        this.getterMethod = getterMethod;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public void ontoEntity(Object obj, Cursor cursor) throws ApplicationException, InvocationTargetException, IllegalAccessException {
        this.getSetterMethod().invoke(obj, dbToEntityValue(cursor));
    }

    private Object dbToEntityValue(Cursor cursor) throws ApplicationException {

        if (type == Long.TYPE || type == Long.class) {
            return cursor.getLong(cursor.getColumnIndex(this.getField()));
        } else if (type == Integer.class) {
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
