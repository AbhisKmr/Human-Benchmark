package com.amazingabhishek.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.amazingabhishek.humanbenchmark.databinding.ActivityMainBinding;
import com.amazingabhishek.humanbenchmark.fragment.ReactionFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mainBinding.adView.loadAd(adRequest);

        mainBinding.reaction.setOnClickListener(v -> {
            Fragment fragment = ReactionFragment.newInstance("", "");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentHolder, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}