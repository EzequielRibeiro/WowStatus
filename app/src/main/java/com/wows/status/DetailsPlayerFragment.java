package com.wows.status;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class DetailsPlayerFragment extends Fragment {


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
    TextView textViewDoubleKill;
    TextView textViewLiquidator;
    TextView textViewFireProof;
    TextView textViewWithering;
    TextView textViewConfederator;
    TextView textViewClosedShoot;
    TextView textViewArsionist;
    TextView textViewDetonated;
    TextView textViewDreadnought;
    TextView textViewRetribution;
    TextView textViewFirstBlood;
    TextView textViewHeadButt;
    TextView textViewAirDefense;
    TextView textViewMaxDestroyedShipName;
    TextView textViewMaxPlanesKillShipName;
    TextView textViewMaxXp;
    TextView textViewMaxXpShipName;
    ProgressDialog progressDialog;
    private String clanId;
    private TextView textViewClan;
    private String clanName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);

        final View view = inflater.inflate(R.layout.fragment_details, container, false);

        final String id = getArguments().getString("id");
        final String country = getArguments().getString("country");


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
        textViewClan = (TextView) view.findViewById(R.id.textViewClan);
        textViewMaxDestroyedShipName = (TextView) view.findViewById(R.id.textViewMaxDestroyedShipNameV);
        textViewMaxPlanesKillShipName = (TextView) view.findViewById(R.id.textViewMaxPlanesKillShipNameV);
        textViewMaxXp = (TextView) view.findViewById(R.id.textViewMaxXpV);
        textViewMaxXpShipName = (TextView) view.findViewById(R.id.textViewMaxXpShipNameV) ;

        //achieves
        textViewKraken = (TextView) view.findViewById(R.id.textViewKraken);
        textViewStrike = (TextView) view.findViewById(R.id.textViewDevasting);
        textViewHigh = (TextView) view.findViewById(R.id.textViewHighCaliber);
        textViewDoubleKill = (TextView) view.findViewById(R.id.textViewDoubleKill);
        textViewLiquidator = (TextView) view.findViewById(R.id.textViewLiquidator);
        textViewFireProof = (TextView) view.findViewById(R.id.textViewFireProof);
        textViewWithering = (TextView) view.findViewById(R.id.textViewWitheringSecador);
        textViewConfederator = (TextView) view.findViewById(R.id.textViewSupportConfederado);
        textViewClosedShoot = (TextView) view.findViewById(R.id.textViewCloserShoot);
        textViewArsionist = (TextView) view.findViewById(R.id.textViewArsionist);
        textViewDetonated = (TextView) view.findViewById(R.id.textViewDetonated);
        textViewDreadnought = (TextView) view.findViewById(R.id.textViewDreadnought);
        textViewRetribution = (TextView) view.findViewById(R.id.textViewRetribution);
        textViewFirstBlood = (TextView) view.findViewById(R.id.textViewFirstBlood);
        textViewHeadButt = (TextView) view.findViewById(R.id.textViewHeadButt);
        textViewAirDefense = (TextView) view.findViewById(R.id.textViewAirDefense);



        textViewClan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clanId != null) {
                    Bundle b = new Bundle();
                    b.putString("clanId", clanId);
                    b.putString("country", country);

                    Intent intent = new Intent(getContext(), ClanActivity.class);
                    intent.putExtra("clanBundle", b);
                    getActivity().finish();
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), getText(R.string.player_clan_not_belong), Toast.LENGTH_LONG).show();
                }

            }
        });


        if(nickNameView.getText().toString().equals("NickName"))
             request(id, country);

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
        String dateFormat = "dd-MM-yyyy hh:mm:ss aa";

        if (Locale.getDefault().getDisplayLanguage().equals("English"))
            dateFormat = "MM-dd-yyyy hh:mm:ss aa";

        String date = DateFormat.format(dateFormat, cal).toString();

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

    private String ship_destroyed_name(String shipId)  {

        String url = "https://api.worldofwarships.com/wows/encyclopedia/ships/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&ship_id="+shipId+"&fields=name";
        String shipName = "---";
        String result;

        HttpGetRequest getRequest;

        getRequest = new HttpGetRequest(null);

        JSONObject jsonObject = null;
        try {

            result = getRequest.execute(url).get();
            jsonObject = new JSONObject(result);
            JSONObject objData = jsonObject.getJSONObject("data");
            JSONObject objectId = objData.getJSONObject(shipId);

            if (!objectId.isNull("name")) {
                shipName = objectId.get("name").toString();

            }


        } catch (JSONException | ExecutionException | ClassCastException | InterruptedException e) {
            e.printStackTrace();
        }


          return  shipName;


    }

    
    private String clanName(String idPlayer, String country) {

        String myUrlClanId = "https://api.worldofwarships" + country + "/wows/clans/accountinfo/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + idPlayer + "&fields=clan_id";
        String urlClanName = "https://api.worldofwarships" + country + "/wows/clans/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&clan_id=";
        String urlfinalClanName = "&fields=tag";
        String result;

        HttpGetRequest getRequest;

        try {
            getRequest = new HttpGetRequest(null);
            result = getRequest.execute(myUrlClanId).get();
            JSONObject jsonObject = new JSONObject(result);
            JSONObject objData = jsonObject.getJSONObject("data");
            JSONObject objectId = objData.getJSONObject(idPlayer);

            if (!objectId.isNull("clan_id")) {
                clanId = objectId.get("clan_id").toString();
                urlfinalClanName = urlClanName + clanId + urlfinalClanName;
                getRequest = new HttpGetRequest(null);
                result = getRequest.execute(urlfinalClanName).get();
                jsonObject = new JSONObject(result);
                objData = jsonObject.getJSONObject("data");
                objectId = (JSONObject) objData.get(clanId);
                clanName = "[" + objectId.get("tag").toString() + "]";
            }

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

        return clanName;

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

                            textViewKraken.setText(objAchievements.optString("PCH023_Warrior"));
                            textViewStrike.setText(objAchievements.optString("PCH011_InstantKill"));
                            textViewHigh.setText(objAchievements.optString("PCH003_MainCaliber"));
                            textViewDoubleKill.setText(objAchievements.optString("PCH001_DoubleKill"));
                            ;
                            textViewLiquidator.setText(objAchievements.optString("PCH013_Liquidator"));
                            ;
                            textViewFireProof.setText(objAchievements.optString("PCH017_Fireproof"));
                            textViewWithering.setText(objAchievements.optString("PCH006_Withering"));
                            ;
                            textViewConfederator.setText(objAchievements.optString("PCH005_Support"));
                            ;
                            textViewClosedShoot.setText(objAchievements.optString("PCH020_ATBACaliber"));
                            ;
                            textViewArsionist.setText(objAchievements.optString("PCH012_Arsonist"));
                            ;
                            textViewDetonated.setText(objAchievements.optString("PCH019_Detonated"));
                            ;
                            textViewDreadnought.setText(objAchievements.optString("PCH004_Dreadnought"));
                            ;
                            textViewRetribution.setText(objAchievements.optString("PCH010_Retribution"));
                            ;
                            textViewFirstBlood.setText(objAchievements.optString("PCH016_FirstBlood"));
                            ;
                            textViewHeadButt.setText(objAchievements.optString("PCH014_Headbutt"));
                            ;
                            textViewAirDefense.setText(objAchievements.optString("PCH174_AirDefenseExpert"));
                            ;


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
                "%2Cstatistics.pvp.frags%2Cstatistics.pvp.damage_dealt%2Cstatistics.pvp.max_damage_dealt_ship_id%2Cstatistics.pvp.max_frags_ship_id" +
                "%2Cstatistics.pvp.max_planes_killed_ship_id%2Cstatistics.pvp.max_xp%2Cstatistics.pvp.max_xp_ship_id";
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
                        String hiddenProfile = objectId.get("hidden_profile").toString();
                        final String createdAt = objectId.get("created_at").toString();
                        final String lastBattleTime = objectId.get("last_battle_time").toString();

                        final String wins = objPvp.get("wins").toString();
                        final String battles = objPvp.get("battles").toString();

                        final String maxPlanesKill = objPvp.get("max_planes_killed").toString();
                        final String maxDamage = objPvp.get("max_damage_dealt").toString();
                        String maxDamageshipId = objPvp.get("max_damage_dealt_ship_id").toString();
                        final String survivedBattles = objPvp.get("survived_battles").toString();
                        final String survivedWins = objPvp.get("survived_wins").toString();
                        final String maxChipsDestroyer = objPvp.get("max_frags_battle").toString();
                        final String defeat = objPvp.get("losses").toString();
                        final String planesKilled = objPvp.get("planes_killed").toString();
                        final String draws = objPvp.get("draws").toString();
                        final String shipsDestroyedNumber = objPvp.get("frags").toString();
                        final String damageDealt = objPvp.get("damage_dealt").toString();
                        final String max_frags_ship_name = ship_destroyed_name(objPvp.get("max_frags_ship_id").toString());
                        final String max_planes_killed_ship_name = ship_destroyed_name(objPvp.get("max_planes_killed_ship_id").toString());
                        final String max_xp = objPvp.get("max_xp").toString();
                        final String max_xp_ship_name = ship_destroyed_name(objPvp.get("max_xp_ship_id").toString());


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

                        requestAchievements(id, country);


                        final int finalWinRate = winRate;
                        final float finalKillPer = killPer;
                        final int finalAverageDamage = averageDamage;
                        final float finalDeathKill = deathKill;
                        final String finalMaxDamageshipId = maxDamageshipId;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                textViewClan.setTag(clanId);
                                textViewClan.setText(clanName(id, country));
                                nickNameView.setText(nickName);
                                createdAtView.setText(getDate(Long.valueOf(createdAt)));
                                lastBattleTimeView.setText(getDate(Long.valueOf(lastBattleTime)));
                                winsView.setText(String.format("%,d", Integer.valueOf(wins)));
                                battlesView.setText(String.format("%,d", Integer.valueOf(battles)));
                                maxPlanesKillView.setText(String.format("%,d", Integer.valueOf(maxPlanesKill)));
                                textViewMaxPlanesKillShipName.setText(max_planes_killed_ship_name);
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
                                textViewMaxDestroyedShipName.setText(max_frags_ship_name);
                                textViewMaxXp.setText(max_xp);
                                textViewMaxXpShipName.setText(max_xp_ship_name);

                                if (progressDialog != null)
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



