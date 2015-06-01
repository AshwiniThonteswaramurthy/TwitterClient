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
import com.codepath.apps.twitterclient.models.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User>{

    public static class UserViewHolder {
        ImageView ivUserProfilePic;
        TextView tvUserName;
        TextView tvDescription;
        TextView tvScreenName;
    }

    public UsersAdapter(Context context, List<User> users) {
        super(context, R.layout.item_user, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        UserViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new UserViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            viewHolder.ivUserProfilePic = (ImageView) convertView.findViewById(R.id.ivUserProfilePic);
            viewHolder.tvUserName= (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }

        viewHolder.ivUserProfilePic.setImageResource(android.R.color.transparent);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(5)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(user.getProfileImageUrl())
                .fit()
                .transform(transformation)
                .into(viewHolder.ivUserProfilePic);

        viewHolder.tvUserName.setText(user.getName());
        viewHolder.tvScreenName.setText("@" + user.getScreenName());
        viewHolder.tvDescription.setText(user.getDescription());

        return convertView;
    }
}
