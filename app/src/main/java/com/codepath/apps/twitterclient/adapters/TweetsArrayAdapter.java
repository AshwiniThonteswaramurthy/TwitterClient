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

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAT);
        TextView tvFavoriteCount = (TextView) convertView.findViewById(R.id.tvFavoriteCount);
        final ImageView ivFavoriteImage = (ImageView) convertView.findViewById(R.id.ivFavoriteImage);
        ivFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivFavoriteImage.setImageResource();
                //TODO on click save the favorites
            }
        });

        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvScreenName.setText("@"+tweet.getUser().getScreenName());

        tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));

        //TODO seperate images next to text based on type links, photo, video etc
        tvCreatedAt.setText(DateHelper.getTimeString(new Date(tweet.getCreatedAt())));

        ivProfileImage.setImageResource(android.R.color.transparent);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(5)
                .oval(false)
                .build();

        //TODO viewholder pattern
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .fit()
                .transform(transformation)
                .into(ivProfileImage);

        return convertView;
    }
}
