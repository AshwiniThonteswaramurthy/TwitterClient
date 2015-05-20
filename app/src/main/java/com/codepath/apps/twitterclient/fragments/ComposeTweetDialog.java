package com.codepath.apps.twitterclient.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.TweetTextWatcher;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetDialog extends DialogFragment {

    private EditText etTweetBody;
    private ImageView ivComposeProfileImage;
    private TextView tvComposeUserName;
    private TextView tvComposeScreenName;
    private ImageView ivComposeTweetClose;
    private Button btnTweet;
    private TextView tvTweetWordCount;
    private TwitterClient client;

    //TODO hide the actionbar

    public static ComposeTweetDialog newInstance() {
        ComposeTweetDialog composeTweetDialog = new ComposeTweetDialog();
        return composeTweetDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose_tweet_fragment, container);
        client = new TwitterClient(getActivity());
        User currentlyLoggedinUser = (User) getArguments().get("user");

        ivComposeTweetClose = (ImageView) view.findViewById(R.id.ivComposeTweetClose);
        ivComposeProfileImage = (ImageView) view.findViewById(R.id.ivComposeProfileImage);
        tvComposeUserName = (TextView) view.findViewById(R.id.tvComposeUserName);
        tvComposeScreenName = (TextView) view.findViewById(R.id.tvComposeScreenName);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        tvTweetWordCount = (TextView) view.findViewById(R.id.tvTweetWordCount);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetbody = String.valueOf(etTweetBody.getText());
                client.postTweet(tweetbody, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("POST_TWEET_ON_SUCCESS:", response.toString());
                        Tweet newTweet = Tweet.fromJson(response);
                        Intent data = new Intent();
                        data.putExtra("tweet", newTweet);
                        getActivity().setResult(200, data);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("POST_TWEET_ON_FAILURE", errorResponse.toString());
                    }
                });
            }
        });

        ivComposeTweetClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ivComposeProfileImage.setImageResource(android.R.color.transparent);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(5)
                .oval(false)
                .build();

        Picasso.with(getActivity())
                .load(currentlyLoggedinUser.getProfileImageUrl())
                .fit()
                .transform(transformation)
                .into(ivComposeProfileImage);

        tvComposeUserName.setText(currentlyLoggedinUser.getName());
        tvComposeScreenName.setText(currentlyLoggedinUser.getScreenName());
        etTweetBody = (EditText) view.findViewById(R.id.etTweetBody);
        etTweetBody.requestFocus();
        //to grey out the button before user starts typing any text
        if(etTweetBody.getText().length() == 0)
        {
            btnTweet.setClickable(false);
            btnTweet.setBackgroundColor(getResources().getColor(R.color.twitter_greyed_out));
        }

        TweetTextWatcher textWatcher = new TweetTextWatcher(tvTweetWordCount,etTweetBody, btnTweet, getResources());
        etTweetBody.addTextChangedListener(textWatcher);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}
