package com.wows.status;

import org.json.JSONObject;

public class User {

    private String clanName = "";
    private String clanId = "";
    private String wins = "";
    private String battles = "";
    private String maxPlanesKill = "";
    private String maxDamage = "";
    private String maxDamageshipId = "";
    private String survivedBattles = "";
    private String survivedWins = "";
    private String maxChipsDestroyer = "";
    private String defeat = "";
    private String planesKilled = "";
    private String draws = "";
    private String shipsDestroyedNumber = "";
    private String damageDealt = "";
    private String max_planes_killed_ship_name = "";
    private String max_xp = "";
    private String max_xp_ship_name = "";
    private String hiddenProfile;
    private String createdAt;
    private String lastBattleTime;
    private String userName;
    private String userId;
    private String role;
    private String joined;
    private String maxDamageShip;
    private String maxDamageShipName;
    private String deathKill;
    private String averageDamage;
    private String winRate;
    private JSONObject objAchievements;

    public User() {
    }

    public User(String userName, String userId) {

        this.userName = userName;
        this.userId = userId;

    }

    public String getMaxDamageShip() {
        return maxDamageShip;
    }

    public void setMaxDamageShip(String maxDamageShip) {
        this.maxDamageShip = maxDamageShip;
    }

    public String getMaxDamageShipName() {
        return maxDamageShipName;
    }

    public void setMaxDamageShipName(String maxDamageShipName) {
        this.maxDamageShipName = maxDamageShipName;
    }

    public String getDeathKill() {
        return deathKill;
    }

    public void setDeathKill(String deathKill) {
        this.deathKill = deathKill;
    }

    public String getAverageDamage() {
        return averageDamage;
    }

    public void setAverageDamage(String averageDamage) {
        this.averageDamage = averageDamage;
    }

    public String getAverageShipsDestroyed() {
        return averageShipsDestroyed;
    }

    public void setAverageShipsDestroyed(String averageShipsDestroyed) {
        this.averageShipsDestroyed = averageShipsDestroyed;
    }

    private String averageShipsDestroyed;

    public String getWinRate() {
        return winRate;
    }

    public void setWinRate(String winRate) {
        this.winRate = winRate;
    }

    public String getHiddenProfile() {
        return hiddenProfile;
    }

    public void setHiddenProfile(String hiddenProfile) {
        this.hiddenProfile = hiddenProfile;
    }

    public String getClanId() {
        return clanId;
    }

    public void setClanId(String clanId) {
        this.clanId = clanId;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }


    public JSONObject getObjAchievements() {
        return objAchievements;
    }

    public void setObjAchievements(JSONObject objAchievements) {
        this.objAchievements = objAchievements;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastBattleTime() {
        return lastBattleTime;
    }

    public void setLastBattleTime(String lastBattleTime) {
        this.lastBattleTime = lastBattleTime;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getBattles() {
        return battles;
    }

    public void setBattles(String battles) {
        this.battles = battles;
    }

    public String getMaxPlanesKill() {
        return maxPlanesKill;
    }

    public void setMaxPlanesKill(String maxPlanesKill) {
        this.maxPlanesKill = maxPlanesKill;
    }

    public String getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(String maxDamage) {
        this.maxDamage = maxDamage;
    }

    public String getMaxDamageshipId() {
        return maxDamageshipId;
    }

    public void setMaxDamageshipId(String maxDamageshipId) {
        this.maxDamageshipId = maxDamageshipId;
    }

    public String getSurvivedBattles() {
        return survivedBattles;
    }

    public void setSurvivedBattles(String survivedBattles) {
        this.survivedBattles = survivedBattles;
    }

    public String getSurvivedWins() {
        return survivedWins;
    }

    public void setSurvivedWins(String survivedWins) {
        this.survivedWins = survivedWins;
    }

    public String getMaxChipsDestroyer() {
        return maxChipsDestroyer;
    }

    public void setMaxChipsDestroyer(String maxChipsDestroyer) {
        this.maxChipsDestroyer = maxChipsDestroyer;
    }

    public String getDefeat() {
        return defeat;
    }

    public void setDefeat(String defeat) {
        this.defeat = defeat;
    }

    public String getPlanesKilled() {
        return planesKilled;
    }

    public void setPlanesKilled(String planesKilled) {
        this.planesKilled = planesKilled;
    }

    public String getDraws() {
        return draws;
    }

    public void setDraws(String draws) {
        this.draws = draws;
    }

    public String getShipsDestroyedNumber() {
        return shipsDestroyedNumber;
    }

    public void setShipsDestroyedNumber(String shipsDestroyedNumber) {
        this.shipsDestroyedNumber = shipsDestroyedNumber;
    }

    public String getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(String damageDealt) {
        this.damageDealt = damageDealt;
    }

    public String getMaxPlanesKilledShipName() {
        return max_planes_killed_ship_name;
    }

    public void setMax_planes_killed_ship_name(String max_planes_killed_ship_name) {
        this.max_planes_killed_ship_name = max_planes_killed_ship_name;
    }

    public String getMax_xp() {
        return max_xp;
    }

    public void setMaxXp(String max_xp) {
        this.max_xp = max_xp;
    }

    public String getMax_xp_ship_name() {
        return max_xp_ship_name;
    }

    public void setMaxXpShipName(String max_xp_ship_name) {
        this.max_xp_ship_name = max_xp_ship_name;
    }

    public String getRole() {
        return role;
    }

    public String getJoined() {
        return joined;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }




    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }


}
