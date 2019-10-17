package com.wows.status;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class ShipsAdapter extends ArrayAdapter<Ship> {

    private Context mContext;
    private List<Ship> shipsList;


    public ShipsAdapter(Context context, ArrayList<Ship> list) {
        super(context, 0, list);

        mContext = context;
        shipsList = list;

    }

    public int getCount() {

        return shipsList.size();
    }

    public Ship getItem(int position) {
        return shipsList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private void changeColorseSelcted(boolean selected, View view) {

        TextView nameShip = view.findViewById(R.id.textViewShipName);
        TextView tier = view.findViewById(R.id.textViewShipTier);
        TextView type = view.findViewById(R.id.textViewShipType);
        TextView battle = view.findViewById(R.id.textViewShipBattle);
        TextView nation = view.findViewById(R.id.textViewShipNation);
        TextView porcent = view.findViewById(R.id.textViewShipPorcent);
        TextView wins = view.findViewById(R.id.textViewShipWins);
        TextView winsRate = view.findViewById(R.id.textViewShipWinRate);
        TextView textViewShipKill = view.findViewById(R.id.textViewShipKill);
        TextView textViewPlanesKilled = view.findViewById(R.id.textViewShipTotalPlanes);
        TextView textViewMaxPlanesKilled = view.findViewById(R.id.textViewShipMaxPlanes);
        TextView textViewMaxDamage = view.findViewById(R.id.textViewShipMaxDamage);
        TextView textViewMaxXp = view.findViewById(R.id.textViewShipMaxXp);
        TextView textViewTotalXp = view.findViewById(R.id.textViewShipTotalXp);
        TextView textViewTorpedoesFrags = view.findViewById(R.id.textViewShipTorpKill);
        TextView textViewTorpedoesHits = view.findViewById(R.id.textViewShipTorpHits);


        if (selected) {

            nameShip.setBackgroundColor(Color.GREEN);
            tier.setBackgroundColor(Color.GREEN);
            type.setBackgroundColor(Color.GREEN);
            battle.setBackgroundColor(Color.GREEN);
            nation.setBackgroundColor(Color.GREEN);
            porcent.setBackgroundColor(Color.GREEN);
            wins.setBackgroundColor(Color.GREEN);
            winsRate.setBackgroundColor(Color.GREEN);
            textViewShipKill.setBackgroundColor(Color.GREEN);
            textViewPlanesKilled.setBackgroundColor(Color.GREEN);
            textViewMaxPlanesKilled.setBackgroundColor(Color.GREEN);
            textViewMaxDamage.setBackgroundColor(Color.GREEN);
            textViewMaxXp.setBackgroundColor(Color.GREEN);
            textViewTotalXp.setBackgroundColor(Color.GREEN);
            textViewTorpedoesFrags.setBackgroundColor(Color.GREEN);
            textViewTorpedoesHits.setBackgroundColor(Color.GREEN);



        } else {

            nameShip.setBackgroundResource(R.color.colorAccent);
            tier.setBackgroundColor(Color.BLACK);
            type.setBackgroundResource(R.color.colorAccent);
            nation.setBackgroundColor(Color.BLACK);
            battle.setBackgroundResource(R.color.colorAccent);
            porcent.setBackgroundColor(Color.BLACK);
            wins.setBackgroundResource(R.color.colorAccent);
            winsRate.setBackgroundColor(Color.BLACK);
            textViewShipKill.setBackgroundResource(R.color.colorAccent);
            textViewMaxXp.setBackgroundColor(Color.BLACK);
            textViewTotalXp.setBackgroundResource(R.color.colorAccent);
            textViewMaxDamage.setBackgroundColor(Color.BLACK);
            textViewMaxPlanesKilled.setBackgroundResource(R.color.colorAccent);
            textViewPlanesKilled.setBackgroundColor(Color.BLACK);
            textViewTorpedoesHits.setBackgroundResource(R.color.colorAccent);
            textViewTorpedoesFrags.setBackgroundColor(Color.BLACK);


        }


    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_ships, parent, false);

        Ship currentShip = shipsList.get(position);

        //ImageView image = (ImageView)listItem.findViewById(R.id.imageView_poster);
        //image.setImageResource(currentMovie.getmImageDrawable());

        Integer p = position;

        TextView nameShip = listItem.findViewById(R.id.textViewShipName);
        nameShip.setText(currentShip.getName());
        nameShip.setTag(currentShip.getId());

        TextView tier = listItem.findViewById(R.id.textViewShipTier);
        tier.setText(currentShip.getTier());

        TextView type = listItem.findViewById(R.id.textViewShipType);
        type.setText(currentShip.getType());

        TextView battle = listItem.findViewById(R.id.textViewShipBattle);
        battle.setText(currentShip.getBattles());

        TextView nation = listItem.findViewById(R.id.textViewShipNation);
        nation.setText(currentShip.getNation());

        TextView porcent = listItem.findViewById(R.id.textViewShipPorcent);
        porcent.setText(currentShip.getPorcent());

        TextView wins = listItem.findViewById(R.id.textViewShipWins);
        wins.setText(currentShip.getWins());

        TextView winsRate = listItem.findViewById(R.id.textViewShipWinRate);
        winsRate.setText(currentShip.getWinRate());

        TextView textViewShipKill = listItem.findViewById(R.id.textViewShipKill);
        textViewShipKill.setText(currentShip.getKill());

        TextView textViewPlanesKilled = listItem.findViewById(R.id.textViewShipTotalPlanes);
        textViewPlanesKilled.setText(currentShip.getPlanes_killed());

        TextView textViewMaxPlanesKilled = listItem.findViewById(R.id.textViewShipMaxPlanes);
        textViewMaxPlanesKilled.setText(currentShip.getMax_planes_killed());

        TextView textViewMaxDamage = listItem.findViewById(R.id.textViewShipMaxDamage);
        textViewMaxDamage.setText(currentShip.getMax_damage_dealt());

        TextView textViewMaxXp = listItem.findViewById(R.id.textViewShipMaxXp);
        textViewMaxXp.setText(currentShip.getMax_xp());

        TextView textViewTotalXp = listItem.findViewById(R.id.textViewShipTotalXp);
        textViewTotalXp.setText(currentShip.getTotal_xp());

        TextView textViewTorpedoesFrags = listItem.findViewById(R.id.textViewShipTorpKill);
        textViewTorpedoesFrags.setText(currentShip.getTorpedoes_frags());

        TextView textViewTorpedoesHits = listItem.findViewById(R.id.textViewShipTorpHits);
        textViewTorpedoesHits.setText(currentShip.getTorpedoes_hits());



        if (listItem != null)
            if (listItem.isSelected()) {

                changeColorseSelcted(true, listItem);

            } else {

                changeColorseSelcted(false, listItem);


            }


        return listItem;
    }


}
