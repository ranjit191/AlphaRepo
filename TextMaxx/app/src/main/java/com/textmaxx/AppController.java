package com.textmaxx;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.textmaxx.app.MainActivity;
import com.textmaxx.models.ModelTabHome;
import com.textmaxx.models.ModelTabInbox;

/**
 * Created by sumit on 7/12/16.
 */
public class AppController extends Application {
    public static AppController application;
    public ModelTabInbox modelTabInbox;
    public ModelTabHome modelTabHome;
    MainActivity mainActivity;

    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
//        ProcessPhoenix.triggerRebirth(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (ProcessPhoenix.isPhoenixProcess(this)) {
//            return;
//        }
        // Initialize the singletons so their instances
        // are bound to the application process.
        //initSingletons();
        application = this;

    }

}
