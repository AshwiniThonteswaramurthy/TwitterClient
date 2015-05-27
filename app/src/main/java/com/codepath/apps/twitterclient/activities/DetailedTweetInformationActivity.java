package com.codepath.apps.twitterclient.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.NetworkHelper;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.helpers.TwitterUtils;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailedTweetInformationActivity extends ActionBarActivity {

    private Tweet tweet;
    private ImageView ivDetailedViewProfileImage;
    private TextView tvDetailedViewUserName;
    private TextView tvDetailedViewScreenName;
    private TextView tvDetailedViewBody;
    private ImageView ivDetailedViewEmbeddedImage;
    private TextView tvDetailedViewCreatedAt;
    private TextView tvDetailedViewRetweetCount;
    private TextView tvDetailedViewFavoriteCount;
    private ImageView ivDetailedViewReply;
    private ImageView ivDetailedViewFavorite;
    private ImageView ivDetailedViewRetweet;
    private TwitterClient client;
    private TwitterUtils utils;
    private ArrayList<Long> myFavTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_tweet_information);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_custom_layout_detailed_page);
        actionBar.setDisplayHomeAsUpEnabled(true);

        client = TwitterApplication.getRestClient();
        utils = new TwitterUtils(client, getFragmentManager());

        ivDetailedViewProfileImage = (ImageView) findViewById(R.id.ivDetailedViewProfileImage);
        tvDetailedViewUserName = (TextView) findViewById(R.id.tvDetailedViewUserName);
        tvDetailedViewScreenName = (TextView) findViewById(R.id.tvDetailedViewScreenName);
        tvDetailedViewBody = (TextView) findViewById(R.id.tvDetailedViewBody);
        ivDetailedViewEmbeddedImage = (ImageView) findViewById(R.id.ivDetailedViewEmbeddedImage);
        tvDetailedViewCreatedAt = (TextView) findViewById(R.id.tvDetailedViewCreatedAt);
        tvDetailedViewRetweetCount = (TextView) findViewById(R.id.tvDetailedViewRetweetCount);
        tvDetailedViewFavoriteCount = (TextView) findViewById(R.id.tvDetailedViewFavoriteCount);
        ivDetailedViewReply = (ImageView) findViewById(R.id.ivDetailedViewReply);
        ivDetailedViewFavorite = (ImageView) findViewById(R.id.ivDetailedViewFavorite);
        ivDetailedViewRetweet = (ImageView) findViewById(R.id.ivDetailedViewRetweet);

        if (NetworkHelper.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
            tweet = getIntent().getParcelableExtra("tweet");
            try {
                myFavTweets = utils.getAllMyFavoriteTweets();
            } catch (NullPointerException e) {
                Log.d("DEBUG", "This user does not have any favorites");
            }
            ivDetailedViewProfileImage.setImageResource(android.R.color.transparent);
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(3)
                    .cornerRadiusDp(5)
                    .oval(false)
                    .build();

            Picasso.with(this)
                    .load(tweet.getUser().getProfileImageUrl())
                    .fit()
                    .transform(transformation)
                    .into(ivDetailedViewProfileImage);

            tvDetailedViewUserName.setText(tweet.getUser().getName());
            tvDetailedViewScreenName.setText("@" + tweet.getUser().getScreenName());
            tvDetailedViewBody.setText(tweet.getBody());

            ivDetailedViewEmbeddedImage.setImageResource(android.R.color.transparent);
            if (tweet.getMediaDisplayUrl() != null && tweet.getMediaType().equals("photo")) {
                Picasso.with(this)
                        .load(tweet.getMediaDisplayUrl())
                        .into(ivDetailedViewEmbeddedImage);

            }

            Date date = new Date(tweet.getCreatedAt());
            DateFormat formatter = SimpleDateFormat.getDateTimeInstance();
            tvDetailedViewCreatedAt.setText(formatter.format(date));
            tvDetailedViewRetweetCount.setText(tweet.getRetweetCount() + " RETWEETS");
            tvDetailedViewFavoriteCount.setText(tweet.getFavoriteCount() + " FAVORITES");

            ivDetailedViewReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utils.onComposeTweet(tweet);
                }
            });

            favorite();

            ivDetailedViewRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    client.retweet(String.valueOf(tweet.getTid()), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivDetailedViewRetweet.setImageResource(R.drawable.iconmonstr_retweet_selected);
                            tvDetailedViewRetweetCount.setText(tweet.getRetweetCount()+1 + " RETWEETS");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            //super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        }


    }

    private void favorite() {
        ivDetailedViewFavorite.setClickable(true);
        ivDetailedViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isThisTweetMyFav(tweet.getTid())) {
                    client.unFavorite(tweet.getTid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivDetailedViewFavorite.setImageResource(R.drawable.iconmonstr_star_unfilled);
                            tweet.favoriteCount = tweet.getFavoriteCount() - 1;
                            tvDetailedViewFavoriteCount.setText(tweet.getFavoriteCount() + " FAVORITES");
                            myFavTweets.remove(tweet.getTid());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("FAVORITE ERROR", errorResponse.toString());
                        }
                    });

                } else {
                    client.makeFavorite(tweet.getTid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivDetailedViewFavorite.setImageResource(R.drawable.iconmonstr_star_filled);
                            tweet.favoriteCount = tweet.getFavoriteCount() + 1;
                            tvDetailedViewFavoriteCount.setText(tweet.getFavoriteCount() + " FAVORITES");
                            myFavTweets.add(tweet.getTid());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("FAVORITE ERROR", errorResponse.toString());
                        }
                    });
                }
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
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.miCompose:
                utils.onComposeTweet(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isThisTweetMyFav(Long tweetId) {
        return myFavTweets.contains(tweetId);
    }

}
