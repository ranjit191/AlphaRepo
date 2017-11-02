package com.textmaxx.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.textmaxx.Interfaces.ImageClickListenerInterfaceRel;
import com.textmaxx.Interfaces.OnNotificationReceived;
import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.CommonUtils;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.Utils.ViewUtil;
import com.textmaxx.app.ChatScreen;
import com.textmaxx.app.Information;
import com.textmaxx.listview_adapter.AdapterTabInbox;
import com.textmaxx.models.ModelTabInbox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class FragmentInbox extends Fragment implements ImageClickListenerInterfaceRel, View.OnClickListener, OnNotificationReceived {
    int positionOld;
    List<String> array_name = new ArrayList<String>();
    SwipeMenuListView listView;
    List<ModelTabInbox> rowItems = new ArrayList<ModelTabInbox>();
    AdapterTabInbox adapter;
    ModelTabInbox model;
    Realm mRealm;
    String label, message, cellNo = "19415041245", time, messageInbox, unread;
    SharedPreferences prefs;
    List<String> array_cellno = new ArrayList<String>();
    int positionDeleted;
    private String clicked = "false";
    private TextView txt_title, tv_nomsg;
    //    private ImageView secondImage;
    private String verified = "false", message_count = "0", CHAT_SCREEN_CELL = "", cell, pushCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fraginbox, viewGroup, false);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        tv_nomsg = (TextView) view.findViewById(R.id.tv_nomsg);
        txt_title.setText("Inbox");
        listView = (SwipeMenuListView) view.findViewById(R.id.listHome);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
//        mRealm = Realm.getInstance(getActivity());
        prefs = getActivity().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        mRealm = getmRealm();
//        secondImage = (ImageView) view.findViewById(R.id.secondImage);
//        secondImage.setImageResource(R.drawable.add_icon);
//        ((HomeActivity) getActivity()).changeToolBarText("Inbox");
        InterfaceListener.setImageClickListenerInterfaceRel(this);
        cell = prefs.getString(SharedPreferenceConstants.CHAT_SCREEN_CELL, "");
//        pushCount = prefs.getString(SharedPreferenceConstants.PUSH_COUNT, "");
        adapter = new AdapterTabInbox(view.getContext(), rowItems) {
        };

        saveCellArray();


        initControls();
        listView.setAdapter(new AdapterTabInbox(getActivity(), mRealm.allObjects(ModelTabInbox.class)));

        loadInbox();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SaveName();
                saveCellArray();


                Log.e("tag", "array_cellno is " + array_cellno);

                Intent intent = new Intent(getActivity(), ChatScreen.class);

//                intent.putExtra("cell", model.getCell().substring(0));
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SharedPreferenceConstants.CHATCODE, "inbox_chat");
                editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, array_cellno.get(i).trim());
                editor.putString(SharedPreferenceConstants.VARIFIED_FOR_CHAT, verified);
                editor.putString("chat_name", array_name.get(i).trim());
                editor.apply();
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                AppController.application.modelTabInbox = rowItems.get(i);
//                saveCellArray();


                if (cell.equals(array_cellno.get(i).trim())) {
//                    countNull();
                    clicked = "true";
                }


            }
        });
//        secondImage.setOnClickListener(this);

        InterfaceListener.setmOnNotificationReceived(this);
        return view;
    }

    private void loadInbox() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jsonInbox();
            }
        }, 1500);


    }

    @Override
    public void onResume() {
        super.onResume();

//        if (clicked.equals("true")) {
        jsonInbox();

//        }
    }

//    public void countNull() {
//
//
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(SharedPreferenceConstants.PUSH_COUNT, "0");
//
//        editor.apply();
//
//
//    }

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
    public void ImageClickRel(RelativeLayout imageView) {
        Fragment fragment = new Information();
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        CommonUtils.replaceFragment(fragment, getFragmentManager());


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }


    public void jsonInbox() {


        String url = GlobalConstants.BASE_URL + "/ui";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        rowItems.clear();
                        array_cellno.clear();
                        array_name.clear();
//                        ViewUtil.hideProgressDialog();
//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        Log.e("tag", "Response Inbox" + response);
//                        Toast.makeText(getActivity(), "Response Inbox" + response, Toast.LENGTH_SHORT).show();
                        clearRealmTable();


                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("sessions");
//                            Toast.makeText(getActivity(), "array length is " + jsonArray.length(), Toast.LENGTH_SHORT).show();


                            if (jsonArray.length() == 0) {
                                tv_nomsg.setVisibility(View.VISIBLE);

                            } else {
                                tv_nomsg.setVisibility(View.GONE);

                            }
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject obj2 = jsonArray.getJSONObject(i);
//                                message = obj2.getString("message");
                                label = obj2.getString("label");
                                Log.e("tag", "Response label" + label);
                                cellNo = obj2.getString("cell");
                                time = obj2.getString("updated");
                                messageInbox = obj2.getString("message");
                                unread = obj2.getString("unread");
//                                unread="1";
                                array_cellno.add(cellNo);
                                array_name.add(label);
                                model = new ModelTabInbox();


                                model.setName(label);
//                                model.setCell(cellNo);
                                model.setTime(time);
                                String cell = prefs.getString(SharedPreferenceConstants.CHAT_SCREEN_CELL, "");
//                                String pushCount = prefs.getString(SharedPreferenceConstants.PUSH_COUNT, "");

//                                if (cell.equals(null) || cell.equals("") || cell == null) {
//                                    model.setCount("0");
//
//                                } else if (cell.equals(cellNo)) {
//                                    model.setCount(pushCount);
//                                } else {
//                                    model.setCount("0");
//                                }
                                if (unread.equals("0")) {
                                    model.setCount("");
                                } else {
                                    model.setCount(unread);

                                }

                                model.setMessage(messageInbox);

                                rowItems.add(model);
                                saveDataIntoRealm();

                            }

                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("cell_array_inbox", array_cellno.toString());
                            editor.putString(SharedPreferenceConstants.ARRAY_NAME_INBOX, array_name.toString());
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
//                            saveCellArray();
//                            array_cellno.add("19546093364");
//                            array_cellno.add("19415041245");


                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                String token = prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim();
                Log.e("tag", "CLIENT_USER_TOKEN is " + token);
//                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q6RDhvN1VlNHFhOGclM2Q=");
//                                                      Tnk2MEZLcVQ1bzAlM2Q6RU5RQ0RjTGp3dDAlM2Q=
                headers.put("Authorization", "Basic " + token);


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

    public void jsonUiDelete() {
        ViewUtil.showProgressDialog(getActivity());
        Log.e("tag", "cel deleted is " + cellNo);
        String url = GlobalConstants.BASE_URL + "/ui/" + cellNo.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ViewUtil.hideProgressDialog();
                        Log.e("tag", "Response delete from inbox" + response);
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                        array_cellno.remove(positionDeleted);
                        rowItems.remove(positionDeleted);
                        adapter.notifyDataSetChanged();
                    }


                    ///////////
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ViewUtil.hideProgressDialog();
                        Log.e("tag", "error is is " + error);

                        if (error instanceof NoConnectionError) {

                            Toast.makeText(getActivity(), "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();


                String token = prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim();
                Log.e("tag", "CLIENT_USER_TOKEN is " + token);
                headers.put("Authorization", "Basic " + token);
                headers.put("Accept", "application/json");
//                headers.put("cell", cellNo);

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


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onClick(View view) {
//        if (view == secondImage) {
////            Toast.makeText(getActivity(), "2ndInbox", Toast.LENGTH_SHORT).show();
//
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString(SharedPreferenceConstants.find_user_identity, "find_inbox");
//            editor.apply();
//            Fragment fragment = new FindUser();
//            FragmentManager manager = getFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.replace(R.id.output, fragment);
//            transaction.commit();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//
//        if (!mRealm.isClosed()) {
//            // Do something
//            mRealm.close();
//        }

    }

    public void clearRealmTable() {
        mRealm.beginTransaction();
        mRealm.clear(ModelTabInbox.class);
        mRealm.commitTransaction();
    }

    public void saveDataIntoRealm() {


        mRealm.beginTransaction();
        ModelTabInbox book = mRealm.createObject(ModelTabInbox.class);
        book.setName(label.trim());
        book.setMessage(messageInbox.trim());
        book.setTime(time.trim());


        Log.e("tag", "array size : " + array_cellno);
//        String cell = "19413506523";
//        saveCellArray();

        String cell = prefs.getString(SharedPreferenceConstants.CHAT_SCREEN_CELL, "");
//        String pushCount = prefs.getString(SharedPreferenceConstants.PUSH_COUNT, "");


//        for (int i = 0; i < array_cellno.size(); i++) {

//        if (cell.equals(cellNo)) {
//            book.setCount(pushCount);
////            Toast.makeText(getActivity(), "1111111", Toast.LENGTH_SHORT).show();
//        } else {
////            Toast.makeText(getActivity(), "22222", Toast.LENGTH_SHORT).show();
//            book.setCount("0");
//        }
        if (unread.equals("0")) {
            model.setCount("");
        } else {
            model.setCount(unread);

        }
        mRealm.commitTransaction();
    }

    public void saveCellArray() {
        array_cellno.clear();
        String aa = prefs.getString("cell_array_inbox", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");

        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int j = 0; j < myList.size(); j++) {
            array_cellno.add(myList.get(j));
        }
    }

    public void SaveName() {
        array_name.clear();
        String aa = prefs.getString(SharedPreferenceConstants.ARRAY_NAME_INBOX, "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
        Log.e("tag", "array" + aa);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_name.add(myList.get(i));
            Log.e("tag", "array::::::::::::;;  " + array_name);
        }
    }

    private void initControls() {


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
//                SwipeMenuItem goodItem = new SwipeMenuItem(
//                        getActivity());
                // set item background
//                goodItem.setBackground(new ColorDrawable(Color.rgb(0x30, 0xB1,
//                        0xF5)));
//                // set item width
//                goodItem.setWidth(dp2px(90));
//                // set a icon
//                goodItem.setIcon(R.drawable.ic_action_good);
//                // add to menu
//                menu.addMenuItem(goodItem);

                // create "delete" item
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

//                        saveCellArray();
                        Log.e("tag", "array" + array_cellno);
                        cellNo = array_cellno.get(position);

                        positionDeleted = position;

                        Log.e("tag", "position is " + position);
                        if (array_cellno.size() != 0 && rowItems.size() != 0) {
                            jsonUiDelete();
                        }

                        break;
//                    case 1:
////                        mArrayList.remove(position);
////                        mListDataAdapter.notifyDataSetChanged();
//                        Toast.makeText(getActivity(),"Item deleted",Toast.LENGTH_SHORT).show();
//
//                        break;


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

    @Override
    public void Notificationreceived() {
//        Toast.makeText(getActivity(), "Intent Detected.__7777", Toast.LENGTH_LONG).show();
        jsonInbox();
    }


}