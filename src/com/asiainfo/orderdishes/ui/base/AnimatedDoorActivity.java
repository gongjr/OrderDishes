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
import com.asiainfo.orderdishes.ui.widget.AnimatedDoorLayout;

public abstract class AnimatedDoorActivity extends BaseActivity {

    private AnimatedDoorLayout mAnimated;
    protected int mDoorType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId());

        FrameLayout activityRoot = (FrameLayout) findViewById(android.R.id.content);
        View parent = activityRoot.getChildAt(0);

        // better way ?
        mAnimated = new AnimatedDoorLayout(this);
        activityRoot.removeView(parent);
        activityRoot.addView(mAnimated, parent.getLayoutParams());
        mAnimated.addView(parent);

        mDoorType = getIntent().getIntExtra("door_type", AnimatedDoorLayout.HORIZONTAL_DOOR);
        mAnimated.setDoorType(mDoorType);

        ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimated, ANIMATED_DOOR_LAYOUT_FLOAT_PROPERTY, 1).setDuration(200);
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
        ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimated, ANIMATED_DOOR_LAYOUT_FLOAT_PROPERTY, 0).setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
        animator.start();
    }

    public void backFinish() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimated, ANIMATED_DOOR_LAYOUT_FLOAT_PROPERTY, 0).setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
        animator.start();
    }


    private static final Property<AnimatedDoorLayout, Float> ANIMATED_DOOR_LAYOUT_FLOAT_PROPERTY =
            new Property<AnimatedDoorLayout, Float>(Float.class, "ANIMATED_DOOR_LAYOUT_FLOAT_PROPERTY") {

                @Override
                public void set(AnimatedDoorLayout layout, Float value) {
                    layout.setProgress(value);
                }

                @Override
                public Float get(AnimatedDoorLayout layout) {
                    return layout.getProgress();
                }
            };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public void finishNoAnimator() {
        super.finish();
    }
}
