package com.asiainfo.orderdishes.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewSwitcher.ViewFactory;

import com.asiainfo.orderdishes.R;

public class SlideViewFactory implements ViewFactory {
    LayoutInflater mInflater;

    public SlideViewFactory(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 这个函数就是得到要生成的View，这里实际上直接从布局得到，
     * 我们定义的是一个GridView ，一个GridView用于显示一屏的应用程序
     */
    public View makeView() {
        return mInflater.inflate(R.layout.slidelistview, null);
    }
} 
