package com.codepath.apps.twitterclient.helpers;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.fragments.ComposeTweetDialog;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codepath.apps.twitterclient.models.User.fromJson;

public class TwitterUtils {

    private TwitterClient client;
    private FragmentManager fragmentManager;
    private Activity activity;

    public TwitterUtils(TwitterClient client, Activity activity) {
        this.client = client;
        this.activity = activity;
        this.fragmentManager = activity.getFragmentManager();
    }

    public void onComposeTweet(final Tweet tweet) {
        client.getLoggedInUserDetails(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ComposeTweetDialog dialog = ComposeTweetDialog.newInstance();
                Bundle data = new Bundle();
                data.putParcelable("user", fromJson(response));
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

    public ArrayList<Long> getAllMyFavoriteTweets() {
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

    public void onProfileView(User user) {
        if (user == null) {
            // The user would be the currently loggedin user
            client.getLoggedInUserDetails(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    User loggedinUser = fromJson(response);
                    getAdditionalUserInfo(loggedinUser);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("onFailure", responseString);
                }
            });
        } else {
            getAdditionalUserInfo(user);
        }

    }

    private void getAdditionalUserInfo(User user)
    {
        client.getUsersInformation(user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Extract the information to display the user profile page
                launchProfileView(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void launchProfileView(JSONObject response) {
        Log.d("DEBUG 0", response.toString());
        Intent intent = new Intent(activity, ProfileActivity.class);
        Bundle data = new Bundle();
        data.putParcelable("user", fromJson(response));
        intent.putExtras(data);
        activity.startActivityForResult(intent, 143);
    }

    public void onSearch(String query)
    {

    }
}
