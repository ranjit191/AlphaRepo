package com.textmaxx.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.CommonUtils;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.fragments.FragmentContacts;
import com.textmaxx.fragments.FragmentGroup;
import com.textmaxx.fragments.FragmentHome;
import com.textmaxx.fragments.FragmentInbox;
import com.textmaxx.realm.models.ModelCellInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

//import com.textmaxx.fragments.FindUser;

public class Information extends Fragment {
    SharedPreferences prefs;
    String infoPage;
    Fragment fragment;
    Realm mRealm;
    //    RelativeLayout rel_tag;
    ImageView img_account, edit;
    ImageView img_market;
    String requestBody;
    private TextView tv_cellno, title, tv_name, tv_tags, tv_language;
    private ImageView firstImage, secondImage;
    private String cellNo = "";
    private LinearLayout line_tags;
    private String label, accepts_market, accepts_account, tags, language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information, viewGroup, false);
        prefs = getActivity().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        title = (TextView) view.findViewById(R.id.txt_title);
        title.setText("Information");
        mRealm = getmRealm();

//        rel_tag = (RelativeLayout) view.findViewById(R.id.rel_tag);

        tv_cellno = (TextView) view.findViewById(R.id.tv_cellno);

        tv_name = (TextView) view.findViewById(R.id.tv_name);
        img_account = (ImageView) view.findViewById(R.id.img_account);
        img_market = (ImageView) view.findViewById(R.id.img_market);
//        edit = (ImageView) view.findViewById(R.id.edit);
//        edit.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                jsonInfsso();
//            }
//        });
        tv_language = (TextView) view.findViewById(R.id.tv_language);

        firstImage = (ImageView) view.findViewById(R.id.firstImage);

        prefs = getActivity().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);

        cellNo = prefs.getString("chat_cellno", "");
//        cellNo = prefs.getString("chat_cellno", "").trim();
        firstImage.setImageResource(R.drawable.left_icon);
        firstImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                infoPage = prefs.getString(SharedPreferenceConstants.infoPage, "");
                if (infoPage.equals("info_home")) {

                    fragment = new FragmentGroup();

                } else if (infoPage.equals("info_inbox")) {

                    fragment = new FragmentInbox();

                } else if (infoPage.equals("info_contacts")) {

                    fragment = new FragmentContacts();
//                    fragment = new ContactsFrag();

                } else if (infoPage.equals("info_home2")) {

                    fragment = new FragmentHome();


                }

                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                CommonUtils.replaceFragment(fragment, getFragmentManager());

            }
        });
        jsonInfo();
//        jsonInfsso();
//        setHeader();

        tv_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    jsonInfsso();
                }
                return false;
            }
        });
        return view;


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

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (!mRealm.isClosed()) {
            // Do something
            mRealm.close();
        }

    }

    public void jsonInfo() {

//        Toast.makeText(getActivity(), "cell is"+cellNo, Toast.LENGTH_SHORT).show();
        setDBData();
        String url = GlobalConstants.BASE_URL + "/cell/" + cellNo;


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
                            label = jObj.getString("label");
//                            String ivr = jObj.getString("ivr");
                            accepts_market = jObj.getString("accepts_market");
                            accepts_account = jObj.getString("accepts_account");
                            tags = jObj.getString("tags");
                            language = jObj.getString("language");

                            String cellNumber = cellNo;
                            Log.e("tag", "cell is:::: " + cellNumber);
                            String asubstring = cellNumber.substring(0, 1);

                            if (asubstring.equals("1")) {

                                String cell = cellNumber.substring(1);

                                String st = new String(cell);
                                st = new StringBuffer(st).insert(3, "-").toString();
                                st = new StringBuffer(st).insert(7, "-").toString();
                                tv_cellno.setText(st);
                            } else {
//                Toast.makeText(getActivity(), "222222", Toast.LENGTH_SHORT).show();
                                String st = new String(cellNumber);
                                st = new StringBuffer(st).insert(3, "-").toString();
                                st = new StringBuffer(st).insert(7, "-").toString();
                                tv_cellno.setText(st);


                            }
                            tv_name.setText(label);


                            if (accepts_account.equals("true")) {
                                img_account.setImageResource(R.drawable.okay);

                            } else {
                                img_account.setImageResource(R.drawable.no);

                            }
                            if (accepts_market.equals("true")) {
                                img_market.setImageResource(R.drawable.okay);

                            } else {
                                img_market.setImageResource(R.drawable.no);
                            }
//                            if (tags.equals("null") || tags.isEmpty()) {
//                                rel_tag.setVisibility(View.GONE);
//                            } else {
//                                rel_tag.setVisibility(View.VISIBLE);
//                                tv_tags.setText(tags);
//                            }

                            if (language.equals("en")) {
                                tv_language.setText("English");

                            } else {
                                tv_language.setText("Spanish");
                            }

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


                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q6RDhvN1VlNHFhOGclM2Q=");
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());


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

    public void jsonInfsso() {

        String url = GlobalConstants.BASE_URL + "/cell/" + cellNo;

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("label", tv_name.getText().toString().trim());

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
                        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
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


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    public void clearTempMsg() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ModelCellInfo> rows = realm.where(ModelCellInfo.class).equalTo("cellNo", cellNo).findAll();
                rows.clear();
            }
        });

    }

    public void saveTempMsg() {
        mRealm.beginTransaction();
        ModelCellInfo book = mRealm.createObject(ModelCellInfo.class);

        /*mRealm.copyToRealm(book);
        mRealm.copyToRealmOrUpdate(book);*/

        book.setCellNo(cellNo.trim());
        book.setName(label.trim());
        book.setAcceptMarket(accepts_market.trim());
        book.setAcceptsAccount(accepts_account.trim());
        book.setLanguage(language.trim());
        book.setTags(tags.trim());
        mRealm.commitTransaction();
    }

    public void setDBData() {

        RealmResults<ModelCellInfo> aa = mRealm.where(ModelCellInfo.class).equalTo("cellNo", cellNo.trim()).findAll();


        if (aa.isEmpty() || aa == null || aa.equals("null") || aa.equals("")) {
//                                et_message.setText("");

            Log.e("tag", "no data");
//            Toast.makeText(getActivity(), "No Data Availale", Toast.LENGTH_SHORT).show();
        } else {


            String cellNumber = aa.get(0).getCellNo();
            Log.e("tag", "cell is:::: " + cellNumber);
            String asubstring = cellNumber.substring(0, 1);

            if (asubstring.equals("1")) {

                String cell = cellNumber.substring(1);

                String st = new String(cell);
                st = new StringBuffer(st).insert(3, "-").toString();
                st = new StringBuffer(st).insert(7, "-").toString();
                tv_cellno.setText(st);
            } else {
//                Toast.makeText(getActivity(), "222222", Toast.LENGTH_SHORT).show();
                String st = new String(cellNumber);
                st = new StringBuffer(st).insert(3, "-").toString();
                st = new StringBuffer(st).insert(7, "-").toString();
                tv_cellno.setText(st);
            }
//            Log.e("tag", "asubstring is:::: " + asubstring);
//            Log.e("tag", "cell is:::: " + cell);

            tv_name.setText(aa.get(0).getName());


            if ((aa.get(0).getLanguage()).equals("en")) {
                tv_language.setText("English");

            } else {
                tv_language.setText("Spanish");
            }

//            tv_language.setText(cust_live_update.get(0).getLanguage());


            if (aa.get(0).getAcceptMarket().equals("true")) {
                img_market.setImageResource(R.drawable.okay);

            } else {
                img_market.setImageResource(R.drawable.no);

            }
            if (aa.get(0).getAcceptsAccount().equals("true")) {
                img_account.setImageResource(R.drawable.okay);


            } else {
                img_account.setImageResource(R.drawable.no);
            }
//            if (cust_live_update.get(0).getTags().equals("null")) {
//                rel_tag.setVisibility(View.GONE);
//            } else {
//                rel_tag.setVisibility(View.VISIBLE);
//                tv_tags.setText(cust_live_update.get(0).getTags());
//            }

        }

    }
}
