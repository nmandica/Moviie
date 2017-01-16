package com.nmandica.dev.moviie;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class RecentMoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.movie_title);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(RecentMoviesFragment.newInstance(0), getString(R.string.Famous));
        viewPagerAdapter.addFragment(RecentMoviesFragment.newInstance(1), getString(R.string.top_rated));
        viewPagerAdapter.addFragment(RecentMoviesFragment.newInstance(2), getString(R.string.upcoming));
        viewPager.setAdapter(viewPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> fragmentList = new ArrayList();
        private final List<String> fragmentTitleList = new ArrayList();

        ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        void addFragment(Fragment fragment, String string)
        {
            this.fragmentList.add(fragment);
            this.fragmentTitleList.add(string);
        }

        @Override
        public int getCount()
        {
            return this.fragmentList.size();
        }

        @Override
        public Fragment getItem(int position)
        {
            return this.fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return this.fragmentTitleList.get(position);
        }
    }
}
