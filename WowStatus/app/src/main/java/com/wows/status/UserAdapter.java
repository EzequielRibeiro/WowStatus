package com.wows.status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> userList;


    public UserAdapter(Context context,ArrayList<User> list) {
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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_item,parent,false);

        User currentUser = userList.get(position);

        //ImageView image = (ImageView)listItem.findViewById(R.id.imageView_poster);
        //image.setImageResource(currentMovie.getmImageDrawable());

        Integer p = position;

        TextView name = listItem.findViewById(R.id.textViewUser);
        name.setText(currentUser.getUserName());

        name.setTag(currentUser.getUserId());

        return listItem;
    }


}
