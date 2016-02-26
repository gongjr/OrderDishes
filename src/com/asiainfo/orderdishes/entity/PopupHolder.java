package com.asiainfo.orderdishes.entity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.asiainfo.orderdishes.BaseApplication;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesItem;
import com.asiainfo.orderdishes.entity.litepal.DishesItemType;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.ui.widget.FlowLayoutMargin;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class PopupHolder {
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
     * 业务实体类，订单相关修改
     */
    private DishesEntity dishesEntity;
    /**
     * 全局配置变量
     */
    private BaseApplication baseApp;
    /**
     * 记录当前页面显示的缓存
     */
    private ArrayList<DishesInfo> curData;
    /**
     * 记录当前页面所有标签选过的标签视图集合
     * 方便释放修改状态
     */
    private HashMap<DishesItem, CompoundButton> checkDishesItem = new HashMap<DishesItem, CompoundButton>();
    private LayoutInflater mInflater;
    private ArrayList<DishesItemType> cache;
    private BadgeView shopping_Num;
    private TextView shopping_price;

    public PopupHolder(Activity mActivity, DishesEntity dishesEntity, BaseApplication baseapp) {
        this.mActivity = mActivity;
        this.mViews = new SparseArray<View>();
        this.mScreenWidth = mActivity.getWindowManager().getDefaultDisplay()
                .getWidth();
        this.mScreenHeight = mActivity.getWindowManager().getDefaultDisplay()
                .getHeight();
        this.baseApp = baseapp;
        this.dishesEntity = dishesEntity;
        this.mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 根据ViewHolder对象获取一个弹窗视图，并且更新视图内容返回
     *
     * @return
     */
    public View get(DishesInfo dishesInfo, BadgeView badge, View itemView, BadgeView shopping_Num, ArrayList<DishesInfo> curdata, TextView shopping_price) {
        if (mConvertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            mConvertView = layoutInflater.inflate(
                    R.layout.activity_dish_detail, null);
            dishesPopup = new PopupWindow(mConvertView, mScreenWidth,
                    mScreenHeight);
            dishesPopup.setFocusable(true);
            dishesPopup.setAnimationStyle(R.style.AnimationPreview);
        }
        this.curData = curdata;
        this.shopping_Num = shopping_Num;
        this.shopping_price = shopping_price;
        cache = dishesInfo.getDishesItemList();
        updataView(dishesInfo, badge, itemView);
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
    private void updataView(DishesInfo dishes,
                            BadgeView badge, View itemview) {
        getView(R.id.dish_detail_left).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        baseApp.setDishesHomeTeachState(true);
                        dishesPopup.dismiss();
                        checkDishesItem.clear();
                    }
                });
        ((ImageButton) getView(R.id.dd_close))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        baseApp.setDishesHomeTeachState(true);
                        dishesPopup.dismiss();
                        checkDishesItem.clear();
                    }
                });
        ((TextView) getView(R.id.dd_name)).setText(dishes.getDishesName());
        ((TextView) getView(R.id.dd_price)).setText(dishes.getDishesPrice() + "");
        getView(R.id.dishse_configure).setVisibility(View.GONE);
        LinearLayout consist_group = getView(R.id.dd_consist_group);
        consist_group.removeAllViews();
        TextView dd_consist = (TextView) mInflater.inflate(R.layout.dishes_detail_consist, consist_group, false);
        consist_group.addView(dd_consist);
        dd_consist.setText("主材:" + dishes.getDishesName());
        ImageView im = (ImageView) getView(R.id.dd_image);
        ImageLoader.getInstance().displayImage(
                dishes.getDishesUrl(), im, options);
        final View countGroup = getView(R.id.layout_dd_count);
        final TextView et = ((TextView) getView(R.id.count_input));
        final CheckBox checkBox = ((CheckBox) getView(R.id.dd_select));
        checkBox.setTag(R.id.tag_first, dishes);
        checkBox.setTag(R.id.tag_second, badge);
        checkBox.setTag(R.id.tag_three, itemview);
        Log.i("LitePal", "dishesInfo.name:" + dishes.getDishesName());
        Log.i("LitePal", "dishesInfo.salesid:" + dishes.getDishesId());
        int dishes_num = dishesEntity.findOrderGoodsNumByDishesInfo(dishes);
        Log.i("LitePal", "num:" + dishes_num);
        if (dishes_num == 0) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
            countGroup.setVisibility(View.GONE);
            et.setText("0");

        } else {
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setChecked(true);
            countGroup.setVisibility(View.VISIBLE);
            et.setText(""
                    + dishes_num);
        }
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                DishesInfo dishesinfo = (DishesInfo) buttonView.getTag(R.id.tag_first);
                BadgeView badge = (BadgeView) buttonView.getTag(R.id.tag_second);
                View itemview = (View) buttonView.getTag(R.id.tag_three);
                CheckBox cb = (CheckBox) itemview.findViewById(R.id.dish_item_checkBox);
                View price_group = itemview.findViewById(R.id.dish_item_price_group);
                TextView price = (TextView) itemview.findViewById(R.id.dish_item_price);
                TextView price_icon = (TextView) itemview.findViewById(R.id.dish_item_price_icon);
                boolean isShow = isShowDishesinfo(dishesinfo);
                Log.i("LitePal", "isChecked:" + isChecked);
                if (isChecked) {
                    buttonView.setVisibility(View.INVISIBLE);
                    if (isShow) {
                        cb.setTag(false);
                        cb.setChecked(true);
                        cb.setTag(true);
                        price.setTextColor(mActivity.getResources().getColor(R.color.white));
                        price_icon.setTextColor(mActivity.getResources().getColor(R.color.white));
                        price_group.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.dish_item_price_s));
                    }
                    countGroup.setVisibility(View.VISIBLE);
                    if (dishesEntity.findOrderGoodsNumByDishesInfo(dishesinfo) == 0) {
                        dishesEntity.addOrderGoods(dishesinfo);

                    }
                    et.setText(""
                            + dishesEntity
                            .findOrderGoodsNumByDishesInfo(dishesinfo));
                    Log.i("LitePal", "et:" + et.getText().toString());
                } else {
                    buttonView.setVisibility(View.VISIBLE);
                    if (isShow) {
                        cb.setTag(false);
                        cb.setChecked(false);
                        cb.setTag(true);
//                        price_group.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.dish_item_price_n));
                        price_group.setBackground(null);
                        price.setTextColor(mActivity.getResources().getColor(R.color.dish_item_price_text));
                        price_icon.setTextColor(mActivity.getResources().getColor(R.color.dish_item_price_text));
                    }
                    countGroup.setVisibility(View.GONE);
                    dishesEntity.decreaseAllOrderGoods(dishesinfo);
                }
                int typeSum = dishesEntity.SumOrderGoodsNumByIndex(dishesinfo);
                UpdateShopping_Nums();
                Log.i("LitePal", "typeSum:" + typeSum);
                if (typeSum > 0) {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText("" + typeSum);
                } else {
                    badge.setText("" + 0);
                    badge.setVisibility(View.INVISIBLE);
                }
            }
        });
        et.setText(dishesEntity.findOrderGoodsNumByDishesInfo(dishes) + "");
        Button add_btn = ((Button) getView(R.id.add_btn));
        add_btn.setTag(R.id.tag_first, dishes);
        add_btn.setTag(R.id.tag_second, badge);
        add_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DishesInfo dishesinfo = (DishesInfo) v.getTag(R.id.tag_first);
                BadgeView badge = (BadgeView) v.getTag(R.id.tag_second);
                int num = Integer.valueOf(et.getText()
                        .toString());
                num++;
                et.setText(num + "");
                dishesEntity.addOrderGoods(dishesinfo);
                int typeSum = dishesEntity.SumOrderGoodsNumByIndex(dishesinfo);
                UpdateShopping_Nums();
                Log.i("LitePal", "typeSum:" + typeSum);
                if (typeSum > 0) {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText("" + typeSum);
                } else {
                    badge.setText("" + 0);
                    badge.setVisibility(View.INVISIBLE);
                }
            }
        });
        Button minus_btn = ((Button) getView(R.id.minus_btn));
        minus_btn.setTag(R.id.tag_first, dishes);
        minus_btn.setTag(R.id.tag_second, badge);
        minus_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DishesInfo dishesinfo = (DishesInfo) v.getTag(R.id.tag_first);
                BadgeView badge = (BadgeView) v.getTag(R.id.tag_second);
                int num = Integer.valueOf(et.getText()
                        .toString());
                if (num > 1) {
                    num--;
                    dishesEntity.decreaseOrderGoods(dishesinfo);
                    et.setText(num + "");
                } else {
                    dishesEntity.decreaseOrderGoods(dishesinfo);
                    et.setText("0");
                    checkBox.setChecked(false);
                    checkBox.setVisibility(View.VISIBLE);
                    countGroup.setVisibility(View.GONE);
                    et.setText("0");
                    Iterator<Entry<DishesItem, CompoundButton>> iter = checkDishesItem.entrySet().iterator();
                    while (iter.hasNext()) {
                        iter.next().getValue().setChecked(false);
                    }
                    checkDishesItem.clear();
                }
                int typeSum = dishesEntity.SumOrderGoodsNumByIndex(dishesinfo);
                UpdateShopping_Nums();
                Log.i("LitePal", "typeSum:" + typeSum);
                if (typeSum > 0) {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText("" + typeSum);
                } else {
                    badge.setText("" + 0);
                    badge.setVisibility(View.INVISIBLE);
                }
            }
        });
        OnCheckedChangeListener oncheckedChangeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DishesItem dishesItem = (DishesItem) buttonView.getTag(R.id.tag_first);
                DishesInfo dishesinfo = (DishesInfo) buttonView.getTag(R.id.tag_second);
                System.out.println("isChecked:" + isChecked);
                if (isChecked) {
                    if (dishesEntity.findOrderGoodsNumByDishesInfo(dishesinfo) == 0) {
                        checkBox.setChecked(true);
                        //标签选中的时候，如果菜品没选数量，数量默认为1，其他情况不做判断关联修改
                    }
                    dishesEntity.addDishesItem(dishesItem,"");
                    if (!checkDishesItem.containsKey(dishesItem))
                        checkDishesItem.put(dishesItem, buttonView);
                } else {
                    dishesEntity.delDishesItem(dishesItem,"");
                }
            }
        };
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        LinearLayout item_group = getView(R.id.item_group);
        item_group.removeAllViews();
        for (DishesItemType dishesItemType : cache) {
            addRemark(mInflater, item_group, dishesItemType, dishes, oncheckedChangeListener);
        }


    }

    /**
     * 计算总价
     *
     * @param dishesList
     */
    public long sumPrice(ArrayList<OrderGoods> dishesList) {
        long total = 0;
        if (dishesList != null && dishesList.size() > 0) {
            for (int i = 0; i < dishesList.size(); i++) {
                OrderGoods dm = dishesList.get(i);
                total = total + dm.getSalesNum() * dm.getDishesPrice();
            }
        }
        return total;
    }

    /**
     * 遍历当前页面菜品缓存，当前菜品详情所属菜，是否显示在页面上
     * 显示在页面上返回true相应操作刷新菜品展示页，否则返回false不刷新菜品展示页
     *
     * @param dishesinfo
     * @return boolean
     */
    public boolean isShowDishesinfo(DishesInfo dishesinfo) {
        for (DishesInfo dishes : baseApp.getCurDishesData().mDataItems) {
            if (dishes.getDishesId().endsWith(dishesinfo.getDishesId())) return true;
        }
        return false;

    }

    public void addRemark(LayoutInflater mInflater, LinearLayout item_group, DishesItemType dishesItemType, DishesInfo dishesinfo, OnCheckedChangeListener oncheckedChangeListener) {
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.dishesitemtype, null);
        item_group.addView(layout);
        TextView title = (TextView) layout.findViewById(R.id.dishesitemtype_name);
        title.setText(dishesItemType.getItemTypeName() + ":");
        FlowLayoutMargin item = (FlowLayoutMargin) layout.findViewById(R.id.dishesitemtype_flowLayout);
        for (DishesItem dishesItem : dishesItemType.getItemlist()) {
            CheckBox tv = (CheckBox) mInflater.inflate(R.layout.dishes_item_check,
                    item, false);
            tv.setText(dishesItem.getItemName());
            dishesItem.setDishesId(dishesinfo.getDishesId());
            dishesItem.setItemType(dishesItemType.getItemType());
            dishesItem.setItemTypeName(dishesItemType.getItemTypeName());
            tv.setChecked(dishesEntity.findRemarkItem(dishesItem));
            if (dishesEntity.findRemarkItem(dishesItem)) checkDishesItem.put(dishesItem, tv);
            tv.setOnCheckedChangeListener(oncheckedChangeListener);
            tv.setTag(R.id.tag_first, dishesItem);
            tv.setTag(R.id.tag_second, dishesinfo);
            item.addView(tv);
        }
    }

    /**
     * 更新购物车价钱
     */
    void UpdateShopping_Nums() {
        baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
//		curShoppingOrder=dishesEntity.findShoppingOrder();
        shopping_price.setText(sumPrice(baseApp.getCurShoppingOrder().getOrderGoods()) + "元");
        int totalnum = 0;
        for (OrderGoods orderGoods : baseApp.getCurShoppingOrder().getOrderGoods()) {
            if (!orderGoods.isCompDish()) totalnum += orderGoods.getSalesNum();
        }
        shopping_Num.setText(totalnum + "");
    }

}
