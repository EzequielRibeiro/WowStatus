package com.wows.status;


import static com.wows.status.HttpGetRequest.CONNECTION_TIMEOUT;
import static com.wows.status.HttpGetRequest.READ_TIMEOUT;
import static com.wows.status.HttpGetRequest.REQUEST_METHOD;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private String clanName="";
    private SingletonsClass singletonsClass;
    private String id, country;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        singletonsClass = SingletonsClass.getInstance();
        final View view = inflater.inflate(R.layout.fragment_details, container, false);

        id = getArguments().getString("id");
        country = getArguments().getString("country");

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
        textViewMaxXpShipName = (TextView) view.findViewById(R.id.textViewMaxXpShipNameV);

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
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), getText(R.string.player_clan_not_belong), Toast.LENGTH_LONG).show();
                }

            }
        });

        initLabel(view);

        progressDialog = new ProgressDialog(getContext());
        //progressDialog.setMax(100);
        progressDialog.setMessage(getString(R.string.progress_dialog_mensagem));
        //  progressDialog.setTitle(getString(R.string.progress_dialog_mensagem));
        // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       // progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);

        if(singletonsClass.getUser() == null)
           progressDialog.show();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(SingletonsClass.getInstance().getUser() == null) {
            new TaskSync().execute();
        }
        else
            loadPlayerStats();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void loadPlayerStats()  {

        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {

                try {
                    textViewClan.setTag(singletonsClass.getUser().getClanId());
                    textViewClan.setText(singletonsClass.getUser().getClanName());
                    nickNameView.setText(singletonsClass.getUser().getUserName());
                    createdAtView.setText(singletonsClass.getUser().getCreatedAt());
                    lastBattleTimeView.setText(singletonsClass.getUser().getLastBattleTime());
                    winsView.setText(singletonsClass.getUser().getWins());
                    battlesView.setText(singletonsClass.getUser().getBattles());
                    maxPlanesKillView.setText(singletonsClass.getUser().getMaxPlanesKill());
                    textViewMaxPlanesKillShipName.setText(singletonsClass.getUser().getMaxPlanesKilledShipName());
                    maxDamageView.setText(singletonsClass.getUser().getMaxDamage());
                    survivedBattlesView.setText(singletonsClass.getUser().getSurvivedBattles());
                    survivedWinsView.setText(singletonsClass.getUser().getSurvivedWins());
                    maxChipsDestroyerView.setText(singletonsClass.getUser().getMaxChipsDestroyer());
                    defeatView.setText(singletonsClass.getUser().getDefeat());
                    planesKilledView.setText(singletonsClass.getUser().getPlanesKilled());
                    drawsView.setText(singletonsClass.getUser().getDraws());
                    textViewWinRate.setText(singletonsClass.getUser().getWinRate());
                    shipsDestroyed.setText(singletonsClass.getUser().getShipsDestroyedNumber());
                    averageShipsDestroyed.setText(singletonsClass.getUser().getAverageShipsDestroyed());
                    totalDamage.setText(singletonsClass.getUser().getDamageDealt());
                    averageDamageView.setText(singletonsClass.getUser().getAverageDamage());
                    killDeath.setText(singletonsClass.getUser().getDeathKill());
                    maxDamageShipView.setText(singletonsClass.getUser().getMaxDamageShip());
                    textViewMaxDestroyedShipName.setText(singletonsClass.getUser().getMaxDamageShipName());
                    textViewMaxXp.setText(singletonsClass.getUser().getMax_xp());
                    textViewMaxXpShipName.setText(singletonsClass.getUser().getMax_xp_ship_name());

                    JSONObject objAchievements = singletonsClass.getUser().getObjAchievements();

                    textViewKraken.setText(objAchievements.optString("PCH023_Warrior"));
                    textViewStrike.setText(objAchievements.optString("PCH011_InstantKill"));
                    textViewHigh.setText(objAchievements.optString("PCH003_MainCaliber"));
                    textViewDoubleKill.setText(objAchievements.optString("PCH001_DoubleKill"));
                    textViewLiquidator.setText(objAchievements.optString("PCH013_Liquidator"));
                    textViewFireProof.setText(objAchievements.optString("PCH017_Fireproof"));
                    textViewWithering.setText(objAchievements.optString("PCH006_Withering"));
                    textViewConfederator.setText(objAchievements.optString("PCH005_Support"));
                    textViewClosedShoot.setText(objAchievements.optString("PCH020_ATBACaliber"));
                    textViewArsionist.setText(objAchievements.optString("PCH012_Arsonist"));
                    textViewDetonated.setText(objAchievements.optString("PCH019_Detonated"));
                    textViewDreadnought.setText(objAchievements.optString("PCH004_Dreadnought"));
                    textViewRetribution.setText(objAchievements.optString("PCH010_Retribution"));
                    textViewFirstBlood.setText(objAchievements.optString("PCH016_FirstBlood"));
                    textViewHeadButt.setText(objAchievements.optString("PCH014_Headbutt"));
                    textViewAirDefense.setText(objAchievements.optString("PCH174_AirDefenseExpert"));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void initLabel(View view) {


        DBAdapter dbAdapter = new DBAdapter(getActivity());
        TextView createdAtView = (TextView) view.findViewById(R.id.textViewCreated);
        createdAtView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(1)));
        TextView lastBattleTimeView = (TextView) view.findViewById(R.id.textViewLastBattle);
        lastBattleTimeView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(2)));
        TextView textViewWinRate = (TextView) view.findViewById(R.id.textViewLabelWinRate);
        textViewWinRate.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(3)));
        TextView killDeath = (TextView) view.findViewById(R.id.textViewLabelKillDeath);
        killDeath.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(4)));
        TextView battlesView = (TextView) view.findViewById(R.id.textViewBattle);
        battlesView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(21)));
        TextView winsView = (TextView) view.findViewById(R.id.textViewWins);
        winsView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(22)));
        TextView defeatView = (TextView) view.findViewById(R.id.textViewDefeat);
        defeatView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(23)));
        TextView drawsView = (TextView) view.findViewById(R.id.textViewDraws);
        drawsView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(24)));
        TextView textViewMaxXp = (TextView) view.findViewById(R.id.textViewMaxXp);
        textViewMaxXp.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(25)));
        TextView textViewMaxXpShipName = (TextView) view.findViewById(R.id.textViewMaxXpShipName);
        textViewMaxXpShipName.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(26)));
        TextView maxPlanesKillView = (TextView) view.findViewById(R.id.textViewMaxPlaneKill);
        maxPlanesKillView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(27)));
        TextView textViewMaxPlanesKillShipName = (TextView) view.findViewById(R.id.textViewMaxPlanesKillShipName);
        textViewMaxPlanesKillShipName.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(28)));
        TextView planesKilledView = (TextView) view.findViewById(R.id.textViewPlaneKill);
        planesKilledView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(29)));
        TextView maxDamageView = (TextView) view.findViewById(R.id.textViewMaxDamage);
        maxDamageView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(30)));
        TextView maxDamageShipView = (TextView) view.findViewById(R.id.textViewMaxDamageShip);
        maxDamageShipView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(31)));
        TextView totalDamage = (TextView) view.findViewById(R.id.textViewTotalDamage);
        totalDamage.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(32)));
        TextView averageDamageView = (TextView) view.findViewById(R.id.textViewAverageDamage);
        averageDamageView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(33)));
        TextView maxChipsDestroyerView = (TextView) view.findViewById(R.id.textViewMaxDestroyed);
        maxChipsDestroyerView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(34)));
        TextView textViewMaxDestroyedShipName = (TextView) view.findViewById(R.id.textViewMaxDestroyedShipName);
        textViewMaxDestroyedShipName.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(35)));
        TextView shipsDestroyed = (TextView) view.findViewById(R.id.textViewDestroyedShips);
        shipsDestroyed.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(36)));
        TextView averageShipsDestroyed = (TextView) view.findViewById(R.id.textViewAverageDestroyed);
        averageShipsDestroyed.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(37)));
        TextView survivedBattlesView = (TextView) view.findViewById(R.id.textViewSurvivedBattles);
        survivedBattlesView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(38)));
        TextView survivedWinsView = (TextView) view.findViewById(R.id.textViewSurvivedWins);
        survivedWinsView.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(39)));

        //achieves
        TextView textViewHigh = (TextView) view.findViewById(R.id.textViewLabelCaliber);
        textViewHigh.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(5)));
        TextView textViewKraken = (TextView) view.findViewById(R.id.textViewLabelKraken);
        textViewKraken.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(6)));
        TextView textViewStrike = (TextView) view.findViewById(R.id.textViewLabelDevasting);
        textViewStrike.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(7)));
        TextView textViewDoubleKill = (TextView) view.findViewById(R.id.textViewLabelDoubleKill);
        textViewDoubleKill.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(8)));
        TextView textViewDetonated = (TextView) view.findViewById(R.id.textViewLabelDetonation);
        textViewDetonated.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(9)));
        TextView textViewConfederator = (TextView) view.findViewById(R.id.textViewLabelSupportConfederado);
        textViewConfederator.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(10)));
        TextView textViewClosedShoot = (TextView) view.findViewById(R.id.textViewLabelCloseShoot);
        textViewClosedShoot.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(11)));
        TextView textViewWithering = (TextView) view.findViewById(R.id.textViewLabelWitheringSecador);
        textViewWithering.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(12)));
        TextView textViewDreadnought = (TextView) view.findViewById(R.id.textViewLabelDreadnought);
        textViewDreadnought.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(13)));
        TextView textViewLiquidator = (TextView) view.findViewById(R.id.textViewLabelLiquidator);
        textViewLiquidator.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(14)));
        TextView textViewFireProof = (TextView) view.findViewById(R.id.textViewLabelFireProof);
        textViewFireProof.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(15)));
        TextView textViewArsionist = (TextView) view.findViewById(R.id.textViewLabelArsionist);
        textViewArsionist.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(16)));
        TextView textViewRetribution = (TextView) view.findViewById(R.id.textViewLabelRetribution);
        textViewRetribution.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(17)));
        TextView textViewFirstBlood = (TextView) view.findViewById(R.id.textViewLabelFirstBlood);
        textViewFirstBlood.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(18)));
        TextView textViewHeadButt = (TextView) view.findViewById(R.id.textViewLabelHeadButt);
        textViewHeadButt.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(19)));
        TextView textViewAirDefense = (TextView) view.findViewById(R.id.textViewLabelAirDefense);
        textViewAirDefense.setText(Html.fromHtml(dbAdapter.getMensagemTranslated(20)));

        dbAdapter.close();
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

     //   HttpGetRequest getRequest = new HttpGetRequest(getActivity());

        try {
            result = requestStats(myUrl);
            JSONObject jsonObject = new JSONObject(result);

            JSONObject objData = jsonObject.getJSONObject("data");
            JSONObject objectIdShip = (JSONObject) objData.get(shipId);

            shipName = objectIdShip.get("name").toString();


        } catch (JSONException | NullPointerException |
                ClassCastException j) {
            j.printStackTrace();
            return shipName;

        }
        return shipName;


    }

    private String ship_destroyed_name(String shipId) {

        String url = "https://api.worldofwarships.com/wows/encyclopedia/ships/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&ship_id=" + shipId + "&fields=name";
        String shipName = "---";
        String result;

     //   HttpGetRequest getRequest;

      //  getRequest = new HttpGetRequest(getContext());

        JSONObject jsonObject = null;
        try {

            result = requestStats(url);
            jsonObject = new JSONObject(result);
            JSONObject objData = jsonObject.getJSONObject("data");
            JSONObject objectId = objData.getJSONObject(shipId);

            if (!objectId.isNull("name")) {
                shipName = objectId.get("name").toString();

            }


        } catch (JSONException | ClassCastException e) {
            e.printStackTrace();
            return shipName;
        }
        return shipName;
    }


    private String clanName(String idPlayer, String country) {

        String myUrlClanId = "https://api.worldofwarships" + country + "/wows/clans/accountinfo/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + idPlayer + "&fields=clan_id";
        String urlClanName = "https://api.worldofwarships" + country + "/wows/clans/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&clan_id=";
        String urlfinalClanName = "&fields=tag";
        String result;

      //  HttpGetRequest getRequest;

        try {
          //  getRequest = new HttpGetRequest(getContext());
            result = requestStats(myUrlClanId);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject objData = jsonObject.getJSONObject("data");
            JSONObject objectId = objData.getJSONObject(idPlayer);

            if (!objectId.isNull("clan_id")) {
                clanId = objectId.get("clan_id").toString();
                urlfinalClanName = urlClanName + clanId + urlfinalClanName;
              //  getRequest = new HttpGetRequest(getContext());
                result = requestStats(urlfinalClanName);
                jsonObject = new JSONObject(result);
                objData = jsonObject.getJSONObject("data");
                objectId = (JSONObject) objData.get(clanId);
                clanName = "[" + objectId.get("tag").toString() + "]";
            }

        } catch (JSONException | NullPointerException |
                ClassCastException j) {
            j.printStackTrace();
            return clanName;
        }

        return clanName;

    }
    /*
      private void requestAchievements(final String id, String country) {

          final String myUrl = "https://api.worldofwarships" + country + "/wows/account/achievements/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + id + "&fields=battle";


          new Thread(new Runnable() {
              @Override
              public void run() {


                  try {

                      String result;

                      HttpGetRequest getRequest = new HttpGetRequest(getContext());

                      result = getRequest.execute(myUrl).get();

                      JSONObject jsonObject = new JSONObject(result);

                      JSONObject objData = (JSONObject) jsonObject.get("data");
                      JSONObject objId = (JSONObject) objData.get(id);
                      final JSONObject objAchievements = (JSONObject) objId.get("battle");

                      requireActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {

                              textViewKraken.setText(objAchievements.optString("PCH023_Warrior"));
                              textViewStrike.setText(objAchievements.optString("PCH011_InstantKill"));
                              textViewHigh.setText(objAchievements.optString("PCH003_MainCaliber"));
                              textViewDoubleKill.setText(objAchievements.optString("PCH001_DoubleKill"));
                              textViewLiquidator.setText(objAchievements.optString("PCH013_Liquidator"));
                              textViewFireProof.setText(objAchievements.optString("PCH017_Fireproof"));
                              textViewWithering.setText(objAchievements.optString("PCH006_Withering"));
                              textViewConfederator.setText(objAchievements.optString("PCH005_Support"));
                              textViewClosedShoot.setText(objAchievements.optString("PCH020_ATBACaliber"));
                              textViewArsionist.setText(objAchievements.optString("PCH012_Arsonist"));
                              textViewDetonated.setText(objAchievements.optString("PCH019_Detonated"));
                              textViewDreadnought.setText(objAchievements.optString("PCH004_Dreadnought"));
                              textViewRetribution.setText(objAchievements.optString("PCH010_Retribution"));
                              textViewFirstBlood.setText(objAchievements.optString("PCH016_FirstBlood"));
                              textViewHeadButt.setText(objAchievements.optString("PCH014_Headbutt"));
                              textViewAirDefense.setText(objAchievements.optString("PCH174_AirDefenseExpert"));
                          }
                      });


                  } catch (JSONException | InterruptedException | ExecutionException |
                          NullPointerException | ClassCastException j) {
                      j.printStackTrace();
                  }

              }
          }).start();


      }


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

                      HttpGetRequest getRequest = new HttpGetRequest(getContext());
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

                      singletonsClass.setBattlesTotal(battles);

                      int winRate = 0;
                      float killPer = 0f;
                      int death = 0;
                      float deathKill = 0f;
                      int averageDamage = 0;


                      if (Integer.parseInt(shipsDestroyedNumber) > 0) {

                          killPer = Float.parseFloat(shipsDestroyedNumber) /
                                  (Float.parseFloat(defeat) + Float.parseFloat(wins));

                      }


                      if (Integer.parseInt(wins) > 0) {

                          winRate = (Integer.parseInt(wins) * 100) / (Integer.parseInt(defeat) +
                                  Integer.parseInt(wins));

                      }

                      death = Integer.parseInt(battles) - Integer.parseInt(survivedBattles);


                      if (death > 0) {

                          deathKill = Float.parseFloat(shipsDestroyedNumber) / (float) death;

                      }

                      if (Integer.parseInt(damageDealt) > 0) {

                          averageDamage = Integer.parseInt(damageDealt) / Integer.parseInt(battles);


                      }

                      requestAchievements(id, country);

                      final int finalWinRate = winRate;
                      final float finalKillPer = killPer;
                      final int finalAverageDamage = averageDamage;
                      final float finalDeathKill = deathKill;
                      final String finalMaxDamageshipId = maxDamageshipId;

                      getActivity().runOnUiThread(new Runnable() {
                          @SuppressLint("DefaultLocale")
                          @Override
                          public void run() {

                              textViewClan.setTag(clanId);
                              textViewClan.setText(clanName(id, country));
                              nickNameView.setText(nickName);
                              createdAtView.setText(getDate(Long.parseLong(createdAt)));
                              lastBattleTimeView.setText(getDate(Long.parseLong(lastBattleTime)));
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
                              averageDamageView.setText(String.format("%,d", finalAverageDamage));
                              killDeath.setText(String.format("%.2f", finalDeathKill));
                              maxDamageShipView.setText(max_damage_ship(finalMaxDamageshipId));
                              textViewMaxDestroyedShipName.setText(max_frags_ship_name);
                              textViewMaxXp.setText(max_xp);
                              textViewMaxXpShipName.setText(max_xp_ship_name);

                              if (progressDialog != null)
                                  progressDialog.dismiss();
                          }
                      });


                  } catch (JSONException | InterruptedException | ExecutionException |
                          NullPointerException | ClassCastException j) {
                      j.printStackTrace();
                  }

              }
          }).start();
      }
   */
    private void requestFromApi(final String id, final String country) {

            //Some url endpoint that you may have
            final String myUrl = "https://api.worldofwarships" + country + "/wows/account/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + id +
                    "&fields=nickname%2Ccreated_at%2Chidden_profile%2Clast_battle_time%2Cstatistics.pvp.wins" +
                    "%2Cstatistics.pvp.survived_wins%2Cstatistics.pvp.survived_battles%2Cstatistics.pvp.battles" +
                    "%2Cstatistics.pvp.max_damage_dealt%2Cstatistics.pvp.max_frags_battle" +
                    "%2Cstatistics.pvp.max_planes_killed%2Cstatistics.pvp.losses%2Cstatistics.pvp.draws%2Cstatistics.pvp.planes_killed" +
                    "%2Cstatistics.pvp.frags%2Cstatistics.pvp.damage_dealt%2Cstatistics.pvp.max_damage_dealt_ship_id%2Cstatistics.pvp.max_frags_ship_id" +
                    "%2Cstatistics.pvp.max_planes_killed_ship_id%2Cstatistics.pvp.max_xp%2Cstatistics.pvp.max_xp_ship_id";
            //String to place our result in
            String[] result = new String[1];

                    try {

                        User user = new User();

                        result[0] = requestStats(myUrl);
                        JSONObject jsonObject = new JSONObject(result[0]);

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

                        final String max_frags_ship_name = ship_destroyed_name(objPvp.get("max_frags_ship_id").toString());
                        final String max_planes_killed_ship_name = ship_destroyed_name(objPvp.get("max_planes_killed_ship_id").toString());
                        final String max_xp = objPvp.get("max_xp").toString();
                        final String max_xp_ship_name = ship_destroyed_name(objPvp.get("max_xp_ship_id").toString());

                        singletonsClass.setBattlesTotal(battles);

                        int winRate = 0;
                        float killPer = 0f;
                        int death = 0;
                        float deathKill = 0f;
                        int averageDamage = 0;


                        if (Integer.parseInt(shipsDestroyedNumber) > 0) {

                            killPer = Float.parseFloat(shipsDestroyedNumber) /
                                    (Float.parseFloat(defeat) + Float.parseFloat(wins));

                        }


                        if (Integer.parseInt(wins) > 0) {

                            winRate = (Integer.parseInt(wins) * 100) / (Integer.parseInt(defeat) +
                                    Integer.parseInt(wins));

                        }

                        death = Integer.parseInt(battles) - Integer.parseInt(survivedBattles);


                        if (death > 0) {

                            deathKill = Float.parseFloat(shipsDestroyedNumber) / (float) death;

                        }

                        if (Integer.parseInt(damageDealt) > 0) {

                            averageDamage = Integer.parseInt(damageDealt) / Integer.parseInt(battles);


                        }

                        final int finalWinRate = winRate;
                        final float finalKillPer = killPer;
                        final int finalAverageDamage = averageDamage;

                        user.setClanName(clanName(id, country));
                        user.setClanId(clanId);
                        user.setUserName(nickName);
                        user.setHiddenProfile(hiddenProfile);
                        user.setCreatedAt(getDate(Long.parseLong(createdAt)));
                        user.setLastBattleTime(getDate(Long.parseLong(lastBattleTime)));
                        user.setWins(String.format("%,d", Integer.valueOf(wins)));
                        user.setBattles(String.format("%,d", Integer.valueOf(battles)));
                        user.setMaxPlanesKill(String.format("%,d", Integer.valueOf(maxPlanesKill)));
                        user.setMax_planes_killed_ship_name(max_planes_killed_ship_name);
                        user.setMaxDamage(String.format("%,d", Integer.valueOf(maxDamage)));
                        user.setSurvivedBattles(String.format("%,d", Integer.valueOf(survivedBattles)));
                        user.setSurvivedWins(String.format("%,d", Integer.valueOf(survivedWins)));
                        user.setMaxChipsDestroyer(maxChipsDestroyer);
                        user.setDefeat(String.format("%,d", Integer.valueOf(defeat)));
                        user.setPlanesKilled(String.format("%,d", Integer.valueOf(planesKilled)));
                        user.setDraws(String.format("%,d", Integer.valueOf(draws)));
                        user.setWinRate(String.valueOf(finalWinRate).concat("%"));
                        user.setShipsDestroyedNumber(String.format("%,d", Integer.valueOf(shipsDestroyedNumber)));
                        user.setAverageShipsDestroyed(String.format("%.2f", finalKillPer));
                        user.setDamageDealt(String.format("%,d", Integer.valueOf(damageDealt)));
                        user.setAverageDamage(String.format("%,d", finalAverageDamage));
                        user.setDeathKill(String.format("%.2f", deathKill));
                        user.setMaxDamageShip(max_damage_ship(maxDamageshipId));
                        user.setMaxDamageShipName(max_frags_ship_name);
                        user.setMaxXp(max_xp);
                        user.setMaxXpShipName(max_xp_ship_name);

                        singletonsClass.setUser(user);
                        singletonsClass.getUser().setObjAchievements(requestAchievementsFromApi(id, country));


                      } catch (JSONException | InterruptedException | ExecutionException |
                            NullPointerException |NumberFormatException |ClassCastException j) {
                        j.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"This player's stats are not available.",Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                }


    private JSONObject requestAchievementsFromApi(final String id, String country) throws ExecutionException, InterruptedException, JSONException {

        final String myUrl = "https://api.worldofwarships" + country + "/wows/account/achievements/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + id + "&fields=battle";
          String result;

                  //  HttpGetRequest getRequest = new HttpGetRequest(getContext());

                   // result = getRequest.execute(myUrl).get();

                    result = requestStats(myUrl);
                    JSONObject jsonObject = new JSONObject(result);

                    JSONObject objData = (JSONObject) jsonObject.get("data");
                    JSONObject objId = (JSONObject) objData.get(id);

        return (JSONObject) objId.get("battle");



    }

    private String requestStats(String stringUrl){
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

    private class TaskSync extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... objects) {

            requestFromApi(id,country);

            return "sucess";
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadPlayerStats();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null)
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                }
            });

           ;
        }
    }

}



