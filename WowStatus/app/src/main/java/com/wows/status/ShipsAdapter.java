package com.wows.status;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShipsAdapter  extends ArrayAdapter<Ship> {

    private Context mContext;
    private List<Ship> shipsList;


    public ShipsAdapter(Context context, ArrayList<Ship> list) {
        super(context,0,list);

        mContext  = context;
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


    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_ships,parent,false);

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

        TextView battle= listItem.findViewById(R.id.textViewShipBattle);
        battle.setText(currentShip.getBattles());

        TextView nation= listItem.findViewById(R.id.textViewShipNation);
        nation.setText(currentShip.getNation());

        TextView porcent = listItem.findViewById(R.id.textViewShipPorcent);
        porcent.setText(currentShip.getPorcent());

        TextView wins= listItem.findViewById(R.id.textViewShipWins);
        wins.setText(currentShip.getWins());

        TextView winsRate = listItem.findViewById(R.id.textViewShipWinRate);
        winsRate.setText(currentShip.getWinRate());

        return listItem;
    }



}
