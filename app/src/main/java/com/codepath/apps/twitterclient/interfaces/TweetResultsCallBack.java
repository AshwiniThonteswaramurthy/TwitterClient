package com.codepath.apps.twitterclient.interfaces;

import com.codepath.apps.twitterclient.models.Tweet;

import java.util.List;

public interface TweetResultsCallBack
{
    public void onSuccess(List<Tweet> tweets);
}