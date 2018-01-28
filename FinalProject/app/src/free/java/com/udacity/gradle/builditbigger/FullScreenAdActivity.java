package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import xyz.clairvoyant.androidjokelibrary.activities.JokeAndroidActivity;

public class FullScreenAdActivity extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    private String JOKE="joke";
    private String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_ad);

        if(getIntent().getStringExtra(JOKE)!= null && getIntent().getStringExtra(JOKE).length()>0)
        {

            result=getIntent().getStringExtra(JOKE);
        }
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2124824032206817/4660505684");


        AdRequest adRequest = new AdRequest.Builder()
           //     .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
               .addTestDevice("A0B1C80D08B16679BBC89E4FE9B9192E")
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                moveToNextActivity();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void moveToNextActivity()
    {
        Intent intent = new Intent(FullScreenAdActivity.this, JokeAndroidActivity.class);
        intent.putExtra(JOKE, result);
        startActivity(intent);
        finish();
    }
}
