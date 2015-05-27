package com.codepath.apps.twitterclient.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.helpers.EndlessScrollListener;
import com.codepath.apps.twitterclient.helpers.NetworkHelper;
import com.codepath.apps.twitterclient.helpers.TweetOfflineHelper;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.helpers.TwitterUtils;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeTimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private TweetsArrayAdapter adapter;
    private ArrayList<Tweet> tweets;
    private ListView lvHomeTimeline;
    private Long nextSetOfMaxId = null;
    private SwipeRefreshLayout swipeContainer;
    private TwitterUtils utils;
    private TweetOfflineHelper offlineHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);

        lvHomeTimeline = (ListView) findViewById(R.id.lvHomeTimeline);

        tweets = new ArrayList<>();
        adapter = new TweetsArrayAdapter(this, tweets);
        lvHomeTimeline.setAdapter(adapter);

        client = TwitterApplication.getRestClient();
        utils = new TwitterUtils(client, getFragmentManager());
        offlineHelper = new TweetOfflineHelper();

        populateTimeline();
        setupActionBar();

        showDetailedView();

        refreshTimeline();

        lvHomeTimeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(page == 0)
                {
                    tweets.clear();
                    adapter.notifyDataSetChanged();
                }
                Log.d("DEBUG ENDLESS SCROLLING", "page: " + page + " totalItemsCount: " + totalItemsCount + " max_id: " + nextSetOfMaxId);
                populateTimeline();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Should save data here
        offlineHelper.saveTweetsForOfflineAccess(adapter);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_custom_layout);
    }

    private void showDetailedView() {
        lvHomeTimeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(HomeTimelineActivity.this, DetailedTweetInformationActivity.class);
                intent.putExtra("tweet", adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void refreshTimeline() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //initially until users pulls on the screen there would be no data
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                clearItems();
                populateTimeline();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void clearItems() {
        tweets.clear();
        adapter.clear();
        adapter.notifyDataSetChanged();
        nextSetOfMaxId = null;
    }

    private void populateTimeline() {
        if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            client.getHomeTimeline(nextSetOfMaxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    swipeContainer.setRefreshing(false);
                    adapter.addAll(Tweet.fromJsonArray(response));
                    adapter.notifyDataSetChanged();
                    //index start from 0 so last item would be count -1
                    nextSetOfMaxId = adapter.getItem(adapter.getCount() - 1).getTid();
                    Log.d("DEBUG: ", "nextSetOFMaxId: "+ nextSetOfMaxId);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("FAILURE", errorResponse.toString());
                }
            });
        } else {
            offlineHelper.loadTweetsOfflineMode();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.miCompose:
                utils.onComposeTweet(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
