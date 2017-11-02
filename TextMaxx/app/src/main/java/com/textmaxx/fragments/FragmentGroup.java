package com.textmaxx.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.textmaxx.Interfaces.ImageClickListenerInterface;
import com.textmaxx.Interfaces.ImageClickListenerInterfaceRel;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.app.Information;
import com.textmaxx.listview_adapter.AdapterGroup;
import com.textmaxx.listview_adapter.AdapterHome;
import com.textmaxx.models.ModelTabGrps;
import com.textmaxx.models.ModelTabHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class FragmentGroup extends Fragment implements ImageClickListenerInterface, ImageClickListenerInterfaceRel {


    static EditText et_confirm_cell;
    List<String> array_tags = new ArrayList<String>();
    Boolean hyphenExists;
    SwipeMenuListView listView;
    AdapterGroup adapter;
    ModelTabGrps model;
    List<ModelTabGrps> rowItems = new ArrayList<ModelTabGrps>();
    ImageView imgBackg;
    Realm mRealm;
    SharedPreferences prefs;
    List<String> array_name = new ArrayList<String>();
    int len;
    int positionDeleted;
    String cellNo;
    String alert = "";
    private String clicked = "false";
    //    private ImageView secondImage;
    private TextView txt_title;
    private TextView tv_nomsg, txtAlert;
    private String label, tag_message, message_broadcast, message, cell, count, sentTime, cell_manual, account, marketing, Language = "", requestBody;
    private Context mContext;
    private Dialog dialogConfirmCell, dialog_varify;
    private TextView lang, tv_cell_no;
    private EditText et_cell_name;
    private Dialog dialog_Broad_msg;
    private RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fraghome, viewGroup, false);
        mContext = getActivity();
        prefs = mContext.getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        mRealm = getmRealm();
//        mRealm = Realm.getInstance(getActivity());
//destroy view 111
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
//destroy view 2222


        //
////        Realm.getInstance(FragmentGroup.this.getActivity());
//        RealmConfiguration realmConfiguration = new RealmConfiguration
//                .Builder(getActivity())
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        //


//        txtAlert = (TextView) getActivity().findViewById(R.id.txt_alert);
//        imgBackg = (ImageView) getActivity().findViewById(R.id.img_backg);
//        View txtAlert = getActivity().findViewById(R.id.txt_alert);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        tv_nomsg = (TextView) view.findViewById(R.id.tv_nomsg);
        txt_title.setText("Group");
        listView = (SwipeMenuListView) view.findViewById(R.id.listHome);
//        secondImage = (ImageView) view.findViewById(R.id.secondImage);

//        secondImage.setImageResource(R.drawable.add_icon);
//        initControls();

        //


        //


//        secondImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                confirmCell();
//
//            }
//        });
//        rowItems = new ArrayList<ModelTabHome>();

        InterfaceListener.setImageClickListenerInterface(this);
        InterfaceListener.setImageClickListenerInterfaceRel(this);

        adapter = new AdapterGroup(view.getContext(), rowItems) {
        };


//        saveCellArray();
        listView.setAdapter(new AdapterGroup(getActivity(), mRealm.allObjects(ModelTabGrps.class)));


      loadTags();



//        jsonHome();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                saveTags();
                if (array_tags.size() != 0) {
                    tag_message = array_tags.get(i);
                }

//                Toast.makeText(getActivity(), "tag is " + array_tags.get(i), Toast.LENGTH_SHORT).show();


                dialogSendMessage();

            }
        });

//        saveCellArray();
        return view;
    }

    private void loadTags() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jsonGetTags();
            }
        }, 1500);


    }

    public void dialogSendMessage() {

        dialog_Broad_msg = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog_Broad_msg.setContentView(R.layout.cust_send_braodcast_msg);
        dialog_Broad_msg.setCancelable(false);


        Button btn_send_tag = (Button) dialog_Broad_msg.findViewById(R.id.btn_send_tag);
        final EditText message_ = (EditText) dialog_Broad_msg.findViewById(R.id.et_tag_msg);

        ImageView img_cross_forgot = (ImageView) dialog_Broad_msg.findViewById(R.id.img_cross_forgot);


        btn_send_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message_broadcast = message_.getText().toString();
                if (message_broadcast.equals("")) {
                    Toast.makeText(getActivity(), "Please type a message..", Toast.LENGTH_SHORT).show();
                } else {

                    jsonSendMsg();
                }


//                Toast.makeText(getActivity(), "send send", Toast.LENGTH_SHORT).show();

            }
        });
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Broad_msg.dismiss();

            }
        });

        dialog_Broad_msg.show();
    }

    public void jsonSendMsg() {
//api name Accounts

        String url = GlobalConstants.BASE_URL + "/broadcast";


        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("message", message_broadcast);
            jsonBody.put("language", "en");
            jsonBody.put("account", "true");
            jsonBody.put("marketing", "false");
            jsonBody.put("notify", "email@example.com");
            jsonBody.put("tags", tag_message);


            requestBody = jsonBody.toString();
            Log.e("tag", "1111111" + message_broadcast);
//            Log.e("tag", "2222222" + tag_message);

            Log.e("tag", "Send message" + requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response Message sent " + response);
                        Toast.makeText(getActivity(), "Sent", Toast.LENGTH_SHORT).show();
                        dialog_Broad_msg.dismiss();

                    }

                    ///////////up
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
//                                        error.toString() = "No internet Access, Check your internet connection.";
//                                        error.toString() = "No internet Access, Check your internet connection.";

                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                Log.e("tag", "client user token" + "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, ""));

//                headers.put("Authorization", "Basic QnpLJTJmTCUyYnlxRVVzJTNkOldMM2ROUzhTMExJJTNk");

                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");

//                headers.put("Accept-Encoding", "gzip, deflate");
//                headers.put("User-Agent", "runscope/0.1");
//                headers.put("Content-Length", "152");

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

    private Realm getmRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(mContext).build();

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
    public void ImageClick(ImageView imageView) {
        Fragment fragment = new Information();
        FragmentManager manager = getFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.output, fragment);
        transaction.commit();

    }

    @Override
    public void ImageClickRel(RelativeLayout imageView) {
        Fragment fragment = new Information();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.output, fragment);
        transaction.commit();
    }


    public void jsonGetTags() {


        listView.setAdapter(new AdapterGroup(getActivity(), mRealm.allObjects(ModelTabGrps.class)));


//        ViewUtil.showProgressDialog(getActivity());
        String url = GlobalConstants.BASE_URL + "/tags";
//        String url = "https://api.paymaxxpro.com/sms/ui";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tag", "Response Home" + response);
                        array_tags.clear();
                        clearRealmTable();
//                        Toast.makeText(getActivity(), " tags " + response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("tags");
                            {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj2 = jsonArray.getJSONObject(i);
                                    label = obj2.getString("tag");
                                    array_tags.add(label);
                                    Log.e("tag", "tag is " + label);

                                    model = new ModelTabGrps();

                                    model.setName(label);
//                                    model.setMessage(message);
//                                    model.setCount(count);
//                                    model.setSentTime(sentTime);
                                    rowItems.add(model);
                                    saveDataIntoRealm();
                                }
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);

                            }

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("array_tags", array_tags.toString());
                            editor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        ViewUtil.hideProgressDialog();

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {

                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                Log.e("tag", "CLIENT_TOKEN is " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, ""));
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
//                Tnk2MEZLcVQ1bzAlM2Q=
//                headers.put("X-Mobility-Id", "25e7c16d5ee178519016b4aa12d5b8b8a9b80ad0");


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


//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        mRequestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//        Toast.makeText(getActivity(), "data lenght 2   " + data, Toast.LENGTH_SHORT).show();


    }

    public void saveTags() {
        array_tags.clear();
        String aa = prefs.getString("array_tags", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");

        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int j = 0; j < myList.size(); j++) {
            array_tags.add(myList.get(j));
        }
    }

    public void saveDataIntoRealm() {
        mRealm.beginTransaction();
        ModelTabGrps book = mRealm.createObject(ModelTabGrps.class);
        book.setName(label.trim());
//        book.setLabel(message.trim());
        mRealm.commitTransaction();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (!mRealm.isClosed()) {
            // Do something
            mRealm.close();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mRealm = getmRealm();
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (clicked.equals("true")) {
//            jsonHome();
//
//        }
    }

    public void clearRealmTable() {
        mRealm.beginTransaction();
        mRealm.clear(ModelTabHome.class);
        mRealm.commitTransaction();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    ////////

    ///destryou view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

}