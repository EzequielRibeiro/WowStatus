package com.wows.status;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

public class ShipsDetails extends Fragment {


    private ListView listView;
    private TextView shipName,shipTier;
    private ShipsAdapter shipAdapter;
    SingletonsClass singletonsClass = SingletonsClass.getInstance();
    private static String sortBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_ships, container, false);
        listView = (ListView) v.findViewById(R.id.listOfShips);
        shipName = (TextView) v.findViewById(R.id.textShipName);
        shipTier = (TextView) v.findViewById(R.id.textShipTier);


        shipName.setOnClickListener(new View.OnClickListener() {

            boolean ascDesc = false;

            @Override
            public void onClick(View view) {

                TextView t = (TextView) view;
                sortBy = t.getText().toString();

                if(ascDesc == false){
                    Collections.sort(singletonsClass.getShipsList(),StringAscComparator);
                    shipAdapter.notifyDataSetChanged();
                    ascDesc = true;
                }else{
                    Collections.sort(singletonsClass.getShipsList(),StringDescComparator);
                    shipAdapter.notifyDataSetChanged();
                    ascDesc = false;
                }

            }
        });

        shipTier.setOnClickListener(new View.OnClickListener() {

            boolean ascDesc = false;

            @Override
            public void onClick(View view) {

                TextView t = (TextView) view;
                sortBy = t.getText().toString();

                if(ascDesc == false){
                    Collections.sort(singletonsClass.getShipsList(),StringAscComparator);
                    shipAdapter.notifyDataSetChanged();
                    ascDesc = true;
                }else{
                    Collections.sort(singletonsClass.getShipsList(),StringDescComparator);
                    shipAdapter.notifyDataSetChanged();
                    ascDesc = false;
                }

            }
        });






        return v;
    }


    // Comparator for Ascending Order
    public static Comparator<Ship> StringAscComparator = new Comparator<Ship>() {

        String stringName1 = "";
        String stringName2 = "";
        Integer numberName1 = 0, numberName2 = 0;

        public int compare(Ship app1, Ship app2) {

            switch (sortBy){

                case "Ship Name":
                stringName1 = app1.getName();
                stringName2 = app2.getName();
                break;

                case "Tier":
                numberName1 = Integer.valueOf(app1.getTier());
                numberName2 = Integer.valueOf(app2.getTier());

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

        public int compare(Ship app1, Ship app2) {

            switch (sortBy){

                case "Ship Name":
                    stringName1 = app1.getName();
                    stringName2 = app2.getName();
                    break;

                case "Tier":
                    numberName1 = Integer.valueOf(app1.getTier());
                    numberName2 = Integer.valueOf(app2.getTier());

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

    private static boolean m_iAmVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

        if (m_iAmVisible) {

            loadList();


        } else {

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}