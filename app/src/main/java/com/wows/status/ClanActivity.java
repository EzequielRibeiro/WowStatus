package com.wows.status;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ClanActivity extends AppCompatActivity {

    private JSONArray jsonArray;

    private String clanId_;
    private String country_;
    private String urlClan1 = "https://api.worldofwarships";
    private String urlClan2 = "/wows/clans/info/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&clan_id=";
    private String members_count,name,creator_name,created_at,tag,description;
    private TextView textViewMember, textViewName,textViewCreatorName,textViewCreatedAt,textViewTag,textViewDescripion;
    private ListView listViewMembersClan;
    private Button buttonClanShowMembers;
    private ClanMembersAdapter clanMembersAdapter;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("clanBundle");

        clanId_ = bundle.getString("clanId","0000000000");
        country_ = bundle.getString("country",".com");

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


        textViewMember      = (TextView) findViewById(R.id.textViewClanMembros);
        textViewName        = (TextView) findViewById(R.id.textViewClanName);
        textViewCreatorName = (TextView) findViewById(R.id.textViewClanCreatorName);
        textViewCreatedAt   = (TextView) findViewById(R.id.textViewClanCreatedDate);
        textViewTag         = (TextView) findViewById(R.id.textViewClanTag);
        textViewDescripion  = (TextView) findViewById(R.id.textViewClanDescription) ;
        listViewMembersClan = (ListView) findViewById(R.id.listViewMembersClan);
        buttonClanShowMembers = (Button) findViewById(R.id.buttonClanShowMembers);
        progressBar         = (ProgressBar) findViewById(R.id.progressBarClanList);


        listViewMembersClan.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listViewMembersClan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SingletonsClass singletonsClass = SingletonsClass.getInstance();
                singletonsClass.clear();

                User user = (User) adapterView.getItemAtPosition(i);

                Bundle b = new Bundle();
                b.putString("id", user.getUserId());
                b.putString("country", country_);

                Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                intent.putExtras(b);
                startActivity(intent);



            }
        });


        buttonClanShowMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(clanMembersAdapter != null) {
                    clanMembersAdapter.clear();
                    clanMembersAdapter.notifyDataSetChanged();
                }

                showListMembers();

            }
        });

        clanDetails(clanId_,country_);

    }


    private void showListMembers(){

        if(progressBar.getVisibility() == View.GONE)
            progressBar.setVisibility(View.VISIBLE);


        runOnUiThread(new Runnable() {

                String url = "https://api.worldofwarships"+country_+"/wows/clans/accountinfo/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=";
                String listId = "";
                String result;
                HttpGetRequest getRequest = new HttpGetRequest(null);
                User user;
                ArrayList<User> memberList = new ArrayList<>();

                @Override
                public void run() {

                    try {

                        for(int i = 0;i < jsonArray.length();i++) {

                            listId += jsonArray.get(i).toString() + "%2C";

                        }

                        result = getRequest.execute(url+listId).get();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject jsonData = jsonObject.getJSONObject("data");


                        Iterator<String> iter = jsonData.keys();


                        memberList.clear();

                        while (iter.hasNext()) {

                            String key = iter.next();

                            user = new User();

                            if (!jsonData.isNull(key)) {

                                user.setUserName(jsonData.getJSONObject(key).optString("account_name"));
                                user.setRole(jsonData.getJSONObject(key).optString("role"));
                                user.setJoined(getDate(Long.valueOf(jsonData.getJSONObject(key).optString("joined_at"))));
                                user.setUserId(jsonData.getJSONObject(key).optString("account_id"));

                                memberList.add(user);



                            }
                        }

                        clanMembersAdapter = new ClanMembersAdapter(getApplicationContext(),memberList);
                        listViewMembersClan.setAdapter(clanMembersAdapter);

                        if(progressBar.getVisibility() == View.VISIBLE)
                            progressBar.setVisibility(View.GONE);

                      //  Log.e("url ",role+" "+joined_at+" "+account_id+" "+account_name);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            });

    }

    private  String getDate(long time) {

        Calendar cal = Calendar.getInstance(getResources().getConfiguration().locale);
        cal.setTimeInMillis(Long.valueOf(time) * 1000L);
        String dateFormat = "dd-MM-yyyy";

        if(Locale.getDefault().getDisplayLanguage().equals("English"))
            dateFormat = "MM-dd-yyyy";

        String date = DateFormat.format(dateFormat, cal).toString();

        return date;
    } //"dd-MM-yyyy hh:mm:ss aa"

    private void clanDetails(final String clanId_, final String clanCountry_) {


        runOnUiThread(new Runnable() {

            HttpGetRequest getRequest = new HttpGetRequest(null);
            String urlClan = urlClan1 + clanCountry_ + urlClan2 + clanId_;

            @Override
            public void run() {

                try {
                    String result = getRequest.execute(urlClan).get();
                    JSONObject jsonObject = new JSONObject(result);

                    JSONObject objData = jsonObject.getJSONObject("data");
                    JSONObject objectIdShip = (JSONObject) objData.get(clanId_);
                    jsonArray = objectIdShip.getJSONArray("members_ids");

                    members_count = objectIdShip.get("members_count").toString();
                    name          = objectIdShip.get("name").toString();
                    creator_name  = objectIdShip.get("creator_name").toString();
                    created_at    = objectIdShip.get("created_at").toString();
                    tag           = objectIdShip.get("tag").toString();
                    description   = objectIdShip.get("description").toString();;


                    textViewMember.setText(members_count);
                    textViewName.setText(name);
                    textViewCreatorName.setText(creator_name);
                    textViewCreatedAt.setText(getDate(Long.valueOf(created_at)));
                    textViewTag.setText(tag);
                    textViewDescripion.setText(description);



                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }


            }
        });





    }

}
