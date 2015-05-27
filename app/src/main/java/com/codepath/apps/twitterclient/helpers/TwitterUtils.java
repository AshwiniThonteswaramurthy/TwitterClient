package com.codepath.apps.twitterclient.helpers;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterclient.fragments.ComposeTweetDialog;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TwitterUtils {
    private TwitterClient client;
    private FragmentManager fragmentManager;

    public TwitterUtils(TwitterClient client, FragmentManager fragmentManager) {
        this.client = client;
        this.fragmentManager = fragmentManager;
    }

    public void onComposeTweet(final Tweet tweet) {
        client.getLoggedInUserDetails(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ComposeTweetDialog dialog = ComposeTweetDialog.newInstance();
                Bundle data = new Bundle();
                data.putParcelable("user", User.fromJson(response));
                if (tweet != null) {
                    data.putParcelable("tweet", tweet);
                }
                dialog.setArguments(data);
                dialog.show(fragmentManager, "compose_tweet");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("onFailure", responseString);
            }
        });
    }

    public ArrayList<Long> getAllMyFavoriteTweets()
    {
        final ArrayList<Long> myFavs = new ArrayList<>();
        client.getUsersFavoritesList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    myFavs.add(response.optJSONObject(i).optLong("id"));
                }
            }
        });
        return myFavs;
    }
}
