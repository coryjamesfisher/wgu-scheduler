package com.forthecoder.collegeschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.forthecoder.collegeschedule.entity.Alert;
import com.forthecoder.collegeschedule.entity.AlertRepository;
import com.forthecoder.collegeschedule.exception.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

public class AlertService {

    private final Context context;
    private final AlertRepository alertRepository;
    private final AlarmManager alarmManager;

    public AlertService(BaseActivity context) {
        this.context = context;
        this.alertRepository = new AlertRepository(context.getDatabase());
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void save(Alert alert) throws IllegalAccessException, ApplicationException, InvocationTargetException {
        alertRepository.save(alert);

        PendingIntent pendingIntent = getPendingIntent(alert);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void delete(Alert alert) throws ApplicationException {
        alertRepository.delete(alert);
        alarmManager.cancel(getPendingIntent(alert));
    }

    private PendingIntent getPendingIntent(Alert alert) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("alertId", alert.getRowid());
        intent.putExtra("termId", alert.getTermId());
        intent.putExtra("courseId", alert.getCourseId());
        intent.putExtra("assessmentId", alert.getAssessmentId());
        intent.putExtra("text", alert.getText());
        return PendingIntent.getBroadcast(context.getApplicationContext(), alert.getRowid().intValue(), intent, 0);
    }
}
