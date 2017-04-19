package com.katomaran.changeBound;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    Animation animFadeoutImage, animFadeout, animFadein;
    LinearLayout toast_content, toast_container;
    boolean mExpanded = false;
    ScrollView scrolllin;
    ViewGroup transitionsContainer;
    int maxScroll;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        toast_container = (LinearLayout) transitionsContainer.findViewById(R.id.toast_container);
        toast_content = (LinearLayout) transitionsContainer.findViewById(R.id.toast_content);
        scrolllin = (ScrollView) findViewById(R.id.scrolllin);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animFadeoutImage = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animFadeout.setAnimationListener(this);
        animFadeoutImage.setAnimationListener(this);
        scrolllin.post(new Runnable() {
            @Override
            public void run() {
                scrolllin.fullScroll(View.FOCUS_DOWN);
            }
        });
        toast_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrolllin.fullScroll(View.FOCUS_DOWN);
            }
        });
        scrolllin.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY >= oldScrollY) {
                    if (scrollY == maxScroll && mExpanded == true) {
                        mExpanded = !mExpanded;
                        toast_content.startAnimation(animFadeout);
                    }
                } else {
                    if (scrollY < (maxScroll - 100) && mExpanded == false) {
                        mExpanded = !mExpanded;
                        if (mExpanded) {
                            transitionBounds();
                        }
                    }
                }
                if (scrollY > maxScroll) {
                    maxScroll = scrollY;
                }
            }
        });

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animFadeout) {
            transitionBounds();
        }
        if (animation == animFadeoutImage) {
            toast_container.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void transitionBounds() {

        toast_content.setVisibility(View.GONE);
        Transition transition = new ChangeBounds();
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (mExpanded) {
                    toast_container.setVisibility(View.VISIBLE);
                    toast_container.startAnimation(animFadein);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (mExpanded) {
                    toast_content.setVisibility(View.VISIBLE);
                    toast_content.startAnimation(animFadein);
                } else {
                    toast_container.startAnimation(animFadeoutImage);
                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        TransitionManager.beginDelayedTransition(transitionsContainer, transition);
        ViewGroup.LayoutParams params = toast_container.getLayoutParams();
        params.width = mExpanded ? (int) getResources().
                getDimension(R.dimen.square_width) : (int) getResources().
                getDimension(R.dimen.square_size1);
        toast_container.setLayoutParams(params);
    }


}
