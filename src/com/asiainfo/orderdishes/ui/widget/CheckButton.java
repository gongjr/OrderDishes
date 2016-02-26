package com.asiainfo.orderdishes.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

public class CheckButton extends Button {
    /**
     * 选中情况下背景
     */
    private Drawable selected;
    /**
     * 未选中情况下背景
     */
    private Drawable unselected;
    /**
     * checkbutton当前选择状态
     */
    private boolean flag;
    /**
     * 点击事件
     */
    private OnClickListener checkbuttonListener;

    public CheckButton(Context context, Drawable select, Drawable unselect) {
        super(context);
        this.flag = false;
        this.selected = select;
        this.unselected = unselect;

        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = !flag;
                if (flag) {
                    v.setBackgroundDrawable(selected);
                } else {
                    v.setBackgroundDrawable(unselected);
                }
                if (checkbuttonListener != null)
                    checkbuttonListener.onClick(v);
            }
        });

    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public OnClickListener getCheckbuttonListener() {
        return checkbuttonListener;
    }

    public void setCheckbuttonListener(OnClickListener checkbuttonListener) {
        this.checkbuttonListener = checkbuttonListener;
    }
}
