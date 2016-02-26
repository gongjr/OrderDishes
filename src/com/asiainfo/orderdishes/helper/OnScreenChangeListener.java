package com.asiainfo.orderdishes.helper;

/**
 * 屏幕状态改变事件监听
 *
 * @author gjr
 */
public interface OnScreenChangeListener {
    public void onItemClick(int curIndex, int nextIndex, int screenNumber);
}
