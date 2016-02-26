package com.asiainfo.orderdishes.entity.volley;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesItem;
import com.asiainfo.orderdishes.entity.litepal.DishesItemType;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.ui.widget.FlowLayoutMargin;
import com.asiainfo.orderdishes.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ItemPopupHolder {
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
    private ConcurrentHashMap<DishesItem, CompoundButton> checkDishesItem = new ConcurrentHashMap<DishesItem, CompoundButton>();
    private LayoutInflater mInflater;
    private ArrayList<DishesItemType> cache;
    private BadgeView shopping_Num;
    private TextView shopping_price;
    private long InstanceId=0;
    /**
     * 购物车动画
     */
//    private TextView shopCart;// 购物车
    private ViewGroup anim_mask_layout;// 动画层
    private ImageView buyImg;// 这是在界面上跑的小图片
    private ImageView dishesImage,anim_buyImg;
    private boolean isDismiss;

    public ItemPopupHolder(Activity mActivity, DishesEntity dishesEntity, BaseApplication baseapp) {
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
                    R.layout.activity_dish_detail_item, null);
            anim_mask_layout = (ViewGroup)mConvertView.findViewById(R.id.anim_mask_layout);
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
    private void updataView(final DishesInfo dishes,
                            BadgeView badge, View itemview) {
        isDismiss=true;
        Date date = new Date();
        InstanceId = date.getTime();
        checkDishesItem.clear();
        getView(R.id.dish_detail_left).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(isDismiss){
                        baseApp.setDishesHomeTeachState(true);
                        dishesPopup.dismiss();
                        checkDishesItem.clear();}
                    }
                });
        ((ImageButton) getView(R.id.dd_close))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(isDismiss){
                        baseApp.setDishesHomeTeachState(true);
                        dishesPopup.dismiss();
                        checkDishesItem.clear();}
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
        dishesImage = (ImageView) getView(R.id.dd_image);
        ImageLoader.getInstance().displayImage(
                dishes.getDishesUrl(), dishesImage, options);
        /*anim_buyImg=(ImageView) getView(R.id.anim_mask_dishe_img);
        ImageLoader.getInstance().displayImage(
                dishes.getDishesUrl(), anim_buyImg, options);*/
        final View countGroup = getView(R.id.layout_dd_count);
        final TextView et = ((TextView) getView(R.id.count_input));
        final CheckBox checkBox = ((CheckBox) getView(R.id.dd_select));
        final Button submit = ((Button) getView(R.id.dd_submit));

        submit.setVisibility(View.VISIBLE);
        submit.setTag(R.id.tag_first,dishes);
        submit.setTag(R.id.tag_second,badge);
        submit.setTag(R.id.tag_three,itemview);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isDismiss=false;
                DishesInfo dishesinfo=(DishesInfo)view.getTag(R.id.tag_first);
                BadgeView badgeView=(BadgeView)view.getTag(R.id.tag_second);
                View itemview=(View)view.getTag(R.id.tag_three);
                Iterator<Entry<DishesItem, CompoundButton>> iter = checkDishesItem.entrySet().iterator();
                String remark="[";
                /*ArrayList<String> remarklist=new ArrayList<String>();
                while (iter.hasNext()) {
                    remarklist.add(iter.next().getKey().getItemName());
                }
                for (int i = 0; i <remarklist.size(); i++) {
                    if(i!=remarklist.size()-1)
                    remark+=remarklist.get(i)+",";
                    else  remark+=remarklist.get(i);
                }*/
                remark+="]";
                dishesEntity.addOrderGoodsItem(dishesinfo, InstanceId,remark);
//                dishesEntity.delRemarkItemList(dishesinfo.getDishesId() + "",InstanceId+"");
                View price_group = itemview.findViewById(R.id.dish_item_price_group);
                TextView price = (TextView) itemview.findViewById(R.id.dish_item_price);
                TextView price_icon = (TextView) itemview.findViewById(R.id.dish_item_price_icon);
                boolean isShow = isShowDishesinfo(dishesinfo);
                if (isShow) {
                    price.setTextColor(mActivity.getResources().getColor(R.color.white));
                    price_icon.setTextColor(mActivity.getResources().getColor(
                            R.color.white));
                    price_group.setBackgroundDrawable(mActivity.getResources()
                            .getDrawable(R.drawable.dish_item_price_s));
                }
                buyImg = new ImageView(mActivity);
                buyImg.setTag(R.id.tag_first,dishesinfo);
                buyImg.setTag(R.id.tag_second,badgeView);
                buyImg.setTag(R.id.tag_three,itemview);
                int[] start_location = new int[2];// 一个整型数组，用来存储起始位置的在屏幕的X、Y坐标
                Log.i("location-xy", "x:"+start_location[0]+"  y:"+start_location[1]);
                dishesImage.getLocationInWindow(start_location);
                buyImg.setImageBitmap(convertViewToBitmap2(dishesImage,start_location));
                setAnim(buyImg, start_location);
            }
        });

        countGroup.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        OnCheckedChangeListener oncheckedChangeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DishesItem dishesItem = (DishesItem) buttonView.getTag(R.id.tag_first);
                DishesInfo dishesinfo = (DishesInfo) buttonView.getTag(R.id.tag_second);
                long instanceid=(Long)buttonView.getTag(R.id.tag_three);
                if (isChecked) {
                    Iterator<Entry<DishesItem, CompoundButton>> iter = checkDishesItem.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<DishesItem, CompoundButton> curiter=iter.next();
                    if(curiter.getKey().getItemType().equals(dishesItem.getItemType()))
                        curiter.getValue().setChecked(false);
                }
                    dishesEntity.addDishesItem(dishesItem,instanceid+"");
                    if (!checkDishesItem.containsKey(dishesItem))
                        checkDishesItem.put(dishesItem, buttonView);

                } else {
                    dishesEntity.delDishesItem(dishesItem,instanceid+"");
                    if (checkDishesItem.containsKey(dishesItem))
                        checkDishesItem.remove(dishesItem);
                }
            }
        };
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        LinearLayout item_group = getView(R.id.item_group);
        item_group.removeAllViews();
        for (DishesItemType dishesItemType : cache) {
            addRemark(mInflater, item_group, dishesItemType, dishes, oncheckedChangeListener,InstanceId);
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

    public void addRemark(LayoutInflater mInflater, LinearLayout item_group, DishesItemType dishesItemType, DishesInfo dishesinfo, OnCheckedChangeListener oncheckedChangeListener,long InstanceId) {
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
//            tv.setChecked(dishesEntity.findRemarkItem(dishesItem));//初始化的时候不要读取已选标签
//            if (dishesEntity.findRemarkItem(dishesItem)) checkDishesItem.put(dishesItem, tv);
            tv.setOnCheckedChangeListener(oncheckedChangeListener);
            tv.setTag(R.id.tag_first, dishesItem);
            tv.setTag(R.id.tag_second, dishesinfo);
            tv.setTag(R.id.tag_three,InstanceId);
            item.addView(tv);
        }
    }

    /**
     * 更新购物车价钱
     */
    void UpdateShopping_Nums(BadgeView badge,DishesInfo dishesinfo) {
        baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
//		curShoppingOrder=dishesEntity.findShoppingOrder();
        shopping_price.setText(sumPrice(baseApp.getCurShoppingOrder().getOrderGoods()) + "元");
        int totalnum = 0;
        for (OrderGoods orderGoods : baseApp.getCurShoppingOrder().getOrderGoods()) {
            if (!orderGoods.isCompDish()) totalnum += orderGoods.getSalesNum();
        }
        shopping_Num.setText(totalnum + "");

        int typeSum = dishesEntity.SumOrderGoodsNumByIndex(dishesinfo);
        Log.i("LitePal", "typeSum:" + typeSum);
        if (typeSum > 0) {
            badge.setVisibility(View.VISIBLE);
            badge.setText("" + typeSum);
        } else {
            badge.setText("" + 0);
            badge.setVisibility(View.INVISIBLE);
        }

    }


    private void setAnim(final View v, int[] start_location) {
        anim_mask_layout.addView(v);
        final View view = addViewToAnimLayout(v,
                start_location);
        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)view.getLayoutParams();
        int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
        shopping_Num.getLocationInWindow(end_location);// shopCart是那个购物车
        int endX = end_location[0] - start_location[0];// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        //计算左上角偏移量,有scale需要乘的1/toX
        float toX=0.1f;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, endX-20,0,endY-100);
        translateAnimation.setStartOffset(200);
        translateAnimation.setDuration(200);
        ScaleAnimation myAnimation_Scale =new ScaleAnimation(0.8f, toX, 0.8f, toX);
        myAnimation_Scale.setDuration(200);
        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(myAnimation_Scale);
		set.addAnimation(translateAnimation);
        set.setDuration(400);// 动画的执行时间
        set.setFillBefore(true);
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                DishesInfo dishesinfo=(DishesInfo)v.getTag(R.id.tag_first);
                BadgeView badgeView=(BadgeView)v.getTag(R.id.tag_second);
                View itemview=(View)v.getTag(R.id.tag_three);
                UpdateShopping_Nums(badgeView,dishesinfo);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        isDismiss=true;
                        if(isDismiss){
                        baseApp.setDishesHomeTeachState(true);
                        checkDishesItem.clear();
                        dishesPopup.dismiss();
                        }
                    }
                },50);
            }
        });

    }


    private View addViewToAnimLayout(final View view,int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    // 将定义的view装换成 bitmap格式
    public Bitmap convertViewToBitmap2(View view,int[] start_location) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//		int[] start_location = new int[2];
//		view.getLocationInWindow(start_location);
        Log.i("location-xy", "layout--x:"+start_location[0]+"  y:"+start_location[1]);
        int r=start_location[0]+view.getMeasuredWidth();
        int b=start_location[1]+view.getMeasuredHeight();
        Log.i("location-xy", "layout--start_location[0]+view.getMeasuredWidth():"+r+"  start_location[1]+view.getMeasuredHeight():"+b);
//		view.layout(start_location[0], start_location[1], r, b);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

}
