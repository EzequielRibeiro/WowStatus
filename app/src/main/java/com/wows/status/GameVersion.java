package com.wows.status;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.MODE_PRIVATE;

public class GameVersion extends BroadcastReceiver {

    private String requestVersion;

    @Override
    public void onReceive(final Context context, Intent intent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                StatusServer statusServer = new StatusServer();
                SharedPreferences preferences = context.getSharedPreferences("info", MODE_PRIVATE);
                String currentVersion = preferences.getString("version", "0.0.0.0").replace(".", "");
                requestVersion = statusServer.getServeVersion();
                String temp = requestVersion.replace(".", "");

                int num1 = Integer.valueOf(currentVersion);
                int num2 = Integer.valueOf(temp);

                if (num1 < num2) {
                    preferences.edit().putString("version", requestVersion).commit();
                    notification(context);
                }
            }
        }).start();


    }

    private void notification(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", MODE_PRIVATE);

        Intent intent = new Intent(context, ScrollingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "CHANNEL_ID")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText("Game with new version: " + requestVersion)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, mBuilder.build());
    }
}
