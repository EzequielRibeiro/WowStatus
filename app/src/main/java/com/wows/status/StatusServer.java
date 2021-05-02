package com.wows.status;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static com.wows.status.HttpGetRequest.CONNECTION_TIMEOUT;
import static com.wows.status.HttpGetRequest.READ_TIMEOUT;
import static com.wows.status.HttpGetRequest.REQUEST_METHOD;

public class StatusServer extends AsyncTask {

    private HttpGetRequest httpGetRequest;
    private String na, eu, ru, asia;
    private String[] listServer = {"com", "eu", "ru", "asia"};
    private Context context;
    private String result;
    private TextView textViewNa, textViewEu, textViewRu, textViewAsia;

    public StatusServer(Context context, TextView textViewNa, TextView textViewEu, TextView textViewRu, TextView textViewAsia) {
        this.context = context;
        this.textViewNa = textViewNa;
        this.textViewEu = textViewEu;
        this.textViewRu = textViewRu;
        this.textViewAsia = textViewAsia;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String inputLine;

        try {

            for (String t : listServer) {

                //Create a URL object holding our url
                URL myUrl = new URL("https://api.worldoftanks." + t + "/wgn/servers/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&game=wows");
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                //   InputStreamReader streamReader = new
                //        InputStreamReader( getConnection(stringUrl).getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();

                JSONObject object = new JSONObject(result);
                JSONObject objectData = object.getJSONObject("data");
                JSONArray objArray = objectData.getJSONArray("wows");

                if (t.equals("com")) {

                    na = objArray.getJSONObject(0).get("players_online").toString();

                } else if (t.equals("eu")) {

                    eu = objArray.getJSONObject(0).get("players_online").toString();
                } else if (t.equals("ru")) {

                    ru = objArray.getJSONObject(0).get("players_online").toString();
                } else if (t.equals("asia")) {

                    asia = objArray.getJSONObject(0).get("players_online").toString();
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return null;
}

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewNa.setText(na);
                textViewEu.setText(eu);
                textViewRu.setText(ru);
                textViewAsia.setText(asia);

            }
        });

    }
}
