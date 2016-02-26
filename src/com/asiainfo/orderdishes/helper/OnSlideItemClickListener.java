package com.asiainfo.orderdishes.helper;

import android.view.View;

/**
 * 自定义滑动选择器slideSwith的点击事件监听
 *
 * @author gjr
 */
public interface OnSlideItemClickListener {
    public void onItemClick(View view, int screemIndex, int position, int numberInOneScreen);
}
