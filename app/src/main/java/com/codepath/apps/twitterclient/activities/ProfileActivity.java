package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.UserProfileHeaderAdapter;
import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;

public class ProfileActivity extends ActionBarActivity {

    private UserTimelineFragment userFragment;
    private FragmentTransaction transaction;
    private ViewPager vpProfileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupActionBar();

        TextView tvProfileViewTweetCount = (TextView) findViewById(R.id.tvProfileViewTweetCount);
        TextView tvProfileViewFollowingCount = (TextView) findViewById(R.id.tvProfileViewFollowingCount);
        TextView tvProfileViewFollowersCount = (TextView) findViewById(R.id.tvProfileViewFollowersCount);

        final User user = getIntent().getParcelableExtra("user");

        vpProfileView = (ViewPager) findViewById(R.id.vpProfileView);
        vpProfileView.setAdapter(new UserProfileHeaderAdapter(getSupportFragmentManager(), user));

        tvProfileViewTweetCount.setText(Html.fromHtml("<b><font color=\"black\">" + user.getTweetCount() + "</font></b>" + "<br/>TWEETS"));
        tvProfileViewFollowersCount.setText(Html.fromHtml("<b><font color=\"black\">" + user.getFollowersCount() + "</font></b>" + "<br/>FOLLOWERS"));
        tvProfileViewFollowingCount.setText(Html.fromHtml("<b><font color=\"black\">" + user.getFollowingCount() + "</font></b>" + "<br/>FOLLOWING"));

        tvProfileViewFollowersCount.setClickable(true);
        tvProfileViewFollowersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UsersActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("user", user);
                data.putString("user_type", UsersActivity.UserType.FOLLOWERS.name());
                intent.putExtras(data);
                startActivityForResult(intent, 201);
            }
        });

        tvProfileViewFollowingCount.setClickable(true);
        tvProfileViewFollowingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UsersActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("user", user);
                data.putString("user_type", UsersActivity.UserType.FOLLOWING.name());
                intent.putExtras(data);
                startActivityForResult(intent, 201);
            }
        });

        generateUserTimelineData(user);

    }


    @Override
    public void onBackPressed() {
        if (vpProfileView.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            vpProfileView.setCurrentItem(vpProfileView.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_custom_layout_profile_page);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void generateUserTimelineData(User user) {
        if (userFragment == null) {
            userFragment = UserTimelineFragment.newInstance(user);
        }
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flTimeline, userFragment);
        transaction.commit();
    }

}
