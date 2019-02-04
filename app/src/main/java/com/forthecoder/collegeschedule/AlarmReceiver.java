package com.forthecoder.collegeschedule;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("RECEIVING NOTIFICATION", "RECEIVED");
        createNotificationChannel(context);

        Long notificationId = intent.getLongExtra("alertId", 0L);
        Long assessmentId = intent.getLongExtra("assessmentId", 0L);
        Long courseId = intent.getLongExtra("courseId", 0L);
        Long termId = intent.getLongExtra("termId", 0L);
        String text = intent.getStringExtra("text");

        Class activity;
        Long id;
        Long parentid;
        if (assessmentId != 0) {
            activity = AssessmentDetailsActivity.class;
            id = assessmentId;
            parentid = courseId;
        } else if (courseId != 0) {
            activity = CourseDetailsActivity.class;
            id = courseId;
            parentid = termId;
        } else if (termId != 0) {
            activity = TermDetailsActivity.class;
            id = termId;
            parentid = 0L;
        } else {
            return;
        }

        Log.e("ID", "ROW ID:" + id);
        Log.e("PARENT ID", "ROW ID:" + parentid);

        Intent target = new Intent(context, activity);
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        target.putExtra("rowid", id);

        if (parentid != 0L) {
            target.putExtra("parentid", parentid);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId.intValue(), target, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "Alerts")
                .setSmallIcon(R.drawable.ic_alarm_icon)
                .setContentTitle("College Schedule Notification")
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Log.e("CREATING NOTIFICATION", "ID: " + notificationId.intValue());
        notificationManager.cancelAll();
        notificationManager.notify(notificationId.intValue(), mBuilder.build());
    }



    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.alerts_channel_name);
            String description = context.getString(R.string.alerts_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);        }
    }
}
