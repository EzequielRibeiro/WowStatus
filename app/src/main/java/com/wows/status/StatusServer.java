package com.wows.status;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class StatusServer {


    private HttpGetRequest httpGetRequest;
    private String  na,eu,ru,asia;
    private String [] listServer = {"com","eu","ru","asia"};


    public  StatusServer(){


        String  myUrl;
        String  result;

        for (String t: listServer) {

            myUrl = "https://api.worldoftanks."+t+"/wgn/servers/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&game=wows";
            try {

                httpGetRequest = new HttpGetRequest(null);
                result = httpGetRequest.execute(myUrl).get();


                JSONObject object = new JSONObject(result);
                JSONObject objectData = object.getJSONObject("data");
                JSONArray  objArray = objectData.getJSONArray("wows");

                if(t.equals("com")){

                    na = objArray.getJSONObject(0).get("players_online").toString();

                }else if(t.equals("eu")){

                    eu = objArray.getJSONObject(0).get("players_online").toString();
                }
                 else if(t.equals("ru")){

                    ru = objArray.getJSONObject(0).get("players_online").toString();
                }
                 else if(t.equals("asia")){

                    asia = objArray.getJSONObject(0).get("players_online").toString();
                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }





    }


    public String getNa() {

        return na;
    }

    public String getEu() {

         return eu;
    }

    public String getRu() {

        return ru;
    }

    public String getAsia() {

         return asia;
    }
}
