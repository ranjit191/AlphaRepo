//package com.textmaxx.fragments;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import com.android.volley.AuthFailureError;
//import com.android.volley.Cache;
//import com.android.volley.Network;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.BasicNetwork;
//import com.android.volley.toolbox.DiskBasedCache;
//import com.android.volley.toolbox.HurlStack;
//import com.android.volley.toolbox.StringRequest;
//import com.google.gson.Gson;
//import com.textmaxx.R;
//import com.textmaxx.SharedPref.SharedPreferenceConstants;
//import com.textmaxx.Utils.GlobalConstants;
//import com.textmaxx.demo.ContactResult;
//import com.textmaxx.demo.Match;
//import com.textmaxx.demo.RecycleAdapterContacts;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.exceptions.RealmMigrationNeededException;
//
///**
// * Created by xamarin on 05/08/17.
// */
//
//
//public class ContactsFrag extends Fragment {
//	RecyclerView recyclerView;
//	RecyclerView.Adapter adapter;
//	Context context;
//	List<Match> list = new ArrayList<>();
//	SharedPreferences prefs;
//	Realm mRealm;
//	Dialog pDialog;
//	RequestQueue mRequestQueue;
//	@Override
//	public void onCreate (@Nullable Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		context = this.getContext();
//		prefs = getActivity().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
//		mRealm = getmRealm();
//	}
//
//	@Nullable
//	@Override
//	public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.contct_frag, container, false);
//		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//		recyclerView.setLayoutManager(new LinearLayoutManager(context));
//		adapter = new RecycleAdapterContacts(context, list);
//		recyclerView.setAdapter(adapter);
//		if (mRequestQueue == null) {
//			Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
//			Network network = new BasicNetwork(new HurlStack());
//			mRequestQueue = new RequestQueue(cache, network);
//			mRequestQueue.start();
//		}
//		pDialog = new Dialog(context);
//		pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		pDialog.setContentView(R.layout.progress_dialog_view);
//		callThisApi();
//		return view;
//	}
//
//	private void callThisApi () {
////		pDialog.show();
//		String url = GlobalConstants.BASE_URL + "/cells";
//		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Listener<String>() {
//			@Override
//			public void onResponse (String response) {
//				Log.d("Responce",response);
//				Gson gson = new Gson();
//				ContactResult contactResult = gson.fromJson(response,ContactResult.class);
//				if(contactResult!=null&&contactResult.getMatches()!=null){
//					list.addAll(contactResult.getMatches());
//					adapter.notifyDataSetChanged();
//				}
////			pDialog.dismiss();
//			}
//		}, new ErrorListener() {
//			@Override
//			public void onErrorResponse (VolleyError error) {
////				pDialog.dismiss();
//			}
//		}) {
//
//
//			@Override
//			public Map<String, String> getHeaders () throws AuthFailureError {
//				HashMap<String, String> headers = new HashMap<String, String>();
//
//
////                headers.put("Authorization", "Basic Tnk2MEZLcVQ1bzAlM2Q6RDhvN1VlNHFhOGclM2Q=");
//
//				headers.put("Authorization", "Basic " + prefs.getString(SharedPreferenceConstants.CLIENT_USER_TOKEN, "").trim());
//
//
//				headers.put("Accept", "application/json");
////                headers.put("If-Match", edit_search_.getText().toString());
//
//
//				return headers;
//			}
//		};
//
//		mRequestQueue.add(stringRequest);
//
//
//	}
//
//	private Realm getmRealm() {
//		RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).build();
//
//		try {
//			return Realm.getInstance(realmConfiguration);
//		} catch (RealmMigrationNeededException e) {
//			try {
//				Realm.deleteRealm(realmConfiguration);
//				//Realm file has been deleted.
//				return Realm.getInstance(realmConfiguration);
//			} catch (Exception ex) {
//				throw ex;
//				//No Realm file to remove.
//			}
//		}
//	}
//
//
//	@Override
//	public void onDestroyView () {
//		super.onDestroyView();
//		if (mRequestQueue != null) {
//			mRequestQueue.stop();
//		}
//	}
//
//}
