package com.asiainfo.orderdishes.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedPreferencesUtils {

    Context mContext;
    public SharedPreferences mSharedPreferences;
    public static final String HALL_DATA_SP = "hall_location_desk_sp_name";
    public static final String DESK_LOC_LAST_SYNC = "desk_loc_last_sync_date";
    public static final String DESK_LOC_CODE_PREFIX = "loc_code_";
    public static final String CURRENT_REFRESH_DISHES_TIME = "current_fresh_dishes_time";

    public SharedPreferencesUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 根据当前日期检测是否需要刷新本地数据，进行数据同步
     *
     * @param date
     * @return
     */
    public Boolean isNeedSyncDeskLocData(String newDate) {
        mSharedPreferences = mContext.getSharedPreferences(HALL_DATA_SP,
                mContext.MODE_PRIVATE);
        String latestDate = mSharedPreferences
                .getString(DESK_LOC_LAST_SYNC, "");
        if (latestDate.equals(newDate)) {
            return false;
        }
        return true;
    }

    /**
     * 刷新同步桌子区域的最新日期
     *
     * @param date
     * @return
     */
    public Boolean refreshDeskLocLatestDate(String newDate) {
        mSharedPreferences = mContext.getSharedPreferences(HALL_DATA_SP,
                mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DESK_LOC_LAST_SYNC, newDate);
        Boolean b = editor.commit();
        return b;
    }

    /**
     * 根据当前日期检测是否需要刷新本地数据，进行数据同步
     *
     * @param date
     * @return
     */
    public Boolean isNeedSyncLocDeskDataData(String deskLocCode, String newDate) {
        mSharedPreferences = mContext.getSharedPreferences(HALL_DATA_SP,
                mContext.MODE_PRIVATE);
        String latestDate = mSharedPreferences.getString(DESK_LOC_CODE_PREFIX
                + deskLocCode, "");
        if (latestDate.equals(newDate)) {
            return false;
        }
        return true;
    }

    /**
     * 刷新同步区域对应桌子数据的最新日期
     *
     * @param date
     * @return
     */
    public Boolean refreshLocDeskDataLatestDate(String deskLocCode,
                                                String newDate) {
        mSharedPreferences = mContext.getSharedPreferences(HALL_DATA_SP,
                mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DESK_LOC_CODE_PREFIX + deskLocCode, newDate);
        Boolean b = editor.commit();
        return b;
    }

    /**
     * 根据当前日期检测是否需要更新数据
     *
     * @param date
     * @return
     */
    public Boolean isNeedSyncDishes() {
        String newDate = getCurrentData();
        mSharedPreferences = mContext.getSharedPreferences("DishesFreshTime",
                mContext.MODE_PRIVATE);
        String latestDate = mSharedPreferences.getString(
                CURRENT_REFRESH_DISHES_TIME, "");
        if (latestDate.equals(newDate)) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 刷新菜品的最新日期
     *
     * @param date
     * @return
     */
    public Boolean refreshDishesLatestDate() {
        String newDate = getCurrentData();
//		newDate="11";
        mSharedPreferences = mContext.getSharedPreferences("DishesFreshTime",
                mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CURRENT_REFRESH_DISHES_TIME, newDate);
        Boolean b = editor.commit();
        return b;
    }


    @SuppressLint("SimpleDateFormat")
    public String getCurrentData() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String curtime = dd.format(date);
        return curtime;

    }
}
