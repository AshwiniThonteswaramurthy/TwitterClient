package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> implements Animation.AnimationListener {

    private ImageView animatedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No title
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        animatedView = (ImageView) findViewById(R.id.animatedView);

        animatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login
                getClient().connect();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.rotate);
            zoomin.setAnimationListener(this);
            zoomin.setRepeatCount(Animation.INFINITE);
            animatedView.startAnimation(zoomin);
        }
    }



    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
