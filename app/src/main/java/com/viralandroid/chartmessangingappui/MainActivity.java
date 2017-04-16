package com.viralandroid.chartmessangingappui;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    private ImageView txtsend;
    Animation animFadein;
    Animation animFadeout;
    Animation animFadeoutImage;
    LinearLayout linimg;
    boolean mExpanded = false;
    LinearLayout imageView;
    ScrollView scrolllin;
    ViewGroup transitionsContainer;
    private static String TAG = "xxxxxxxxxxxxxx";
    int maxScroll;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtsend = (ImageView) findViewById(R.id.txtsend);
        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        imageView = (LinearLayout) transitionsContainer.findViewById(R.id.linjump);
        linimg = (LinearLayout) transitionsContainer.findViewById(R.id.linimg);
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

        scrolllin.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e(TAG, "new  " + scrollY + "  old  " + oldScrollY);
                if (scrollY >= oldScrollY) {
                    Log.e(TAG, "new  " + "dwwn");
                    if (scrollY == maxScroll && mExpanded == true) {
                        mExpanded = !mExpanded;
                        linimg.startAnimation(animFadeout);
                    }
                } else {
                    Log.e(TAG, "new  " + "upppp");
                    if (scrollY < (maxScroll - 100) && mExpanded == false) {
                        mExpanded = !mExpanded;
                        if (mExpanded) {
                            transitionBounds();
                        }
                    }
                }
                Log.d(TAG, "scrollY: " + scrollY);
                if (scrollY > maxScroll) {
                    maxScroll = scrollY;
                }
                Log.d(TAG, "maxScroll: " + maxScroll);
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
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void transitionBounds() {
        linimg.setVisibility(View.GONE);
        Transition transition = new ChangeBounds();
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (mExpanded) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.startAnimation(animFadein);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                if (mExpanded) {
                    linimg.setVisibility(View.VISIBLE);
                    linimg.startAnimation(animFadein);
                } else {
                    imageView.startAnimation(animFadeoutImage);
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

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = mExpanded ? (int) getResources().
                getDimension(R.dimen.square_width) : (int) getResources().
                getDimension(R.dimen.square_size1);
        imageView.setLayoutParams(params);
    }


}
