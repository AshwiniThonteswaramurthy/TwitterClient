package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name="user")
public class User extends Model implements Parcelable {

    @Column(name="name")
    private String name;
    @Column(name="uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name="screenName")
    private String screenName;
    @Column(name="profileImageUrl")
    private String profileImageUrl;
    @Column(name="followingCount")
    private int followingCount;
    @Column(name="followersCount")
    private int followersCount;
    @Column(name="tweetCount")
    private int tweetCount;
    @Column(name="location")
    private String location;
    @Column(name="description")
    private String description;
    @Column(name="headerUrl")
    private String headerUrl;

    public static User fromJson(JSONObject userObject)
    {
        User user = new User();
        user.name = userObject.optString("name");
        user.uid = userObject.optLong("id");
        user.screenName = userObject.optString("screen_name");
        user.profileImageUrl = userObject.optString("profile_image_url");
        user.followersCount = userObject.optInt("followers_count");
        user.followingCount = userObject.optInt("friends_count");
        user.tweetCount = userObject.optInt("statuses_count");
        user.headerUrl = userObject.optString("profile_banner_url");
        user.description = userObject.optString("description");
        return user;
    }

    public static ArrayList<User> fromJsonArray(JSONArray usersJson)
    {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < usersJson.length(); i++) {
            users.add(User.fromJson(usersJson.optJSONObject(i)));
        }
        return users;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public User() {
    }

    public List<Tweet> items() {
        return getMany(Tweet.class, "User");
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", uid=" + uid +
                ", screenName='" + screenName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", followingCount=" + followingCount +
                ", followersCount=" + followersCount +
                ", tweetCount=" + tweetCount +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeInt(this.followingCount);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.tweetCount);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeString(this.headerUrl);
    }

    private User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.followingCount = in.readInt();
        this.followersCount = in.readInt();
        this.tweetCount = in.readInt();
        this.location = in.readString();
        this.description = in.readString();
        this.headerUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
