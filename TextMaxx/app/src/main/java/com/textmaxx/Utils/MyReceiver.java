package com.textmaxx.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sumit on 25/1/17.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        /*Intent intent1 = new Intent(context, ChatScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent1);
        //ChatScreen.test();*/


        /*Intent i = new Intent();
        i.setClassName("com.textmaxx", "com.textmaxx.app.ChatScreen");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/
//        Toast.makeText(context, "push came", Toast.LENGTH_LONG).show();
        InterfaceListener.getOnNotificationReceived();

        //////////////////////


    }
}