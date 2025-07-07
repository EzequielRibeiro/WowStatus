package com.wows.status;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Stack;


public class BrowserActivity extends AppCompatActivity {

    private Stack<String> stack;
    private WebView webView;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stack = new Stack<>();
        String urlPage = "https://blog.worldofwarships.com";
        stack.push(urlPage);
        progressBar = (ProgressBar) findViewById(R.id.progressBarBrowser);
        webView = (WebView) findViewById(R.id.webViewBrowser);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(urlPage);


    }

    @Override
    public void onBackPressed() {

        if (stack.size() > 1) {
            progressBar.setVisibility(View.VISIBLE);
            stack.pop();
            webView.loadUrl(stack.pop());
        } else {
            super.onBackPressed();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            progressBar.setVisibility(View.VISIBLE);
            if (!stack.contains(url)) {
                stack.push(url);
            }
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Toast.makeText(BrowserActivity.this, "Internet Connection error: " + error.getDescription().toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

}