package com.textmaxx.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements View.OnClickListener {
    private final static String HEX = "0123456789ABCDEF";
    public SharedPreferences prefs;
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.bnt_login)
    Button bnt_login;
    @Bind(R.id.forgot_pas)
    TextView forgot_pas;
    @Bind(R.id.iv_pattern)
    ImageView iv_pattern;

    @Bind(R.id.fingerPrint)
    ImageView fingerPrint;


    @Bind(R.id.activity_main)
    LinearLayout activity_main;
    String username, password, id, reset, error;
    String device_unique_id, token, client, Client_base64, Client_user_base64;
//    private Activity activity;

    public static String toHex(byte[] buf) {

        if (buf == null) return "";

        int l = buf.length;
        StringBuffer result = new StringBuffer(2 * l);

        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }

        return result.toString();

    }

    private static void appendHex(StringBuffer sb, byte b) {

        sb.append(HEX.charAt((b >> 4) & 0x0f))
                .append(HEX.charAt(b & 0x0f));

    }

    public static String SHA1(String text) {

        try {

            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"),
                    0, text.length());
            byte[] sha1hash = md.digest();

            return toHex(sha1hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);

        device_unique_id = prefs.getString(SharedPreferenceConstants.DEVICE_TOKEN, "").trim();
        Log.e("tag", "login device token " + device_unique_id);

        listner();

        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    if (isValidated()) {
                        jsonSave();

                    }
                }
                return false;
            }
        });

    }

    public void listner() {
        bnt_login.setOnClickListener(this);
        forgot_pas.setOnClickListener(this);
        fingerPrint.setOnClickListener(this);
        iv_pattern.setOnClickListener(this);
        activity_main.setOnClickListener(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
////        et_username.setText("India1");
////        et_password.setText("1India");
//
//    }

    @Override
    public void onClick(View view) {
        if (view == bnt_login) {
//            login();
            //login2();

            if (isValidated()) {
                jsonSave();
            }

        }
        if (view == forgot_pas) {

//            forgotPas();
            dialogForgot();
        }

        if (view == fingerPrint) {
            Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

        }

        if (view == iv_pattern) {
            Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

        }

        if (view == activity_main) {

            hideKeyboard();
        }
    }

    private boolean isValidated() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if (username.equals("")) {
            et_username.setError("Please Enter Username");
            et_password.setError(null);
            et_username.requestFocus();
        } else if (password.equals("")) {
            et_username.setError(null);
            et_password.setError("Please Enter Password");
            et_password.requestFocus();
        } else {
            et_username.setError(null);
            et_password.setError(null);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();

    }

    public void jsonSave() {
        ViewUtil.showProgressDialog(MainActivity.this);

        String url = GlobalConstants.BASE_URL + "/tokenize/" + username + "/" + password;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ViewUtil.hideProgressDialog();
//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_LONG).show();
                        Log.e("tag", "Login Response is " + response);
//                        Toast.makeText(MainActivity.this, "response is" + response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.has(GlobalConstants.TOKEN)) {
                                token = obj.getString(GlobalConstants.TOKEN);
                            }
                            if (obj.has("client")) {
                                client = obj.getString("client");
                            }

                            if (obj.has(GlobalConstants.ID)) {
                                id = obj.getString(GlobalConstants.ID);
                            }
                            if (obj.has(GlobalConstants.RESET)) {
                                reset = obj.getString(GlobalConstants.RESET);
                            }
                            if (obj.has(GlobalConstants.ERROR)) {
                                error = obj.getString(GlobalConstants.ERROR);
                            }


                            converClientIntoBase();
                            converClientUserIntoBase();
                            //


//                            if (error.equals("") || error.isEmpty()) {

                            SharedPreferences.Editor editor = prefs.edit();
//                                Log.e("client user token ", Client_user_base64);
//                                Log.e("client token ", Client_base64);
                            editor.putString(SharedPreferenceConstants.CLIENT_USER_TOKEN, Client_user_base64);
                            editor.putString(SharedPreferenceConstants.ID, id);
                            editor.putString(SharedPreferenceConstants.CLIENT_TOKEN, Client_base64);
                            editor.apply();
                            if (reset.equals("true")) {

                                Intent intent = new Intent(MainActivity.this, ChangePassword.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                            } else {
//                                    SharedPreferences.Editor editor2 = prefs.edit();
                                editor.putBoolean(SharedPreferenceConstants.IS_LOGGED_IN, true);
                                editor.apply();

                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);

                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                                    finish();
                            }


//                            }
//                            else {
//
//
//                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ///////////
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ViewUtil.hideProgressDialog();
                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(getApplicationContext(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        } else {
//                            dialogForgot();
                            wrongLogin();
//                            Toast.makeText(getApplicationContext(), "Wrong input", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {


//                device_unique_id = prefs.getString(SharedPreferenceConstants.DEVICE_TOKEN, "");

//                Log.e("tag", "token is" + token);


//                if (device_unique_id.equals(null) || device_unique_id.equals("")) {
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//
//                    Toast.makeText(MainActivity.this, "no device token", Toast.LENGTH_SHORT).show();
//                }


                String test = new String(device_unique_id + username);
                String SHA1 = SHA1(test).toLowerCase();


                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SharedPreferenceConstants.DEVICETOKEN, device_unique_id);
                editor.putString(SharedPreferenceConstants.MOBILITYID, SHA1);
                editor.apply();

                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Host", "api.paymaxxpro.com");
//                headers.put("User-Agent", "fiddler");
                headers.put("X-Device-Token", device_unique_id);

                headers.put("X-Mobility-Id", SHA1);


                headers.put("Accept", "application/json");
                headers.put("User-Agent", "PP_GOOGLE");

                Log.e("tag", "device token" + device_unique_id);
                Log.e("tag", "SHA1 " + SHA1);
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


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void dialogForgot() {
        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.cust_wrong_pas);
        dialog.setCancelable(false);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        ImageView img_cross_forgot = (ImageView) dialog.findViewById(R.id.img_cross_forgot);
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void wrongLogin() {
        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.cust_login_wrong);
        dialog.setCancelable(false);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
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

    public void converClientIntoBase() {
        byte[] data;

//                            String str = "xyzstring";

        try {

            data = client.getBytes("UTF-8");

            Client_base64 = Base64.encodeToString(data, Base64.DEFAULT);

            Log.e("Base 64 ", Client_base64);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }
    }

    public void converClientUserIntoBase() {
        byte[] data1;

        String str = client + ":" + token;

        try {

            data1 = str.getBytes("UTF-8");

            Client_user_base64 = Base64.encodeToString(data1, Base64.DEFAULT);

            Log.e("Base client user 64 ", Client_user_base64);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }
}
