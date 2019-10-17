package com.wows.status;


/*
tabLayout.setupWithViewPager() – Assigns the ViewPager to TabLayout.
setupViewPager() – Defines the number of tabs by setting appropriate fragment and tab name.
ViewPagerAdapter – Custom adapter class provides fragments required for the view pager.
*/

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;


public class TabActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {


        String id = getIntent().getExtras().getString("id");
        String country = getIntent().getExtras().getString("country");


        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("country", country);

        Details details = new Details();
        details.setArguments(bundle);

        Graphic graphic = new Graphic();
        graphic.setArguments(bundle);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(details, "PROFILE");
        adapter.addFragment(graphic, "CHART");
        adapter.addFragment(new ShipsDetails(), "Ships Details");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SingletonsClass singletonsClass = SingletonsClass.getInstance();
        singletonsClass.clear();

    }


}
