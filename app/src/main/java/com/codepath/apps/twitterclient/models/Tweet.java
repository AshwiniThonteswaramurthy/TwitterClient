package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "tweets")
public class Tweet extends Model implements Parcelable {

    @Column(name = "body")
    private String body;
    @Column(name = "tid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tid;
    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "createdAt")
    private String createdAt;
    @Column(name = "retweetCount")
    private int retweetCount = 0;
    @Column(name = "favoriteCount")
    public int favoriteCount = 0;
    @Column(name = "mediaType")
    private String mediaType;
    @Column(name = "mediaDisplayUrl")
    private String mediaDisplayUrl;

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.optString("text");
        tweet.tid = jsonObject.optLong("id");
        tweet.createdAt = jsonObject.optString("created_at");
        tweet.retweetCount = jsonObject.optInt("retweet_count", 0);
        tweet.favoriteCount = jsonObject.optInt("favorite_count", 0);
        tweet.user = User.fromJson(jsonObject.optJSONObject("user"));
        JSONObject entities = jsonObject.optJSONObject("entities");
        if (entities != null) {
            try {
                JSONArray media = entities.optJSONArray("media");
                if (media != null) {
                    if (media.length() > 0) {
                        tweet.mediaType = media.optJSONObject(0).optString("type");
                        tweet.mediaDisplayUrl = media.optJSONObject(0).optString("media_url");
                    }
                }
            } catch (NullPointerException exception) {
                //This generally means media is not present move on
            }
        }
        return tweet;
    }


    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(Tweet.fromJson(jsonArray.optJSONObject(i)));
        }
        return tweets;
    }

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .orderBy("createdAt DESC")
                .execute();
    }

    public String getBody() {
        return body;
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

    public String getMediaType() {
        return mediaType;
    }

    public String getMediaDisplayUrl() {
        return mediaDisplayUrl;
    }

    public long getTid() {
        return tid;
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
        dest.writeLong(this.tid);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.createdAt);
        dest.writeInt(this.retweetCount);
        dest.writeInt(this.favoriteCount);
        dest.writeString(this.mediaType);
        dest.writeString(this.mediaDisplayUrl);
    }

    private Tweet(Parcel in) {
        this.body = in.readString();
        this.tid = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.retweetCount = in.readInt();
        this.favoriteCount = in.readInt();
        this.mediaType = in.readString();
        this.mediaDisplayUrl = in.readString();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    @Override
    public String toString() {
        return "Tweet{" +
                "body='" + body + '\'' +
                ", id=" + tid +
                ", user=" + user.toString() +
                ", createdAt='" + createdAt + '\'' +
                ", retweetCount=" + retweetCount +
                ", favoriteCount=" + favoriteCount +
                ", mediaType='" + mediaType + '\'' +
                ", mediaDisplayUrl='" + mediaDisplayUrl + '\'' +
                '}';
    }
}
