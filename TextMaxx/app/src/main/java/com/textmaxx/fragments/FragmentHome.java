package com.textmaxx.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.textmaxx.Interfaces.ImageClickListenerInterface;
import com.textmaxx.Interfaces.ImageClickListenerInterfaceRel;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.Utils.ViewUtil;
import com.textmaxx.app.ChatScreen;
import com.textmaxx.app.Information;
import com.textmaxx.listview_adapter.AdapterHome;
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

public class FragmentHome extends Fragment implements ImageClickListenerInterface, ImageClickListenerInterfaceRel {

    static EditText et_confirm_cell;
    List<String> array_cellno = new ArrayList<String>();
    String clicked = "false";
    Boolean hyphenExists;
    SwipeMenuListView listView;
    AdapterHome adapter;
    ModelTabHome model;
    List<ModelTabHome> rowItems = new ArrayList<ModelTabHome>();
    ImageView imgBackg;
    Realm mRealm;
    SharedPreferences prefs;
    List<String> array_name = new ArrayList<String>();
    int len;
    int positionDeleted;
    String cellNo;
    String alert;
    private ImageView secondImage;
    private TextView txt_title, tv_nomsg, txtAlert;
    private String label, message, cell, count, sentTime, cell_manual, account, marketing, Language = "", requestBody;
    private Context mContext;
    private Dialog dialogConfirmCell, dialog_varify;
    private TextView lang, tv_cell_no;
    private EditText et_cell_name;
    private String version;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fraghome, viewGroup, false);
        mContext = getActivity();
        prefs = mContext.getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        mRealm = getmRealm();
        jsonVersion();
//        saveFcmCount();
//        mRealm = Realm.getInstance(getActivity());


        //
////        Realm.getInstance(FragmentHome.this.getActivity());
//        RealmConfiguration realmConfiguration = new RealmConfiguration
//                .Builder(getActivity())
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        //
        txtAlert = (TextView) getActivity().findViewById(R.id.txt_alert);
        imgBackg = (ImageView) getActivity().findViewById(R.id.img_backg);
//        View txtAlert = getActivity().findViewById(R.id.txt_alert);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        tv_nomsg = (TextView) view.findViewById(R.id.tv_nomsg);
        txt_title.setText("Home");
        listView = (SwipeMenuListView) view.findViewById(R.id.listHome);
        secondImage = (ImageView) view.findViewById(R.id.secondImage);

        secondImage.setImageResource(R.drawable.add_icon);
        initControls();

        ///


        //


        secondImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "2nd image", Toast.LENGTH_SHORT).show();

//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString(SharedPreferenceConstants.find_user_identity, "find_home");
//                editor.apply();

//                Fragment fragment = new FindUser();
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.output, fragment);
//                transaction.commit();
                confirmCell();

            }
        });
//        rowItems = new ArrayList<ModelTabHome>();
        InterfaceListener.setImageClickListenerInterface(this);
        InterfaceListener.setImageClickListenerInterfaceRel(this);

        adapter = new AdapterHome(view.getContext(), rowItems) {
        };
        listView.setAdapter(new AdapterHome(getActivity(), mRealm.allObjects(ModelTabHome.class)));
        jsonHome();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                saveCellArray();
                SaveName();
                Intent intent = new Intent(getActivity(), ChatScreen.class);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SharedPreferenceConstants.CHATCODE, "home_chat");
                editor.putString("chat_cellno", array_cellno.get(i));
                editor.putString("chat_name", array_name.get(i));
                editor.apply();
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                AppController.application.modelTabHome = rowItems.get(i);
                clicked = "true";
            }
        });

        setVersion();
        return view;
    }

//    private void saveFcmCount() {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(String.valueOf(SharedPreferenceConstants.FCM_COUNT), 0);
//        editor.apply();
//
//    }

    public void setVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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

    public void jsonDeleteAwaits() {
        ViewUtil.showProgressDialog(getActivity());

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("awaiting", "false");

            requestBody = jsonBody.toString();

            Log.e("tag", "Send message" + requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("tag", "Cell cell is " + cellNo);
        String url = GlobalConstants.BASE_URL + "/cell/" + cellNo;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ViewUtil.hideProgressDialog();
                        Log.e("tag", "Response await delete" + response);

                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

//                        int cust_live_update = Integer.parseInt(alert);
//                        String bb = String.valueOf(cust_live_update - 1);
//
//
//                        txtAlert.setText(bb);
                        rowItems.remove(positionDeleted);
                        adapter.notifyDataSetChanged();

                    }


                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {
                            ViewUtil.hideProgressDialog();

                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                String token = prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "");
                Log.e("tag", "CLIENT_USER_TOKEN is " + token);
                headers.put("Authorization", "Basic " + token);
                headers.put("Accept", "application/json");
                headers.put("Accept-Language", "application/json");
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


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void jsonHome() {
        tv_nomsg.setVisibility(View.GONE);


//        ViewUtil.showProgressDialog(getActivity());
        String url = GlobalConstants.BASE_URL + "/awaits";
//        String url = "https://api.paymaxxpro.com/sms/ui";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rowItems.clear();
//                        ViewUtil.hideProgressDialog();
                        clearRealmTable();
//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        Log.e("tag", "Response Home" + response);
//                        Toast.makeText(getActivity(
// ), "Response Home" + response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("awaits");

                            if (jsonArray.length() == 0) {
                                tv_nomsg.setVisibility(View.VISIBLE);

                                txtAlert.setVisibility(View.GONE);
                                imgBackg.setVisibility(View.GONE);


                            } else {
                                tv_nomsg.setVisibility(View.GONE);

                                txtAlert.setVisibility(View.VISIBLE);
                                imgBackg.setVisibility(View.VISIBLE);
//                                Log.e("tag", "alert is " + jsonArray.length());
//                                alert = String.valueOf((jsonArray.length( )));
                                alert = String.valueOf((jsonArray.length()));

                                txtAlert.setText(alert);

                            }


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj2 = jsonArray.getJSONObject(i);
                                message = obj2.getString("message");
                                label = obj2.getString("label");
                                cell = obj2.getString("cell");

                                count = obj2.getString("count");
                                sentTime = obj2.getString("sent");
                                array_name.add(label);
                                array_cellno.add(cell);

                                model = new ModelTabHome();

                                model.setName(label);
                                model.setMessage(message);
                                model.setCount(count);
                                model.setSentTime(sentTime);
                                rowItems.add(model);

                                saveDataIntoRealm();


//
//                                //
//                                book.setTemp_id(sender.trim());
////                                obj.setField2(field2);
//                                mRealm.beginTransaction();
//                                mRealm.copyToRealmOrUpdate(book);
//                                mRealm.commitTransaction();


                            }
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("cell_array_home", array_cellno.toString());
                            editor.putString("cell_array_name", array_name.toString());
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
//                                        error.toString() = "No internet Access, Check your internet connection.";
                            if (mRealm.allObjects(ModelTabHome.class).isEmpty()) {
                                tv_nomsg.setVisibility(View.VISIBLE);

                            }
// else {
//
//                                tv_nomsg.setVisibility(View.GONE);
//
//                            }


                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Host", "api.paymaxxpro.com");
//                headers.put("User-Agent", "fiddler");
//                String cust_live_update = prefs.getString(SharedPreferenceConstants.tokenNew, "");
                Log.e("tag", "CLIENT_TOKEN is " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, ""));
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, ""));
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


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//        Toast.makeText(getActivity(), "data lenght 2   " + data, Toast.LENGTH_SHORT).show();


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

        if (clicked.equals("true")) {
            jsonHome();

        }
    }

    public void clearRealmTable() {
        mRealm.beginTransaction();
        mRealm.clear(ModelTabHome.class);
        mRealm.commitTransaction();
    }

    public void saveDataIntoRealm() {
        mRealm.beginTransaction();
        ModelTabHome book = mRealm.createObject(ModelTabHome.class);
        book.setName(label.trim());
        book.setMessage(message.trim());
        book.setCount(count.trim());
        book.setSentTime(sentTime.trim());
        mRealm.commitTransaction();
    }

    public void saveCellArray() {
        String aa = prefs.getString("cell_array_home", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
//        Log.e("tag", "array" + cust_live_update);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_cellno.add(myList.get(i));
        }
    }

    public void confirmCell() {
        dialogConfirmCell = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialogConfirmCell.setContentView(R.layout.cust_confirm_cell);
        dialogConfirmCell.setCancelable(false);

        et_confirm_cell = (EditText) dialogConfirmCell.findViewById(R.id.et_confirm_cell);
        ImageView img_cross = (ImageView) dialogConfirmCell.findViewById(R.id.img_cross);
        Button btn_send = (Button) dialogConfirmCell.findViewById(R.id.btn_send);
        et_confirm_cell.addTextChangedListener(Mask.insert("###-###-#####", et_confirm_cell));

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_confirm_cell.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter the cell Number", Toast.LENGTH_SHORT).show();
                } else {
                    cell_manual = et_confirm_cell.getText().toString();
                    contactVarify();

                }


            }
        });
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmCell.dismiss();
            }
        });

        dialogConfirmCell.show();
    }

    public void contactVarify() {
        ViewUtil.showProgressDialog(getActivity());


        String url = GlobalConstants.BASE_URL + "/verified/" + cell_manual;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ViewUtil.hideProgressDialog();

//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        Log.e("tag", "Response Varified" + response);
//                        Toast.makeText(getActivity(), "Response Contacts" + response, Toast.LENGTH_SHORT).show();


                        try {
                            JSONObject obj = new JSONObject(response);
                            String confirmed = obj.getString("confirmed");

                            String verified = obj.getString("verified");


                            if (confirmed.equals("true")) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, cell_manual);
                                editor.apply();


                                dialogConfirmCell.dismiss();

                                dialogVarify();
                            } else {
                                Toast.makeText(getActivity(), "Cell number don't exist", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

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
                            Toast.makeText(getActivity(), "Cell number don't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q6RDhvN1VlNHFhOGclM2Q=");
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, ""));


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

    public void dialogVarify() {
        dialog_varify = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog_varify.setContentView(R.layout.cust_add_manual_contact);
        dialog_varify.setCancelable(false);
        lang = (TextView) dialog_varify.findViewById(R.id.spinner);
        Button btn_sendRequest = (Button) dialog_varify.findViewById(R.id.btn_sendRequest);
        et_cell_name = (EditText) dialog_varify.findViewById(R.id.et_cell_name);
        tv_cell_no = (TextView) dialog_varify.findViewById(R.id.tv_cell_no);
        Button b_cancel_varify = (Button) dialog_varify.findViewById(R.id.b_cancel_varify);


        final RadioButton rb_account = (RadioButton) dialog_varify.findViewById(R.id.rb_account);
        final RadioButton rb_both = (RadioButton) dialog_varify.findViewById(R.id.rb_both);
        final RadioButton rb_promo = (RadioButton) dialog_varify.findViewById(R.id.rb_promo);

        tv_cell_no.setText(cell_manual);
        b_cancel_varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_varify.dismiss();
            }
        });
        rb_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account = "false";
                marketing = "true";

                rb_promo.setChecked(true);

                rb_account.setChecked(false);
                rb_both.setChecked(false);
                Log.e("tag", "account" + account + "market" + marketing);
            }
        });
        rb_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marketing = "false";
                account = "true";
                rb_promo.setChecked(false);

                rb_account.setChecked(true);
                rb_both.setChecked(false);
                Log.e("tag", "account" + account + "market" + marketing);
            }
        });
        rb_both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marketing = "true";
                account = "true";
                rb_promo.setChecked(false);

                rb_account.setChecked(false);
                rb_both.setChecked(true);
                Log.e("tag", "account" + account + "market" + marketing);
            }
        });
        btn_sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "send", Toast.LENGTH_SHORT).show();
                if (et_cell_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please select the label", Toast.LENGTH_SHORT).show();
                }
//                else if (Language.equals("")) {
//                    Toast.makeText(getActivity(), "Please Select Language", Toast.LENGTH_SHORT).show();
//                }
                else {
                    if (Language.equals("English")) {
                        Language = "en";
                    } else if (Language.equals("Spanish")) {
                        Language = "es";
                    } else {
                        Language = "en";
                    }

                    jsonSendVarification();
                }

            }
        });

        Button dialogButton = (Button) dialog_varify.findViewById(R.id.dialogButtonOK);
        ImageView cancel = (ImageView) dialog_varify.findViewById(R.id.call);
        // if button is clicked, close the cust_forgot_pas dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_varify.dismiss();
            }
        });
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner();
            }
        });
        dialog_varify.show();
    }


    public void spinner() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.cust_select_lang);
        dialog.setCancelable(false);
        dialog.show();
        final ListView list_lang = (ListView) dialog.findViewById(R.id.list_lang);
        String[] languages = {"English", "Spanish"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, languages);
        list_lang.setAdapter(adapter);
        ImageView img_cross = (ImageView) dialog.findViewById(R.id.img_cross);
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        list_lang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lang.setText((list_lang.getItemAtPosition(i)).toString());
                dialog.dismiss();

                Language = (list_lang.getItemAtPosition(i)).toString();
            }
        });


    }

    public void jsonSendVarification() {


        String url = GlobalConstants.BASE_URL + "/verify/" + "19135750812";


        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("label", et_cell_name.getText().toString());
            jsonBody.put("account", account);
            jsonBody.put("marketing", marketing);
            requestBody = jsonBody.toString();
            Log.e("tag", "sen varify json " + requestBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        Log.e("tag", "Response SendRequest" + response);
                        dialog_varify.dismiss();
                        Toast.makeText(getActivity(), "Request Sent", Toast.LENGTH_SHORT).show();
//ss

                        Log.e("tag", "");
                        Intent intent = new Intent(getActivity(), ChatScreen.class);
                        startActivity(intent);
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

                        } else {
//                            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();

                            dialog_varify.dismiss();

                            NoMessage();
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                Toast.makeText(getActivity(), "lang is " + Language, Toast.LENGTH_SHORT).show();


//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q=");
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_TOKEN, ""));

//                Toast.makeText(getActivity(), "lang is " + Language, Toast.LENGTH_SHORT).show();
                headers.put("Accept", "application/json");
                headers.put("Accept-Language", Language);
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


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void NoMessage() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.cust_notime);
        dialog.setCancelable(false);


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

    public void SaveName() {

        String aa = prefs.getString("cell_array_name", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
        Log.e("tag", "array" + aa);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_name.add(myList.get(i));

        }
    }

    private void initControls() {


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete_icon);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:

                        cellNo = array_cellno.get(position);
                        array_cellno.remove(position);
                        positionDeleted = position;
                        Log.e("tag", "cellNo is " + cellNo);
                        Log.e("tag", "position is " + position);
                        jsonDeleteAwaits();
                        break;
                }
                return true;
            }
        });

        //mListView

        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {

            }

            @Override
            public void onMenuClose(int position) {

            }
        });

        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public static class Mask {
        public static String unmask(String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "")
                    .replaceAll("[/]", "").replaceAll("[(]", "")
                    .replaceAll("[)]", "");

        }

        public static TextWatcher insert(final String mask, final EditText ediTxt) {
            return new TextWatcher() {
                boolean isUpdating;
                String old = "";

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    String str = Mask.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                    String aa = et_confirm_cell.getText().toString();
                    Log.e("Tag", "phone no" + aa);
                }
            };
        }
    }

    public void jsonVersion() {

        String url = GlobalConstants.VERSION_URL;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("tag", "Response search" + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject obj2 = obj.getJSONObject("data");
                            String versionLive = obj2.getString("android_version");
                            if (!versionLive.equals(version)) {

                                downloadLatest();

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
                        ViewUtil.hideProgressDialog();

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
//                headers.put("If-Match", edit_search_.getText().toString());


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
//        Log.e("tag", "row size" + rowItems.size());

    }

    public void downloadLatest() {
        final Dialog dialog_temp = new Dialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog_temp.setContentView(R.layout.cust_live_update);
        dialog_temp.setCancelable(false);

        TextView txt_update = (TextView) dialog_temp.findViewById(R.id.txt_update);
//        TextView txt_exit = (TextView) dialog_temp.findViewById(R.id.txt_exit);

//        ImageView img_cross = (ImageView) dialog_temp.findViewById(R.id.img_cross);
        // if button is clicked, close the cust_forgot_pas dialog
//        img_cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_temp.dismiss();
//            }
//        });
//        txt_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(mContext, "exitt", Toast.LENGTH_SHORT).show();
//                dialog_temp.dismiss();
//                getActivity().finish();
//            }
//        });
        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "update", Toast.LENGTH_SHORT).show();
//                dialog_temp.dismiss();
                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity  object
                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

            }
        });
        dialog_temp.show();
    }


}