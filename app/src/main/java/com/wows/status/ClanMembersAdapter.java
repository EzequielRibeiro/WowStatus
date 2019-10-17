package com.wows.status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClanMembersAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> userList;



    public ClanMembersAdapter(Context context, ArrayList<User> list) {
        super(context,0,list);

        mContext = context;
        userList =  list;
    }


    public int getCount() {

        return userList.size();
    }

    public User getItem(int position) {
        return userList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_members,parent,false);

        User currentUser = userList.get(position);

        TextView textViewPlayerName  = listItem.findViewById(R.id.textViewListNamePlayer);
        TextView textViewRole        = listItem.findViewById(R.id.textViewListRole);
        TextView textViewJoined      = listItem.findViewById(R.id.textViewListJoinedAt);

        textViewPlayerName.setText(currentUser.getUserName());
        textViewRole.setText(currentUser.getRole());
        textViewJoined.setText(currentUser.getJoined());


        return listItem;
    }


}

