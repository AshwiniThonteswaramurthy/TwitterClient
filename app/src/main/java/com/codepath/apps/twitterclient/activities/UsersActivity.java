package com.codepath.apps.twitterclient.activities;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.UsersAdapter;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersActivity extends ActionBarActivity {

    public enum UserType {
        FOLLOWING, FOLLOWERS
    }

    private ArrayList<User> users;
    private ListView lvUsers;
    private UsersAdapter adapter;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        setupActionBar();

        users = new ArrayList<>();
        lvUsers = (ListView) findViewById(R.id.lvUsers);
        adapter = new UsersAdapter(this, users);
        lvUsers.setAdapter(adapter);
        client = TwitterApplication.getRestClient();

        User user = getIntent().getParcelableExtra("user");
        String type = getIntent().getStringExtra("user_type");

        getListOfUsers(user, type);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getListOfUsers(User user, String type) {
        if (type.equals(UserType.FOLLOWERS.name())) {
            client.getAllFollowers(user, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    adapter.clear();
                    adapter.addAll(User.fromJsonArray(response.optJSONArray("users")));
                }
            });
        } else if (type.equals(UserType.FOLLOWING.name())) {
            client.getAllFriends(user, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    adapter.clear();
                    adapter.addAll(User.fromJsonArray(response.optJSONArray("users")));
                }
            });
        }
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_custom_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
