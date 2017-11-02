package com.textmaxx.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;

public class SplashScreen extends Activity {
    //    private static final int MY_NOTIFICATION_ID = 1;
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    SharedPreferences prefs;
    //    private Activity activity;
    String device_token;
    String type = "", cell = "", label;
    boolean isLoggedIn;
    String tokenNullCheck = "1";
//    int fcm_count = 0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
//        saveActivity();
//        activity = this;

        Log.d("qqqqq", "number ######");
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        device_token = prefs.getString(SharedPreferenceConstants.DEVICE_TOKEN, "");
        Log.d("qqqqq", "device_token " + device_token);
        //fcm_count = prefs.getInt(SharedPreferenceConstants.FCM_COUNT, 0);
        isLoggedIn = prefs.getBoolean(SharedPreferenceConstants.IS_LOGGED_IN, false);

        if (tokenNullCheck.equals("0")) {
            setContentView(R.layout.activity_splash_screen);
        }
        if (device_token.equals(null) || device_token.equals("")) {
            // 1 number
            Log.d("tag", "number 1 number");
            getFCMToken();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SharedPreferenceConstants.TOKEN_NULL_CHECK, "0");
            editor.apply();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        } else if (isLoggedIn) {


            final Bundle bundle = getIntent().getExtras();
            Log.d("tag", " bundle type " + bundle);

            if (bundle == null || (bundle.equals("null")) || (bundle.isEmpty())) {
                // 3 number
                Log.d("tag", "number 3 number");
                Intent mainIntent = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            } else {

                type = (getIntent().getExtras().getString("type"));
                Log.d("tag", " type type " + type);
//                Toast.makeText(SplashScreen.this, "type is " + type, Toast.LENGTH_SHORT).show();

                if (type == null || type.equals("null") || type.isEmpty()) {


                    // 4 number
                    Log.d("tag", "number 4 number");
                    runMethod();
                } else {
                    if (type.equals("PNT_NEW_MESSAGE")) {
                        // 5 number
//                        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
//                        BroadcastReceiver mReceiver = new MyReceiver();
//                        registerReceiver(mReceiver, filter);

                        Log.d("tag", "number 5 number");
                        cell = (getIntent().getExtras().getString("cell"));
                        label = (getIntent().getExtras().getString("label"));
                        Log.d("tag", " cell cell" + cell);
//                        Toast.makeText(SplashScreen.this, "push came " + cell, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SplashScreen.this, ChatScreen.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, cell);
                        editor.putString("chat_name", label);
                        editor.putString(SharedPreferenceConstants.GO_BACK_LOC, "push");
                        editor.apply();
                    } else if (type.equals("PNT_AWAIT")) {
                        // 6 number
                        Log.d("tag", "number 6 number");

//                        Intent intent2 = new Intent();
//                        intent2.setAction("com.tutorialspoint.CUSTOM_INTENT");
//                        sendBroadcast(intent2);
                        Intent intent = new Intent(SplashScreen.this, ChatScreen.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, cell);
                        editor.putString("chat_name", label);
                        editor.putString(SharedPreferenceConstants.GO_BACK_LOC, "push");
                        editor.apply();

                    }
                }

            }
        } else {

            setContentView(R.layout.activity_splash_screen);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable() {

                                          @Override
                                          public void run() {

                                              // 7 number
                                              Log.d("tag", "number 7 number");
                                              Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                                              startActivity(mainIntent);
                                              overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                              finish();
                                          }


                                      }

                    , SPLASH_DISPLAY_LENGTH);

        }


//        CommonUtils.FullScreencall(activity);


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferenceConstants.TOKEN_NULL_CHECK, "1");
        editor.apply();
    }

    public void runMethod() {
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {


                                          // 7 number
                                          Log.d("tag", " 7 number");
                                          Intent mainIntent = new Intent(SplashScreen.this, HomeActivity.class);
                                          startActivity(mainIntent);
                                          overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                          finish();
                                      }


                                  }

                , SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //    public void saveActivity() {
//
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(SharedPreferenceConstants.ACTIVITY, "OtherScreen");
//        editor.apply();
//
//    }
    String getFCMToken() {
        String refreshedToken = "";
//        fcm_count++;
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SharedPreferenceConstants.DEVICE_TOKEN, refreshedToken);
//            editor.putInt(String.valueOf(SharedPreferenceConstants.FCM_COUNT), fcm_count);
            editor.apply();

            Log.e("tag", "refresh token " + refreshedToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return refreshedToken;
    }
}