package com.wows.status;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SingletonsClass {

    private static String url;
    private static ArrayList<Ship> shipsList;
    private static Map<String, Ship> entry;
    private static String battlesTotal = "0";
    private static SingletonsClass ourInstance ;
    private static User user;

    public static User getUser(){

        return user;
    }

    public static void setUser(User u){

        user = u;
    }

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


    protected void setUrl(String url){

        this.url = url;

    }

    protected void setBattlesTotal(String battles){

        this.battlesTotal = battles;

    }

    protected String getBattlesTotal(){

        return battlesTotal;

    }


    private JSONObject request(){

        String result = "";
        JSONObject objData = null;



        try {

            HttpGetRequest getRequest = new HttpGetRequest(null);
            result = getRequest.execute(url).get();
            JSONObject jsonObject = new JSONObject(result);
            objData = (JSONObject) jsonObject.get("data");


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objData;


    }

    public void clear()
    {
       ourInstance = null;
       shipsList = null;
       entry = null;

    }





}
