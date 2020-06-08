package com.wows.status;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import androidx.fragment.app.Fragment;

import java.util.Collections;
import java.util.Comparator;

public class ShipsDetails extends Fragment implements View.OnClickListener {


    private ListView listView;
    private Button shipName, shipTier, shipType, shipNation, shipBattle, shipPercent, shipWins, shipWinRate,shipKill,
            maxXp,totalXp,maxDamage,maxPlanes,totalPlanes,torpsHits,torpsKill;
    private ShipsAdapter shipAdapter;
    SingletonsClass singletonsClass = SingletonsClass.getInstance();
    private static String sortBy = "Battles";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View v = inflater.inflate(R.layout.activity_ships, container, false);
        listView = (ListView) v.findViewById(R.id.listOfShips);
        shipName = (Button) v.findViewById(R.id.buttonShipName);
        shipTier = (Button) v.findViewById(R.id.buttonShipTier);
        shipType = (Button) v.findViewById(R.id.buttonShipType);
        shipNation = (Button) v.findViewById(R.id.buttonShipNation);
        shipBattle = (Button) v.findViewById(R.id.buttonShipBattle);
        shipPercent = (Button) v.findViewById(R.id.buttonShipPorcent);
        shipWins = (Button) v.findViewById(R.id.buttonShipWins);
        shipWinRate = (Button) v.findViewById(R.id.buttonShipWinRate);
        shipKill  = (Button) v.findViewById(R.id.buttonShipKills) ;
        maxXp     = (Button) v.findViewById(R.id.buttonShipMaxXp);
        totalXp   = (Button) v.findViewById(R.id.buttonShipTotalXp);
        maxDamage = (Button) v.findViewById(R.id.buttonShipMaxDamage);
        maxPlanes = (Button) v.findViewById(R.id.buttonShipMaxPlanes);
        totalPlanes = (Button) v.findViewById(R.id.buttonShipTotalPlanes);
        torpsHits = (Button) v.findViewById(R.id.buttonShipTorpHits);
        torpsKill = (Button) v.findViewById(R.id.buttonShipTorpKill);


        shipName.setOnClickListener(this);
        shipTier.setOnClickListener(this);
        shipType.setOnClickListener(this);
        shipNation.setOnClickListener(this);
        shipBattle.setOnClickListener(this);
        shipPercent.setOnClickListener(this);
        shipWins.setOnClickListener(this);
        shipWinRate.setOnClickListener(this);
        shipKill.setOnClickListener(this);
        maxXp.setOnClickListener(this);
        totalXp.setOnClickListener(this);
        maxDamage.setOnClickListener(this);
        maxPlanes.setOnClickListener(this);
        totalPlanes.setOnClickListener(this);
        torpsHits.setOnClickListener(this);
        torpsKill.setOnClickListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setSelected(true);
                        shipAdapter.notifyDataSetChanged();
                    }
                });


            }
        });


        loadList();
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    // Comparator for Ascending Order
    public static Comparator<Ship> StringAscComparator = new Comparator<Ship>() {

        String stringName1 = "";
        String stringName2 = "";
        Integer numberName1 = 0, numberName2 = 0;
        Float floatName1, floatName2;

        public int compare(Ship app1, Ship app2) {

            switch (sortBy) {

                case "Ship Name":
                    stringName1 = app1.getName();
                    stringName2 = app2.getName();
                    break;

                case "Tier":
                    numberName1 = Integer.valueOf(app1.getTier());
                    numberName2 = Integer.valueOf(app2.getTier());
                    return numberName1.compareTo(numberName2);

                case "Type":
                    stringName1 = app1.getType();
                    stringName2 = app2.getType();
                    break;

                case "Nation":
                    stringName1 = app1.getNation();
                    stringName2 = app2.getNation();
                    break;

                case "Battles":
                    numberName1 = Integer.valueOf(app1.getBattles());
                    numberName2 = Integer.valueOf(app2.getBattles());
                    return numberName1.compareTo(numberName2);

                case "%":
                    floatName1 = Float.valueOf(app1.getPorcent().replace("%", "").replace(",", "."));
                    floatName2 = Float.valueOf(app2.getPorcent().replace("%", "").replace(",", "."));
                    return floatName1.compareTo(floatName2);

                case "Wins":
                    numberName1 = Integer.valueOf(app1.getWins());
                    numberName2 = Integer.valueOf(app2.getWins());
                    return numberName1.compareTo(numberName2);

                case "WinRate":
                    floatName1 = Float.valueOf(app1.getWinRate().replace("%", "").replace(",", "."));
                    floatName2 = Float.valueOf(app2.getWinRate().replace("%", "").replace(",", "."));
                    return floatName1.compareTo(floatName2);

                case "Kills":
                    numberName1 = Integer.valueOf(app1.getKill());
                    numberName2 = Integer.valueOf(app2.getKill());
                    return numberName1.compareTo(numberName2);

                case "Max Xp":
                    numberName1 = Integer.valueOf(app1.getMax_xp());
                    numberName2 = Integer.valueOf(app2.getMax_xp());
                    return numberName1.compareTo(numberName2);

                case "Total Xp":
                    numberName1 = Integer.valueOf(app1.getTotal_xp());
                    numberName2 = Integer.valueOf(app2.getTotal_xp());
                    return numberName1.compareTo(numberName2);

                case "Max Damage":
                    numberName1 = Integer.valueOf(app1.getMax_damage_dealt());
                    numberName2 = Integer.valueOf(app2.getMax_damage_dealt());
                    return numberName1.compareTo(numberName2);

                case "Max Planes":
                    numberName1 = Integer.valueOf(app1.getMax_planes_killed());
                    numberName2 = Integer.valueOf(app2.getMax_planes_killed());
                    return numberName1.compareTo(numberName2);

                case "Total Planes":
                    numberName1 = Integer.valueOf(app1.getPlanes_killed());
                    numberName2 = Integer.valueOf(app2.getPlanes_killed());
                    return numberName1.compareTo(numberName2);

                case "Torps Hits":
                    numberName1 = Integer.valueOf(app1.getTorpedoes_hits());
                    numberName2 = Integer.valueOf(app2.getTorpedoes_hits());
                    return numberName1.compareTo(numberName2);

                case "Torps Kill":
                    numberName1 = Integer.valueOf(app1.getTorpedoes_frags());
                    numberName2 = Integer.valueOf(app2.getTorpedoes_frags());
                    return numberName1.compareTo(numberName2);



            }


            return stringName1.compareToIgnoreCase(stringName2);
        }
    };

    //Comparator for Descending Order
    public static Comparator<Ship> StringDescComparator = new Comparator<Ship>() {

        String stringName1 = "";
        String stringName2 = "";
        Integer numberName1 = 0, numberName2 = 0;
        Float floatName1, floatName2;

        public int compare(Ship app1, Ship app2) {

            switch (sortBy) {

                case "Ship Name":
                    stringName1 = app1.getName();
                    stringName2 = app2.getName();
                    break;

                case "Tier":
                    numberName1 = Integer.valueOf(app1.getTier());
                    numberName2 = Integer.valueOf(app2.getTier());
                    return numberName2.compareTo(numberName1);

                case "Type":
                    stringName1 = app1.getType();
                    stringName2 = app2.getType();
                    break;

                case "Nation":
                    stringName1 = app1.getNation();
                    stringName2 = app2.getNation();
                    break;

                case "Battles":
                    numberName1 = Integer.valueOf(app1.getBattles());
                    numberName2 = Integer.valueOf(app2.getBattles());
                    return numberName2.compareTo(numberName1);

                case "%":
                    floatName1 = Float.valueOf(app1.getPorcent().replace("%", "").replace(",", "."));
                    floatName2 = Float.valueOf(app2.getPorcent().replace("%", "").replace(",", "."));
                    return floatName2.compareTo(floatName1);

                case "Wins":
                    numberName1 = Integer.valueOf(app1.getWins());
                    numberName2 = Integer.valueOf(app2.getWins());
                    return numberName2.compareTo(numberName1);

                case "WinRate":
                    floatName1 = Float.valueOf(app1.getWinRate().replace("%", "").replace(",", "."));
                    floatName2 = Float.valueOf(app2.getWinRate().replace("%", "").replace(",", "."));
                    return floatName2.compareTo(floatName1);

                case "Kills":
                    numberName1 = Integer.valueOf(app1.getKill());
                    numberName2 = Integer.valueOf(app2.getKill());
                    return numberName2.compareTo(numberName1);

                case "Max Xp":
                    numberName1 = Integer.valueOf(app1.getMax_xp());
                    numberName2 = Integer.valueOf(app2.getMax_xp());
                    return numberName2.compareTo(numberName1);

                case "Total Xp":
                    numberName1 = Integer.valueOf(app1.getTotal_xp());
                    numberName2 = Integer.valueOf(app2.getTotal_xp());
                    return numberName2.compareTo(numberName1);

                case "Max Damage":
                    numberName1 = Integer.valueOf(app1.getMax_damage_dealt());
                    numberName2 = Integer.valueOf(app2.getMax_damage_dealt());
                    return numberName2.compareTo(numberName1);

                case "Max Planes":
                    numberName1 = Integer.valueOf(app1.getMax_planes_killed());
                    numberName2 = Integer.valueOf(app2.getMax_planes_killed());
                    return numberName2.compareTo(numberName1);

                case "Total Planes":
                    numberName1 = Integer.valueOf(app1.getPlanes_killed());
                    numberName2 = Integer.valueOf(app2.getPlanes_killed());
                    return numberName2.compareTo(numberName1);

                case "Torps Hits":
                    numberName1 = Integer.valueOf(app1.getTorpedoes_hits());
                    numberName2 = Integer.valueOf(app2.getTorpedoes_hits());
                    return numberName2.compareTo(numberName1);

                case "Torps Kill":
                    numberName1 = Integer.valueOf(app1.getTorpedoes_frags());
                    numberName2 = Integer.valueOf(app2.getTorpedoes_frags());
                    return numberName2.compareTo(numberName1);


            }

            return stringName2.compareToIgnoreCase(stringName1);
        }
    };

    private void loadList() {

        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

              shipAdapter = new ShipsAdapter(getContext(), singletonsClass.getShipsList());
              listView.setAdapter(shipAdapter);


                }

            });

    }


    @Override
    public void onResume() {
      super.onResume();

        }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    private static boolean m_iAmVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        m_iAmVisible = isVisibleToUser;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    static boolean ascDesc = false;

    private void startCompare(View view) {


        if (shipAdapter == null) loadList();


        if (view != null) {
            Button t = (Button) view;
            sortBy = t.getText().toString();
        } else {
            sortBy = shipName.getText().toString();
        }

        if (ascDesc == false) {

            Collections.sort(singletonsClass.getShipsList(), StringAscComparator);
            shipAdapter.notifyDataSetChanged();
            ascDesc = true;
        } else {
            Collections.sort(singletonsClass.getShipsList(), StringDescComparator);
            shipAdapter.notifyDataSetChanged();
            ascDesc = false;
        }


    }

    @Override
    public void onClick(final View view) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                startCompare(view);

            }
        });

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }




}