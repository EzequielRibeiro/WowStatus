package com.wows.status;


import android.util.Log;

public class Ship implements  Comparable{

    private String id = "empty";
    private String name = "empty";
    private String tier = "empty";
    private String nation = "empty";
    private String type = "empty";
    private String wins = "0";
    private String battles = "0";

    public void setWins(String wins) {

        if(wins != null)
        this.wins = wins;
    }

    public void setBattles(String battles) {
        if(battles != null)
            this.battles = battles;
    }


    public void setId(String id) {

        if(id != null)
           this.id = id;
    }

    public void setName(String name) {
        if(name != null)
        this.name = name;
    }

    public void setTier(String tier) {
        if(tier != null)
        this.tier = tier;
    }

    public void setNation(String nation) {
        if(nation != null)
        this.nation = nation;
    }

    public void setType(String type) {
        if(type != null)
        this.type = type;
    }

    public Ship(){}
    public Ship(String id, String name, String tier, String nation, String type, String battles, String wins){

         this.id   = id;
         this.name = name;
         this.tier = tier;
         this.nation = nation;
         this.type = type;
         this.battles = battles;
         this.wins = wins;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTier() {
        return tier;
    }

    public String getNation() {
        return nation;
    }

    public String getType() {
        return type;
    }

    public String getWins() {
        return this.wins;
    }

    public String getBattles() {
        return battles;
    }

    public String getPorcent(){

        SingletonsClass singletonsClass = SingletonsClass.getInstance();
        String battlesTotal = singletonsClass.getBattlesTotal();


        float t1 = 0;
        float t2 = (float) Integer.valueOf(battlesTotal);
        float t3 = (float) Integer.valueOf(battles);

        if(t2 > 0 && t3 > 0)
            t1 = (t3 * 100) / t2 ;

        return String.format("%.2f", t1)+ "%";


    }

    public String getWinRate(){

        float value = 0.0f;
        float wins_  =  (float) Integer.valueOf(getWins());
        float battles_ = (float) Integer.valueOf(getBattles());

        if(Integer.valueOf(getWins()) > 0)
             value  =   (wins_ * 100) / battles_ ;

        return String.format("%.2f", value)+ "%";


    }

    @Override
    public int compareTo(Object o) {
        String battlesC  = ((Ship) o).getBattles();

        // ascending order
        // return (int) (this.salary - compareSalary);

        // descending order
        return (int) (Integer.valueOf(battlesC) - Integer.valueOf(battles));
    }


}
