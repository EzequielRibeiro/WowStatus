package com.wows.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SingletonsClass {

    private static ArrayList<Ship> shipsList;
    private static Map<String, Ship> entry;
    private static String battlesTotal = "0";
    private static SingletonsClass ourInstance ;

    public static ArrayList<Ship> getShipsList() {

       // Collections.sort(shipsList);
        return shipsList;
    }


    public static synchronized SingletonsClass getInstance() {

        if (ourInstance == null) { //Check for the first time

            synchronized (SingletonsClass.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (ourInstance == null)
                    ourInstance = new SingletonsClass();

                if(shipsList == null)
                    shipsList = new ArrayList<Ship>();

                if(entry == null)
                    entry = new HashMap<>();
            }
        }

        return ourInstance;
    }

    private SingletonsClass() {
    }


    public static Map<String, Ship> getEntry() {
        return entry;
    }

    public static void setEntry(Map<String, Ship> entry) {
        SingletonsClass.entry = entry;
    }

    public ArrayList getListShips(){

        return shipsList;
    }

   protected void setAddShipsList(Ship ship){

        shipsList.add(ship);

   }


    protected void setBattlesTotal(String battles){

        this.battlesTotal = battles;

    }

    protected String getBattlesTotal(){

        return battlesTotal;

    }



    public void clear()
    {
       ourInstance = null;
       shipsList = null;
       entry = null;

    }





}
