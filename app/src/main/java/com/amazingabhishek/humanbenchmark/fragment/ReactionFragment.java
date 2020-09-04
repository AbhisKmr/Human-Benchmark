package com.amazingabhishek.humanbenchmark.fragment;

import android.content.Context;
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
    int redColor = R.color.activeRed;

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
        long timeWhenQuestionShowed = System.currentTimeMillis();

        reactionBinding.reactionBackground.setOnClickListener(v-> {
            red(reactionBinding.imageView, reactionBinding.textView, reactionBinding.dis);
            reactionBinding.reactionBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.activeRed));

            if (reactionBinding.reactionBackground.getSolidColor() == ContextCompat.getColor(context, R.color.activeGreen)){
                long timeWhenUserReacted = System.currentTimeMillis();
                long reactionTime = timeWhenQuestionShowed - timeWhenUserReacted;
                Toast.makeText(context, "" + reactionTime, Toast.LENGTH_SHORT).show();
            }
            handler.postDelayed(() -> {
                reactionBinding.reactionBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.activeGreen));
                green(reactionBinding.imageView, reactionBinding.textView, reactionBinding.dis);
            }, (rnd.nextInt(15-3) + 3 )* 1000);
        });

        return reactionBinding.getRoot();
    }

    private void red(ImageView imageView, TextView title, TextView dis){
        imageView.setImageDrawable(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_bubble_chart_24));
        title.setText("Wait for green");
        dis.setText("");
    }

    private void green(ImageView imageView, TextView title, TextView dis){
        imageView.setImageDrawable(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_bubble_chart_24));
        title.setText("Click!");
        dis.setText("");
    }
    
    private void blue(ImageView imageView, TextView title, TextView dis){
        imageView.setImageDrawable(ContextCompat.getDrawable(fragmentActivity, R.drawable.ic_baseline_bubble_chart_24));
        title.setText("Too soon!");
        dis.setText("");
    }
}