package com.asiainfo.orderdishes.entity;

import com.asiainfo.orderdishes.entity.litepal.DishesInfo;

import java.util.ArrayList;

/**
 * 该类模拟了功能菜单的数据部分
 */
public class DishesData {
    /**
     * 该常量代表每一屏能够容纳的菜品数目
     */
    public static final int NUMBER_IN_ONE_SCREEN = 6;

    /**
     * 该类代表了一个屏的所有菜品
     */
    public static class DishesDataOneScreen {
        public ArrayList<DishesInfo> mDataItems = new ArrayList<DishesInfo>();
    }

    /**
     * 该数据时该类的主要部分，所有屏的列表，实际上该类就是代表了所有的屏
     */
    ArrayList<DishesDataOneScreen> mScreens = new ArrayList<DishesDataOneScreen>();

    /**
     * 对该类进行赋予数据
     */
    public void setMenuItems(ArrayList<DishesInfo> dataItems) {
        int screenNum = dataItems.size() / NUMBER_IN_ONE_SCREEN;
        int remain = dataItems.size() % NUMBER_IN_ONE_SCREEN;
        screenNum += remain == 0 ? 0 : 1;

        int pos = 0;
        for (int i = 0; i < screenNum; i++) {
            DishesDataOneScreen screen = new DishesDataOneScreen();
            for (int j = 0; j < NUMBER_IN_ONE_SCREEN; j++) {
                if (pos <= dataItems.size() - 1) {
                    screen.mDataItems.add(dataItems.get(pos));
                    pos++;
                }
            }
            mScreens.add(screen);
        }
    }

    /**
     * 获取屏的数目
     */
    public int getScreenNumber() {
        return mScreens.size();
    }

    /**
     * 根据屏的索引，获取某个屏的数据
     */
    public DishesDataOneScreen getScreen(int screenIndex) {
        return mScreens.get(screenIndex);
    }

    public int getNumberInOneScreen() {
        return NUMBER_IN_ONE_SCREEN;
    }
}
