package com.wows.status;

//https://api.worldofwarships.com/wows/clans/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=br
//https://developer.android.com/training/data-storage/shared-preferences#java

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnFailureListener;


public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton radioButtonCountry, radioButtonSearch;
    RadioButton radioButtonNa, radioButtonEu, radioButtonRu, radioButtonAsia, radioButtonClan, radioButtonPlayer;
    private String countrySelected, searchSelected;
    private ListView listView;
    private AdView mAdView;
    private TextView textViewNa, textViewEu, textViewRu, textViewAsia, textViewServerVersion;
    private UserAdapter userAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean multipleNick = false;
    private String[] listNicks;
    private HttpGetRequest getRequest;
    private ProgressBar progressBar;
    private EditText editText;
    private SharedPreferences prefs;

    public static void rateApp(final Activity activity) throws Exception {
        final ReviewManager reviewManager = ReviewManagerFactory.create(activity);
        //reviewManager = new FakeReviewManager(this);
        com.google.android.play.core.tasks.Task<ReviewInfo> request = reviewManager.requestReviewFlow();

        request.addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(com.google.android.play.core.tasks.Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    Log.e("Rate Task", "Complete");
                    ReviewInfo reviewInfo = task.getResult();
                    com.google.android.play.core.tasks.Task<Void> flow = reviewManager.launchReviewFlow(activity, reviewInfo);
                    flow.addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(com.google.android.play.core.tasks.Task<Void> task) {
                            Log.e("Rate Flow", "Complete");
                        }
                    });

                    flow.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            activity.getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
                            Log.e("Rate Flow", "Fail");
                            e.printStackTrace();
                        }
                    });

                } else {
                    activity.getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
                    Log.e("Rate Task", "Fail");
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                activity.getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
                e.printStackTrace();
                Log.e("Rate Request", "Fail");
            }
        });

    }

    public static void showRequestRateApp(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Feedback");
        builder.setMessage(activity.getString(R.string.rate_msg));
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    rateApp(activity);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(exception);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTab);

        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_item_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBarContent);

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    getRequest.cancel(true);
                }

            }
        });


        radioButtonNa = (RadioButton) findViewById(R.id.radioButtonNa);
        radioButtonNa.setOnClickListener(this);

        radioButtonEu = (RadioButton) findViewById(R.id.radioButtonEu);
        radioButtonEu.setOnClickListener(this);

        radioButtonRu = (RadioButton) findViewById(R.id.radioButtonRu);
        radioButtonRu.setOnClickListener(this);

        radioButtonAsia = (RadioButton) findViewById(R.id.radioButtonAsia);
        radioButtonAsia.setOnClickListener(this);

        radioButtonClan = (RadioButton) findViewById(R.id.radioButtonClan);
        radioButtonClan.setOnClickListener(this);

        radioButtonPlayer = (RadioButton) findViewById(R.id.radioButtonPlayer);
        radioButtonPlayer.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        editText = (EditText) findViewById(R.id.editText);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setBackgroundColor(getResources().getColor(R.color.bar_background));


        prefs = getSharedPreferences("info", MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!prefs.contains("version"))
                    prefs.edit().putString("version", new StatusServer().getServeVersion()).commit();
            }
        }).start();

        fab.setOnClickListener(new View.OnClickListener() {

                                   int maxLength;

                                   @Override
                                   public void onClick(View view) {

                                       radioButtonSelected();

                                       if (searchSelected.equals("clan"))
                                           maxLength = 2;
                                       else
                                           maxLength = 3;

                                       if (checkNetworkConnection())
                                           if (editText.getText().length() >= maxLength) {

                                               if (editText.getText().toString().contains(",")) {
                                                   multipleNick = true;
                                                   listNicks = editText.getText().toString().replaceAll("\\s+", "").split(",");
                                               }

                                               if (checkNetworkConnection()) {

                                                   listView.setFocusableInTouchMode(true);
                                                   listView.requestFocus();
                                                   hideKeyboard();
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

        mAdView.setAdListener(new AdListener() {


            @Override
            public void onAdLoaded() {
                if (mAdView.getVisibility() == View.GONE)
                    mAdView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

                if (errorCode == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.i("admob", String.valueOf(errorCode));

                    mAdView.setVisibility(View.GONE);

                    Bundle bundle = new Bundle();
                    bundle.putString("ERROR", String.valueOf(errorCode));
                    bundle.putString("COUNTRY", getResources().getConfiguration().locale.getDisplayCountry());
                    mFirebaseAnalytics.logEvent("ADMOB", bundle);
                }

            }


        });

        int rated = getSharedPreferences("rated", MODE_PRIVATE).getInt("time", 0);
        getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", rated + 1).commit();

        if (rated == 3) {
            showRequestRateApp(ScrollingActivity.this);
        } else if (rated == 40) {
            getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
        }

    }


    private void serverStatus() {

        textViewNa = (TextView) findViewById(R.id.textViewServerNA);
        textViewEu = (TextView) findViewById(R.id.textviewServerEU);
        textViewRu = (TextView) findViewById(R.id.textViewServerRU);
        textViewAsia = (TextView) findViewById(R.id.textViewServerAsia);
        textViewServerVersion = (TextView) findViewById(R.id.textViewServerVersion);

        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    StatusServer statusServer = new StatusServer();

                    @Override
                    public void run() {
                        textViewNa.setText(statusServer.getNa());
                        textViewEu.setText(statusServer.getEu());
                        textViewRu.setText(statusServer.getRu());
                        textViewAsia.setText(statusServer.getAsia());
                        textViewServerVersion.setText("Game version: " + prefs.getString("version", statusServer.getServeVersion()));
                    }
                });

            }
        }).start();


    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = editText;
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private void progressDialogShow() {

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

                if (getRequest != null)
                    getRequest.cancel(true);

            }
        });

        progressDialog.show();


    }

    private void radioButtonSelected() {

        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroupCountry);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroupSearch);

        int selectedIdCountry = radioGroup1.getCheckedRadioButtonId();
        int selectedIdSearch = radioGroup2.getCheckedRadioButtonId();

        radioButtonCountry = (RadioButton) findViewById(selectedIdCountry);
        radioButtonSearch = (RadioButton) findViewById(selectedIdSearch);

        countrySelected = radioButtonCountry.getTag().toString();
        searchSelected = radioButtonSearch.getTag().toString();


    }

    private void request(String name, String country) {

        //Some url endpoint that you may have
        String myUrl = "https://api.worldofwarships" + country + "/wows/account/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=" + name;
        String myUrlMultipleNick = "https://api.worldofwarships" + country + "/wows/account/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=";
        String myUrlClan = "https://api.worldofwarships" + country + "/wows/clans/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=" + name + "&fields=clan_id%2Ctag";

        String result;
        String temp = "";
        String name_ = "nickname";
        String account_id = "account_id";


        if (multipleNick) {

            for (int i = 0; i < listNicks.length; i++) {

                if (i != (listNicks.length - 1))
                    temp += listNicks[i] + "%2C";

            }

            temp += listNicks[listNicks.length - 1];

            myUrl = myUrlMultipleNick + temp + "&type=exact";

            multipleNick = false;

        }

        getRequest = new HttpGetRequest(progressBar);
        //Perform the doInBackground method, passing in our url

        try {

            if (searchSelected.equals("clan")) {

                result = getRequest.execute(myUrlClan).get();
                name_ = "tag";
                account_id = "clan_id";

            } else {

                result = getRequest.execute(myUrl).get();

            }

            JSONObject jObj = new JSONObject(result);

            JSONArray arr = jObj.getJSONArray("data");

            if (arr.length() > 0) {
                ArrayList<User> arrayList = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject mJsonObject = arr.getJSONObject(i);

                    arrayList.add(new User(mJsonObject.getString(name_), mJsonObject.getString(account_id)));


                }

                userAdapter = new UserAdapter(this, arrayList);
                listView.setAdapter(userAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        User user = (User) parent.getItemAtPosition(position);
                        Bundle b = new Bundle();

                        if (searchSelected.equals("clan")) {

                            b.putString("clanId", user.getUserId());
                            b.putString("country", countrySelected);

                            Intent intent = new Intent(getApplicationContext(), ClanActivity.class);
                            intent.putExtra("clanBundle", b);
                            startActivity(intent);


                        } else {

                            b.putString("id", user.getUserId());
                            b.putString("country", countrySelected);

                            Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                            intent.putExtras(b);
                            startActivity(intent);
                        }


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

        restorePrefs();

        //clear list of ship to new values
        SingletonsClass singletonsClass = SingletonsClass.getInstance();
        singletonsClass.clear();

        if (checkNetworkConnection())
            serverStatus();


        if (mAdView != null) {
            mAdView.resume();
        }


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

        MenuItem menuItem = menu.findItem(R.id.action_version);
        menuItem.setTitle("v" + BuildConfig.VERSION_NAME);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_privacy_policy) {

            Intent intent = new Intent(getBaseContext(), PrivacyPolicyHelp.class);
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact) {

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_contact)));
            startActivity(i);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {

            Bundle bundle = new Bundle();
            bundle.putString("help", "yes");
            Intent i = new Intent(getBaseContext(), PrivacyPolicyHelp.class);
            i.putExtra("helpB", bundle);
            i.putExtras(bundle);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }


    private void restorePrefs() {


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        searchSelected = preferences.getString("searchSelected", "player");
        countrySelected = preferences.getString("countrySelected", ".com");

        editText.setText(preferences.getString("text", ""));


        if (searchSelected.equals("player"))
            radioButtonPlayer.setChecked(true);
        else
            radioButtonClan.setChecked(true);


        switch (countrySelected) {

            case ".com":
                radioButtonNa.setChecked(true);
                break;
            case ".eu":
                radioButtonEu.setChecked(true);
                break;
            case ".ru":
                radioButtonRu.setChecked(true);
                break;
            case ".asia":
                radioButtonAsia.setChecked(true);
                break;


        }


    }

    private void savePrefs() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("text", editText.getText().toString());
        editor.putString("searchSelected", searchSelected);
        editor.putString("countrySelected", countrySelected);
        editor.apply();


    }

    @Override
    public void onClick(View v) {

        if (userAdapter != null) {

            userAdapter.clear();
            userAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }
        savePrefs();
        super.onPause();
    }


    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}
