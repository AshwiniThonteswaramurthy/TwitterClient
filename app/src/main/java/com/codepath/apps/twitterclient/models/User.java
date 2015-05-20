package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class User implements Parcelable {
    private String name;
    private long id;
    private String screenName;
    private String profileImageUrl;

    public static User fromJson(JSONObject jsonObject)
    {
        User user = new User();
        user.name = jsonObject.optString("name");
        user.id = jsonObject.optLong("id");
        user.screenName = jsonObject.optString("screen_name");
        user.profileImageUrl = jsonObject.optString("profile_image_url");
        return user;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.id);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
    }

    public User() {
    }

    private User(Parcel in) {
        this.name = in.readString();
        this.id = in.readLong();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", screenName='" + screenName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
