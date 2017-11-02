package com.textmaxx.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.ViewUtil;
import com.textmaxx.app.MainActivity;
import com.textmaxx.app.MyAccount;
import com.textmaxx.app.WebViewActivity;
import com.textmaxx.app.Youtube;
import com.textmaxx.models.ModelChat;
import com.textmaxx.models.ModelFindContacts;
import com.textmaxx.models.ModelTabContacts;
import com.textmaxx.models.ModelTabHome;
import com.textmaxx.models.ModelTabInbox;
import com.textmaxx.models.ModelTempList;
//import com.textmaxx.realm.adapter.AdapterstaticContacts;
import com.textmaxx.realm.models.ModelCellInfo;
import com.textmaxx.realm.models.ModelMyAccount;
import com.textmaxx.realm.models.ModelTempMsges;
//import com.textmaxx.realm.models.StaticModelContacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class FragmentSettings extends Fragment implements View.OnClickListener {

    public SharedPreferences prefs;
    TextView txt_myAccount, txt_chngpas, txt_logout, tv_web_link, txt_youtube, txt_changPattrn, txt_version;
    private Realm mRealm;
    private TextView txt_title;
    private ImageView iv_fb, iv_youtube, iv_twitter, iv_insta;
    private RelativeLayout rel_need_help, rel_youtue, rel_myaccount;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragsettings, viewGroup, false);
        mContext = getActivity();

        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText("Settings");
        prefs = getActivity().getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
//        txt_changPattrn = (TextView) view.findViewById(R.id.txt_changPattrn);
        txt_version = (TextView) view.findViewById(R.id.txt_version);
        txt_myAccount = (TextView) view.findViewById(R.id.txt_myAccount);
        tv_web_link = (TextView) view.findViewById(R.id.tv_web_link);
        txt_chngpas = (TextView) view.findViewById(R.id.txt_chngpas);
        txt_logout = (TextView) view.findViewById(R.id.txt_logout);
        iv_fb = (ImageView) view.findViewById(R.id.iv_fb);
        iv_insta = (ImageView) view.findViewById(R.id.iv_insta);

        iv_twitter = (ImageView) view.findViewById(R.id.iv_twitter);

        iv_youtube = (ImageView) view.findViewById(R.id.iv_youtube);

        rel_need_help = (RelativeLayout) view.findViewById(R.id.rel_need_help);
        rel_youtue = (RelativeLayout) view.findViewById(R.id.rel_youtue);
        rel_myaccount = (RelativeLayout) view.findViewById(R.id.rel_myaccount);

        mRealm = getmRealm();
        clickListner();
        setVersion();
        return view;
    }

    public void setVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            txt_version.setText("Version - " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Realm getmRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getActivity()).build();

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex) {
                throw ex;
                //No Realm file to remove.
            }
        }
    }


    public void clickListner() {
        txt_myAccount.setOnClickListener(this);
        txt_chngpas.setOnClickListener(this);
        txt_logout.setOnClickListener(this);
        iv_fb.setOnClickListener(this);
        iv_youtube.setOnClickListener(this);
        iv_insta.setOnClickListener(this);
        iv_twitter.setOnClickListener(this);
        tv_web_link.setOnClickListener(this);
        rel_need_help.setOnClickListener(this);
        rel_youtue.setOnClickListener(this);
        rel_myaccount.setOnClickListener(this);
//        txt_changPattrn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txt_myAccount) {
//            Toast.makeText(getActivity(), "yet to work", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), MyAccount.class);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        if (v == tv_web_link) {
//            Toast.makeText(getActivity(), "Google", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("title", "Website");
            editor.apply();
            String urlString = GlobalConstants.APP_URL; // missing 'http://' will cause crashed
            WebViewActivity.createInstance(mContext, urlString);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        if (v == txt_chngpas) {

            dialogForgot();
//            Toast.makeText(getActivity(), "yet to work", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(getContext(), ChangePassword.class);
//            startActivity(intent);
//            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        if (v == txt_logout) {
            dialogLogout();

        }

        if (v == iv_fb) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("title", "Facebook");
            editor.apply();
            String urlString = GlobalConstants.APP_FB; // missing 'http://' will cause crashed
            WebViewActivity.createInstance(mContext, urlString);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


////            Toast.makeText(getActivity(), "facebook", Toast.LENGTH_SHORT).show();
//            Uri uri = Uri.parse("https://www.facebook.com/textmaxxpro/"); // missing 'http://' will cause crashed
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        }
        if (v == iv_youtube) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("title", "Youtube");
            editor.apply();
            String urlString = GlobalConstants.APP_YOUTUBE; // missing 'http://' will cause crashed
            WebViewActivity.createInstance(mContext, urlString);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        if (v == iv_twitter) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("title", "Twitter");
            editor.apply();

            try {
                String twitterName = "@TextmaxxPro";
                String formattedTwitterAddress = "http://twitter.com/" + twitterName;
                WebViewActivity.createInstance(mContext, formattedTwitterAddress);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            } catch (Exception e) {

            }

        }
        if (v == iv_insta) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("title", "Website");
            editor.apply();
            String urlString = GlobalConstants.APP_URL; // missing 'http://' will cause crashed
            WebViewActivity.createInstance(mContext, urlString);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            Toast.makeText(getActivity(), "Not Available", Toast.LENGTH_SHORT).show();
        }
        if (v == rel_need_help) {

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@textmaxxpro.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
            startActivity(intent);
        }
        if (v == rel_youtue) {

            Intent intent = new Intent(getContext(), Youtube.class);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
        if (v == rel_myaccount) {
            Intent intent = new Intent(getContext(), MyAccount.class);
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
//        if (v == txt_changPattrn) {
//            Toast.makeText(getActivity(), "Not Available Yet", Toast.LENGTH_SHORT).show();
//        }
    }

    public void logoutApi() {
        ViewUtil.showProgressDialog(getActivity());
        String url = GlobalConstants.BASE_URL + "/tokenize";


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response LOGOUT " + response);
                        ViewUtil.hideProgressDialog();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(SharedPreferenceConstants.IS_LOGGED_IN, false);
                        editor.apply();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        getActivity().finish();
                        clearDB();

                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ViewUtil.hideProgressDialog();
                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";


                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        } else {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(SharedPreferenceConstants.IS_LOGGED_IN, false);
                            editor.apply();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            getActivity().finish();
                            clearDB();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q=");
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
                headers.put("X-Mobility-Id", prefs.getString(SharedPreferenceConstants.MOBILITYID, "").trim());
                headers.put("X-Device-Token", prefs.getString(SharedPreferenceConstants.DEVICETOKEN, "").trim());
                headers.put("Accept", "application/json");

                return headers;
            }

//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Log.e("tag", "Does it assign Headers?");
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("apns_device_token", "abcdef");
//                return params;
//            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void dialogForgot() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.cust_chang_pas);
        dialog.setCancelable(false);


        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void dialogLogout() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.cust_logout);
        dialog.setCancelable(false);


        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                logoutApi();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    public void clearDB() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                mRealm.clear(ModelChat.class);
                mRealm.clear(ModelFindContacts.class);
                mRealm.clear(ModelTabHome.class);
                mRealm.clear(ModelTabInbox.class);
                mRealm.clear(ModelTempList.class);

                mRealm.clear(ModelCellInfo.class);
                mRealm.clear(ModelMyAccount.class);
                mRealm.clear(ModelTempMsges.class);

                mRealm.clear(ModelTabContacts.class);

            }
        });
    }



}


