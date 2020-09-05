package com.amazingabhishek.humanbenchmark.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazingabhishek.humanbenchmark.R;
import com.amazingabhishek.humanbenchmark.databinding.FragmentReactionBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class ReactionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private FragmentReactionBinding reactionBinding;
    private FragmentActivity fragmentActivity;
    final Handler handler = new Handler();
    long timeWhenQuestionShowed;
    private InterstitialAd mInterstitialAd;
    ArrayList<Long> scoreList = new ArrayList<>();

    public ReactionFragment() {
        // Required empty public constructor
    }

    public static ReactionFragment newInstance(String param1, String param2) {
        ReactionFragment fragment = new ReactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reactionBinding = FragmentReactionBinding.inflate(inflater, container, false);
        fragmentActivity = (FragmentActivity) context;
        Random rnd = new Random();

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        reactionBinding.startReaction.setVisibility(View.VISIBLE);
        reactionBinding.resultContainer.setVisibility(View.GONE);

        reactionBinding.reactionBackground.setOnClickListener(v -> {

            if (scoreList.size() < 5) {
                if (((ColorDrawable) reactionBinding.reactionBackground.getBackground()).getColor() == ContextCompat.getColor(context, R.color.activeBlue)) {

                    playMethod(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_bubble_chart_24), "Wait for green", "");
                    reactionBinding.reactionBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.activeRed));

                    handler.postDelayed(() -> {

                        if (((ColorDrawable) reactionBinding.reactionBackground.getBackground()).getColor() == ContextCompat.getColor(context, R.color.activeRed)) {

                            reactionBinding.reactionBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.activeGreen));
                            playMethod(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_bubble_chart_24), "Click", "");
                            timeWhenQuestionShowed = System.currentTimeMillis();
                        }
                    }, (rnd.nextInt(10 - 5) + 5) * 1000);

                } else if (((ColorDrawable) reactionBinding.reactionBackground.getBackground()).getColor() == ContextCompat.getColor(context, R.color.activeRed)) {

                    playMethod(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_info_24), "Too soon!", "Click to try again.");
                    reactionBinding.reactionBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.activeBlue));

                } else if (((ColorDrawable) reactionBinding.reactionBackground.getBackground()).getColor() == ContextCompat.getColor(context, R.color.activeGreen)) {

                    reactionBinding.reactionBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.activeBlue));
                    long timeWhenUserReacted = System.currentTimeMillis();
                    long score = timeWhenUserReacted - timeWhenQuestionShowed;

                    playMethod(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_access_time_24), score + " ms", "Click to keep going");
                    scoreList.add(score);

                    if (scoreList.size() == 5) {

                        reactionBinding.startReaction.setVisibility(View.GONE);
                        reactionBinding.resultContainer.setVisibility(View.VISIBLE);

                        reactionBinding.best.setText("Height: " + getMax(scoreList));
                        reactionBinding.lowest.setText("Lowest: " + getMin(scoreList));
                        reactionBinding.average.setText("Average: " + ((getMax(scoreList) + getMin(scoreList)) / 2));
                    }
                }
            }

        });

        reactionBinding.restart.setOnClickListener(v -> {
            reactionBinding.startReaction.setVisibility(View.VISIBLE);
            reactionBinding.resultContainer.setVisibility(View.GONE);
            scoreList.clear();
            playMethod(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_fast_forward_24), "Reaction time test",
                    "When the red box turns green, click as quickly as you can. Click anywhere to start");
            reactionBinding.reactionBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.activeBlue));

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        });

        return reactionBinding.getRoot();
    }

    private void playMethod(Drawable drawable, String title, String dis) {
        reactionBinding.imageView.setImageDrawable(drawable);
        reactionBinding.textView.setText(title);
        reactionBinding.dis.setText(dis);
    }

    public long getMax(ArrayList<Long> inputArray) {
        long maxValue = inputArray.get(0);
        for (int i = 1; i < inputArray.size(); i++) {
            if (inputArray.get(i) > maxValue) {
                maxValue = inputArray.get(i);
            }
        }
        return maxValue;
    }

    public long getMin(ArrayList<Long> inputArray) {
        long minValue = inputArray.get(0);
        for (int i = 1; i < inputArray.size(); i++) {
            if (inputArray.get(i) < minValue) {
                minValue = inputArray.get(i);
            }
        }
        return minValue;
    }

    public long getAverage(ArrayList<Long> inputArray) {
        long minValue = 0;
        for (int i = 1; i < inputArray.size(); i++) {
            minValue += inputArray.get(i);
        }
        return minValue / 5;
    }
}