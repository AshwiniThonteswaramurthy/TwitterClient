package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.fragments.ComposeTweetDialog;
import com.codepath.apps.twitterclient.helpers.EndlessScrollListener;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);

        lvHomeTimeline = (ListView) findViewById(R.id.lvHomeTimeline);
        tweets = new ArrayList<>();
        adapter = new TweetsArrayAdapter(this, tweets);
        lvHomeTimeline.setAdapter(adapter);
        client = TwitterApplication.getRestClient();

        //TODO the home label is little off place it in center
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_custom_layout);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //initially until users pulls on the screen there would be no data
        ///TODO check if this is the behaviour required
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        endlessScrolling();

    }

    private void endlessScrolling() {
        lvHomeTimeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });
    }

    private void populateTimeline() {
        client.getHomeTimeline(nextSetOfMaxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                adapter.addAll(Tweet.fromJsonArray(response));
                //index start from 0 so last item would be count -1
                nextSetOfMaxId = tweets.get(adapter.getCount() - 1).getId();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
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
                onComposeTweet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onComposeTweet() {

        client.getLoggedInUserDetails(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("onSuccess", response.toString());
                ComposeTweetDialog dialog = ComposeTweetDialog.newInstance();
                Bundle data = new Bundle();
                data.putParcelable("user", User.fromJson(response));
                dialog.setArguments(data);
                dialog.show(getFragmentManager(), "compose_tweet");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("onFailure", responseString);
                //  super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}
