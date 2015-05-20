package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet implements Parcelable {

    private String body;
    private long id;
    private User user;
    private String createdAt;
    private int retweetCount = 0;
    private int favoriteCount = 0;

    public static Tweet fromJson(JSONObject jsonObject)
    {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.optString("text");
        tweet.id = jsonObject.optLong("id");
        tweet.createdAt =jsonObject.optString("created_at");
        tweet.retweetCount = jsonObject.optInt("retweet_count",0);
        tweet.favoriteCount = jsonObject.optInt("favorite_count",0);
        tweet.user = User.fromJson(jsonObject.optJSONObject("user"));
        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray)
    {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            tweets.add(Tweet.fromJson(jsonArray.optJSONObject(i)));
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }


    public Tweet() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.id);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.createdAt);
        dest.writeInt(this.retweetCount);
        dest.writeInt(this.favoriteCount);
    }

    private Tweet(Parcel in) {
        this.body = in.readString();
        this.id = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.retweetCount = in.readInt();
        this.favoriteCount = in.readInt();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
