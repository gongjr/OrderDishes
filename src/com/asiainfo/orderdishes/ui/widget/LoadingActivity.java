package com.asiainfo.orderdishes.ui.widget;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.asiainfo.orderdishes.BaseActivity;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.helper.LoadingActivityListener;
import com.asiainfo.orderdishes.util.AnimationUtils;

import java.util.Random;

public class LoadingActivity extends BaseActivity implements LoadingActivityListener {

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 进度条最大延迟随机等待42秒走完一圈，通常随机数总和21秒左右进度跑完一圈
                case REFRESH_PROGRESS:
                    if (mProgress == 0)
                        Log.i("mProgress", "mProgress:" + mProgress);
                    if (mProgress == 100)
                        Log.i("mProgress", "mProgress:" + mProgress);
                    if (mProgress < 20) {
                        // 30时间段以内最大2秒走完
                        mProgress += 1;
                        // 随机200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(100));
                        mLeafLoadingView.setProgress(mProgress);
                    } else if (mProgress >= 20 && mProgress < 70) {
                        // 30-70段最大20秒走完
                        mProgress += 1;
                        // 随机400ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(400));
                        mLeafLoadingView.setProgress(mProgress);
                    } else if (mProgress >= 70 && mProgress < 90) {
                        // 70-90时间段最大用12秒走完
                        mProgress += 1;
                        // 随机600ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(600));
                        mLeafLoadingView.setProgress(mProgress);
                    } else if (mProgress >= 90 && mProgress < 100) {
                        // 末尾最大9秒走完
                        mProgress += 1;
                        // 随机200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(900));
                        mLeafLoadingView.setProgress(mProgress);
                    } else if (mProgress == 100) {
                        mProgress = 0;
                        // 随机200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(300));
                        mLeafLoadingView.setProgress(mProgress);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private static final int REFRESH_PROGRESS = 0x10;
    private LeafLoadingView mLeafLoadingView;
    private View mFanView;
    private int mProgress = 0;
    public static LoadingActivity loadingActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leafloading);
        loadingActivity = this;
        mFanView = findViewById(R.id.fan_pic);
        RotateAnimation rotateAnimation = AnimationUtils.initRotateAnimation(false, 1500, true,
                Animation.INFINITE);
        mFanView.startAnimation(rotateAnimation);
        mLeafLoadingView = (LeafLoadingView) findViewById(R.id.leaf_loading);
        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 1000);
        setLoadingActivityListener(this);
    }

    @Override
    public void onfinish() {
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
