package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.helpers.TweetOfflineHelper;

public class HomeTimelineFragment extends ListFragment
{
    public static HomeTimelineFragment newInstance()
    {
        HomeTimelineFragment fragment = new HomeTimelineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //loading tweets for the first time
        customLoadMoreDataFromApi(0);

        return view;
    }

    public void customLoadMoreDataFromApi(int offset) {
        if (!adapter.isEmpty()) {
            offlineHelper.populateTimeline(adapter.getItem(adapter.getCount() -1).getTid(), TweetOfflineHelper.TweetType.HOME);
        }else{
           offlineHelper.populateTimeline(offset, TweetOfflineHelper.TweetType.HOME);
        }

    }
}
