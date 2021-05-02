package com.wows.status;

//https://api.worldofwarships.com/wows/clans/list/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&search=br
//https://developer.android.com/training/data-storage/shared-preferences#java

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnFailureListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String alertTranslate = "The application will be translated into your language in a few seconds.";
    private final static String[] MENSAGEM_ARRAY = {"Created", "Last Battle", "Win Rate", "Killed / was killed",
            "High Caliber", "Kraken", "Devastating", "Double Kill", "Detonation", "Confederate", "Death by Secondary", "Drying", "Dreadnought",
            "Liquidator", "Fireproof", "Arsionist", "Retribution", "First Death", "Die Hard", "Air Defense", "Battles", "Wins", "Defeat", "Tied Game", "Max XP", "Max XP with",
            "Max Planes Destroyed", "Max Planes Destroyed with", "Planes destroyed", "Max Damage", "Max Damage with", "Total Damage",
            "Average Damage", "Max ship destroyed", "Max ship destroyed with", "Destroyed ships", "Average destroyed ships",
            "Survived battles", "Survived with wins", "Click here for version details", "Search", "Profile", "Chart", "Progress", "Ships Details",
            "not found", "Battle for Types", "Cruiser", "Battleship", "Destroyer", "Carrier", "Battle for Nation", "USA", "German", "USSR", "UK",
            "Japan", "France", "Pan Asia", "Battle for Tier", "Your progress: Total Battles, Number of Battles", "Last 28 days played", "Ship Name",
            "Tier", "Type", "Nation", "No games played in the last 28 days"};
    private final static String LOCALE_DEFAULT = "en";
    private RadioButton radioButtonCountry, radioButtonSearch;
    private RadioButton radioButtonNa, radioButtonEu, radioButtonRu, radioButtonAsia, radioButtonClan, radioButtonPlayer;
    private String countrySelected, searchSelected;
    private ListView listView;
    private AdView mAdView;
    private TextView textViewNa, textViewEu, textViewRu, textViewAsia, textViewServerVersion;
    private UserAdapter userAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean multipleNick = false;
    private String[] listNicks;
    private HttpGetRequest httpGetRequest;
    private ProgressBar progressBar;
    private TextInputEditText editText;
    private SharedPreferences prefs;
    private Locale locale;
    private String languageCode;
    private ArrayList<User> arrayList = new ArrayList<>();
    private final String mensagem_to_translate = "Translating application to your language.";

    private String translate(String langFrom, String langTo, String text) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (langFrom.equals(langTo))
            return text;

        final String yourURL = "https://script.google.com/macros/s/AKfycbyHrs_kLCmXJB-fH_mS2ODtud3y0lR4Povq9nE2EqCSBPSiqjF80PNMKJohEV3TrZws/exec";

        try {

            String urlStr = yourURL + "?q=" + URLEncoder.encode(text, "UTF-8") + "&target=" + langTo + "&source="
                    + langFrom;

            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            con.setRequestProperty("encoding", "UTF-8");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();

        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return "";
        }

    }

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

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTab);

        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        locale = getResources().getConfiguration().locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // countryCode = getResources().getConfiguration().getLocales().get(0).getCountry();
            languageCode = getResources().getConfiguration().getLocales().get(0).getLanguage();
            //  countDisplayName = getResources().getConfiguration().getLocales().get(0).getDisplayCountry();
        } else {
            // countryCode = getResources().getConfiguration().locale.getCountry();
            languageCode = getResources().getConfiguration().locale.getLanguage();
            //  countDisplayName = getResources().getConfiguration().locale.getDisplayCountry();
        }
        userAdapter = new UserAdapter(MainActivity.this, arrayList);
        listView = (ListView) findViewById(R.id.list_item_content);
        listView.setAdapter(userAdapter);
        progressBar = (ProgressBar) findViewById(R.id.progressBarContent);

        textViewNa = (TextView) findViewById(R.id.textViewServerNA);
        textViewEu = (TextView) findViewById(R.id.textviewServerEU);
        textViewRu = (TextView) findViewById(R.id.textViewServerRU);
        textViewAsia = (TextView) findViewById(R.id.textViewServerAsia);

        LinearLayout linearlayout = findViewById(R.id.linearLayoutTranslate);
        linearlayout.setVisibility(View.GONE);

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

        editText = (TextInputEditText) findViewById(R.id.editText);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        textViewServerVersion = (TextView) findViewById(R.id.textViewServerVersion);

        textViewServerVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), BrowserActivity.class));
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user = (User) parent.getItemAtPosition(position);
                Bundle b = new Bundle();

                if (searchSelected.equals("clan")) {

                    b.putString("clanId", user.getUserId());
                    b.putString("country", countrySelected);

                    Intent intent = new Intent(MainActivity.this, ClanActivity.class);
                    intent.putExtra("clanBundle", b);
                    startActivity(intent);

                } else {

                    b.putString("id", user.getUserId());
                    b.putString("country", countrySelected);

                    Intent intent = new Intent(MainActivity.this, TabActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }


            }
        });


        prefs = getSharedPreferences("info", MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!prefs.contains("version"))
                    prefs.edit().putString("version", getServeVersion(MainActivity.this)).commit();
                getSharedPreferences("msg", MODE_PRIVATE).edit().putString("text",
                        Html.fromHtml(translate(LOCALE_DEFAULT, languageCode, mensagem_to_translate)).toString()).apply();
            }
        }).start();

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
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mAdView.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("ERROR", String.valueOf(loadAdError));
                bundle.putString("COUNTRY", getResources().getConfiguration().locale.getDisplayCountry());
                mFirebaseAnalytics.logEvent("ADMOB", bundle);

            }

        });

        int rated = getSharedPreferences("rated", MODE_PRIVATE).getInt("time", 0);
        getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", rated + 1).commit();

        if (rated == 5) {
            showRequestRateApp(MainActivity.this);
        } else if (rated == 40) {
            getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
        }

        DBAdapter dbAdapter = new DBAdapter(MainActivity.this);
        if (dbAdapter.getCountMensagem() < MENSAGEM_ARRAY.length) {
            for (String m : MENSAGEM_ARRAY)
                dbAdapter.insertMensagem(m, m);
        }

        editText.setHint(Html.fromHtml(dbAdapter.getMensagemTranslated(41)));
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                radioButtonSelected();
                int maxLength = 0;
                if (searchSelected.equals("clan"))
                    maxLength = 2;
                else
                    maxLength = 3;

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND) {

                    if (checkNetworkConnection())
                        if (editText.getText().length() >= maxLength) {

                            if (editText.getText().toString().contains(",")) {
                                multipleNick = true;
                                listNicks = editText.getText().toString().replaceAll("\\s+", "").split(",");
                            }

                            if (checkNetworkConnection()) {
                                hideKeyboard();
                                request(editText.getText().toString(), countrySelected);
                            }


                        } else {

                            Snackbar.make(v, R.string.enter_text, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }

                    handled = true;
                }
                return handled;
            }
        });
        dbAdapter.close();

    }

    public static String getServeVersion(Context context) {
        String result = "0.0.0.0";
        String url = "https://api.worldofwarships.com/wows/encyclopedia/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&fields=game_version";

        HttpGetRequest httpGetRequest = new HttpGetRequest(context);
        try {
            result = httpGetRequest.execute(url).get();
            JSONObject object = new JSONObject(result);
            JSONObject objectData = object.getJSONObject("data");
            if (objectData != null)
                result = objectData.get("game_version").toString();

        } catch (ExecutionException | NullPointerException e) {
            e.printStackTrace();
            return "0.0.0.0";
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
            return "0.0.0.0";
        }
        return result;


    }

    private void serverStatus() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DBAdapter dbAdapter = new DBAdapter(MainActivity.this);
                textViewServerVersion.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(40) +
                        " " + prefs.getString("version", getServeVersion(MainActivity.this))));
                dbAdapter.close();

                StatusServer statusServer = new StatusServer(MainActivity.this, textViewNa, textViewEu, textViewRu, textViewAsia);
                statusServer.execute();
            }
        });
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
        String temp = "";

        if (multipleNick) {

            for (int i = 0; i < listNicks.length; i++) {

                if (i != (listNicks.length - 1))
                    temp += listNicks[i] + "%2C";

            }

            temp += listNicks[listNicks.length - 1];

            myUrl = myUrlMultipleNick + temp + "&type=exact";

            multipleNick = false;

        }

        httpGetRequest = new HttpGetRequest(progressBar, MainActivity.this, listView, userAdapter, arrayList);

        if (searchSelected.equals("clan")) {
            httpGetRequest.setParams("tag","clan_id");
            httpGetRequest.execute(myUrlClan);
        } else {
            httpGetRequest.setParams("nickname","account_id");
            httpGetRequest.execute(myUrl);
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

        if (id == R.id.action_translate) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(getSharedPreferences("msg", MODE_PRIVATE).getString("text", mensagem_to_translate))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DBAdapter dbAdapter = new DBAdapter(MainActivity.this);
                            dbAdapter.updatetoFalseTranslated();
                            dbAdapter.close();

                            new TranslateTopics(MainActivity.this).execute();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
        }

        if (id == R.id.action_rate) {
            try {
                rateApp(MainActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private class TranslateTopics extends AsyncTask {

        private DBAdapter dbAdapter;
        private Activity activity;
        private LinearLayout linearLayoutTranslate;
        private TextView textViewTranslate;
        private TextView textViewAlertTranslate;


        public TranslateTopics(Context context) {
            dbAdapter = new DBAdapter(context);
            activity = (Activity) context;
            linearLayoutTranslate = ((Activity) context).findViewById(R.id.linearLayoutTranslate);
            textViewTranslate = ((Activity) context).findViewById(R.id.textViewTranslate);
            textViewAlertTranslate = ((Activity) context).findViewById(R.id.textViewAlertTranslate);
            this.linearLayoutTranslate.setVisibility(View.VISIBLE);
            hideKeyboard();

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editText.setEnabled(false);
                    textViewAlertTranslate.setText(Html.fromHtml(translate(LOCALE_DEFAULT, locale.getLanguage(), alertTranslate)));
                }
            });


            final List<DBAdapter.Mensagem> topicList = dbAdapter.getAllMensagem();
            for (int i = 0; i < topicList.size(); i++) {
                String topic = topicList.get(i).getMensagem();
                String translate = "";
                if (!topicList.get(i).getIsTranlated()) {
                    translate = translate(LOCALE_DEFAULT, locale.getLanguage(), topic);

                    final int finalI = i;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewTranslate.setText(finalI + "/" + topicList.size());
                        }
                    });


                    if (translate.length() > 0)
                        dbAdapter.updateMensagem(topicList.get(i).getId(), translate);
                    else
                        dbAdapter.updateMensagem(topicList.get(i).getId(), topic);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayoutTranslate.setVisibility(View.GONE);
                    editText.setEnabled(true);
                }
            });


            for (DBAdapter.Mensagem m : dbAdapter.getAllMensagem()) {
                Log.d("Mensagem", m.getId() + " " + m.getMensagem() + " - " + m.getTranslated());
            }

            dbAdapter.close();


        }
    }


}

