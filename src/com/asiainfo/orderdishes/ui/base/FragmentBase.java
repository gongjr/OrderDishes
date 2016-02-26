package com.asiainfo.orderdishes.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.asiainfo.orderdishes.BaseApplication;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.util.BaseUtils;

/**
 * @author ouyangyj
 *         <p/>
 *         2015年4月22日
 */
public class FragmentBase extends Fragment {

    Activity mActivity;
    BaseApplication baseApp;
    RequestQueue requestQueue;
    BaseUtils baseUtils;
    Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        initToast();
    }


    protected RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = ((BaseApplication) mActivity.getApplication()).getmQueue();
        return requestQueue;
    }

    protected BaseUtils BaseUtils(Context mContext) {
        if (baseUtils == null)
            baseUtils = new BaseUtils(mContext);
        return baseUtils;
    }

    private void initToast() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.toast_content_view, null, false);
        mToast = new Toast(mActivity.getApplicationContext());
        mToast.setView(view);
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
}
