package com.textmaxx.fcm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.NotificationUtils;
import com.textmaxx.app.ChatScreen;

public class MyFirebaseMEssagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final int MY_NOTIFICATION_ID = 1;
    //    String cell, type, title, messageBody, icon;
    String cell = "", label = "", cellChat = "", body, cellNo;
    SharedPreferences prefs;
    NotificationManager notificationManager;
    Notification myNotification;
//    String push_count;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // TODO: Handle FCM messages here.
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        cellNo = prefs.getString(SharedPreferenceConstants.CHAT_SCREEN_CELL, "").trim();
//        push_count = prefs.getString(SharedPreferenceConstants.PUSH_COUNT, "").trim();

        Log.e("teg", "cellNo shared " + cellNo);
        body = remoteMessage.getNotification().getBody();
//        cellChat = prefs.getString(SharedPreferenceConstants.CHAT_SCREEN_CELL, "");
        if (remoteMessage.getData().size() > 0) {
            cell = remoteMessage.getData().get("cell").trim();
            label = remoteMessage.getData().get("label").trim();
            Log.e("teg", "cellcellcellcell is " + cell);
            saveData();
        } else {
            Log.e("tag", "json is not valid");
        }

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            String activity = prefs.getString(SharedPreferenceConstants.ACTIVITY, "");
            if (activity.equals("") || activity.equals(null) || activity == null || activity.equals("OtherScreen") && !activity.equals("ChatScreen")) {
//                saveCount();
                createNotification();
//                saveData();
                Intent intent = new Intent();
                intent.setAction("count");
                sendBroadcast(intent);
                Log.e("teg", "111111111111111");
//                saveCount();
            } else if (activity.equals("ChatScreen")) {
                Log.e("teg", "cell server" + cell);
                Log.e("teg", "cell shared" + cellNo);

                if (cell.equals(cellNo)) {
                    Log.e("teg", "222222222222222");
                    Intent intent = new Intent();
                    intent.setAction("push");
                    sendBroadcast(intent);

                } else {
                    createNotification();
                    Log.e("teg", "3333333333333");
                    Intent intent = new Intent();
                    intent.setAction("count");
                    sendBroadcast(intent);
//                    saveCount();
                }


            }
//            else {
//
//            }

        }

    }

    public void saveData() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, cell);
        editor.putString("chat_name", label);
        editor.apply();
    }

//    public void saveCount() {
//        SharedPreferences.Editor editor = prefs.edit();
//        if (push_count.equals("") || push_count.equals(null) || push_count == null) {
//            editor.putString(SharedPreferenceConstants.PUSH_COUNT, "0");
//        } else {
//            int count = Integer.parseInt(push_count) + 1;
//            editor.putString(SharedPreferenceConstants.PUSH_COUNT, String.valueOf(count));
//
//        }
//
//
//        editor.apply();
//    }

    public void createNotification() {

        Intent myIntent = new Intent(getApplicationContext(), ChatScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myNotification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Textmaxxpro")
                .setContentText(body)


//                            .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.app_icon)
                .build();

        notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
    }
}