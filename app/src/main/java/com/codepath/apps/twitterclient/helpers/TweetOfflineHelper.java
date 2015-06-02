package com.codepath.apps.twitterclient.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.interfaces.TweetResultsCallBack;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TweetOfflineHelper extends JsonHttpResponseHandler {

    private TweetsArrayAdapter adapter;
    private TweetResultsCallBack callBack;
    private TwitterClient client;

    public enum TweetType {
        HOME, MENTIONS, USER
    }

    public TweetOfflineHelper(TweetsArrayAdapter adapter, TweetResultsCallBack callBack) {
        this.adapter = adapter;
        this.callBack = callBack;
        client = TwitterApplication.getRestClient();
    }

    //All the tweets loaded in the adapter would be saved to the SQLiteDB
    public synchronized void saveTweetsForOfflineAccess() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < adapter
                        .getCount(); ++i) {
                    Tweet tweet = adapter.getItem(i);
                    tweet.getUser().save();
                    tweet.save();
                }
                return null;
            }

        }.execute();


    }

    public synchronized void loadTweetsOfflineMode() {
        new AsyncTask<Void, Void, List<Tweet>>() {

            @Override
            protected List<Tweet> doInBackground(Void... params) {
                return Tweet.getAll();
            }
        }.execute();
    }

    public void populateTimeline(final String screenName, final long maxId, TweetType type) {
        switch (type) {
            case HOME:
                client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        adapter.addAll(Tweet.fromJsonArray(response));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("FAILURE", errorResponse.toString());
                    }
                });
                break;
            case MENTIONS:
                client.getMentionsTimeline(maxId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        adapter.addAll(Tweet.fromJsonArray(response));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("FAILURE", errorResponse.toString());
                    }
                });
                break;
            case USER:
                client.getUserTimeline(screenName, maxId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        adapter.addAll(Tweet.fromJsonArray(response));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("FAILURE", errorResponse.toString());
                    }
                });
                break;
        }
    }

//    public void populateUserTimeline(final String screenName, final long maxId) {
//        client.getUserTimeline(screenName, maxId, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                adapter.addAll(Tweet.fromJsonArray(response));
//            }
//        });
//    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        int RESULT_OK = 200;
        if (statusCode == RESULT_OK) {
            callBack.onSuccess(Tweet.fromJsonArray(response));
        }
    }

    public void populateSearchResults(final String query, final long maxId) {
        client.searchTweets(query, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                adapter.addAll(Tweet.fromJsonArray(response.optJSONArray("statuses")));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("ERROR_SEARCH", errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
