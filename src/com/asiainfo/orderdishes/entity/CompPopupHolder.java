package com.asiainfo.orderdishes.entity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.asiainfo.orderdishes.BaseApplication;
import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.eventbus.CompDishesEventData;
import com.asiainfo.orderdishes.entity.eventbus.Event;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesType;
import com.asiainfo.orderdishes.ui.DishSetActivity;
import com.asiainfo.orderdishes.util.BaseUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import de.greenrobot.event.EventBus;

public class CompPopupHolder {
    /**
     * 菜单显示图片的加载参数
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.dishes_default)
            .showImageOnFail(R.drawable.dishes_default)
            .showImageForEmptyUri(R.drawable.dishes_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .displayer(new RoundedBitmapDisplayer(20))
            .displayer(new FadeInBitmapDisplayer(100))
            .build();
    /**
     * 子视图组
     */
    private final SparseArray<View> mViews;
    /**
     * root视图
     */
    private View mConvertView;
    /**
     * 上下文环境
     */
    private Activity mActivity;
    /**
     * 详情弹窗
     */
    private PopupWindow dishesPopup;
    /**
     * 屏幕高宽
     */
    private int mScreenWidth, mScreenHeight;
    /**
     * 全局配置变量
     */
    private BaseApplication baseApp;
    private LayoutInflater mInflater;
    private DishesEntity dishesEntity;
    private MerchantCompDishesInfo merchantCompDishesInfo;

    public CompPopupHolder(Activity mActivity, DishesEntity dishesEntity, BaseApplication baseapp) {
        this.mActivity = mActivity;
        this.mViews = new SparseArray<View>();
        this.mScreenWidth = mActivity.getWindowManager().getDefaultDisplay()
                .getWidth();
        this.mScreenHeight = mActivity.getWindowManager().getDefaultDisplay()
                .getHeight();
        this.baseApp = baseapp;
        this.mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BaseUtils baseUtils = new BaseUtils(mActivity);
        this.dishesEntity = dishesEntity;
    }

    /**
     * 根据ViewHolder对象获取一个弹窗视图，并且更新视图内容返回
     *
     * @param dishesInfo
     * @param position
     * @return
     */
    public View get(DishesInfo dishesInfo, MerchantCompDishesInfo dd, int catagoriesIndex) {
        merchantCompDishesInfo = dd;
        if (mConvertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            mConvertView = layoutInflater.inflate(
                    R.layout.activity_dish_detail, null);
            dishesPopup = new PopupWindow(mConvertView, mScreenWidth,
                    mScreenHeight);
            dishesPopup.setFocusable(true);
            dishesPopup.setAnimationStyle(R.style.AnimationPreview);
        }
        updataView(dishesInfo, dd, catagoriesIndex);
        dishesPopup.update();
        mConvertView.setTag(dishesPopup);
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    @SuppressLint("NewApi")
    private void updataView(DishesInfo dishes, final MerchantCompDishesInfo dd, final int catagoriesIndex) {
        getView(R.id.dish_detail_left).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        baseApp.setDishesHomeTeachState(true);
                        dishesPopup.dismiss();
                    }
                });
        ((ImageButton) getView(R.id.dd_close))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        baseApp.setDishesHomeTeachState(true);
                        dishesPopup.dismiss();
                    }
                });
        ((TextView) getView(R.id.dd_name)).setText(dishes.getDishesName());
        ((TextView) getView(R.id.dd_price)).setText(dishes.getDishesPrice() + "");
        ImageView im = (ImageView) getView(R.id.dd_image);
        ImageLoader.getInstance().displayImage(
                dishes.getDishesUrl(), im, options);
        getView(R.id.layout_dd_count).setVisibility(View.GONE);
        getView(R.id.dd_select).setVisibility(View.GONE);
        getView(R.id.remark_group).setVisibility(View.GONE);
        LinearLayout consist_group = getView(R.id.dd_consist_group);
        consist_group.removeAllViews();
        for (MerchantCompDishesType cmopDishesType : dd.getCompDishesTypeList()) {
            TextView dd_consist = (TextView) mInflater.inflate(R.layout.dishes_detail_consist, consist_group, false);
            consist_group.addView(dd_consist);
            String remark = cmopDishesType.getDishesTypeName() + ":   ";
            for (int i = 0; i < cmopDishesType.getDishesInfoList().size(); i++) {
                if (i != cmopDishesType.getDishesInfoList().size() - 1)
                    remark += cmopDishesType.getDishesInfoList().get(i).getDishesName() + "、";
                else remark += cmopDishesType.getDishesInfoList().get(i).getDishesName();
            }
            remark += "(" + cmopDishesType.getDishesCount() + "选" + cmopDishesType.getMaxSelect() + ")";
            dd_consist.setText(remark);
            /*switch (i) {
			case 0:
				dd_consist.setText("主食:  浓情鸡肉烔饭、黑椒牛柳面、浓香芝士培根饭 (3选2)");
				break;
			case 1:
				dd_consist.setText("小食:  芒果甜心、墨风烤肠、酥炸鱿鱼须 (3选2)");
				break;
			case 2:
				dd_consist.setText("汤饮:  精致浓汤、咖啡、可乐、香浓牛奶 (4选2)");
				break;
			default:
				break;
			}*/
        }
        getView(R.id.dishse_configure).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                baseApp.setDishesHomeTeachState(true);
                dishesPopup.dismiss();
                Intent intent = new Intent(mActivity, DishSetActivity.class);
                intent.putExtra("OrderEntityId", dishesEntity.getOrderEntity().getId());
                intent.putExtra("MerchantCompDishesInfoId", dd.getId());
                intent.putExtra("catagoriesIndex", catagoriesIndex);
                Bundle dishesdata = new Bundle();
                dishesdata.putSerializable("dishesOrder", dishesEntity.getOrderEntity());
                intent.putExtras(dishesdata);
                mActivity.startActivityForResult(intent, Constants.DishesHome_requestcode_dishesset);
                mActivity.overridePendingTransition(0, 0);
                Thread eventThread = new Thread(eventThreadDelay);
                eventThread.start();
            }
        });

    }

    Runnable eventThreadDelay = new Runnable() {
        public void run() {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Event<CompDishesEventData> event = new Event<CompDishesEventData>();
            CompDishesEventData compDishesEventData = new CompDishesEventData();
            compDishesEventData.setMerchantCompDishesInfo(merchantCompDishesInfo);
            compDishesEventData.setDishesOrder(dishesEntity.getOrderEntity());
            event.setData(compDishesEventData);
            event.setType(1);
            EventBus.getDefault().post(event);
        }
    };
}
