package com.wows.status;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class GraphicFragment extends Fragment {


    private WebView webViewIndex;
    private ProgressBar progressBarChart;
    int usa = 0, japan = 0, ussr = 0, germany = 0, uk = 0, france = 0, italy = 0,europe = 0,netherlands = 0,spain = 0, panAsia = 0, panAmerica = 0, commonWealth = 0, poland = 0;
    int cruiser = 0, battleship = 0, destroyer = 0, carrier = 0, submarine = 0;
    private int[] tier;
    private SingletonsClass singletonsClass;
    private Map<String, Ship> shipMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graphic, container, false);
        singletonsClass = SingletonsClass.getInstance();
        tier = new int[11];
        webViewIndex = v.findViewById(R.id.webViewIndex);
        progressBarChart = v.findViewById(R.id.progressBarChart);

        final String id = getArguments().getString("id");
        final String country = getArguments().getString("country");

        if (singletonsClass.getListShips().size() == 0) {
            shipMap = new HashMap<>();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    requestShipData(id, country);

                }
            }).start();


        }else{

            resultToGrafic(singletonsClass.getEntry());


        }

        return v;
    }





    private void requestShipData(final String idPlayer, String country) {


        final String urlBattles = "https://api.worldofwarships" + country + "/wows/ships/stats/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + idPlayer + "&fields=pvp.battles%2Cship_id%2Cpvp.wins%2Cpvp.frags" +
                "%2Cpvp.planes_killed%2Cpvp.max_planes_killed%2Cpvp.max_damage_dealt%2Cpvp.max_xp%2Cpvp.xp%2Cpvp.torpedoes.frags%2Cpvp.torpedoes.hits";
        final String urlShip = "https://api.worldofwarships.com/wows/encyclopedia/ships/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&ship_id=";
        final String urlShipFinal = "&fields=nation%2Ctype%2Cname%2Ctier%2Cship_id";


                try {

                    String result;
                    String shipListId = "";

                    HttpGetRequest getRequest = new HttpGetRequest(getContext());

                    result = getRequest.execute(urlBattles).get();
                    Ship ship;
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject objData = (JSONObject) jsonObject.get("data");
                    JSONArray objShips = objData.getJSONArray(idPlayer);


                    //check if value is above 100
                    if (objShips.length() > 100) {

                        int timeOfFor = (int) (objShips.length() / 100);
                        int sum = 0;

                        // for every 100 ships a new url is created
                        for (int i = 0; i < timeOfFor; i++) {
                            for (int j = 0; j < 100; j++) {
                                JSONObject jsonobject = objShips.getJSONObject(sum + j);
                                JSONObject jsonPVP = jsonobject.getJSONObject("pvp");
                                JSONObject jsonTorpedoes = jsonPVP.getJSONObject("torpedoes");
                                shipListId += jsonobject.getString("ship_id") + "%2C";

                                ship = new Ship();
                                ship.setBattles(jsonPVP.getString("battles"));
                                ship.setWins(jsonPVP.getString("wins"));
                                ship.setKill(jsonPVP.getString("frags"));
                                ship.setPlanes_killed(jsonPVP.getString("planes_killed"));
                                ship.setMax_planes_killed(jsonPVP.getString("max_planes_killed"));
                                ship.setMax_damage_dealt(jsonPVP.getString("max_damage_dealt"));
                                ship.setMax_xp(jsonPVP.getString("max_xp"));
                                ship.setTotal_xp(jsonPVP.getString("xp"));
                                ship.setTorpedoes_frags(jsonTorpedoes.getString("frags"));
                                ship.setTorpedoes_hits(jsonTorpedoes.getString("hits"));
                                shipMap.put(jsonobject.getString("ship_id"), ship);


                            }
                            sum += 100;

                            shipListId = urlShip + shipListId + urlShipFinal;
                            rateCruiserNation(shipListId);
                            shipListId = "";


                        }

                        //check if there is a ship left
                        if (sum < objShips.length()) {

                            for (int j = sum; j < objShips.length(); j++) {
                                JSONObject jsonobject = objShips.getJSONObject(j);
                                JSONObject jsonPVP = jsonobject.getJSONObject("pvp");
                                JSONObject jsonTorpedoes = jsonPVP.getJSONObject("torpedoes");
                                shipListId += jsonobject.getString("ship_id") + "%2C";

                                ship = new Ship();
                                ship.setBattles(jsonPVP.getString("battles"));
                                ship.setWins(jsonPVP.getString("wins"));
                                ship.setKill(jsonPVP.getString("frags"));
                                ship.setPlanes_killed(jsonPVP.getString("planes_killed"));
                                ship.setMax_planes_killed(jsonPVP.getString("max_planes_killed"));
                                ship.setMax_damage_dealt(jsonPVP.getString("max_damage_dealt"));
                                ship.setMax_xp(jsonPVP.getString("max_xp"));
                                ship.setTotal_xp(jsonPVP.getString("xp"));
                                ship.setTorpedoes_frags(jsonTorpedoes.getString("frags"));
                                ship.setTorpedoes_hits(jsonTorpedoes.getString("hits"));
                                shipMap.put(jsonobject.getString("ship_id"), ship);

                            }

                            shipListId = urlShip + shipListId + urlShipFinal;
                            rateCruiserNation(shipListId);
                            resultToGrafic(shipMap);


                        }


                    } else {


                        for (int j = 0; j < objShips.length(); j++) {
                            JSONObject jsonobject = objShips.getJSONObject(j);
                            JSONObject jsonPVP = jsonobject.getJSONObject("pvp");
                            JSONObject jsonTorpedoes = jsonPVP.getJSONObject("torpedoes");
                            shipListId += jsonobject.getString("ship_id") + "%2C";

                            ship = new Ship();
                            ship.setBattles(jsonPVP.getString("battles"));
                            ship.setWins(jsonPVP.getString("wins"));
                            ship.setKill(jsonPVP.getString("frags"));
                            ship.setPlanes_killed(jsonPVP.getString("planes_killed"));
                            ship.setMax_planes_killed(jsonPVP.getString("max_planes_killed"));
                            ship.setMax_damage_dealt(jsonPVP.getString("max_damage_dealt"));
                            ship.setMax_xp(jsonPVP.getString("max_xp"));
                            ship.setTotal_xp(jsonPVP.getString("xp"));
                            ship.setTorpedoes_frags(jsonTorpedoes.getString("frags"));
                            ship.setTorpedoes_hits(jsonTorpedoes.getString("hits"));
                            shipMap.put(jsonobject.getString("ship_id"), ship);
                        }

                        shipListId = urlShip + shipListId + urlShipFinal;
                        rateCruiserNation(shipListId);
                        resultToGrafic(shipMap);

                    }

                    singletonsClass.setEntry(shipMap);


                } catch (JSONException | NullPointerException | InterruptedException |
                        ExecutionException | ClassCastException j) {
                    j.printStackTrace();
                }


    }

    private void resultToGrafic( Map<String, Ship> shipMap_) {


        for (Map.Entry<String, Ship> entry : shipMap_.entrySet()) {

            switch (entry.getValue().getNation()) {
                case "uk":
                    uk += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "japan":
                    japan += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "pan_asia":
                    panAsia += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "pan_america":
                    panAmerica += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "france":
                    france += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "germany":
                    germany += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "usa":
                    usa += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "ussr":
                    ussr += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "europe":
                    europe += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "netherlands":
                    netherlands += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "spain":
                    spain += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "commonWealth":
                    commonWealth += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "poland":
                    poland += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "italy":
                    italy += Integer.parseInt(entry.getValue().getBattles());
                    break;

            }

            switch (entry.getValue().getType()) {
                case "Cruiser":
                    cruiser = cruiser + Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "Battleship":
                    battleship = battleship + Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "Destroyer":
                    destroyer = destroyer + Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "AirCarrier":
                    carrier = carrier + Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "Submarine":
                    submarine = submarine + Integer.parseInt(entry.getValue().getBattles());
                    break;

            }

            switch (entry.getValue().getTier()) {
                case "1":
                    tier[0] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "2":
                    tier[1] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "3":
                    tier[2] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "4":
                    tier[3] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "5":
                    tier[4] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "6":
                    tier[5] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "7":
                    tier[6] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "8":
                    tier[7] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "9":
                    tier[8] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "10":
                    tier[9] += Integer.parseInt(entry.getValue().getBattles());
                    break;
                case "11":
                    tier[10] += Integer.parseInt(entry.getValue().getBattles());
                    break;

            }

        }


     if(getActivity() != null)
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                webViewIndex.addJavascriptInterface(new WebInterface(), "WebInterface");
                // webViewType.getSettings().setDomStorageEnabled(true);
                // webViewType.getSettings().setAppCacheMaxSize( 10 * 1024 * 1024 );
                webViewIndex.getSettings().setJavaScriptEnabled(true);
                webViewIndex.getSettings().setDomStorageEnabled(true);
              // webViewIndex.getSettings().setUserAgentString(Locale.getDefault().getLanguage());
                webViewIndex.setWebViewClient(new WebViewClient() {


                    public void onPageFinished(WebView view, String url) {

                        if (progressBarChart.getVisibility() == View.VISIBLE) {

                            progressBarChart.setVisibility(View.GONE);

                        }

                    }

                    public void onPageStarted(WebView view, String url, Bitmap favicon) {

                        if (progressBarChart.getVisibility() == View.GONE) {

                            progressBarChart.setVisibility(View.VISIBLE);

                        }

                    }


                    @SuppressWarnings("deprecation")
                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        // Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                        view.loadUrl("file:///android_asset/error.html");
                        Log.e("error", String.valueOf(errorCode));
                    }

                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {

                        Log.e("error", String.valueOf(rerr.getErrorCode()));
                        // Redirect to deprecated method, so you can use it in all SDK versions
                        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());

                    }


                });
                webViewIndex.clearCache(false);
                webViewIndex.loadUrl("file:///android_asset/index.html");


            }
        });


    }

    private void rateCruiserNation(final String url) {


        try {

            String result;

            HttpGetRequest getRequest = new HttpGetRequest(getContext());

            result = getRequest.execute(url).get();

            JSONObject jsonObject = new JSONObject(result);
            JSONObject objData = (JSONObject) jsonObject.get("data");

            Iterator<String> iter = objData.keys();

            String nation, type, tier, id, name;

            while (iter.hasNext()) {

                String key = iter.next();

                if (!objData.isNull(key)) {

                    nation = objData.getJSONObject(key).optString("nation");
                    type = objData.getJSONObject(key).optString("type");
                    tier = objData.getJSONObject(key).optString("tier");
                    id = objData.getJSONObject(key).optString("ship_id");
                    name = objData.getJSONObject(key).optString("name");


                    Ship ship = new Ship(id, name, tier, nation, type,
                            Objects.requireNonNull(shipMap.get(id)).getBattles(),
                            Objects.requireNonNull(shipMap.get(id)).getWins(),
                            Objects.requireNonNull(shipMap.get(id)).getKill(),
                            Objects.requireNonNull(shipMap.get(id)).getPlanes_killed(),
                            Objects.requireNonNull(shipMap.get(id)).getMax_planes_killed(),
                            Objects.requireNonNull(shipMap.get(id)).getMax_damage_dealt(),
                            Objects.requireNonNull(shipMap.get(id)).getMax_xp() ,
                            Objects.requireNonNull(shipMap.get(id)).getTotal_xp() ,
                            Objects.requireNonNull(shipMap.get(id)).getTorpedoes_frags(),
                            Objects.requireNonNull(shipMap.get(id)).getTorpedoes_hits());
                    //Log.e("ship",ship.toString());
                    shipMap.put(id,ship);
                    singletonsClass.setAddShipsList(ship);

                }

            }

        } catch (JSONException | InterruptedException | NullPointerException | ExecutionException |
                ClassCastException j) {
            j.printStackTrace();
        }

    }


    private class WebInterface {

        @JavascriptInterface
        public int getCruiser() {
            return cruiser;
        }

        @JavascriptInterface
        public int getBattleship() {
            return battleship;
        }

        @JavascriptInterface
        public int getDestroyer() {
            return destroyer;
        }

        @JavascriptInterface
        public int getCarrier() {
            return carrier;
        }

        @JavascriptInterface
        public int getSubmarine() {
            return submarine;
        }

       //country
        @JavascriptInterface
        public int getUsa() {
            return usa;
        }

        @JavascriptInterface
        public int getGerman() {
            return germany;
        }

        @JavascriptInterface
        public int getItaly() {
            return italy;
        }

        @JavascriptInterface
        public int getEurope() {
            return europe;
        }

        @JavascriptInterface
        public int getUssr() {
            return ussr;
        }

        @JavascriptInterface
        public int getUk() {
            return uk;
        }

        @JavascriptInterface
        public int getJapan() {
            return japan;
        }

        @JavascriptInterface
        public int getFrance() {
            return france;
        }

        @JavascriptInterface
        public int getAsia() {
            return panAsia;
        }

        @JavascriptInterface
        public int getPanAmerica() {
            return panAmerica;
        }

        @JavascriptInterface
        public int getCommon() {
            return commonWealth;
        }

        @JavascriptInterface
        public int getPoland() {
            return poland;
        }

        @JavascriptInterface
        public int getSpain() {
            return spain;
        }

        @JavascriptInterface
        public int getNetherlands() {
            return netherlands;
        }
        //end country


        @JavascriptInterface
        public int getTier1() {
            return tier[0];
        }

        @JavascriptInterface
        public int getTier2() {
            return tier[1];
        }

        @JavascriptInterface
        public int getTier3() {
            return tier[2];
        }

        @JavascriptInterface
        public int getTier4() {
            return tier[3];
        }

        @JavascriptInterface
        public int getTier5() {
            return tier[4];
        }

        @JavascriptInterface
        public int getTier6() {
            return tier[5];
        }

        @JavascriptInterface
        public int getTier7() {
            return tier[6];
        }

        @JavascriptInterface
        public int getTier8() {
            return tier[7];
        }

        @JavascriptInterface
        public int getTier9() {
            return tier[8];
        }

        @JavascriptInterface
        public int getTier10() {
            return tier[9];
        }

        @JavascriptInterface
        public int getTier11() {return tier[10];}


    }


}
