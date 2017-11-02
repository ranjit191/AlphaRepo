package com.textmaxx.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.realm.models.ModelMyAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class MyAccount extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.tv_login)
    EditText tv_login;

//    @Bind(R.id.tv_pasw)
//    EditText tv_pasw;

    @Bind(R.id.tv_email)
    EditText tv_email;
//
//    @Bind(R.id.tv_cellNo)
//    EditText tv_cellNo;

    @Bind(R.id.tv_fistName)
    EditText tv_fistName;

    @Bind(R.id.tv_lastName)
    EditText tv_lastName;

    @Bind(R.id.b_update)
    Button b_update;

    @Bind(R.id.firstImage)
    ImageView firstImage;

    Realm mRealm;
    SharedPreferences prefs;
    String user_id, requestBody, first_name, last_name, cell, email_address, login, clientToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_account);

        ButterKnife.bind(this);
        TextView txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("MyAccount");
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getString(SharedPreferenceConstants.ID, "").trim();
        Log.e("tag", "id is " + user_id);
        clientToken = prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, "");

//        GlobalConstants.ID = user_id;


        firstImage.setVisibility(View.VISIBLE);
        firstImage.setImageResource(R.drawable.left_icon);
//        firstImage.setPadding(0, 10, 10, 10);
        mRealm = getmRealm();
        jsonUserInfo();
        b_update.setOnClickListener(this);
        firstImage.setOnClickListener(this);
    }

    public void jsonUserInfo() {
        getData();
        String url = GlobalConstants.BASE_URL + "/user/" + user_id.trim();
//        String url = GlobalConstants.BASE_URL + "/user/4473";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        clearTempMsg();
//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_LONG).show();
                        Log.e("tag", "Response User INfo" + response);
//                        Toast.makeText(getActivity(), "Response Contacts" + response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            login = jObj.getString("login");
                            email_address = jObj.getString("email_address");
                            first_name = jObj.getString("first_name");
                            last_name = jObj.getString("last_name");
                            cell = jObj.getString("cell");

                            tv_fistName.setText(first_name);
                            tv_lastName.setText(last_name);
                            tv_email.setText(email_address);
                            tv_login.setText(login);
//                            tv_cellNo.setText(cell);

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
                            getData();


                            Toast.makeText(MyAccount.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q6RDhvN1VlNHFhOGclM2Q=");
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
                Log.e("tag", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, ""));

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


        RequestQueue requestQueue = Volley.newRequestQueue(MyAccount.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    @Override
    public void onClick(View view) {
        if (view == b_update) {


//            jsonUpdateInfo();
            jsonUploadImage();
        }
        if (view == firstImage) {

            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
    }

    private Realm getmRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(MyAccount.this).build();

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


    public void clearTempMsg() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ModelMyAccount> rows = realm.where(ModelMyAccount.class).equalTo("UserId", user_id).findAll();
                rows.clear();

            }
        });

    }

    public void saveTempMsg() {
        mRealm.beginTransaction();
        ModelMyAccount book = mRealm.createObject(ModelMyAccount.class);
        book.setUserId(user_id.trim());
        book.setFirstName(first_name);
        book.setLastName(last_name);
        book.setCellNo(cell);
        book.setEmail(email_address);
        book.setUsername(login);
        mRealm.commitTransaction();
    }

    public void getData() {

        RealmResults<ModelMyAccount> aa = mRealm.where(ModelMyAccount.class).equalTo("UserId", user_id.trim()).findAll();

        Log.e("tag", "data" + aa);

        if (aa.isEmpty()) {

            Log.e("tag", "no data");
        } else {
            tv_fistName.setText(aa.get(0).getFirstName());
            tv_lastName.setText(aa.get(0).getLastName());
//            tv_cellNo.setText(cust_live_update.get(0).getCellNo());
            tv_login.setText(aa.get(0).getUsername());
            tv_email.setText(aa.get(0).getEmail());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (!mRealm.isClosed()) {
            // Do something
            mRealm.close();
        }

    }


    public void jsonUploadImage() {
//api name Accounts
//        String url = "https://sandbox.paymaxxpro.com/sms/user/700";
        String url = GlobalConstants.BASE_URL + "/user/" + user_id.trim();
//        converClientUserIntoBase();


//        String url = "https://sandbox.paymaxxpro.com/sms/file/store";

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("first_name", tv_fistName.getText().toString());
            jsonBody.put("last_name", tv_lastName.getText().toString());
            jsonBody.put("login",tv_login.getText().toString() );
            jsonBody.put("email_address", tv_email.getText().toString());
            requestBody = jsonBody.toString();

            Log.e("tag", "Send message" + requestBody);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response Message sent " + response);
                        Toast.makeText(MyAccount.this, "Updated", Toast.LENGTH_SHORT).show();
                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(MyAccount.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

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


        RequestQueue requestQueue = Volley.newRequestQueue(MyAccount.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //
    }


}
