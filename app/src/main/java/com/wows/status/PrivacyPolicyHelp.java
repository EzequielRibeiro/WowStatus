package com.wows.status;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PrivacyPolicyHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        String help = "no";

        Bundle bundle = getIntent().getBundleExtra("helpB");

        if(bundle != null)
         help = bundle.getString("help","no");

        WebView webView = (WebView) findViewById(R.id.webViewPolicy);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        if(help.equals("yes"))
          webView.loadUrl("file:///android_asset/help.html");
        else
          webView.loadUrl("file:///android_asset/privacy_policy.html");


    }
}
