package com.wows.status;


/*
tabLayout.setupWithViewPager() – Assigns the ViewPager to TabLayout.
setupViewPager() – Defines the number of tabs by setting appropriate fragment and tab name.
ViewPagerAdapter – Custom adapter class provides fragments required for the view pager.
*/

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class TabActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void setupViewPager(ViewPager viewPager) {


        String id = getIntent().getExtras().getString("id");
        String country = getIntent().getExtras().getString("country");


        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("country", country);

        DetailsPlayerFragment details = new DetailsPlayerFragment();
        details.setArguments(bundle);

        GraphicFragment graphic = new GraphicFragment();
        graphic.setArguments(bundle);

        ProgressFragment progressFragment = new ProgressFragment();
        progressFragment.setArguments(bundle);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(details, "PROFILE");
        adapter.addFragment(graphic, "CHART");
        adapter.addFragment(progressFragment, "PROGRESS");
        adapter.addFragment(new ShipsDetailsFragment(), "Ships Details");

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

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }


}
