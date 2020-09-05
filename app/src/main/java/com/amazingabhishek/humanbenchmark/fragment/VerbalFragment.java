package com.amazingabhishek.humanbenchmark.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazingabhishek.humanbenchmark.R;
import com.amazingabhishek.humanbenchmark.databinding.FragmentVerbalBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class VerbalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentVerbalBinding verbalBinding;
    private ArrayList<String> wordList = new ArrayList<>();
    int randomIndex = 0;
    int score = 0;
    int toteHealth = 3;
    Context context;
    private InterstitialAd mInterstitialAd;

    public VerbalFragment() {
        // Required empty public constructor
    }

    public static VerbalFragment newInstance(String param1, String param2) {
        VerbalFragment fragment = new VerbalFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        verbalBinding = FragmentVerbalBinding.inflate(inflater, container, false);
        verbalBinding.startVerb.setVisibility(View.VISIBLE);
        verbalBinding.gamePlay.setVisibility(View.GONE);
        Random rnd = new Random();
        Collections.addAll(wordList, getResources().getStringArray(R.array.words));

        Set<String> newWord = new HashSet<>();

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

        verbalBinding.relativeLayout.setOnClickListener(v->{
            verbalBinding.startVerb.setVisibility(View.GONE);
            verbalBinding.gamePlay.setVisibility(View.VISIBLE);

            randomIndex = rnd.nextInt(wordList.size());

            verbalBinding.lives.setText(String.valueOf(toteHealth));
            updateScore(score);
            updateWord(randomIndex);

        });

        verbalBinding.newWord.setOnClickListener(v->{
            String word = wordList.get(randomIndex);
            if (!newWord.contains(word)) {
                rightAnswer();
                newWord.add(word);
                randomIndex = rnd.nextInt(wordList.size());
            } else {
                randomIndex = rnd.nextInt(wordList.size());
                wrongAnswer();
            }

            if (toteHealth <= 0){
                gameOver();
            }
        });

        verbalBinding.seen.setOnClickListener(v->{
            if (newWord.contains(wordList.get(randomIndex))){
                rightAnswer();
                randomIndex = rnd.nextInt(wordList.size());
            }else {
                newWord.add(wordList.get(randomIndex));
                randomIndex = rnd.nextInt(wordList.size());
                wrongAnswer();
            }

            if (toteHealth <= 0){
                gameOver();
            }
        });

        verbalBinding.tryA.setOnClickListener(v->{

            newWord.clear();
            verbalBinding.startVerb.setVisibility(View.VISIBLE);
            verbalBinding.gamePlay.setVisibility(View.GONE);
            verbalBinding.finalScore.setVisibility(View.GONE);
            score = 0;
            toteHealth = 3;
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        });

        return verbalBinding.getRoot();
    }

    private void rightAnswer(){
        score++;
        updateScore(score);
        updateWord(randomIndex);
    }

    private void wrongAnswer(){
        updateWord(randomIndex);
        toteHealth--;
        verbalBinding.lives.setText(String.valueOf(toteHealth));
    }

    private void updateWord(int index){
        verbalBinding.tv.setText(wordList.get(index));
    }

    private void updateScore(int point){
        verbalBinding.score.setText(String.valueOf(point));
    }

    private void gameOver(){
        verbalBinding.finalScore.setVisibility(View.VISIBLE);
        verbalBinding.startVerb.setVisibility(View.GONE);
        verbalBinding.gamePlay.setVisibility(View.GONE);
        verbalBinding.textView11.setText(String.valueOf(score + " Words"));
    }
}