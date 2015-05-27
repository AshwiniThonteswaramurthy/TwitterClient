package com.codepath.apps.twitterclient.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.TweetOfflineHelper;
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

import java.io.File;
import java.util.ArrayList;

public class ComposeTweetDialog extends DialogFragment {

    private EditText etTweetBody;
    private ImageView ivComposeProfileImage;
    private TextView tvComposeUserName;
    private TextView tvComposeScreenName;
    private ImageView ivComposeTweetClose;
    private Button btnTweet;
    private TextView tvTweetWordCount;
    private TwitterClient client;
    private TweetOfflineHelper helper;
    private ArrayList mediaIds;

    public final String APP_TAG = "MyTwitterApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "mytwitterphoto.jpg";


    public static ComposeTweetDialog newInstance() {
        return new ComposeTweetDialog();
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
        mediaIds = new ArrayList();
        User currentlyLoggedinUser = (User) getArguments().get("user");
        final Tweet tweet = (Tweet) getArguments().get("tweet");
        helper = new TweetOfflineHelper();
        ivComposeTweetClose = (ImageView) view.findViewById(R.id.ivComposeTweetClose);
        ivComposeProfileImage = (ImageView) view.findViewById(R.id.ivComposeProfileImage);
        tvComposeUserName = (TextView) view.findViewById(R.id.tvComposeUserName);
        tvComposeScreenName = (TextView) view.findViewById(R.id.tvComposeScreenName);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        tvTweetWordCount = (TextView) view.findViewById(R.id.tvTweetWordCount);
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
        if(tweet != null)
        {
            etTweetBody.setText("@"+tweet.getUser().getScreenName());
        }

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetbody = String.valueOf(etTweetBody.getText());
                String inReplyToStatusId = (tweet != null) ? String.valueOf(tweet.getId()) : null;
                client.postTweet(tweetbody, inReplyToStatusId, mediaIds, new JsonHttpResponseHandler() {
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

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }


    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name
        // Start the image capture intent to take photo
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File photoFile = new File(mediaStorageDir.getPath()+File.separator+fileName);
        if(photoFile.exists())
        {
            Toast.makeText(getActivity(),"file exists: "+ photoFile.getAbsolutePath() , Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(getActivity(),"file not exists: "+ photoFile.getAbsolutePath() , Toast.LENGTH_LONG).show();
        }
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
    }
}
