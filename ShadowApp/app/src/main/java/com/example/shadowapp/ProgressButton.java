package com.example.shadowapp;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

//to be finished

class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;

    Animation fade_in;

    ProgressButton(Context ct, View view) {

        cardView = view.findViewById(R.id.cardView);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);

    }

    void ButtonActivated() {
        progressBar.setVisibility(View.VISIBLE);

    }
    void ButtonFinished() {
        progressBar.setVisibility(View.GONE);
    }
}