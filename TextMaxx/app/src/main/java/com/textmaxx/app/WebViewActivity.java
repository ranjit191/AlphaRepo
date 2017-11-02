package com.textmaxx.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.textmaxx.R;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.ViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.firstImage)
    ImageView firstImage;
    @Bind(R.id.txt_title)
    TextView txt_title;
    TextView close;

    SharedPreferences prefs;
    String title;


    public static void createInstance(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(GlobalConstants.URL, url);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        ButterKnife.bind(this);
        prefs = getApplicationContext().getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
        title = prefs.getString("title", "");
        txt_title.setText(title);
        firstImage.setVisibility(View.VISIBLE);
        firstImage.setClickable(true);
        firstImage.setImageResource(R.drawable.left_icon);
        firstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        OpeningWebView(RecevingURLfromIntent());

    }//End of OnCreate

    /**
     * @return URL as String received in Intent from other screen which has to be opened.
     */
    private String RecevingURLfromIntent() {
        return getIntent().getStringExtra(GlobalConstants.URL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    /**
     * @param url URL is passed into parameter which has to be opened in webview.
     */
    private void OpeningWebView(String url) {
        WebView wb = (WebView) findViewById(R.id.webView);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        //wb.getSettings().setPluginsEnabled(true);
        wb.setWebViewClient(new HelloWebViewClient());
        wb.loadUrl(url);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ViewUtil.hideProgressDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ViewUtil.showProgressDialog(WebViewActivity.this);
        }
    }
}
