package com.wows.status;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private ProgressBar progressBar;
    private ListView listView;
    private Context context;
    private UserAdapter userAdapter;
    private String name_, account_id;
    private ArrayList<User> arrayList;

    public HttpGetRequest(ProgressBar progressBar, Context context, ListView listView, UserAdapter userAdapter,ArrayList<User> arrayList) {

        this.progressBar = progressBar;
        this.progressBar.setVisibility(View.VISIBLE);
        this.listView = listView;
        this.context = context;
        this.userAdapter = userAdapter;
        this.arrayList = arrayList;
        name_ = "nickname";
        account_id = "account_id";

        if (progressBar != null)
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
            }
    }

    public HttpGetRequest(Context context, ListView listView) {
        name_ = "tag";
        account_id = "clan_id";
        this.context = context;
        this.listView = listView;
    }

    protected String doInBackground(String... params) {
        String stringUrl = params[0];
        String result;
        String inputLine;

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection = (HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSONObject jObj = null;
        try {
            jObj = new JSONObject(result);
            JSONArray arr = jObj.getJSONArray("data");

            if (arr.length() > 0) {

                arrayList.clear();
                 for (int i = 0; i < arr.length(); i++) {

                    JSONObject mJsonObject = arr.getJSONObject(i);
                    arrayList.add(new User(mJsonObject.getString(name_), mJsonObject.getString(account_id)));
                }

                    userAdapter.notifyDataSetChanged();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            DBAdapter dbAdapter = new DBAdapter(context);
            Toast.makeText(context, dbAdapter.getMensagemTranslated(46), Toast.LENGTH_LONG).show();
            dbAdapter.close();
        }


        if (progressBar != null)
            if (progressBar.getVisibility() == View.VISIBLE)
                progressBar.setVisibility(View.GONE);

    }


}