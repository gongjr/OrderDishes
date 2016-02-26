package com.asiainfo.orderdishes.ui.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.widget.FrameLayout;

import com.asiainfo.orderdishes.BaseActivity;
import com.asiainfo.orderdishes.ui.widget.AnimatedRectLayout;

public abstract class AnimatedRectActivity extends BaseActivity {

    private AnimatedRectLayout mAnimated;
    protected int mAnimationType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId());

        FrameLayout activityRoot = (FrameLayout) findViewById(android.R.id.content);
        View parent = activityRoot.getChildAt(0);

        // better way ?
        mAnimated = new AnimatedRectLayout(this);
        activityRoot.removeView(parent);
        activityRoot.addView(mAnimated, parent.getLayoutParams());
        mAnimated.addView(parent);

        mAnimationType = getIntent().getIntExtra("animation_type", AnimatedRectLayout.ANIMATION_RANDOM);
        mAnimated.setAnimationType(mAnimationType);

        ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimated, ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY, 1).setDuration(600);

        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                Log.d("TAG", "cuurent value is " + currentValue);

            }
        });
        animator.start();
    }

    protected abstract int layoutResId();

    @Override
    public void onBackPressed() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimated, ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY, 0).setDuration(600);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
        animator.start();
    }


    private static final Property<AnimatedRectLayout, Float> ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY =
            new Property<AnimatedRectLayout, Float>(Float.class, "ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY") {

                @Override
                public void set(AnimatedRectLayout layout, Float value) {
                    layout.setProgress(value);
                }

                @Override
                public Float get(AnimatedRectLayout layout) {
                    return layout.getProgress();
                }
            };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
