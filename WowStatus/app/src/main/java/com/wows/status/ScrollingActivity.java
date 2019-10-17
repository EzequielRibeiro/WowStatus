package com.wows.status;

//https://api.worldofwarships.com/wows/clans/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=br


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton radioButton;
    private String countrySelected;
    private ListView listView;
    private AdView mAdView;
    private TextView textViewNa, textViewEu, textViewRu, textViewAsia;
    private UserAdapter userAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean multipleNick = false;
    private String [] listNicks;
    private HttpGetRequest getRequest;
    private FrameLayout frameLayoutContent;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

       // Crashlytics.getInstance().crash();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTab);

        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_item_content);
       // webViewContent = (WebView) findViewById(R.id.webViewContent);
        progressBar = (ProgressBar) findViewById(R.id.progressBarContent);

       // webViewContent.loadUrl("file:///android_asset/help.html");


        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    getRequest.cancel(true);
                }

            }
        });

        /*
        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.getSettings().setAppCacheEnabled(true);


        if(checkNetworkConnection())
            webViewContent.loadUrl(getString(R.string.url_web));
        else
            webViewContent.loadUrl("file:///android_asset/error.html");


        webViewContent.setWebViewClient(new WebViewClient() {


            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }



         public  void onPageFinished(WebView view, String url){

             if(progressBar.getVisibility() == View.VISIBLE){

                 progressBar.setVisibility(View.GONE);

             }

         }

          public void onPageStarted(WebView view, String url, Bitmap favicon){

              if(progressBar.getVisibility() == View.GONE){

                  progressBar.setVisibility(View.VISIBLE);

              }

          }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
               // Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                view.loadUrl("file:///android_asset/error.html");
            }
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        */


        RadioButton radioButtonNa = (RadioButton) findViewById(R.id.radioButtonNa);
        radioButtonNa.setOnClickListener(this);
        RadioButton radioButtonEu = (RadioButton) findViewById(R.id.radioButtonEu);
        radioButtonEu.setOnClickListener(this);
        RadioButton radioButtonru = (RadioButton) findViewById(R.id.radioButtonRu);
        radioButtonru.setOnClickListener(this);
        RadioButton radioButtonAsia = (RadioButton) findViewById(R.id.radioButtonAsia);
        radioButtonAsia.setOnClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            EditText editText = (EditText) findViewById(R.id.editText);


            @Override
            public void onClick(View view) {

                radioButtonSelected();


                if (checkNetworkConnection())
                    if (editText.getText().length() >= 3) {

                        if(editText.getText().toString().contains(",")){
                              multipleNick = true;
                              listNicks = editText.getText().toString().replaceAll("\\s+","").split(",");
                          }

                          if(checkNetworkConnection()) {

                              request(editText.getText().toString(), countrySelected);
                          }


                    } else {

                        Snackbar.make(view, R.string.enter_text, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }

                           }
        }


        );

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener(){

            @Override
            public void onAdLoaded() {
                if(mAdView.getVisibility() == View.GONE)
                    mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

                    mAdView.setVisibility(View.GONE);
                    Log.i("admob error", String.valueOf(errorCode));

            }
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("admob", "carregado");
            }


        });


    }


    private void serverStatus() {


        textViewNa = (TextView) findViewById(R.id.textViewServerNA);
        textViewEu = (TextView) findViewById(R.id.textviewServerEU);
        textViewRu = (TextView) findViewById(R.id.textViewServerRU);
        textViewAsia = (TextView) findViewById(R.id.textViewServerAsia);


                runOnUiThread(new Runnable() {

                    StatusServer statusServer = new StatusServer();

                    @Override
                    public void run() {

                        textViewNa.setText(statusServer.getNa());
                        textViewEu.setText(statusServer.getEu());
                        textViewRu.setText(statusServer.getRu());
                        textViewAsia.setText(statusServer.getAsia());

                    }
                });




    }

    private void progressBarShow(){


        if(progressBar.getVisibility() == View.GONE){

            progressBar.setVisibility(View.VISIBLE);

        }

    }

    private void progressDialogShow(){

        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(ScrollingActivity.this);
        //progressDialog.setMax(100);
        progressDialog.setMessage(getString(R.string.progress_dialog_mensagem));
        //  progressDialog.setTitle(getString(R.string.progress_dialog_mensagem));
        // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                if(getRequest != null)
                getRequest.cancel(true);

            }
        });

        progressDialog.show();


    }

    private void radioButtonSelected() {

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupCountry);

        int selectedId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(selectedId);

        countrySelected = radioButton.getTag().toString();


    }

    private void request(String namePlayer, String country) {

        //Some url endpoint that you may have
        String myUrl = "https://api.worldofwarships"+country+"/wows/account/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=" + namePlayer;
        String myUrlMultipleNick = "https://api.worldofwarships"+country+"/wows/account/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=";
        //String to place our result in
        String result;


        String temp = "";

            if(multipleNick){

                for(int i = 0; i < listNicks.length;i++){

                    if(i != (listNicks.length - 1))
                    temp += listNicks[i] + "%2C";

                 }

              temp += listNicks[listNicks.length-1];

              myUrl = myUrlMultipleNick + temp + "&type=exact";

              multipleNick = false;

            }

        progressBarShow();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                getRequest = new HttpGetRequest(progressBar);

                try {
                    result = getRequest.execute(myUrl).get();

                    JSONObject jObj = new JSONObject(result);

                    JSONArray arr = jObj.getJSONArray("data");

                    if (arr.length() > 0) {
                        ArrayList<User> arrayList = new ArrayList<>();

                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject mJsonObject = arr.getJSONObject(i);

                            arrayList.add(new User(mJsonObject.getString("nickname"), mJsonObject.getString("account_id")));


                        }

                        userAdapter = new UserAdapter(this, arrayList);
                        listView.setAdapter(userAdapter);

            }
        });




                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        User user = (User) parent.getItemAtPosition(position);

                        Bundle b = new Bundle();
                        b.putString("id", user.getUserId());
                        b.putString("country", countrySelected);

                        Intent intent = new Intent(getApplicationContext(), activity_tab.class);
                        intent.putExtras(b);

                        startActivity(intent);


                    }
                });


            } else {

                Toast.makeText(this, "Not Found", Toast.LENGTH_LONG).show();

            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException j) {
            j.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();


        }


    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkNetworkConnection())
            serverStatus();



    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {

            isConnected = true;

        } else {

            Toast.makeText(this, getText(R.string.no_connection), Toast.LENGTH_LONG).show();

        }

        return isConnected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {


        if(userAdapter != null){

            userAdapter.clear();
            userAdapter.notifyDataSetChanged();

        }


    }
}
