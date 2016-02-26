package com.asiainfo.orderdishes;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.asiainfo.orderdishes.helper.DialogDelayListener;
import com.asiainfo.orderdishes.helper.LoadingActivityListener;
import com.asiainfo.orderdishes.ui.widget.LeafLoadingDialog;
import com.asiainfo.orderdishes.ui.widget.LoadingActivity;
import com.asiainfo.orderdishes.util.BaseUtils;

import roboguice.activity.RoboActivity;

public class BaseActivity extends RoboActivity {

    private static final String TAG = BaseActivity.class.getName();

    public Context mContext;

    public DisplayMetrics gMetrice;

    public BaseApplication baseApp;

    public RequestQueue requestQueue;

    public BaseUtils baseUtils;

    public Activity mActivity;

    public final String VolleyLogTag = "VolleyLogTag";

    public static LoadingActivityListener loadingListener;

    public LeafLoadingDialog leafDialog;

    public Toast mToast, myToast;

    private DialogDelayListener delay;
    private DialogDelayListener initdelay;

    public Dialog dialog;

    //广播的内部类，当收到关闭事件时，调用finish方法结束activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("allfinish","broadcastReceiver");
            finish();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        setActivityModel();
        getDisplayMetrics();
        getBaseApplication();
        getRequestQueue();
        BaseUtils(mContext);
        initToast();
        initMyToast();
        //在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.EXIT_ACTION);
        this.registerReceiver(this.broadcastReceiver, filter);
        baseApp.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        baseApp.removeActivity(this);
        this.unregisterReceiver(this.broadcastReceiver);
        super.onDestroy();
    }

    private BaseApplication getBaseApplication() {
        if (baseApp == null)
            baseApp = (BaseApplication) getApplication();
        return baseApp;
    }

    private void setActivityModel() {
        // 无标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 不默认弹出输入框
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private DisplayMetrics getDisplayMetrics() {
        if (gMetrice == null) {
            gMetrice = new DisplayMetrics();
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            defaultDisplay.getMetrics(gMetrice);
        }
        return gMetrice;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = ((BaseApplication) getApplication()).getmQueue();
        return requestQueue;
    }

    private BaseUtils BaseUtils(Context mContext) {
        if (baseUtils == null)
            baseUtils = new BaseUtils(mContext);
        return baseUtils;
    }

    protected OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText textView = (EditText) v;
            String hint;
            if (hasFocus) {
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }
        }
    };

    public void showLoadingActivity() {
        Intent intent = new Intent();
        intent.setClass(mContext, LoadingActivity.class);// 跳转到加载界面
        startActivity(intent);
    }

    public void dismissLoadingActivity() {
        if (baseUtils
                .isTopActivy("ComponentInfo{com.asiainfo.orderdishes/com.asiainfo.orderdishes.ui.widget.LoadingActivity}"))
            loadingListener.onfinish();
        else
            System.out.println("loadingdialogactivity不在最前列，忽视之");
    }

    public void setLoadingActivityListener(LoadingActivityListener temp) {
        loadingListener = temp;
    }

    ;

    public void showLoadingDialog(DialogDelayListener delay, int delaytime) {
        if (leafDialog == null)
            leafDialog = new LeafLoadingDialog();
        if (!leafDialog.isAdded()) {
            if (delay != null) {
                this.delay = delay;
                mHandler.sendEmptyMessageDelayed(Constants.Handler_Dialog_Delay,
                        delaytime);
            }
            leafDialog.show(getFragmentManager(), "leafDialog");
        } else {
            Log.i("monkey", "leafDialog dialogFragment already add");
        }


    }

    public void showDelay(DialogDelayListener delay, int delaytime) {

        this.initdelay = delay;
        mHandler.sendEmptyMessageDelayed(Constants.Handler_DishesHome_initDelay,
                delaytime);
    }

    public void dismissLoadingDialog() {
        if (leafDialog != null) {
            if (leafDialog.getISRun()) {
                leafDialog.dismiss();
            }
        }
    }

    private void initToast() {
        View view = getLayoutInflater().inflate(R.layout.toast_content_view, null, false);
        mToast = new Toast(getApplicationContext());
        mToast.setView(view);
    }

    private void initMyToast() {
        View view = getLayoutInflater().inflate(R.layout.dishes_comp_mytoast, null, false);
        myToast = new Toast(getApplicationContext());
        myToast.setView(view);
    }

    public void showShortTip(String tip) {
        View v = mToast.getView();
        ((TextView) v.findViewById(R.id.toast_view)).setText(tip);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showLongTip(String tip) {
        View v = mToast.getView();
        ((TextView) v.findViewById(R.id.toast_view)).setText(tip);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Handler_Dialog_Delay:
                    delay.onexecute();
                    break;
                case Constants.Handler_DishesHome_initDelay:
                    initdelay.onexecute();
                    break;

            }
        }

        ;
    };

    public void showMyShortTip(String tip) {
        View v = myToast.getView();
        ((TextView) v.findViewById(R.id.dishes_toast_view)).setText(tip);
        myToast.setDuration(Toast.LENGTH_SHORT);
        myToast.show();
    }
}
