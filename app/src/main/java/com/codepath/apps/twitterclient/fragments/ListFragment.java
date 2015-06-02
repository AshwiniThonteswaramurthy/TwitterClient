package com.codepath.apps.twitterclient.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.DetailedTweetInformationActivity;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.helpers.EndlessScrollListener;
import com.codepath.apps.twitterclient.helpers.NetworkHelper;
import com.codepath.apps.twitterclient.helpers.PullToRefreshListView;
import com.codepath.apps.twitterclient.helpers.TweetOfflineHelper;
import com.codepath.apps.twitterclient.interfaces.TweetResultsCallBack;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements TweetResultsCallBack {

    public TweetsArrayAdapter adapter;
    public PullToRefreshListView lvTweets;
    public TweetOfflineHelper offlineHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TweetsArrayAdapter(getActivity(), new ArrayList<Tweet>());
        offlineHelper = new TweetOfflineHelper(adapter, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_common, container, false);

        lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(adapter);

        refresh();

        showDetailedView();

        endlessScroll();

        return view;
    }

    private void refresh() {

        lvTweets.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    adapter.clear();
                    customLoadMoreDataFromApi(0);
                    adapter.notifyDataSetChanged();
                }
                lvTweets.onRefreshComplete();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();
        offlineHelper.saveTweetsForOfflineAccess();
    }

    private void showDetailedView() {
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), DetailedTweetInformationActivity.class);
                intent.putExtra("tweet", adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void endlessScroll() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    customLoadMoreDataFromApi(totalItemsCount);
                } else {
                    offlineHelper.loadTweetsOfflineMode();
                }
            }
        });
    }

    public void customLoadMoreDataFromApi(int offset) {
        //This will be overridden in each implementation
    }

    @Override
    public void onSuccess(List<Tweet> tweets) {
        adapter.addAll(tweets);
    }
}
