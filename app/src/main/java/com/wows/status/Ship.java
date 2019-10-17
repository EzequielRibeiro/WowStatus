package com.wows.status;

public class Ship implements  Comparable{

    private String id = "empty";
    private String name = "empty";
    private String tier = "0";
    private String nation = "empty";
    private String type = "empty";
    private String wins = "0";
    private String battles = "0";
    private String kill = "0";
    private String planes_killed = "0";
    private String max_planes_killed = "0";
    private String max_damage_dealt = "0";
    private String max_xp = "0";
    private String total_xp = "0";
    private String torpedoes_frags = "0";
    private String torpedoes_hits = "0";

    public String getPlanes_killed() {
        return planes_killed;
    }

    public void setPlanes_killed(String planes_killed) {
        this.planes_killed = planes_killed;
    }

    public String getMax_planes_killed() {
        return max_planes_killed;
    }

    public void setMax_planes_killed(String max_planes_killed) {
        this.max_planes_killed = max_planes_killed;
    }

    public String getMax_damage_dealt() {
        return max_damage_dealt;
    }

    public void setMax_damage_dealt(String max_damage_dealt) {
        this.max_damage_dealt = max_damage_dealt;
    }

    public String getMax_xp() {
        return max_xp;
    }

    public void setMax_xp(String max_xp) {
        this.max_xp = max_xp;
    }

    public String getTotal_xp() {
        return total_xp;
    }

    public void setTotal_xp(String total_xp) {
        this.total_xp = total_xp;
    }

    public String getTorpedoes_hits() {
        return torpedoes_hits;
    }

    public void setTorpedoes_hits(String torpedoes_hits) {
        this.torpedoes_hits = torpedoes_hits;
    }


    public String getTorpedoes_frags() {
        return torpedoes_frags;
    }

    public void setTorpedoes_frags(String torpedoes_frags) {
        this.torpedoes_frags = torpedoes_frags;
    }


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

    public void setKill(String kill){
        if(kill != null)
         this.kill = kill;


    }

    public Ship(){}
    public Ship(
          String id,
          String name,
          String tier,
          String nation,
          String type,
          String battles,
          String wins,
          String kills,
          String planes_killed ,
          String max_planes_killed ,
          String max_damage_dealt,
          String max_xp ,
          String total_xp ,
          String torpedoes_frags,
          String torpedoes_hits){

         this.id   = id;
         this.name = name;
         this.tier = tier;
         this.nation = nation;
         this.type = type;
         this.battles = battles;
         this.wins = wins;
         this.kill = kills;
         this.planes_killed = planes_killed;
         this.max_damage_dealt = max_damage_dealt;
         this.max_planes_killed = max_planes_killed;
         this.max_xp = max_xp;
         this.total_xp = total_xp;
         this.torpedoes_frags = torpedoes_frags;
         this.torpedoes_hits = torpedoes_hits;

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

    public  String getKill(){ return kill;}

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

    @Override
    public String toString() {
        return "Ship{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tier='" + tier + '\'' +
                ", nation='" + nation + '\'' +
                ", type='" + type + '\'' +
                ", wins='" + wins + '\'' +
                ", battles='" + battles + '\'' +
                ", kill='" + kill + '\'' +
                ", planes_killed='" + planes_killed + '\'' +
                ", max_planes_killed='" + max_planes_killed + '\'' +
                ", max_damage_dealt='" + max_damage_dealt + '\'' +
                ", max_xp='" + max_xp + '\'' +
                ", total_xp='" + total_xp + '\'' +
                ", torpedoes_frags='" + torpedoes_frags + '\'' +
                ", torpedoes_hits='" + torpedoes_hits + '\'' +
                '}';
    }
}
