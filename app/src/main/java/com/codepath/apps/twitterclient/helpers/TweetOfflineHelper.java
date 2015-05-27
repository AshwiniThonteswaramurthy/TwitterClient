package com.codepath.apps.twitterclient.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

public class TweetOfflineHelper {

    //All the tweets loaded in the adapter would be saved to the SQLiteDB
    public synchronized void saveTweetsForOfflineAccess(final TweetsArrayAdapter tweetsArrayAdapter) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < tweetsArrayAdapter
                        .getCount(); ++i) {
                    Tweet tweet = tweetsArrayAdapter.getItem(i);
                        tweet.getUser().save();
                    tweet.save();
                }
                return null;
            }
        }.execute();

    }

    public synchronized void loadTweetsOfflineMode()
    {
        new AsyncTask<Void, Void, List<Tweet>>() {

            @Override
            protected List<Tweet> doInBackground(Void... params) {
                return Tweet.getAll();
            }
        }.execute();
    }

    public synchronized void uploadMedia(final String fileUrl)
    {
        new AsyncTask<Void,Void,String>()
        {
            JSONObject jsonObject;
            @Override
            protected String doInBackground(Void... voids) {
                TwitterClient client = TwitterApplication.getRestClient();
                client.uploadMedia(fileUrl, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                         jsonObject = response;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("FAILURE", errorResponse.toString());
                    }
                });
                return String.valueOf(jsonObject.optLong("media_id"));
            }
        }.execute();
    }

}
