package com.wows.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SingletonsClass {

    private ArrayList<Ship> shipsList;
    private Map<String, Ship> entry;
    private String battlesTotal = "0";
    private User user;
    private static SingletonsClass ourInstance;


    public ArrayList<Ship> getShipsList() {
        // Collections.sort(shipsList);
        return shipsList;
    }


    public static synchronized SingletonsClass getInstance() {

        if (ourInstance == null) { //Check for the first time

            synchronized (SingletonsClass.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (ourInstance == null) {
                    ourInstance = new SingletonsClass();
                }


            }
        }

        return ourInstance;
    }

    public SingletonsClass() {
        shipsList = new ArrayList<Ship>();
        entry = new HashMap<>();
    }

    public Map<String, Ship> getEntry() {
        return entry;
    }

    public void setEntry(Map<String, Ship> entry) {
        this.entry = entry;
    }

    public ArrayList getListShips() {

        return shipsList;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    protected void setAddShipsList(Ship ship) {

        shipsList.add(ship);

    }


    protected void setBattlesTotal(String battles) {

        this.battlesTotal = battles;

    }

    protected String getBattlesTotal() {

        return battlesTotal;

    }

    public void clear() {
        ourInstance = null;
    }

}
