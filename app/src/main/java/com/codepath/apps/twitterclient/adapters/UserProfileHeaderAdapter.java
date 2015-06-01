package com.codepath.apps.twitterclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitterclient.fragments.ProfileHeaderPageOneFragment;
import com.codepath.apps.twitterclient.fragments.ProfileHeaderPageTwoFragment;
import com.codepath.apps.twitterclient.models.User;

public class UserProfileHeaderAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Page1", "Page2" };
    private User user;

    public UserProfileHeaderAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProfileHeaderPageOneFragment.newInstance(user);
            case 1:
                return ProfileHeaderPageTwoFragment.newInstance(user);
            default:
                return ProfileHeaderPageOneFragment.newInstance(user);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}

