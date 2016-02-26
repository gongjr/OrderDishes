package com.asiainfo.orderdishes.helper;

import android.widget.CompoundButton;

import com.asiainfo.orderdishes.entity.litepal.DishesInfo;

public interface OnSlidCheckedChangeListener {
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked, DishesInfo dishesinfo);
}
