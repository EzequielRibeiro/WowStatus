package com.wows.status;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import static android.content.Context.MODE_PRIVATE;
import static com.wows.status.MainActivity.CHANNEL_ID;
import static com.wows.status.MainActivity.getServeVersion;
import static com.wows.status.MainActivity.notificationPermission;

public class GameVersion extends BroadcastReceiver {


    private String requestVersion;

    @Override
    public void onReceive(Context context, Intent intent) {

     new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = context.getSharedPreferences("info", MODE_PRIVATE);
                String currentVersion = preferences.getString("version", "00.00.00").replace(".", "");
                requestVersion = getServeVersion(context);
                String temp = requestVersion.replace(".", "");

                int num1 = Integer.valueOf(currentVersion);
                int num2 = Integer.valueOf(temp);

                if (num1 < num2) {
                    preferences.edit().putString("version", requestVersion).apply();

                    NotificationManagerCompat notification = NotificationManagerCompat.from(context);
                    boolean isEnabled = notification.areNotificationsEnabled();

                    if(isEnabled)
                       notification(context);
                    else
                       notificationPermission(context,false);


                }
            }
        }).start();


    }

    private void notification(Context context) {

        Intent intent = new Intent(context, BrowserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText("Game with new version: " + requestVersion)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            notificationManager.notify(0, mBuilder.build());

        }

    }


}
