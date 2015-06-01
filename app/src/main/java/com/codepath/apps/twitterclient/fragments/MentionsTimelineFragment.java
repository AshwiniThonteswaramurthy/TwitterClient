package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.helpers.TweetOfflineHelper;

public class MentionsTimelineFragment extends ListFragment {

    public static MentionsTimelineFragment newInstance() {
        MentionsTimelineFragment fragment = new MentionsTimelineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO work on the contents of the fragment xml
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //loading tweets for the first time
        customLoadMoreDataFromApi(0);

        return view;
    }

    public void customLoadMoreDataFromApi(int offset) {
        if (offset == 0) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            offlineHelper.populateTimeline(0l, TweetOfflineHelper.TweetType.MENTIONS);
        }
        if (!adapter.isEmpty()) {
            offlineHelper.populateTimeline(adapter.getItem(adapter.getCount() - 1).getTid(), TweetOfflineHelper.TweetType.MENTIONS);
        }

    }
}
