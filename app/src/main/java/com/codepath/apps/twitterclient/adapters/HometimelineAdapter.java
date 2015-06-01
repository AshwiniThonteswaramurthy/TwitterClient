package com.codepath.apps.twitterclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;

public class HometimelineAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String[] tabtitles = new String[]{"Home", "Mentions"};

    public HometimelineAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("GET_ITEM",position+"");
        switch (position) {
            case 0:
                return HomeTimelineFragment.newInstance();
            case 1:
                return MentionsTimelineFragment.newInstance();
            default:
                return HomeTimelineFragment.newInstance();
        }


    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
