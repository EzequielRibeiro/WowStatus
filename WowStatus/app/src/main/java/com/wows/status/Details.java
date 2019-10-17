package com.wows.status;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

import java.util.concurrent.ExecutionException;

public class Details extends Fragment {


    TextView nickNameView;
    TextView createdAtView;
    TextView textViewWinRate;
    TextView lastBattleTimeView;
    TextView winsView;
    TextView battlesView;
    TextView maxPlanesKillView;
    TextView maxDamageView;
    TextView survivedBattlesView;
    TextView survivedWinsView;
    TextView maxChipsDestroyerView;
    TextView defeatView;
    TextView planesKilledView;
    TextView drawsView;
    TextView shipsDestroyed;
    TextView averageShipsDestroyed;
    TextView totalDamage;
    TextView averageDamageView;
    TextView killDeath;
    TextView maxDamageShipView;
    TextView textViewKraken;
    TextView textViewStrike;
    TextView textViewHigh;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_details, container, false);

        String id = getArguments().getString("id");
        String country = getArguments().getString("country");


        nickNameView = (TextView) view.findViewById(R.id.textViewNick);
        textViewWinRate = (TextView) view.findViewById(R.id.textViewWinRate);
        createdAtView = (TextView) view.findViewById(R.id.textViewCreatedV);
        lastBattleTimeView = (TextView) view.findViewById(R.id.textViewLastBattleV);
        winsView = (TextView) view.findViewById(R.id.textViewWinsV);
        battlesView = (TextView) view.findViewById(R.id.textViewBattleV);
        maxPlanesKillView = (TextView) view.findViewById(R.id.textViewMaxPlaneKillV);
        maxDamageView = (TextView) view.findViewById(R.id.textViewMaxDamageV);
        survivedBattlesView = (TextView) view.findViewById(R.id.textViewSurvivedBattlesV);
        survivedWinsView = (TextView) view.findViewById(R.id.textViewSurvivedWinsV);
        maxChipsDestroyerView = (TextView) view.findViewById(R.id.textViewMaxDestroyedV);
        defeatView = (TextView) view.findViewById(R.id.textViewDefeatV);
        planesKilledView = (TextView) view.findViewById(R.id.textViewPlaneKillV);
        drawsView = (TextView) view.findViewById(R.id.textViewDrawsV);
        shipsDestroyed = (TextView) view.findViewById(R.id.textViewDestroyedShipsV);
        averageShipsDestroyed = (TextView) view.findViewById(R.id.textViewAverageDestroyedV);
        totalDamage = (TextView) view.findViewById(R.id.textViewTotalDamageV);
        averageDamageView = (TextView) view.findViewById(R.id.textViewAverageDamageV);
        killDeath = (TextView) view.findViewById(R.id.textViewKillDeath);
        maxDamageShipView = (TextView) view.findViewById(R.id.textViewMaxDamageShipV);
        textViewKraken = (TextView) view.findViewById(R.id.textViewKraken);
        textViewStrike = (TextView) view.findViewById(R.id.textViewDevasting);
        textViewHigh = (TextView) view.findViewById(R.id.textViewHighCaliber);

        request(id, country);
        requestAchievements(id, country);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private String getDate(long time) {

        Calendar cal = Calendar.getInstance(getResources().getConfiguration().locale);
        cal.setTimeInMillis(Long.valueOf(time) * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss aa", cal).toString();

        return date;
    }

    private String max_damage_ship(String shipId) {

        String myUrl = "https://api.worldofwarships.com/wows/encyclopedia/ships/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&ship_id=" + shipId;
        String result;
        String shipName = "---";

        HttpGetRequest getRequest = new HttpGetRequest(null);

        try {
            result = getRequest.execute(myUrl).get();
            JSONObject jsonObject = new JSONObject(result);

            JSONObject objData = jsonObject.getJSONObject("data");
            JSONObject objectIdShip = (JSONObject) objData.get(shipId);

            shipName = objectIdShip.get("name").toString();


        } catch (JSONException j) {
            j.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }


        return shipName;


    }


    private void requestAchievements(final String id, String country) {

        final String myUrl = "https://api.worldofwarships" + country + "/wows/account/achievements/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + id + "&fields=battle";


        new Thread(new Runnable() {
            @Override
            public void run() {


                try {


                    String result;

                    HttpGetRequest getRequest = new HttpGetRequest(null);

                    result = getRequest.execute(myUrl).get();

                    JSONObject jsonObject = new JSONObject(result);

                    JSONObject objData = (JSONObject) jsonObject.get("data");
                    JSONObject objId = (JSONObject) objData.get(id);
                    final JSONObject objAchievements = (JSONObject) objId.get("battle");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textViewKraken.setText(objAchievements.optString("WARRIOR"));
                            textViewStrike.setText(objAchievements.optString("INSTANT_KILL"));
                            textViewHigh.setText(objAchievements.optString("MAIN_CALIBER"));


                        }
                    });



                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    /*
    private int requestPveBattles(String country, String id){

        String myUrlPve = "https://api.worldofwarships"+country+"/wows/account/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id="+id+"&fields=statistics.pve.battles&extra=statistics.pve";
        int value = 0;

                try {


                    String result;

                    HttpGetRequest getRequest = new HttpGetRequest();

                    result = getRequest.execute(myUrlPve).get();

                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject objData = (JSONObject) jsonObject.get("data");
                    JSONObject objId = (JSONObject) objData.get(id);
                    JSONObject statistics = (JSONObject) objId.get("statistics");
                    JSONObject pve = (JSONObject) statistics.get("pve");

                    if(!pve.isNull("battles"))
                      value = Integer.valueOf(pve.get("battles").toString());


                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

                return value;



    }
    */
    private void request(final String id, final String country) {


        //Some url endpoint that you may have
        final String myUrl = "https://api.worldofwarships" + country + "/wows/account/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + id +
                "&fields=nickname%2Ccreated_at%2Chidden_profile%2Clast_battle_time%2Cstatistics.pvp.wins" +
                "%2Cstatistics.pvp.survived_wins%2Cstatistics.pvp.survived_battles%2Cstatistics.pvp.battles" +
                "%2Cstatistics.pvp.max_damage_dealt%2Cstatistics.pvp.max_frags_battle" +
                "%2Cstatistics.pvp.max_planes_killed%2Cstatistics.pvp.losses%2Cstatistics.pvp.draws%2Cstatistics.pvp.planes_killed" +
                "%2Cstatistics.pvp.frags%2Cstatistics.pvp.damage_dealt%2Cstatistics.pvp.max_damage_dealt_ship_id";
        //String to place our result in
        final String[] result = new String[1];
        //Instantiate new instance of our class





        progressDialog = new ProgressDialog(getContext());
        //progressDialog.setMax(100);
        progressDialog.setMessage(getString(R.string.progress_dialog_mensagem));
      //  progressDialog.setTitle(getString(R.string.progress_dialog_mensagem));
       // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    HttpGetRequest getRequest = new HttpGetRequest(null);
                    //Perform the doInBackground method, passing in our url

                    result[0] = getRequest.execute(myUrl).get();

                    JSONObject jsonObject = new JSONObject(result[0]);

                    if (jsonObject == null)
                        return;

                    JSONObject objData = jsonObject.getJSONObject("data");
                    JSONObject objectId = (JSONObject) objData.get(id);
                    JSONObject objStats = (JSONObject) objectId.get("statistics");
                    JSONObject objPvp = (JSONObject) objStats.get("pvp");

                    final String nickName = objectId.get("nickname").toString();
                    final String hiddenProfile = objectId.get("hidden_profile").toString();
                    final String createdAt = objectId.get("created_at").toString();
                    final String lastBattleTime = objectId.get("last_battle_time").toString();

                    final String wins = objPvp.get("wins").toString();
                    final String battles = objPvp.get("battles").toString();

                    final String maxPlanesKill = objPvp.get("max_planes_killed").toString();
                    final String maxDamage = objPvp.get("max_damage_dealt").toString();
                    final String maxDamageshipId = objPvp.get("max_damage_dealt_ship_id").toString();
                    final String survivedBattles = objPvp.get("survived_battles").toString();
                    final String survivedWins = objPvp.get("survived_wins").toString();
                    final String maxChipsDestroyer = objPvp.get("max_frags_battle").toString();
                    final String defeat = objPvp.get("losses").toString();
                    final String planesKilled = objPvp.get("planes_killed").toString();
                    final String draws = objPvp.get("draws").toString();
                    final String shipsDestroyedNumber = objPvp.get("frags").toString();
                    final String damageDealt = objPvp.get("damage_dealt").toString();


                    SingletonsClass singletonsClass = SingletonsClass.getInstance();
                    singletonsClass.setBattlesTotal(battles);

                    int winRate = 0;
                    float killPer = 0f;
                    int death = 0;
                    float deathKill = 0f;
                    int averageDamage = 0;


                    if (Integer.valueOf(shipsDestroyedNumber) > 0) {

                        killPer = Float.valueOf(shipsDestroyedNumber) / (Float.valueOf(defeat) + Float.valueOf(wins));

                    }


                    if (Integer.valueOf(wins) > 0) {

                        winRate = (Integer.valueOf(wins) * 100) / (Integer.valueOf(defeat) + Integer.valueOf(wins));

                    }

                    death = Integer.valueOf(battles) - Integer.valueOf(survivedBattles);


                    if (death > 0) {

                        deathKill = Float.valueOf(shipsDestroyedNumber) / Float.valueOf(death);

                    }

                    if (Integer.valueOf(damageDealt) > 0) {

                        averageDamage = Integer.valueOf(damageDealt) / Integer.valueOf(battles);


                    }

                    final int finalWinRate = winRate;
                    final float finalKillPer = killPer;
                    final int finalAverageDamage = averageDamage;
                    final float finalDeathKill = deathKill;
                    final String finalMaxDamageshipId = maxDamageshipId;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            nickNameView.setText(nickName);
                            createdAtView.setText(getDate(Long.valueOf(createdAt)));
                            lastBattleTimeView.setText(getDate(Long.valueOf(lastBattleTime)));
                            winsView.setText(String.format("%,d", Integer.valueOf(wins)));
                            battlesView.setText(String.format("%,d", Integer.valueOf(battles)));
                            maxPlanesKillView.setText(String.format("%,d", Integer.valueOf(maxPlanesKill)));
                            maxDamageView.setText(String.format("%,d", Integer.valueOf(maxDamage)));
                            survivedBattlesView.setText(String.format("%,d", Integer.valueOf(survivedBattles)));
                            survivedWinsView.setText(String.format("%,d", Integer.valueOf(survivedWins)));
                            maxChipsDestroyerView.setText(maxChipsDestroyer);
                            defeatView.setText(String.format("%,d", Integer.valueOf(defeat)));
                            planesKilledView.setText(String.format("%,d", Integer.valueOf(planesKilled)));
                            drawsView.setText(String.format("%,d", Integer.valueOf(draws)));
                            textViewWinRate.setText(String.valueOf(finalWinRate).concat("%"));
                            shipsDestroyed.setText(String.format("%,d", Integer.valueOf(shipsDestroyedNumber)));
                            averageShipsDestroyed.setText(String.format("%.2f", finalKillPer));
                            totalDamage.setText(String.format("%,d", Integer.valueOf(damageDealt)));
                            averageDamageView.setText(String.format("%,d", Integer.valueOf(finalAverageDamage)));
                            killDeath.setText(String.format("%.2f", finalDeathKill));
                            maxDamageShipView.setText(max_damage_ship(finalMaxDamageshipId));

                            progressDialog.dismiss();
                        }
                    });



                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


}



