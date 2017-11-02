package com.textmaxx.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Spinner;
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
import com.textmaxx.Interfaces.ImageClickListenerInterface;
import com.textmaxx.Interfaces.onRecyclerClick;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.Utils.ViewUtil;
import com.textmaxx.adapter_recycler.RecycleAdapterContacts;
import com.textmaxx.app.AutoAddTextWatcher;
import com.textmaxx.app.ChatScreen;
import com.textmaxx.app.Information;
import com.textmaxx.demo.Match;
import com.textmaxx.models.ModelTabContacts;

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

public class FragmentContacts extends Fragment implements View.OnClickListener, ImageClickListenerInterface, onRecyclerClick {
    static EditText et_confirm_cell;
    private static String cellText;
    Fragment fragment = null;
    Spinner spinner;
    ListView listView;
    List<ModelTabContacts> rowItems = new ArrayList<ModelTabContacts>();
    ModelTabContacts model;
    //    AdapterContacts adapter;
    String label;
    SharedPreferences prefs;
    Realm mRealm;
    List<String> array_varified = new ArrayList<String>();
    int count_min_4 = 0;
    List<String> array_cellno_contacts = new ArrayList<String>();
    List<String> array_name = new ArrayList<String>();
    String cellNo;
    EditText et_cell_name;
    Dialog dialogConfirmCell;
    int count_search = 0;
    private String Language = "English", cell_manual = "", requestBody;
    private String verified;
    private TextView txt_title, tv_cell_no, lang, tv_nomsg, edit_search_, tv_text;
    private ImageView firstImage, secondImage, cell_search;
    private Dialog dialog_varify;
    private String marketing = "false";
    private String account = "true";
    private String clicked = "false", list_type = "contact", click = "false";
    private RequestQueue mRequestQueue;
    ////////////////
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    //    Context context;
    List<Match> list = new ArrayList<>();
    private String callApi = "false";

    //    SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragcontacts, viewGroup, false);
        mRealm = getmRealm();
        initializeView(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecycleAdapterContacts(getActivity(), rowItems);

        cell_search.setImageResource(R.drawable.search);
        firstImage.setImageResource(R.drawable.username);

        prefs = getActivity().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);


        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        //
//        adapter = new AdapterContacts(view.getContext(), rowItems) {
//        };
        //11111
//        jsonContacts();
//        jsonContacts22();

        recyclerView.setAdapter(new RecycleAdapterContacts(getActivity(), mRealm.allObjects(ModelTabContacts.class)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callThisApi();
            }
        }, 1500);



        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        UponClickingrow(position);

                    }
                })


        );*/


        //        recyclerView.setAdapter(adapter);


        edit_search_.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(getActivity(), "111111", Toast.LENGTH_SHORT).show();
                int len = edit_search_.getText().toString().length();
                Log.e("tag", "length is" + len);
                count_min_4 = len;
                if (len == 0) {
                    if (count_search == 1) {

//222
//                        jsonContacts();
//                        jsonContacts22();
                        callThisApi();
                        count_search = 0;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listener();

        return view;


    }//OnCreateEnds


    private void initializeView(View view) {

        firstImage = (ImageView) view.findViewById(R.id.firstImage);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        cell_search = (ImageView) view.findViewById(R.id.cell_search);
        cell_search.setImageResource(R.drawable.search);
        tv_nomsg = (TextView) view.findViewById(R.id.tv_nomsg);
        firstImage.setImageResource(R.drawable.username);
        edit_search_ = (EditText) view.findViewById(R.id.edit_search);

    }

    private void listener() {

        firstImage.setOnClickListener(this);
//        edit_search_.setOnClickListener(this);
        cell_search.setOnClickListener(this);
//        tv_text.setOnClickListener(this);

        InterfaceListener.setImageClickListenerInterface(this);
        InterfaceListener.setMonOnRecyclerClick(this);
    }

    private void UponClickingrow(int position) {
        SaveCell();
        SaveName();


//        if (list_type.equals("contact")) {
//            listClickData(position);
//
//        } else
//            if (list_type.equals("search")) {

        SaveVarified();
        if (array_varified.size() != 0 || array_name.size() != 0 || array_cellno_contacts.size() != 0) {
            if (array_varified.get(position).trim().equals("true")) {
                listClickData(position);
            } else {
                cell_manual = array_cellno_contacts.get(position).trim();


                dialogVarify();
            }
        }
//        } else {


//        }
    }

    private void listClickData(int position) {
        Intent intent = new Intent(getActivity(), ChatScreen.class);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferenceConstants.CHATCODE, "inbox_chat");
        editor.putString("chat_cellno", array_cellno_contacts.get(position));
        editor.putString("chat_name", array_name.get(position));
        editor.apply();
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        clicked = "true";

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


    public void jsonSearch() {

        ViewUtil.showProgressDialog(getActivity());
//        listView.setAdapter(new AdapterFindContacts(getActivity(), mRealm.allObjects(ModelFindContacts.class)));
        String url = GlobalConstants.BASE_URL + "/cells";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ViewUtil.hideProgressDialog();
                        list_type = "search";
                        count_search = 1;

                        rowItems.clear();
                        clearRealmTable();
                        array_name.clear();
                        array_cellno_contacts.clear();
                        array_varified.clear();


//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        Log.e("tag", "Response search" + response);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jsonArray = jObj.getJSONArray("matches");
//                            if ((jsonArray.length() > 1)) {
//                                cellLenth = 1;
//
//                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jmatches = jsonArray.getJSONObject(i);

                                verified = jmatches.getString("verified");
//                                if (verified.equals("false")) {
                                label = jmatches.getString("label");
                                String cell = jmatches.getString("cell");

                                Log.d("tag", " varified varified");
                                array_varified.add(verified);
                                array_name.add(label);
                                array_cellno_contacts.add(cell);
                                model = new ModelTabContacts();

                                model.setTitle(label);
                                model.setVarified(verified);
//                                model.setCellNo(cell);

                                rowItems.add(model);
                                saveDataIntoRealm();
//                                } else {
//                                    Log.d("tag", "not varified");
//                                }


                            }
                            adapter.notifyDataSetChanged();
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("cell_array", array_cellno_contacts.toString());
                            editor.putString("cell_array_home", array_cellno_contacts.toString());
                            editor.putString(SharedPreferenceConstants.ARRAY_NAME_CONTACTS, array_name.toString());
                            editor.putString("array_varified", array_varified.toString());

                            editor.apply();
                            if (jsonArray.isNull(0)) {
//                                count_search = 1;
                                tv_nomsg.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                                tv_nomsg.setText("No data found");
                                noDataFound();
                            } else {

//                                Toast.makeText(getActivity(), "aaaaaaaa", Toast.LENGTH_SHORT).show();

                                tv_nomsg.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                recyclerView.setAdapter(adapter);

                                // listView.setAdapter(adapter);
                                // adapter.notifyDataSetChanged();
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
                headers.put("If-Match", edit_search_.getText().toString());


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
        Log.e("tag", "row size" + rowItems.size());


    }

    public void noDataFound() {
        final Dialog dialog_ndf = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog_ndf.setContentView(R.layout.cust_no_contcat);
        dialog_ndf.setCancelable(false);


        TextView txt_cancel = (TextView) dialog_ndf.findViewById(R.id.txt_cancel);
        TextView txt_add = (TextView) dialog_ndf.findViewById(R.id.txt_add);

        // if button is clicked, close the cust_forgot_pas dialog
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ndf.dismiss();
                callThisApi();
            }
        });

        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi = "true";
                confirmCell();
                dialog_ndf.dismiss();
            }
        });

        dialog_ndf.show();
    }

//    public void jsonContacts22() {
//
////        ViewUtil.showProgressDialog(getActivity());
//        listView.setAdapter(new AdapterstaticContacts(getActivity(), mRealm.allObjects(StaticModelContacts.class)));
//        String url = GlobalConstants.BASE_URL + "/cells";
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                        ViewUtil.hideProgressDialog();
//                        list_type = "search";
////                        count_search = 0;
//
//
//                        rowItems.clear();
//                        clearRealmTable();
//                        array_name.clear();
//                        array_cellno_contacts.clear();
//                        array_varified.clear();
//
//
////                                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
//                        Log.e("tag", "Response search" + response);
//
//                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            JSONArray jsonArray = jObj.getJSONArray("matches");
////                            if ((jsonArray.length() > 1)) {
////                                cellLenth = 1;
////
////                            }
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jmatches = jsonArray.getJSONObject(i);
//
//                                verified = jmatches.getString("verified");
////                                if (verified.equals("false")) {
//                                label = jmatches.getString("label");
//                                String cell = jmatches.getString("cell");
//
//                                Log.d("tag", " varified varified");
//                                array_varified.add(verified);
//                                array_name.add(label);
//                                array_cellno_contacts.add(cell);
//                                model = new ModelTabContacts();
//
//                                model.setTitle(label);
////                                model.setVarified(verified);
////                                model.setCellNo(cell);
//
//                                rowItems.add(model);
////                                saveDataIntoRealm();
////                                } else {
////                                    Log.d("tag", "not varified");
////                                }
//
//                                saveDataIntoRealm();
//                            }
//                            adapter.notifyDataSetChanged();
////                            listView.setAdapter(adapter);
//                            if (jsonArray.isNull(0)) {
////                                count_search = 1;
//                                tv_nomsg.setVisibility(View.VISIBLE);
//                                listView.setVisibility(View.INVISIBLE);
//                                tv_nomsg.setText("No data found");
//                            } else {
//
////                                Toast.makeText(getActivity(), "aaaaaaaa", Toast.LENGTH_SHORT).show();
//
//                                tv_nomsg.setVisibility(View.GONE);
//                                listView.setVisibility(View.VISIBLE);
//
////                                listView.setAdapter(new AdapterContacts(getActivity(), rowItems));
//
//                                listView.setAdapter(adapter);
//                                // adapter.notifyDataSetChanged();
//                            }
//
//
//                            SharedPreferences.Editor editor = prefs.edit();
//                            editor.putString("cell_array", array_cellno_contacts.toString());
//                            editor.putString("cell_array_home", array_cellno_contacts.toString());
//                            editor.putString(SharedPreferenceConstants.ARRAY_NAME_CONTACTS, array_name.toString());
//                            editor.putString("array_varified", array_varified.toString());
//
//                            editor.apply();
////                            if (jsonArray.isNull(0)) {
//////                                count_search = 1;
////                                tv_nomsg.setVisibility(View.VISIBLE);
////                                listView.setVisibility(View.INVISIBLE);
////                                tv_nomsg.setText("No data found");
////                            } else {
////
//////                                Toast.makeText(getActivity(), "aaaaaaaa", Toast.LENGTH_SHORT).show();
////
////                                tv_nomsg.setVisibility(View.GONE);
////                                listView.setVisibility(View.VISIBLE);
////
////                                listView.setAdapter(new AdapterContacts(getActivity(), rowItems));
////
//
////                                // adapter.notifyDataSetChanged();
////                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    ///////////
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        ViewUtil.hideProgressDialog();
//
//                        Log.e("tag", "error is is " + error);
//
//                        if (error instanceof NoConnectionError) {
////                                        error.toString() = "No internet Access, Check your internet connection.";
//
//                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }) {
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//
//
////                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q6RDhvN1VlNHFhOGclM2Q=");
//
//                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
//
//
//                headers.put("Accept", "application/json");
////                headers.put("If-Match", edit_search_.getText().toString());
//
//
//                return headers;
//            }
//
////
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////
////                Log.e("tag", "Does it assign Headers?");
////
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("apns_device_token", "abcdef");
////                return params;
////            }
//
//
//        };
//
//
////        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        mRequestQueue.add(stringRequest);
////        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
////                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
////                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////        Log.e("tag", "row size" + rowItems.size());
//
//    }

    @Override
    public void onClick(View view) {
        if (view == firstImage) {


            confirmCell();
        }
//        if (view == secondImage) {
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString(SharedPreferenceConstants.find_user_identity, "find_contacts");
//            editor.apply();
//            fragment = new FindUser();
//            CommonUtils.replaceFragment(fragment, getActivity().getSupportFragmentManager());
//        }

        if (view == cell_search) {

//            if (click.equals("false")) {
//                tv_text.setVisibility(View.GONE);
//                edit_search_.setVisibility(View.VISIBLE);

//                edit_search_.setClickable(true);
//                view_.setVisibility(View.VISIBLE);
//                click = "true";
//                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

//                jsonContacts22();
//            } else {
//                tv_text.setVisibility(View.GONE);
//                edit_search_.setVisibility(View.VISIBLE);


//                count_min_4 = 4;
            if (edit_search_.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Please Enter The Name.", Toast.LENGTH_SHORT).show();
            } else if (count_min_4 < 3) {
                Log.e("tag", "jeeeeeeeeeeeee" + count_min_4);
                Toast.makeText(getActivity(), "Please enter minimum 3 characters.", Toast.LENGTH_SHORT).show();

            } else {
                jsonSearch();
//                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
            }

        }


//        }
//        if (view == tv_text) {
//
////            if (click.equals("false")) {
////                edit_search_.setFocusable(false);
////                click = "true";
////
////                Log.d("aaaaaaaaaaaaaaa", "onee");
////                Toast.makeText(getActivity(), "onee", Toast.LENGTH_SHORT).show();
////            } else {
////                edit_search_.setFocusable(true);
////                Log.d("aaaaaaaaaaaaaaa", "twoooo");
////                Toast.makeText(getActivity(), "twoo", Toast.LENGTH_SHORT).show();
////
////
////            }
//
////            Toast.makeText(getActivity(), "testtest", Toast.LENGTH_SHORT).show();
////            edit_search_.setFocusable(true);
//        }
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
        mRealm.beginTransaction();
        mRealm.clear(ModelTabContacts.class);
        mRealm.commitTransaction();
    }

    public void saveDataIntoRealm() {
        mRealm.beginTransaction();
        ModelTabContacts book = mRealm.createObject(ModelTabContacts.class);
        book.setTitle(label.trim());
        book.setVarified(verified.trim());
//        book.setLabel(message.trim());
        mRealm.commitTransaction();
    }


    public void SaveCell() {
        array_cellno_contacts.clear();
        String aa = prefs.getString("cell_array", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
        Log.e("tag", "array" + aa);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_cellno_contacts.add(myList.get(i));
        }
    }

    public void SaveName() {
        array_name.clear();
        String aa = prefs.getString(SharedPreferenceConstants.ARRAY_NAME_CONTACTS, "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
        Log.e("tag", "array" + aa);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_name.add(myList.get(i));
        }
    }

    public void SaveVarified() {
        array_varified.clear();
        String aa = prefs.getString("array_varified", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
        Log.e("tag", "array" + aa);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_varified.add(myList.get(i).trim());
        }
    }


    public void confirmCell() {
        dialogConfirmCell = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialogConfirmCell.setContentView(R.layout.cust_confirm_cell);
        dialogConfirmCell.setCancelable(false);

        et_confirm_cell = (EditText) dialogConfirmCell.findViewById(R.id.et_confirm_cell);
        ImageView img_cross = (ImageView) dialogConfirmCell.findViewById(R.id.img_cross);
        Button btn_send = (Button) dialogConfirmCell.findViewById(R.id.btn_send);
//        Mask.insert("###-###-#####", et_confirm_cell);
//        et_confirm_cell.addTextChangedListener(Mask.insert("###-###-#####", et_confirm_cell));
        ImageView img_cancel = (ImageView) dialogConfirmCell.findViewById(R.id.img_cancel);


        et_confirm_cell.addTextChangedListener(new AutoAddTextWatcher(et_confirm_cell,
                "-",
                3, 6));
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_confirm_cell.setText("");
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_confirm_cell.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter the cell Number", Toast.LENGTH_SHORT).show();
                } else {
//                    cell_manual = et_confirm_cell.getText().toString();
                    cell_manual = et_confirm_cell.getText().toString().replace("-", "");
                    contactVarify();
                }


            }
        });
        // if button is clicked, close the cust_forgot_pas dialog
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callApi.equals("true")) {
                    dialogConfirmCell.dismiss();
                    callThisApi();
                } else {
                    dialogConfirmCell.dismiss();
                }

            }
        });

        dialogConfirmCell.show();
    }

    @Override
    public void onResume() {
        super.onResume();
//
//        if (clicked.equals("true")) {
/////333
////            jsonContacts();
////            jsonContacts22();
//            callThisApi();
//            adapter.notifyDataSetChanged();
//
//
//        }
    }

    @Override
    public void onPause() {
        super.onPause();


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

        String cellNumber = cell_manual;
        Log.e("tag", "cell is:::: " + cellNumber);
        String asubstring = cellNumber.substring(0, 1);

        if (asubstring.equals("1")) {

            String cell = cellNumber.substring(1);

            String st = new String(cell);
            st = new StringBuffer(st).insert(3, "-").toString();
            st = new StringBuffer(st).insert(7, "-").toString();
            tv_cell_no.setText(st);
        } else {
//                Toast.makeText(getActivity(), "222222", Toast.LENGTH_SHORT).show();
            String st = new String(cellNumber);
            st = new StringBuffer(st).insert(3, "-").toString();
            st = new StringBuffer(st).insert(7, "-").toString();
            tv_cell_no.setText(st);
        }


//        tv_cell_no.setText(cell_manual);
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
                } else if (Language.equals("")) {
                    Toast.makeText(getActivity(), "Please Select Language", Toast.LENGTH_SHORT).show();
                } else {
                    if (Language.equals("English")) {
                        Language = "en";
                    } else {
                        Language = "es";
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

    public void jsonSendVarification() {

        ViewUtil.showProgressDialog(getActivity());

//        Toast.makeText(getActivity(), "click click", Toast.LENGTH_SHORT).show();

//        String url = GlobalConstants.BASE_URL + "/verify/" + "19135750812";
        String url = GlobalConstants.BASE_URL + "/verify/" + cell_manual;


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

                        ViewUtil.hideProgressDialog();

                        Log.e("tag", "");
//                        Intent intent = new Intent(getActivity(), ChatScreen.class);
//                        startActivity(intent);
                        //44444
//                        jsonContacts();
//                        jsonContacts22();
                        callThisApi();
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
                headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());

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

    public void contactVarify() {
        ViewUtil.showProgressDialog(getActivity());
        Log.e("tag", "cell manual" + cell_manual);

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

                            if (confirmed.equals("true") && verified.equals("true")) {
                                Toast.makeText(getActivity(), "Contact already exits.", Toast.LENGTH_SHORT).show();

                            } else if (confirmed.equals("true") && verified.equals("false")) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, cell_manual);
                                editor.apply();


                                dialogConfirmCell.dismiss();

                                dialogVarify();

                            } else if (confirmed.equals("false") && verified.equals("false")) {
                                Toast.makeText(getActivity(), "Cell number don't exist", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), "Cell number don't exist", Toast.LENGTH_SHORT).show();

                            }


//
//                            if (confirmed.equals("true")) {
//                                SharedPreferences.Editor editor = prefs.edit();
//                                editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, cell_manual);
//                                editor.apply();
//
//
//                                dialogConfirmCell.dismiss();
//
//                                dialogVarify();
//                            } else {
//                                Toast.makeText(getActivity(), "Cell number don't exist", Toast.LENGTH_SHORT).show();
//
//                            }


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

    @Override
    public void ImageClick(ImageView imageView) {
        Fragment fragment = new Information();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.output, fragment);
        transaction.commit();
    }

    //destroy view


    private void callThisApi() {
//		pDialog.show();

        //        listView.setAdapter(new AdapterstaticContacts(getActivity(), mRealm.allObjects(StaticModelContacts.class)));

        String url = GlobalConstants.BASE_URL + "/cells";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responce", response);
                list_type = "search";
                callApi = "false";
//                        count_search = 0;


                array_name.clear();
                array_cellno_contacts.clear();
                array_varified.clear();
                rowItems.clear();
                clearRealmTable();


//////////////////////11111111111

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("matches");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jmatches = jsonArray.getJSONObject(i);

                        verified = jmatches.getString("verified");
//                                if (verified.equals("false")) {
                        label = jmatches.getString("label");
                        String cell = jmatches.getString("cell");

                        Log.d("tag", " varified " + verified);
                        array_varified.add(verified);
                        array_name.add(label);
                        array_cellno_contacts.add(cell);
                        model = new ModelTabContacts();

                        model.setTitle(label);
                        model.setVarified(verified);
//                                model.setCellNo(cell);
                        rowItems.add(model);

                        saveDataIntoRealm();
                    }
                    adapter.notifyDataSetChanged();
//                            listView.setAdapter(adapter);
                    if (jsonArray.isNull(0)) {
//                                count_search = 1;
                        tv_nomsg.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        tv_nomsg.setText("No data found");
                    } else {

//                                Toast.makeText(getActivity(), "aaaaaaaa", Toast.LENGTH_SHORT).show();

                        tv_nomsg.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

//                                listView.setAdapter(new AdapterContacts(getActivity(), rowItems));

                        recyclerView.setAdapter(adapter);
                        // adapter.notifyDataSetChanged();
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("cell_array", array_cellno_contacts.toString());
                    editor.putString("cell_array_home", array_cellno_contacts.toString());
                    editor.putString(SharedPreferenceConstants.ARRAY_NAME_CONTACTS, array_name.toString());
                    editor.putString("array_varified", array_varified.toString());

                    editor.apply();
//s
//                            SharedPreferences.Editor editor = prefs.edit();
//                            editor.putString("cell_array", array_cellno_contacts.toString());
//                            editor.putString("cell_array_home", array_cellno_contacts.toString());
//                            editor.putString(SharedPreferenceConstants.ARRAY_NAME_CONTACTS, array_name.toString());
//                            editor.putString("array_varified", array_varified.toString());
//
//                            editor.apply();
//                            if (jsonArray.isNull(0)) {
////                                count_search = 1;
//                                tv_nomsg.setVisibility(View.VISIBLE);
//                                listView.setVisibility(View.INVISIBLE);
//                                tv_nomsg.setText("No data found");
//                            } else {
//
////                                Toast.makeText(getActivity(), "aaaaaaaa", Toast.LENGTH_SHORT).show();
//
//                                tv_nomsg.setVisibility(View.GONE);
//                                listView.setVisibility(View.VISIBLE);
//
//                                listView.setAdapter(new AdapterContacts(getActivity(), rowItems));
//

//                                // adapter.notifyDataSetChanged();
//                            }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ////////////222222222


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//				pDialog.dismiss();
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
        };

        mRequestQueue.add(stringRequest);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

    @Override
    public void onRecyclerItemClick(int pos) {
        UponClickingrow(pos);
    }
}