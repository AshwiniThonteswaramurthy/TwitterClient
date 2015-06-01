package com.codepath.apps.twitterclient.helpers;

import android.content.Context;

import com.codepath.apps.twitterclient.models.User;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.util.List;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = "CdzFMj4J6ExodY7Z2vESnL0Hg";
    public static final String REST_CONSUMER_SECRET = "xnRVcY4n8ZUvE3pBI4Bl2iKRprWqTaFk0oVABKVyPM5Rv1mP7U";
    public static final String REST_CALLBACK_URL = "oauth://cptwitterclient";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        if(maxId != 0l)
        {
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        if(maxId != 0l)
        {
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, Long maxId, AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        if(screenName != null || !screenName.isEmpty())
        {
            params.put("screen_name", screenName);
        }
        if(maxId != 0l)
        {
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void searchTweets(String query,long maxId, AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();
        params.add("q", query);
        if(maxId != 0l)
        {
            params.put("max_id", maxId);
        }
        client.get(apiUrl,params,handler);
    }
    public void postTweet(String body, String inReplyToStatusId,List<String> mediaId, AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        if(inReplyToStatusId != null && !inReplyToStatusId.isEmpty())
        {
            params.put("in_reply_to_status_id", inReplyToStatusId);
        }
        if(mediaId != null && !mediaId.isEmpty())
        {
            params.put("media_ids", mediaId);
        }
        client.post(apiUrl, params, handler);

    }

    public void postDirectMessage(String body, String toSreenName, AsyncHttpResponseHandler handler)
    {
        String url = getApiUrl("direct_messages/new.json");
        RequestParams params = new RequestParams();
        params.add("screen_name", toSreenName);
        params.add("text", body);
        client.post(url,params,handler);
    }

    public void getLoggedInUserDetails(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("skip_status", String.valueOf(true));
        client.get(apiUrl, params, handler);
    }

    public void makeFavorite(long statusId, AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", String.valueOf(statusId));
        client.post(apiUrl, params, handler);
    }

    public void unFavorite(long statusId, AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", String.valueOf(statusId));
        client.post(apiUrl, params, handler);
    }

    public void getUsersFavoritesList(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("favorites/list.json");
        client.get(apiUrl, null, handler);
    }

    public void retweet(String tweetId, AsyncHttpResponseHandler handler)
    {
        String url = getApiUrl("statuses/retweet/"+tweetId+".json");
        RequestParams params = new RequestParams();
        params.add("id", tweetId);
        client.post(url, params, handler);
    }

    public void getUsersInformation(User user, AsyncHttpResponseHandler handler)
    {
        String url = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.add("user_id", String.valueOf(user.getUid()));
        params.add("screen_name", String.valueOf(user.getScreenName()));
        client.get(url, params, handler);
    }

    public void getAllFollowers(User user, AsyncHttpResponseHandler handler)
    {
        String url = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        params.add("screen_name", user.getScreenName());
        client.get(url, params, handler);
    }

    public void getAllFriends(User user, AsyncHttpResponseHandler handler)
    {
        String url = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.add("screen_name", user.getScreenName());
        client.get(url, params, handler);
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}