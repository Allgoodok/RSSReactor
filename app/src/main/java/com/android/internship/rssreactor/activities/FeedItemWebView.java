package com.android.internship.rssreactor.activities;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internship.rssreactor.R;

public class FeedItemWebView extends AppCompatActivity {
    private WebView mWebView;
    private Toolbar mToolBar;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_item_web_view);

        mWebView = (WebView) findViewById(R.id.webview);
        mToolBar = (Toolbar) findViewById(R.id.toolbarWebView);
        mTextView = (TextView) findViewById(R.id.toolbarTitleItem);

        final String urlItem = getIntent().getExtras().getString("itemUrl");
        final String urlTitle = getIntent().getExtras().getString("itemTitle");
        Log.d("debug", urlTitle);

        setSupportActionBar(mToolBar);

        mTextView.setTextColor(Color.WHITE);
        mTextView.setText(urlTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mWebView.getSettings();

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(FeedItemWebView.this, description, Toast.LENGTH_SHORT).show();
            }
        });
        mWebView.loadUrl(urlItem);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
