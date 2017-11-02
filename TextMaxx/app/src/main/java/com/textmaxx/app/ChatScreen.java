package com.textmaxx.app;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.textmaxx.Interfaces.OnNotificationReceived;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.Utils.ViewUtil;
import com.textmaxx.listview_adapter.AdapterChat;
import com.textmaxx.listview_adapter.AdapterTemplates;
import com.textmaxx.models.ModelChat;
import com.textmaxx.models.ModelTempList;
import com.textmaxx.realm.adapter.AdapterChatStatic;
import com.textmaxx.realm.models.ModelTempMsges;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ChatScreen extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener, OnNotificationReceived {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static Context mContext;
    //    @Bind(R.id.iv_template)
//    ImageView iv_template;
    private static int RESULT_LOAD_IMAGE = 1;
    @Bind(R.id.line1)
    LinearLayout line1;
    //    @Bind(R.id.dollor)
//    ImageView dollor;
    @Bind(R.id.iv_image)
    ImageView sendImage;
    @Bind(R.id.txt_title)
    TextView txt_title;
    //    @Bind(R.id.iv_call)
//    ImageView ivCall;
    @Bind(R.id.b_send_msg)
    ImageView b_send_msg;
    //    @Bind(R.id.iv_comment)
//    ImageView iv_comment;
//    @Bind(R.id.iv_web)
//    ImageView iv_web;
    @Bind(R.id.et_message)
    EditText et_message;
    int countToast = 0;
    @Bind(R.id.txt_varified)
    TextView txt_varified;
    String requestBody, requestBody2;
    Spinner spinner;
    ImageView firstImage;
    Button btn_send_payment;
    ListView listView;
    AdapterChat adapter;
    AdapterTemplates temp_adapter;
    ModelChat model;
    ModelTempList modelTempList;
    List<ModelChat> rowItems = new ArrayList<ModelChat>();
    List<ModelChat> rowItems_temporary = new ArrayList<ModelChat>();
    List<ModelTempList> rowItems_temp = new ArrayList<ModelTempList>();
    int comment_count = 0;
    String account_on_file, call, web;
    List<String> adapter_chat_msg = new ArrayList<String>();
    List<String> adapter_chat_msg_temp = new ArrayList<String>();
    List<String> adapter_spinner = new ArrayList<String>();
    List<String> token_list = new ArrayList<String>();
    List<String> adapter_temp_id = new ArrayList<String>();
    List<String> adapter_msg_id = new ArrayList<String>();
    List<ModelChat> chatsList2 = new ArrayList<ModelChat>();
    SharedPreferences prefs;
    ListView list_temp;
    Dialog dialog_temp, dialog_money;
    String cellNo = "", temp_msg;
    String temp_id;
    Realm mRealm;
    String messageId, imageBase64;
    //don't delete this
    int messageSendCount = 0;
    int countItems = 0;
    int accountLength = 0;
    String tempLength = "null", cellDetail = "null", varifiedNumber = "false", callPermission = "", net = "no", commentJson = "false";
    int totalListCount = 0;
    String goBackLoc = "";
    String picturePath;
    private String message, Name, token_from_acount, direction, label, comment = "false", sent = "";
    private EditText et_payment_amount;
    private int CALL_CODE = 23;
    private int Position = 0;
    private boolean last_msg = true;
    private Uri fileUri;
    private String chatMessage, imgUpload = "false", txt_errorr;

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        ButterKnife.bind(this);
        mContext = this;
        listView = (ListView) findViewById(R.id.listview_chat);
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);

        mRealm = getmRealm();
        cellNo = prefs.getString(SharedPreferenceConstants.CHAT_SCREEN_CELL, "").trim();
        String push = prefs.getString(SharedPreferenceConstants.GO_BACK_LOC, "").trim();
        if (!push.equals(null) || push != null || !push.equals("null")) {
            goBackLoc = push;
        }

        Log.e("tag", "cell is " + cellNo);
        Name = prefs.getString("chat_name", "").trim();

        callPermission = prefs.getString("callPermission", "");
        saveGoBack();
        b_send_msg.setClickable(false);
        b_send_msg.setAlpha((float) .5);
        txt_title.setText(Name);
        firstImage = (ImageView) findViewById(R.id.firstImage);
        firstImage.setImageResource(R.drawable.left_icon);
        firstImage.setEnabled(true);

        adapter = new AdapterChat(ChatScreen.this, rowItems);


        temp_adapter = new AdapterTemplates(ChatScreen.this, rowItems_temp);


        if (messageSendCount == 0) {
            List<ModelChat> chatsList = new ArrayList<>();
            chatsList = mRealm.where(ModelChat.class).equalTo("cellNO", cellNo.trim()).findAll();
            Log.e("tag", "chatlist" + chatsList);
            AdapterChatStatic adapterChat = new AdapterChatStatic(ChatScreen.this, chatsList);
            listView.setAdapter(adapterChat);
            listView.setStackFromBottom(true);

        } else {
        }
        jsonVarified();

        jsonCellDetails();

        jsonAccountDetails();

        listView.setOnScrollListener(ChatScreen.this);
        listener();


        InterfaceListener.setmOnNotificationReceived(this);
        jsonInbox();
        putActivity();
        Fabric.with(this, new Crashlytics());
//        jsonUploadImage();


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (net.equals("yes") && imgUpload.equals("true")) {
                            chatMessage = adapter_chat_msg.get(i);
                            String s1 = "http://smsimg.co/";
                            if (chatMessage.contains(s1)) {
                                dialogImageShow();
                            }
                        } else {
//                            Toast.makeText(mContext, "Loading...", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    @Override
    protected void onPause() {
        super.onPause();
        saveActivity();
//        Toast.makeText(ChatScreen.this, "on pause", Toast.LENGTH_SHORT).show();
    }

    public void saveActivity() {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferenceConstants.ACTIVITY, "OtherScreen");
        editor.apply();
    }

    public void putActivity() {
        //this method is used for the broadcasting
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferenceConstants.ACTIVITY, "ChatScreen");
        editor.apply();
    }

    private Realm getmRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(ChatScreen.this).build();

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

    public void listener() {
//        dollor.setOnClickListener(this);
        sendImage.setOnClickListener(this);
//        ivCall.setOnClickListener(this);
        firstImage.setOnClickListener(this);
        b_send_msg.setOnClickListener(this);
//        iv_template.setOnClickListener(this);
//        iv_comment.setOnClickListener(this);
//        iv_web.setOnClickListener(this);
        line1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == line1) {
            Toast.makeText(ChatScreen.this, "This function will be enabled in Version 2.0", Toast.LENGTH_SHORT).show();
        }


//        if (view == dollor) {
//            if (net.equals("yes") && !cellDetail.equals("null")) {
//
//                if (account_on_file.equals("") || account_on_file.isEmpty()) {
//                    Toast.makeText(ChatScreen.this, "Not Available", Toast.LENGTH_SHORT).show();
//
//
//                } else if (accountLength <= 0) {
//                    Toast.makeText(ChatScreen.this, "Not Available.", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    dialogMoneyTransfer();
//                    spinner.setAdapter(new ArrayAdapter<String>(ChatScreen.this, android.R.layout.simple_spinner_dropdown_item, adapter_spinner));
//                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                        @Override
//                        public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                                   int arg2, long arg3) {
//                            // TODO Auto-generated method stub
//
//                            token_from_acount = token_list.get(arg2);
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> arg0) {
//                            //
//                            //
//                            //
//                            //
//                            // TODO Auto-generated method stub
//
//                        }
//                    });
//                }
//
//            } else {
//                Toast.makeText(ChatScreen.this, "Not Available", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }


        if (view == sendImage) {
//            Toast.makeText(ChatScreen.this, "Upcoming Feature.", Toast.LENGTH_SHORT).show();
            ChatScreenPermissionsDispatcher.chooseImageWithCheck(this);

//            chooseImage();
        }
//        if (view == ivCall) {
//            if (net.equals("yes") && !cellDetail.equals("null")) {
//                if (call.equals("") || call.isEmpty()) {
//
//
//                    Toast.makeText(ChatScreen.this, "Not available", Toast.LENGTH_SHORT).show();
//
//
//                } else {
//                    Log.e("tag", "call no is " + call);
//
//                    if (isReadStorageAllowed()) {
//                        //If permission is already having then showing the toast
//
////                        Toast.makeText(MainActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
//                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
//
//                        callIntent.setData(Uri.parse("tel:" + Uri.encode(call.trim())));
//                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(callIntent);
//                        //Existing the method with return
//                        return;
//                    }
//
//                    //If the app has not the permission then asking for the permission
//                    requestStoragePermission();
//
//
//                }
//            } else {
//                Toast.makeText(ChatScreen.this, "Cell Not Found", Toast.LENGTH_SHORT).show();
//            }


//        }
        if (view == firstImage) {
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        }
        if (view == b_send_msg) {


            message = et_message.getText().toString();
            if (message.equals("")) {
                Toast.makeText(ChatScreen.this, "Please type a message", Toast.LENGTH_SHORT).show();
            } else {
                et_message.setText("");

                if (net.equals("yes")) {
                    /*Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String strDate = sdf.format(c.getTime());
                    String[] separated = strDate.split(" ");
                    String date = separated[0].trim();
*/
                    model = new ModelChat();
                    model.setMessage(message.trim());

                    /**
                     * Will return current date and time
                     */

                    String currentdate = getCurrentDate();
                    Log.e("tag", "current date" + currentdate);
//String cust_live_update=UtcTimeToLocalTime(currentdate);
                    model.setMsgSentTIme(currentdate.trim());

                    model.setDirection("out".trim());
                    model.setComment(comment.trim());
                    rowItems.add(model);

                    jsonSendMsg();
                    adapter.notifyDataSetChanged();
                    listView.setSelection(rowItems.size() - 1);
                } else {
                }

            }

        }
//        if (view == iv_comment) {
//
//            if (comment_count == 0) {
//                iv_comment.setAlpha((float) .5);
//                comment = "true";
//                comment_count = 1;
//            } else {
//                iv_comment.setAlpha((float) 1);
//                comment = "false";
//                comment_count = 0;
//
//            }
//        }
//        if (view == iv_template) {
//            if (tempLength.equals("null")) {
//                Toast.makeText(ChatScreen.this, "Templates are not available", Toast.LENGTH_SHORT).show();
//            } else {
//                dialogForgot();
//            }
//
//
//        }
//        if (view == iv_web) {
////            if (net.equals("yes") && !cellDetail.equals("null")) {
////
////                if (web.equals("") || web.isEmpty()) {
////                    Toast.makeText(ChatScreen.this, "Not available", Toast.LENGTH_SHORT).show();
////
////
////                } else {
////                    et_message.setText(web);
////                }
////            } else {
////                Toast.makeText(ChatScreen.this, "Not Available", Toast.LENGTH_SHORT).show();
////            }
//            Toast.makeText(ChatScreen.this, "Coming Soon" +
//                    "", Toast.LENGTH_SHORT).show();
//        }
    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Log.d("tag", "data issssssss" + simpleDateFormat);
        Date date1 = c.getTime();
        return simpleDateFormat.format(date1);
    }

    @Override
    public void onBackPressed() {
        if (goBackLoc.equals("push")) {
            Intent intent = new Intent(ChatScreen.this, HomeActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SharedPreferenceConstants.GO_BACK_LOC, "inbox");
            editor.apply();
        } else {
            super.onBackPressed();
        }


        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void jsonInbox() {
//        ViewUtil.showProgressDialog(getActivity());

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("agent", "Textmaxx mobile app v0.0.1beta");

            requestBody = jsonBody.toString();

            Log.e("tag", "Send message" + requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = GlobalConstants.BASE_URL + "/ui/" + cellNo;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response Inbox" + response);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                String token = prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim();
                Log.e("tag", "CLIENT_USER_TOKEN is " + token);
                headers.put("Authorization", "Basic " + token.trim());
                headers.put("Accept", "application/json");
//                headers.put("cell", cellNo);

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


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void dialogMoneyTransfer() {
        dialog_money = new Dialog(ChatScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog_money.setContentView(R.layout.cust_chatscreen_moneytransfer);
        dialog_money.setCancelable(false);
        spinner = (Spinner) dialog_money.findViewById(R.id.spinner);

        et_payment_amount = (EditText) dialog_money.findViewById(R.id.et_payment_amount);
        btn_send_payment = (Button) dialog_money.findViewById(R.id.btn_send_payment);
        Button b_cancel = (Button) dialog_money.findViewById(R.id.b_cancel);
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_money.dismiss();
            }
        });
        btn_send_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_payment_amount.getText().toString().equals("") || et_payment_amount.getText().toString().equals("0")) {
                    Toast.makeText(ChatScreen.this, "Please Enter The Amount", Toast.LENGTH_SHORT).show();
                } else {
                    jsonMoneyTransfer();
                }

            }
        });


        ImageView img_cancel = (ImageView) dialog_money.findViewById(R.id.img_cancel);
        // if button is clicked, close the cust_forgot
        // _pas dialog
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_money.dismiss();

            }
        });

        dialog_money.show();


    }

    public void jsonAccountDetails() {

//api name Accounts
        String url = GlobalConstants.BASE_URL + "/accounts/" + cellNo.trim();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response accounts " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("accounts");
                            accountLength = jsonArray.length();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject obj_acount = jsonArray.getJSONObject(i);
                                String label = obj_acount.getString("label");
                                token_from_acount = obj_acount.getString("token");
                                String type = obj_acount.getString("type");

                                token_list.add(token_from_acount);


                                adapter_spinner.add(label);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q=");
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, "").trim());


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


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void jsonMoneyTransfer() {

        dialog_money.show();

        //api name pay
        String url = GlobalConstants.BASE_URL + "/pay/" + cellNo.trim();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token_from_acount);
            jsonBody.put("amount", et_payment_amount.getText().toString());
            requestBody2 = jsonBody.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ChatScreen.this, "Payment Sent", Toast.LENGTH_LONG).show();
                        Log.e("tag", "Response Fund transfer " + response);
                        dialog_money.dismiss();

                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());

                headers.put("Accept", "application/json");
//                headers.put("Accept-Language", "en");

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
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody2 == null ? null : requestBody2.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody2, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void jsonSendMsg() {

//api name Accounts
        ViewUtil.showProgressDialog(ChatScreen.this);

        String url = GlobalConstants.BASE_URL + "/send/" + cellNo.trim();


        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("message", message);
//            jsonBody.put("sender", "Android");
//            jsonBody.put("marketing", "true");
//            jsonBody.put("comment", comment);

            requestBody = jsonBody.toString();

            Log.e("tag", "Send message" + requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response Message sent " + response);
                        ViewUtil.hideProgressDialog();
                        comment = "false";
//                        iv_comment.setAlpha((float) 1);
                        comment_count = 0;
                        messageSendCount = 1;
                        jsonChat();
                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ViewUtil.hideProgressDialog();
//                        error.getStackTrace();
//                        Log.d("Error.Response", error.toString());

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        } else {
                            txt_errorr = "" + new String(error.networkResponse.data).split(":")[1].replace("{", "").replace("}", "").replace("\"", "").replace("\"", "");
                            NoMessage();
                            Log.d("Error.4444", "volley error" + new String(error.networkResponse.data).split(":")[1]);
//                            Toast.makeText(ChatScreen.this, "" + new String(error.networkResponse.data).split(":")[1], Toast.LENGTH_LONG).show();
                            b_send_msg.setAlpha((float) .5);
                            b_send_msg.setClickable(false);
                            rowItems.remove(totalListCount - 1);
                            adapter.notifyDataSetChanged();
                        }

//                        if (error.networkResponse != null && error.networkResponse.data != null) {
//                            Log.d("Error.11111", "volley error" + error.getMessage());
//                            Log.d("Error.2222", "volley error" + error.getLocalizedMessage());
//
//                            Log.d("Error.3333", "volley error" + error.getLocalizedMessage());
//
//                            Log.d("Error.4444", "volley error" + new String(error.networkResponse.data).split(":")[1]);
//
//                        }

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                Log.e("tag", "client user token" + "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, ""));

                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
                headers.put("Accept", "application/json");
//                headers.put("X-Timezone-Offset", "-5");


                return headers;
            }


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }


        };


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void jsonChat() {


        String url = GlobalConstants.BASE_URL + "/messages/" + cellNo.trim() + "/20/0/after";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        ViewUtil.hideProgressDialog();


                        adapter_chat_msg.clear();

                        rowItems.clear();

                        clearRealmTable();

                        b_send_msg.setClickable(true);
                        b_send_msg.setAlpha((float) 1);


                        Log.e("tag", "Response Chat" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj2 = jsonArray.getJSONObject(i);
                                message = obj2.getString("message");
                                adapter_chat_msg.add(message);

                                direction = obj2.getString("direction");
                                commentJson = obj2.getString("c");
                                sent = obj2.getString("sent");
                                String id = obj2.getString("id");
                                adapter_msg_id.add(id);

                                model = new ModelChat();
                                model.setMessage(message);
                                model.setMsgSentTIme(sent);
                                model.setDirection(direction);
                                model.setComment(commentJson);
                                rowItems.add(model);

                                saveDataIntoRealm();
                                net = "yes";
                                imgUpload = "true";
                            }

                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            listView.setStackFromBottom(true);

                            messageId = adapter_msg_id.get(0);
                            Log.e("tag", "messageId id " + messageId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("tag", "error is is " + error);
//                        ViewUtil.hideProgressDialog();

                        if (error instanceof NoConnectionError) {
                            net = "no";
                            b_send_msg.setClickable(false);
                            b_send_msg.setAlpha((float) .5);

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, "").trim());


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


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void jsonCellDetails() {


        String url = GlobalConstants.BASE_URL + "/cell/" + cellNo.trim();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(ChatScreen.this, response, Toast.LENGTH_LONG).show();
                        Log.e("tag", "Response search" + response);
                        cellDetail = "respone";
                        try {
                            JSONObject jObj = new JSONObject(response);
                            web = jObj.getString("web");
                            call = jObj.getString("ivr");
                            account_on_file = jObj.getString("account_on_file");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());


                headers.put("Accept", "application/json");


                return headers;
            }


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


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void jsonGetTemplates() {
        list_temp.setAdapter(new AdapterTemplates(ChatScreen.this, mRealm.allObjects(ModelTempList.class)));

//api name Accounts
        String url = GlobalConstants.BASE_URL + "/templates/account/own/all";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        clearRealmTemp();
//                        Toast.makeText(ChatScreen.this, response, Toast.LENGTH_LONG).show();
                        Log.e("tag", "Response temp " + response);
//                        Toast.makeText(ChatScreen.this, "Response temp" + response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("templates");
                            if (jsonArray.length() != 0) {
                                tempLength = "Temp";
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj_temp = jsonArray.getJSONObject(i);
                                label = obj_temp.getString("label");
                                temp_id = obj_temp.getString("id");
                                String owned = obj_temp.getString("owned");

                                adapter_temp_id.add(temp_id);
                                modelTempList = new ModelTempList();

                                modelTempList.setTemp_id(temp_id);
                                modelTempList.setLabel(label);


                                rowItems_temp.add(modelTempList);

                                saveTempInRealm();

                            }
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("tempId_array", adapter_temp_id.toString());
                            editor.apply();
                            temp_adapter.notifyDataSetChanged();
                            list_temp.setAdapter(temp_adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {

                            String aa = prefs.getString("tempId_array", "");
                            String replace = aa.replace("[", "");
                            String replace1 = replace.replace("]", "");
                            List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
                            for (int i = 0; i < myList.size(); i++) {
                                adapter_temp_id.add(myList.get(i));
                            }


//                            listView.setAdapter(new ArrayAdapter<StaticModelTempList>(ChatScreen.this), mRealm.allObjects(StaticModelTempList.class)));

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, "").trim());


                headers.put("Accept", "application/json");

                headers.put("Accept-Language", "en");
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


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        list_temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                temp_id = adapter_temp_id.get(i);
                jsonTempDetail();
                dialog_temp.dismiss();
            }
        });

    }

//    public void dialogForgot() {
//        dialog_temp = new Dialog(ChatScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog_temp.setContentView(R.layout.cust_templates);
//        dialog_temp.setCancelable(false);
//
//        list_temp = (ListView) dialog_temp.findViewById(R.id.listview);
//
//        ImageView img_cross = (ImageView) dialog_temp.findViewById(R.id.img_cross);
//        jsonGetTemplates();
//        // if button is clicked, close the cust_forgot_pas dialog
//        img_cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_temp.dismiss();
//            }
//        });
//
//        dialog_temp.show();
//    }


    public void dialogImageShow() {

        dialog_temp = new Dialog(ChatScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog_temp.setContentView(R.layout.cust_show_image);
        dialog_temp.setCancelable(false);


        ImageView img_cross = (ImageView) dialog_temp.findViewById(R.id.img_cross);
        ImageView img_show = (ImageView) dialog_temp.findViewById(R.id.img_show);
//
//        Picasso.with(this)
//                .load(chatMessage).placeholder(R.drawable.loading).error(R.drawable.noimg).fit()
//                .into(img_show);

        Glide.with(this)
                .load(chatMessage)
                .placeholder(R.drawable.loading).error(R.drawable.noimg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_show);

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_temp.dismiss();
            }
        });
        dialog_temp.show();
//
//        if (img_show.getDrawable() != null) {
//            ViewUtil.hideProgressDialog();
//        }
    }

    public void jsonTempDetail() {


//api name Accounts
        String url = GlobalConstants.BASE_URL + "/template/" + temp_id.trim();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        clearTempMsg();
                        Log.e("tag", "Response temp " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            temp_msg = obj.getString("contents");
                            et_message.setText(temp_msg);

                            saveTempMsg();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";
                            RealmResults<ModelTempMsges> aa = mRealm.where(ModelTempMsges.class).equalTo("id", temp_id.trim()).findAll();


                            if (aa.isEmpty()) {
                                et_message.setText("");

                                Log.e("tag", "no data");
                                Toast.makeText(ChatScreen.this, "No Data Availale", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("tag", "data is " + aa.get(0).getMessage());
                                et_message.setText("");
                                et_message.setText(aa.get(0).getMessage());
                            }


                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, "").trim());


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


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (!mRealm.isClosed()) {
            // Do something
            mRealm.close();

        }

    }

    public void clearRealmTable() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ModelChat> rows = realm.where(ModelChat.class).equalTo("cellNO", cellNo.trim()).findAll();
                rows.clear();
            }
        });


    }

    public void clearTempMsg() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ModelTempMsges> rows = realm.where(ModelTempMsges.class).equalTo("id", temp_id).findAll();
                rows.clear();
            }
        });

    }

    public void clearRealmTemp() {
        mRealm.beginTransaction();
        mRealm.clear(ModelTempList.class);
        mRealm.commitTransaction();
    }

    public void saveDataIntoRealm() {
        mRealm.beginTransaction();
        ModelChat book = mRealm.createObject(ModelChat.class);

        book.setCellNO(cellNo.trim());
        book.setDirection(direction.trim());
        book.setComment(commentJson.trim());
        book.setMessage(message.trim());
        book.setMsgSentTIme(sent.trim());
        mRealm.commitTransaction();
    }

    public void saveTempInRealm() {
        mRealm.beginTransaction();
        ModelTempList book = mRealm.createObject(ModelTempList.class);
        book.setLabel(label.trim());
        book.setTemp_id(temp_id.trim());
        mRealm.commitTransaction();
    }

    public void saveTempMsg() {
        mRealm.beginTransaction();
        ModelTempMsges book = mRealm.createObject(ModelTempMsges.class);
        book.setId(temp_id.trim());
        book.setMessage(temp_msg.trim());
        mRealm.commitTransaction();
    }

    public void jsonChatOld() {

        String url = GlobalConstants.BASE_URL + "/messages/" + cellNo.trim() + "/20/" + messageId + "/before";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response old data" + response);
                        //clear and add data from db

                        chatsList2 = mRealm.where(ModelChat.class).equalTo("cellNO", cellNo.trim()).findAll();

                        //clear and add data from json
                        adapter_msg_id.clear();
                        rowItems_temporary.addAll(rowItems);
                        countItems = rowItems.size();
                        rowItems.clear();
                        adapter_chat_msg_temp.addAll(adapter_chat_msg);
                        adapter_chat_msg.clear();
                        //


                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj2 = jsonArray.getJSONObject(i);
                                message = obj2.getString("message");
                                direction = obj2.getString("direction");
                                comment = obj2.getString("c");
                                String sent = obj2.getString("utc");

                                String id = obj2.getString("id");
                                adapter_msg_id.add(id);
                                adapter_chat_msg.add(message);


                                model = new ModelChat();
                                model.setMessage(message);
                                model.setMsgSentTIme(sent);
                                model.setDirection(direction);
                                model.setComment(comment);

                                rowItems.add(model);

                            }
                            adapter.notifyDataSetChanged();
                            //

                            rowItems.addAll(rowItems_temporary);
                            adapter.notifyDataSetChanged();
                            rowItems_temporary.clear();
                            //mesgs save for url image
                            adapter_chat_msg.addAll(adapter_chat_msg_temp);
                            adapter_chat_msg_temp.clear();


                            //setting message id for loading previous msg
                            if (adapter_msg_id.size() == 0) {
//                                listView.setSelection(countItems-5);
                                if (countToast < 1) {
                                    Toast.makeText(ChatScreen.this, "No more data", Toast.LENGTH_LONG).show();
                                    //for old message check
                                    last_msg = false;


                                }

                                countToast++;
                            } else {

                                Log.d("tag", "else else else");
                                Log.e("tag", "Positoin" + Position);
                                if (Position == 0) {
//                                    adapter.notifyDataSetChanged();
//                                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

                                    listView.setStackFromBottom(false);
                                    listView.setAdapter(adapter);
                                    listView.setSelection((rowItems.size()) - (countItems + 1));


                                } else {

//                                    listView.setSelection(Position);
                                    Log.d("tag", "else else else 333");
                                }

                                messageId = adapter_msg_id.get(0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, "").trim());


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


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        hideKeyboard();

        if (i == SCROLL_STATE_IDLE) {
            if (listView.getFirstVisiblePosition() == 0) {

                if (last_msg == true) {
                    jsonChatOld();
                }


            }
        }
    }

    @Override
    public void onScroll(AbsListView lw, final int firstVisibleItem,
                         final int visibleItemCount, final int totalItemCount) {
        Log.d("tag", "positionnnn" + firstVisibleItem);
        Position = firstVisibleItem;
        totalListCount = totalItemCount;
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }


//    private void requestStoragePermissionCamera() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CALL_CODE);
//    }

//    private boolean isReadStorageAllowed() {
//
//        //Getting the permission status
//        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//
//        //If permission is granted returning true
//        if (result == PackageManager.PERMISSION_GRANTED)
//            return true;
//
//        //If permission is not granted returning false
//        return false;
//    }

    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//
//        //Checking the request code of our request
//        if (requestCode == CALL_CODE) {
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                //Displaying a toast
//                Toast.makeText(this, "Permission granted.", Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                startActivityForResult(intent, 7);
//
//                // start the image capture Intent
//                //startActivityForResult(intent, ACTION_IMAGE_CAPTURE);
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    public void jsonVarified() {
        ViewUtil.showProgressDialog(ChatScreen.this);

        Log.e("cell Chat", " cell chat is : " + cellNo);
        String url = GlobalConstants.BASE_URL + "/verified/" + cellNo;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        Log.e("tag", "Response Varified" + response);
//                        Toast.makeText(getActivity(), "Response Contacts" + response, Toast.LENGTH_SHORT).show();


                        try {
                            JSONObject obj = new JSONObject(response);
                            String confirmed = obj.getString("confirmed");

                            varifiedNumber = obj.getString("verified");
//                            Log.e("Tag", "varified varified variffied " + varifiedNumber);

                            if (varifiedNumber.equals("true")) {
                                txt_varified.setVisibility(View.GONE);

                                jsonChat();
                                if (net.equals("yes")) {
                                    b_send_msg.setClickable(true);
                                    b_send_msg.setAlpha((float) 1);
                                } else {
                                    b_send_msg.setClickable(false);
                                    b_send_msg.setAlpha((float) .5);
                                }
                            } else {
                                b_send_msg.setClickable(false);
                                b_send_msg.setAlpha((float) .5);
                                txt_varified.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ViewUtil.hideProgressDialog();

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ViewUtil.hideProgressDialog();

                        Log.e("tag", "error is is " + error);

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q6RDhvN1VlNHFhOGclM2Q=");
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, "").trim());


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


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    @Override
    public void Notificationreceived() {
//        Toast.makeText(ChatScreen.this, "Intent Detected.__7777", Toast.LENGTH_LONG).show();
        jsonChat();
    }

    public void NoMessage() {
        final Dialog dialog = new Dialog(ChatScreen.this, android.R.style.Theme_Translucent_NoTitleBar);

        dialog.setContentView(R.layout.cust_notime);
        dialog.setCancelable(false);

        ImageView img_cross_forgot = (ImageView) dialog.findViewById(R.id.img_cross_forgot);
        TextView txt_errorr2 = (TextView) dialog.findViewById(R.id.txt_errorr);
        txt_errorr2.setText(txt_errorr);
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void saveGoBack() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferenceConstants.GO_BACK_LOC, "");

        editor.apply();
    }

    @NeedsPermission(Manifest.permission.CAMERA)


    public void chooseImage() {


        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatScreen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 7);
                } else if (items[item].equals("Choose from Library")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }
//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatScreenPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmap = getResizedBitmap(bitmap, 800);
//                Bitmap bitmap = resizeBitmap(selectedImage, ChatScreen.this);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                        Base64.NO_WRAP);
                String encodedurl = URLEncoder.encode(imgString, "UTF-8");
                Log.e("tag", "bitmap 222 " + bitmap);
                imageBase64 = encodedurl;
                jsonUploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        if (requestCode == 7 && resultCode == RESULT_OK) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo = getResizedBitmap(photo, 800);
                String imgString = Base64.encodeToString(getBytesFromBitmap(photo),
                        Base64.NO_WRAP);
                String encodedurl = URLEncoder.encode(imgString, "UTF-8");
                Log.e("tag", "encodedurl2222222 " + encodedurl);
                imageBase64 = encodedurl;
                jsonUploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }

    //    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }
//    public static Bitmap resizeBitmap(Uri selectedImage, Context context) throws FileNotFoundException {
//        //BitmapFactory.Options o = new BitmapFactory.Options();
//        /*o.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;*/
//        int scale = 16;
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        o2.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
//        Log.e("tag", "bitmap is " + bitmap);
////                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
//
//
//        return bitmap;
//    }

    public void jsonUploadImage() {
        ViewUtil.showProgressDialog(this);
        String url = GlobalConstants.BASE_URL + "/file/store";
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("file_name", "image.png");
            jsonBody.put("data", imageBase64);
            requestBody = jsonBody.toString();
            Log.e("tag", "Send message" + requestBody);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ViewUtil.hideProgressDialog();

                        imgUpload = "false";
                        Log.e("tag", "Response Message sent " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            String url = obj.getString("url");
                            if (url != null || !url.equals("")) {
                                message = url;
                                jsonSendMsg();
                            } else {
                                Toast.makeText(ChatScreen.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

                            }
//                            et_message.setText(url);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
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

                            Toast.makeText(ChatScreen.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//                    responseString = String.valueOf(response.statusCode);
//                    Log.e("tag", "res222" + responseString);
//                    // can get more details such as response.headers
//
//
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//
//            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(ChatScreen.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //
    }

//    public void converClientUserIntoBase() {
//        byte[] data1;
//
//        String str = picturePath;
//
//
//        try {
//
//            data1 = str.getBytes("UTF-8");
//
//            String aaaa = Base64.encodeToString(data1, Base64.DEFAULT);
//            imageBase64 = URLEncoder.encode(aaaa, "utf-8");
//
//            Log.e("Base client user 64 ", imageBase64);
//
//        } catch (UnsupportedEncodingException e) {
//
//            e.printStackTrace();
//
//        }
//
//    }

}