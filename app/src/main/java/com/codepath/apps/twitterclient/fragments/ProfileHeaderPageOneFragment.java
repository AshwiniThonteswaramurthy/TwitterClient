package com.codepath.apps.twitterclient.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ProfileHeaderPageOneFragment extends Fragment {

    private User user;

    public static ProfileHeaderPageOneFragment newInstance(User user) {
        ProfileHeaderPageOneFragment fragment = new ProfileHeaderPageOneFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_page_one, container, false);

        ImageView ivProfilePic = (ImageView) view.findViewById(R.id.ivProfileViewProfileImage);
        TextView tvProfileViewUserName = (TextView) view.findViewById(R.id.tvProfileViewUserName);
        TextView tvProfileViewScreenName = (TextView) view.findViewById(R.id.tvProfileViewScreenName);
        ImageView ivHeaderPic = (ImageView) view.findViewById(R.id.ivProfileViewHeaderImage);

        if (user.getHeaderUrl() != null) {
            Picasso.with(getActivity())
                    .load(user.getHeaderUrl())
                    .into(ivHeaderPic);
        }
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(5)
                .oval(false)
                .build();

        Picasso.with(getActivity())
                .load(user.getProfileImageUrl())
                .fit()
                .transform(transformation)
                .into(ivProfilePic);

        tvProfileViewUserName.setText(user.getName());
        tvProfileViewScreenName.setText("@" + user.getScreenName());

        return view;
    }
}
