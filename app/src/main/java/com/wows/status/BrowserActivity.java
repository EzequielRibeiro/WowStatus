package com.wows.status;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Stack;


public class BrowserActivity extends AppCompatActivity {

    private Stack<String> stack;
    private WebView webView;
    private ProgressBar progressBar;
    private String urlPage = "https://worldofwarships.com/news/?category=game-updates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stack = new Stack<>();
        stack.push(urlPage);
        progressBar = (ProgressBar) findViewById(R.id.progressBarBrowser);
        webView = (WebView) findViewById(R.id.webViewBrowser);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(urlPage);


    }

    @Override
    public void onBackPressed() {

        if (stack.size() > 1) {
            progressBar.setVisibility(View.VISIBLE);
            stack.pop();
            webView.loadUrl(stack.pop());
        } else
            finish();
        startActivity(new Intent(getApplicationContext(), ScrollingActivity.class));
    }

    public class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            progressBar.setVisibility(View.VISIBLE);
            stack.push(url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.loadUrl(urlPage);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

}