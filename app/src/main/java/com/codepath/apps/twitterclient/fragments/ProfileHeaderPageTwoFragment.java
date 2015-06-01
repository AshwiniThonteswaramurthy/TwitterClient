package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.BlurTransformation;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ProfileHeaderPageTwoFragment extends Fragment {

    private static final float BLUR_RADIUS = 25F;
    private User user;

    public static ProfileHeaderPageTwoFragment newInstance(User user) {
        ProfileHeaderPageTwoFragment fragment = new ProfileHeaderPageTwoFragment();
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
        View view = inflater.inflate(R.layout.user_profile_page_two, container, false);

        ImageView ivHeaderPic = (ImageView) view.findViewById(R.id.ivProfileViewHeaderImage);
        TextView tvProfileViewHeaderDescription = (TextView) view.findViewById(R.id.tvProfileViewHeaderDescription);

        ivHeaderPic.setFocusable(false);
        Transformation blurTransformation = new BlurTransformation(getActivity(), BLUR_RADIUS);
        if (user.getHeaderUrl() != null) {
            Picasso.with(getActivity())
                    .load(user.getHeaderUrl())
                    .transform(blurTransformation)
                    .into(ivHeaderPic);
        }

        tvProfileViewHeaderDescription.setFocusable(true);
        tvProfileViewHeaderDescription.setText(Html.fromHtml("<p align=\"center\">" + user.getDescription()));

        return view;
    }
}
