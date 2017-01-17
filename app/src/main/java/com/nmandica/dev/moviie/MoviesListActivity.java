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

public class MoviesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.movie_title);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public Fragment getItem(int position)
        {
            return MoviesListFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position) {
                case 0 :
                    return getResources().getString(R.string.famous);
                case 1 :
                    return getResources().getString(R.string.top_rated);
                default:
                case 2 :
                    return getResources().getString(R.string.upcoming);
            }
        }
    }
}
