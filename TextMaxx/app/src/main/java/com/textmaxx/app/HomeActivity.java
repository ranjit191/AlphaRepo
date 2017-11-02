package com.textmaxx.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.CommonUtils;
import com.textmaxx.Utils.GlobalConstants;
//import com.textmaxx.fragments.ContactsFrag;
import com.textmaxx.fragments.FragmentContacts;
import com.textmaxx.fragments.FragmentGroup;
import com.textmaxx.fragments.FragmentHome;
import com.textmaxx.fragments.FragmentInbox;
import com.textmaxx.fragments.FragmentSettings;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class HomeActivity extends FragmentActivity implements OnClickListener {
    @Bind(R.id.img_hom2)
    ImageView img_hom2;
    @Bind(R.id.iv_home)
    ImageView iv_home;
    @Bind(R.id.iv_inbox)
    ImageView iv_inbox;
    @Bind(R.id.iv_contacts)
    ImageView iv_contacts;
    @Bind(R.id.iv_settings)
    ImageView iv_settings;
    Fragment fragment = null;
    int count = 0;
    SharedPreferences prefs;
    String goBackLoc = "";
    private TextView txt_title;
    private Context mContext;
    private RelativeLayout line1, line2, line3, line4, line5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mContext = this;
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);

        initializeViews();
        colorTabs();

        listner();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferenceConstants.GO_BACK_LOC, "");
        editor.apply();
        Fabric.with(this, new Crashlytics());
//        throw new RuntimeException("This is a crash");

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initializeViews() {
//        img_hom2.setBackground(getResources().getDrawable(R.drawable.await_select));
//        iv_home.setBackground(getResources().getDrawable(R.drawable.inbox));
//        iv_inbox.setBackground(getResources().getDrawable(R.drawable.contact));
//        iv_contacts.setBackground(getResources().getDrawable(R.drawable.group));
//        iv_settings.setBackground(getResources().getDrawable(R.drawable.setting));
        img_hom2.setImageResource(R.drawable.await_select);
        iv_home.setImageResource(R.drawable.inbox);
        iv_inbox.setImageResource(R.drawable.contact);
        iv_contacts.setImageResource(R.drawable.group);
        iv_settings.setImageResource(R.drawable.setting);


        txt_title = (TextView) findViewById(R.id.txt_title);

        line1 = (RelativeLayout) findViewById(R.id.line1);
        line2 = (RelativeLayout) findViewById(R.id.line2);
        line3 = (RelativeLayout) findViewById(R.id.line3);
        line4 = (RelativeLayout) findViewById(R.id.line4);
        line5 = (RelativeLayout) findViewById(R.id.line5);
        line5.setBackgroundColor(Color.parseColor("#006BA6"));
        line1.setBackgroundColor(Color.parseColor("#1F85D5"));
        line2.setBackgroundColor(Color.parseColor("#1F85D5"));
        line3.setBackgroundColor(Color.parseColor("#1F85D5"));
        line4.setBackgroundColor(Color.parseColor("#1F85D5"));
    }

    public void colorTabs() {
        String push = prefs.getString(SharedPreferenceConstants.GO_BACK_LOC, "").trim();
        if (!push.equals(null) || push != null || !push.equals("null")) {
            goBackLoc = push;

        }

        if (goBackLoc.equals("inbox")) {
            fragment = new FragmentInbox();
//            iv_home.setBackground(getResources().getDrawable(R.drawable.group));
//            iv_inbox.setBackground(getResources().getDrawable(R.drawable.inbox_selected));
//            iv_contacts.setBackground(getResources().getDrawable(R.drawable.contact));
//            iv_settings.setBackground(getResources().getDrawable(R.drawable.setting));
//            img_hom2.setBackground(getResources().getDrawable(R.drawable.await));


            iv_home.setImageResource(R.drawable.group);
            iv_inbox.setImageResource(R.drawable.inbox_selected);
            iv_contacts.setImageResource(R.drawable.contact);
            iv_settings.setImageResource(R.drawable.setting);
            img_hom2.setImageResource(R.drawable.await);


            line1.setBackgroundColor(Color.parseColor("#0086D0"));
            line2.setBackgroundColor(Color.parseColor("#176AAB"));
            line3.setBackgroundColor(Color.parseColor("#1F85D5"));
            line4.setBackgroundColor(Color.parseColor("#1F85D5"));
            line5.setBackgroundColor(Color.parseColor("#1F85D5"));
        } else {
            fragment = new FragmentHome();
//            iv_home.setBackground(getResources().getDrawable(R.drawable.inbox));
//            iv_inbox.setBackground(getResources().getDrawable(R.drawable.contact));
//            iv_contacts.setBackground(getResources().getDrawable(R.drawable.group));
//            iv_settings.setBackground(getResources().getDrawable(R.drawable.setting));
//            img_hom2.setBackground(getResources().getDrawable(R.drawable.await_select));


            iv_home.setImageResource(R.drawable.inbox);
            iv_inbox.setImageResource(R.drawable.contact);
            iv_contacts.setImageResource(R.drawable.group);
            iv_settings.setImageResource(R.drawable.setting);
            img_hom2.setImageResource(R.drawable.await_select);


            line1.setBackgroundColor(Color.parseColor("#1F85D5"));
            line2.setBackgroundColor(Color.parseColor("#1F85D5"));
            line3.setBackgroundColor(Color.parseColor("#1F85D5"));
            line4.setBackgroundColor(Color.parseColor("#1F85D5"));
            line5.setBackgroundColor(Color.parseColor("#176AAB"));
        }

        CommonUtils.replaceFragment(fragment, getSupportFragmentManager());

    }

    public void listner() {

        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        line4.setOnClickListener(this);
        line5.setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {

        if (view == line1) {
            fragment = new FragmentInbox();
//            img_hom2.setBackground(getResources().getDrawable(R.drawable.await));
//            iv_home.setBackground(getResources().getDrawable(R.drawable.inbox_selected));
//            iv_inbox.setBackground(getResources().getDrawable(R.drawable.contact
//            ));
//            iv_contacts.setBackground(getResources().getDrawable(R.drawable.group));
//            iv_settings.setBackground(getResources().getDrawable(R.drawable.setting));


            img_hom2.setImageResource(R.drawable.await);
            iv_home.setImageResource(R.drawable.inbox_selected);
            iv_inbox.setImageResource(R.drawable.contact);
            iv_contacts.setImageResource(R.drawable.group);
            iv_settings.setImageResource(R.drawable.setting);


            count = 1;

            line1.setBackgroundColor(Color.parseColor("#176AAB"));
            line2.setBackgroundColor(Color.parseColor("#1F85D5"));
            line3.setBackgroundColor(Color.parseColor("#1F85D5"));
            line4.setBackgroundColor(Color.parseColor("#1F85D5"));
            line5.setBackgroundColor(Color.parseColor("#1F85D5"));
        } else if (view == line2) {
            fragment = new FragmentContacts();
//            fragment = new ContactsFrag();
//            iv_home.setBackground(getResources().getDrawable(R.drawable.inbox));
//            iv_inbox.setBackground(getResources().getDrawable(R.drawable.contact_selected));
//            iv_contacts.setBackground(getResources().getDrawable(R.drawable.group));
//            iv_settings.setBackground(getResources().getDrawable(R.drawable.setting));
//            img_hom2.setBackground(getResources().getDrawable(R.drawable.await));
//


            img_hom2.setImageResource(R.drawable.await);
            iv_home.setImageResource(R.drawable.inbox);
            iv_inbox.setImageResource(R.drawable.contact_selected);
            iv_contacts.setImageResource(R.drawable.group);
            iv_settings.setImageResource(R.drawable.setting);


            count = 2;
            line1.setBackgroundColor(Color.parseColor("#1F85D5"));
            line2.setBackgroundColor(Color.parseColor("#176AAB"));
            line3.setBackgroundColor(Color.parseColor("#1F85D5"));
            line4.setBackgroundColor(Color.parseColor("#1F85D5"));
            line5.setBackgroundColor(Color.parseColor("#1F85D5"));
        } else if (view == line3) {
            fragment = new FragmentGroup();
//            iv_home.setBackground(getResources().getDrawable(R.drawable.inbox));
//            iv_inbox.setBackground(getResources().getDrawable(R.drawable.contact));
//            iv_contacts.setBackground(getResources().getDrawable(R.drawable.group_selected));
//            iv_settings.setBackground(getResources().getDrawable(R.drawable.setting));
//            img_hom2.setBackground(getResources().getDrawable(R.drawable.await));


            img_hom2.setImageResource(R.drawable.await);
            iv_home.setImageResource(R.drawable.inbox);
            iv_inbox.setImageResource(R.drawable.contact);
            iv_contacts.setImageResource(R.drawable.group_selected);
            iv_settings.setImageResource(R.drawable.setting);


//
            line1.setBackgroundColor(Color.parseColor("#1F85D5"));
            line2.setBackgroundColor(Color.parseColor("#1F85D5"));
            line3.setBackgroundColor(Color.parseColor("#176AAB"));
            line4.setBackgroundColor(Color.parseColor("#1F85D5"));
            line5.setBackgroundColor(Color.parseColor("#1F85D5"));

//            line4.setBackgroundColor(Color.parseColor("#2085D5"));
//            line2.setBackgroundColor(Color.parseColor("#2085D5"));
//            line1.setBackgroundColor(Color.parseColor("#2085D5"));
//            line3.setBackgroundColor(Color.parseColor("#176AAC"));
            count = 3;

        } else if (view == line4) {

            fragment = new FragmentSettings();
//            iv_home.setBackground(getResources().getDrawable(R.drawable.inbox));
//            iv_inbox.setBackground(getResources().getDrawable(R.drawable.contact));
//            iv_contacts.setBackground(getResources().getDrawable(R.drawable.group));
//            iv_settings.setBackground(getResources().getDrawable(R.drawable.setting_selected));
//            img_hom2.setBackground(getResources().getDrawable(R.drawable.await));


            img_hom2.setImageResource(R.drawable.await);
            iv_home.setImageResource(R.drawable.inbox);
            iv_inbox.setImageResource(R.drawable.contact);
            iv_contacts.setImageResource(R.drawable.group);
            iv_settings.setImageResource(R.drawable.setting_selected);


            count = 4;

            line1.setBackgroundColor(Color.parseColor("#1F85D5"));
            line2.setBackgroundColor(Color.parseColor("#1F85D5"));
            line3.setBackgroundColor(Color.parseColor("#1F85D5"));
            line4.setBackgroundColor(Color.parseColor("#176AAB"));
            line5.setBackgroundColor(Color.parseColor("#1F85D5"));
        } else if (view == line5) {

            fragment = new FragmentHome();
//            iv_home.setBackground(getResources().getDrawable(R.drawable.inbox));
//            iv_inbox.setBackground(getResources().getDrawable(R.drawable.contact));
//            iv_contacts.setBackground(getResources().getDrawable(R.drawable.group));
//            iv_settings.setBackground(getResources().getDrawable(R.drawable.setting));
//            img_hom2.setBackground(getResources().getDrawable(R.drawable.await_select));


            img_hom2.setImageResource(R.drawable.await_select);
            iv_home.setImageResource(R.drawable.inbox);
            iv_inbox.setImageResource(R.drawable.contact);
            iv_contacts.setImageResource(R.drawable.group);
            iv_settings.setImageResource(R.drawable.setting);


            count = 4;
            line1.setBackgroundColor(Color.parseColor("#1F85D5"));
            line2.setBackgroundColor(Color.parseColor("#1F85D5"));
            line3.setBackgroundColor(Color.parseColor("#1F85D5"));
            line4.setBackgroundColor(Color.parseColor("#1F85D5"));
            line5.setBackgroundColor(Color.parseColor("#176AAB"));
        }
        CommonUtils.replaceFragment(fragment, getSupportFragmentManager());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}