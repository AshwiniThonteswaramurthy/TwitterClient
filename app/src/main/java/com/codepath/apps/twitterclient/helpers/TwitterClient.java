package com.codepath.apps.twitterclient.helpers;

import android.content.Context;
import android.util.Log;

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
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "CdzFMj4J6ExodY7Z2vESnL0Hg";
    public static final String REST_CONSUMER_SECRET = "xnRVcY4n8ZUvE3pBI4Bl2iKRprWqTaFk0oVABKVyPM5Rv1mP7U";
    public static final String REST_CALLBACK_URL = "oauth://cptwitterclient"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        if(maxId != null)
        {
            Log.d("DEBUG MAX ID:" , maxId +" maxID");
            params.put("max_id", maxId);
        }
        client.get(apiUrl, params, handler);
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

    public void getLoggedInUserDetails(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("skip_status", String.valueOf(true));
        client.get(apiUrl, params, handler);
    }
    public void makeFavorite(long statusId, AsyncHttpResponseHandler handler)
    {
        String apiUrl = "https://api.twitter.com/1.1/favorites/create.json";
        RequestParams params = new RequestParams();
        params.put("id", String.valueOf(statusId));
        client.post(apiUrl, params, handler);
    }

    public void unFavorite(long statusId, AsyncHttpResponseHandler handler)
    {
        String apiUrl = "https://api.twitter.com/1.1/favorites/destroy.json";
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
        String url = "https://api.twitter.com/1.1/statuses/retweet/"+tweetId+".json";
        RequestParams params = new RequestParams();
        params.add("id", tweetId);
        client.post(url, params, handler);
    }

    //TODO camera wait until the picture is taken and then upload media
    public void uploadMedia(String fileUrl, AsyncHttpResponseHandler handler)
    {
        String url = "https://upload.twitter.com/1.1/media/upload.json";
        RequestParams params = new RequestParams();
        params.put("file", fileUrl);
        params.put("file-field", "media");
        client.post(url, params, handler);
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