package com.textmaxx.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.textmaxx.R;

/**
 * Created by sumit on 17/10/16.
 */
public class CommonUtils {

    public static void replaceFragment(Fragment fragment, FragmentManager supportFragmentManager) {
        FragmentManager manager = supportFragmentManager;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.output, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    public static void FullScreencall(Activity activity) {
//        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = activity.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if(Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            View decorView = activity.getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//    }
}
