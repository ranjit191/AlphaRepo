package com.textmaxx.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.textmaxx.R;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.listview_adapter.AdapterChannelList;
import com.textmaxx.models.ModelChannelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Youtube extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    //    private static final String YoutubeDeveloperKey = "AIzaSyDhEeq_FYAdCzMMSigmMcRwF_nQEhUXS-0";
    private static final String YoutubeDeveloperKey = GlobalConstants.YOUTUBE_KEY;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    ModelChannelList model;
    List<ModelChannelList> rowItems = new ArrayList<ModelChannelList>();
    AdapterChannelList adapter;
    ListView listYouTube;
    ImageView firstImage;
    List<String> array_videoId = new ArrayList<String>();
    String videoId = "";
    YouTubePlayerView youTubeView;
    private YouTubePlayer YPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        listYouTube = (ListView) findViewById(R.id.listYouTube);

        firstImage = (ImageView) findViewById(R.id.firstImage);
        TextView title = (TextView) findViewById(R.id.txt_title);
        title.setText("Help Videos");
        firstImage.setVisibility(View.VISIBLE);
        firstImage.setImageResource(R.drawable.left_icon);
        firstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(YoutubeDeveloperKey, this);
        adapter = new AdapterChannelList(Youtube.this, rowItems) {
        };
        jsonVideoId();
        jsonChannelList();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.you_tube_api, menu);
//        return true;
//    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YoutubeDeveloperKey, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onInitializationSuccess(Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        YPlayer = player;
        YPlayer.cueVideo("fcVrk7nY8j0");
//        YPlayer.setShowFullscreenButton(false);

//        player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
//        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
 /*

 * Now that this variable YPlayer is global you can access it
 * throughout the activity, and perform all the player actions like
 * play, pause and seeking to a position by code.
 */
        // if (!wasRestored) {
//        YPlayer.cueVideo(fcVrk7nY8j0);
        player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
//        playVideo(videoId);

        // }
    }

    private void playVideo(String videoId) {

        Log.d("start", "video id      " + videoId);
        Log.d("start", "player      " + YPlayer);
        YPlayer.cueVideo(videoId);
    }

    public void jsonChannelList() {


        String url = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyDYrOevuonrREFWYTWJGLl9qW78p-nqc0M&channelId=UCJVyXztIap_igWjLa8B5-sw&part=snippet&order=date&maxResults=5";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_LONG).show();
                        Log.e("tag", "Response Contacts" + response);
//                        Toast.makeText(getActivity(), "Response Contacts" + response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jsonArray = jObj.getJSONArray("items");


                            for (int i = 0; i < jsonArray.length() - 1; i++) {
                                JSONObject objItem = jsonArray.getJSONObject(i);
                                JSONObject objSnippet = objItem.getJSONObject("snippet");
                                String tille = objSnippet.getString("title");
                                JSONObject objthumb = objSnippet.getJSONObject("thumbnails");
                                JSONObject objMedium = objthumb.getJSONObject("medium");
                                String image = objMedium.getString("url");


                                model = new ModelChannelList();
                                model.setTitle(tille);
                                model.setImage(image);
                                model.setVideoId(videoId);
                                rowItems.add(model);

                            }
                            adapter.notifyDataSetChanged();
                            listYouTube.setAdapter(adapter);

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
//                            saveCellArray();
                            Toast.makeText(Youtube.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//
//
//
//
//
//
//                return headers;
//            }

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


        RequestQueue requestQueue = Volley.newRequestQueue(Youtube.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        listYouTube.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                videoId = array_videoId.get(i);

//                Toast.makeText(Youtube.this, "array_videoId id is " + videoId, Toast.LENGTH_SHORT).show();
                // youTubeView.initialize(YoutubeDeveloperKey, Youtube.this);
                playVideo(videoId);
//                YPlayer.cueVideo(videoId);
            }
        });

    }

    public void jsonVideoId() {


        String url = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyBmYkJZwxr3_P6vwd6hrCUP6IARyFmqp5E&channelId=UCJVyXztIap_igWjLa8B5-sw&part=snippet&order=date&maxResults=5";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                                    Toast.makeText(Registration.this, response, Toast.LENGTH_LONG).show();
                        Log.e("tag", "Response Contacts" + response);
//                        Toast.makeText(getActivity(), "Response Contacts" + response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jsonArray = jObj.getJSONArray("items");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objItem = jsonArray.getJSONObject(i);
                                JSONObject objid = objItem.getJSONObject("id");
                                String video = objid.getString("videoId");

                                array_videoId.add(video);

                                Log.e("tag", "videoId is " + array_videoId);
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
//                            saveCellArray();
                            Toast.makeText(Youtube.this, "No internet Access, Check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//
//
//
//
//
//
//                return headers;
//            }

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


        RequestQueue requestQueue = Volley.newRequestQueue(Youtube.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        listYouTube.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                videoId = array_videoId.get(i);
//                Toast.makeText(Youtube.this, "vid id is " + videoId, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YPlayer = null;

    }
}
