package com.codepath.apps.twitterclient.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.DateHelper;
import com.codepath.apps.twitterclient.models.Tweet;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvCreatedAt;
        TextView tvRetweetCount;
        TextView tvFavoriteCount;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAT);
            viewHolder.tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
            viewHolder.tvFavoriteCount = (TextView) convertView.findViewById(R.id.tvFavoriteCount);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
        if(tweet.getRetweetCount() >0) {
            viewHolder.tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        }
        if(tweet.getFavoriteCount() > 0) {
            viewHolder.tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
        }
        viewHolder.tvCreatedAt.setText(DateHelper.getTimeString(new Date(tweet.getCreatedAt())));

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(5)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .fit()
                .transform(transformation)
                .into(viewHolder.ivProfileImage);

        return convertView;
    }
}
