package com.textmaxx.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    SharedPreferences prefs;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("tag", "token fcm : " + refreshedToken);
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        String token_old = prefs.getString(SharedPreferenceConstants.DEVICE_TOKEN, "");
        if (token_old.equals(null) || token_old.equals("")) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SharedPreferenceConstants.DEVICE_TOKEN, refreshedToken);
            editor.apply();

        }

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);


    }

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.


    }
}