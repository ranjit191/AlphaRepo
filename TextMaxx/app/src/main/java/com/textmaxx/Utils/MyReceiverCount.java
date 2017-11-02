package com.textmaxx.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sumit on 25/1/17.
 */
public class MyReceiverCount extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "push count", Toast.LENGTH_LONG).show();
        InterfaceListener.getOnNotificationReceived();

    }
}