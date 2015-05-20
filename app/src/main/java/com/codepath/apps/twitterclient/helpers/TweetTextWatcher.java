package com.codepath.apps.twitterclient.helpers;

import android.content.res.Resources;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;


public class TweetTextWatcher implements TextWatcher {

    private TextView tvTweetWordCount;
    private EditText etTweetBody;
    private Button btnTweet;
    private Resources resources;
    private final int UPPER_LIMIT_OF_CHARACTERS = 140;

    public TweetTextWatcher(TextView tvTweetWordCount, EditText etTweetBody, Button btnTweet, Resources resources) {
        this.tvTweetWordCount = tvTweetWordCount;
        this.etTweetBody = etTweetBody;
        this.btnTweet = btnTweet;
        this.resources = resources;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int currentCount = etTweetBody.getText().length();
        int difference = UPPER_LIMIT_OF_CHARACTERS - currentCount;
        if(difference <= 10)
        {
            tvTweetWordCount.setTextColor(Color.RED);
        }else
        {
            tvTweetWordCount.setTextColor(Color.BLACK);
        }

        if(difference < 0)
        {
            btnTweet.setClickable(false);
            btnTweet.setBackgroundColor(resources.getColor(R.color.twitter_greyed_out));
        }else
        {
            btnTweet.setClickable(true);
            btnTweet.setBackgroundColor(resources.getColor(R.color.twitter_blue));
        }

        tvTweetWordCount.setText(String.valueOf(difference));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
