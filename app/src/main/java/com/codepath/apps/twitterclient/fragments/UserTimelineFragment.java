package com.codepath.apps.twitterclient.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.helpers.TweetOfflineHelper;
import com.codepath.apps.twitterclient.models.User;

public class UserTimelineFragment extends ListFragment {

    private User user;

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
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
            offlineHelper.populateTimeline(user.getScreenName(), adapter.getItem(adapter.getCount() - 1).getTid(), TweetOfflineHelper.TweetType.USER);
        } else {
            adapter.clear();
            offlineHelper.populateTimeline(user.getScreenName(), 0l, TweetOfflineHelper.TweetType.USER);
            adapter.notifyDataSetChanged();
        }
    }
}
