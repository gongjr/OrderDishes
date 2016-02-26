package com.asiainfo.orderdishes.helper;

import android.view.View;

public interface OnDishItemClickListener {
    /**
     * 点餐专用回调接口， clickType类型,
     */
    public static final int ClickType_DELETE = 1; //删除
    public static final int ClickType_ADD = 2; //数量增加
    public static final int ClickType_MINUS = 3; //数量减少

    public void onItemClick(View v, int position, int clickType);
}
