package com.asiainfo.orderdishes.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.DishesData.DishesDataOneScreen;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.helper.OnItemClickListener;
import com.asiainfo.orderdishes.helper.OnSlidCheckedChangeListener;
import com.asiainfo.orderdishes.http.imageloader.ImagesLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;

public class OneScreenAdapter extends BaseAdapter {
    private static final String TAG = OneScreenAdapter.class.getSimpleName();
    /**
     * 菜单显示图片的加载参数
     * 默认是ARGB_8888， 使用RGB_565会比使用ARGB_8888少消耗2倍的内存，但是这样会没有透明度
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
            .build();
    /**
     * 单屏的数据展示类
     */
    private DishesDataOneScreen mScreen;
    /**
     * 上下文实体，取资源系统服务类变量
     */
    private Context mContext;
    /**
     *
     */
    private LayoutInflater mInflater;
    /**
     * 点击事件监听器
     */
    private OnItemClickListener OnItemClickListener;
    /**
     * 快捷选菜，选择监听器
     */
    private OnSlidCheckedChangeListener OnCheckedChangeListener;
    /**
     * 图片下载类
     */
    private ImagesLoader loader;
    /**
     * 系统像素判断用于对应裁剪图片
     */
    private int density;
    /**
     * 业务实体类，订单相关修改
     */
    private DishesEntity dishesEntity;
    private AlphaAnimation myAnimation_Alpha_down;
    private ScaleAnimation myAnimation_Scale_down;
    private AlphaAnimation myAnimation_Alpha_up;
    private ScaleAnimation myAnimation_Scale_up;
    private AnimationSet myset_down;
    private AnimationSet myset_up;
    /**
     * 属性动画缓存变量
     */
    private HashMap<ImageView, ImageAnimator> animatorMap = new HashMap<ImageView, ImageAnimator>();

    public OneScreenAdapter(Context context, DishesEntity dishesEntity,
                            ImagesLoader imagesLoader) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.density = (int) mContext.getResources().getDisplayMetrics().density;
        this.loader = imagesLoader;
        this.dishesEntity = dishesEntity;
        this.myAnimation_Alpha_down = new AlphaAnimation(1.0f, 0.7f);
        this.myAnimation_Scale_down = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f);
        this.myAnimation_Alpha_up = new AlphaAnimation(1.0f, 0.5f);
        this.myAnimation_Scale_up = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
        this.myset_down = new AnimationSet(false);
        myset_down.setFillAfter(false);
        myset_down.addAnimation(myAnimation_Alpha_down);
        myset_down.addAnimation(myAnimation_Scale_down);
        myset_down.setDuration(100);
        this.myset_up = new AnimationSet(false);
        myset_up.setFillAfter(false);
        myset_up.addAnimation(myAnimation_Alpha_up);
        myset_up.addAnimation(myAnimation_Scale_up);
        myset_up.setDuration(100);
    }

    /**
     * 这里将数据赋予Adapter
     */
    public void setScreenData(DishesDataOneScreen screenData) {
        mScreen = screenData;
        notifyDataSetChanged();
    }

    /**
     * 返回数据
     */
    public DishesDataOneScreen getScreenData() {
        return mScreen;
    }

    public int getCount() {
        return mScreen.mDataItems.size();
    }

    public Object getItem(int position) {
        return mScreen.mDataItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 该函数中将数据和View进行关联
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolers viewHolers;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.dise_item, null);
            viewHolers=new ViewHolers();
            viewHolers.imageView = (ImageView) view.findViewById(R.id.dish_item_img);
            viewHolers.name = (TextView) view.findViewById(R.id.dish_item_name);
            viewHolers.price = (TextView) view
                    .findViewById(R.id.dish_item_price);
            viewHolers.price_group = view.findViewById(R.id.dish_item_price_group);
            viewHolers.price_icon = (TextView) view
                    .findViewById(R.id.dish_item_price_icon);
            viewHolers.member_price = (TextView) view
                    .findViewById(R.id.dish_item_member_price);
            viewHolers.member_price_icon = (TextView) view
                    .findViewById(R.id.dish_item_member_price_icon);
            viewHolers.member_price_title = (TextView) view
                    .findViewById(R.id.dish_item_member_price_title);
            view.setTag(R.id.tag_first,viewHolers);
        }else{
            viewHolers=(ViewHolers)view.getTag(R.id.tag_first);
        }

        viewHolers.imageView.setTag(R.id.tag_first, mScreen.mDataItems.get(position)
                .getDishesUrl());
        ImageLoader.getInstance().displayImage(
                mScreen.mDataItems.get(position).getDishesUrl(), viewHolers.imageView,
                options, mImageLoadingListener);
        viewHolers.imageView.setTag(R.id.tag_three, mScreen.mDataItems.get(position));
        view.setTag(R.id.tag_second,mScreen.mDataItems.get(position));
        viewHolers.name.setText(mScreen.mDataItems.get(position).getDishesName());

        if(mScreen.mDataItems.get(position).getMemberPrice()!=null){
            viewHolers.price.setText("" + mScreen.mDataItems.get(position).getMemberPrice()
                    + "");
            viewHolers.member_price.setVisibility(View.VISIBLE);
            viewHolers.member_price_icon.setVisibility(View.VISIBLE);
            viewHolers.member_price_title.setVisibility(View.VISIBLE);
            viewHolers.member_price.setText("" + mScreen.mDataItems.get(position).getDishesPrice()
                    + "");
        }else{
            viewHolers.price.setText("" + mScreen.mDataItems.get(position).getDishesPrice()
                    + "");
        }
        CheckBox cb = (CheckBox) view.findViewById(R.id.dish_item_checkBox);
        if (mScreen.mDataItems.get(position).getIsComp().equals("1"))
            cb.setVisibility(View.GONE);
        else if(mScreen.mDataItems.get(position).getDishesItemList()!=null&&mScreen.mDataItems.get(position).getDishesItemList().size()>=1)
            cb.setVisibility(View.GONE);
        else  cb.setVisibility(View.VISIBLE);

        if (dishesEntity.findOrderGoodsNumByDishesInfo(mScreen.mDataItems
                .get(position)) == 0) {
            if (mScreen.mDataItems.get(position).getIsComp().equals("0")) {
                cb.setTag(false);
                cb.setChecked(false);
                cb.setTag(true);
            }
//            viewHolers.price_group.setBackgroundDrawable(mContext.getResources()
//                    .getDrawable(R.drawable.dish_item_price_n));
            viewHolers.price_group.setBackgroundDrawable(null);
            viewHolers.price.setTextColor(mContext.getResources().getColor(
                    R.color.dish_item_price_text));
            viewHolers.price_icon.setTextColor(mContext.getResources().getColor(
                    R.color.dish_item_price_text));
        } else {
            if (mScreen.mDataItems.get(position).getIsComp().equals("0")) {
                cb.setTag(false);
                cb.setChecked(true);
                cb.setTag(true);
            }
            viewHolers.price.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolers.price_icon.setTextColor(mContext.getResources().getColor(
                    R.color.white));
            viewHolers.price_group.setBackgroundDrawable(mContext.getResources()
                    .getDrawable(R.drawable.dish_item_price_s));

        }
        viewHolers.price_group.setTag(mScreen.mDataItems.get(position).getDishesId());

//		cb.setTag(false);
        viewHolers.imageView.setTag(R.id.tag_second, view);
        addAnimatorToMap(viewHolers.imageView);
        if (OnItemClickListener != null) {
            viewHolers.imageView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("Property", "-----------myset_down");
                            animatorMap.get(v).animator_down.start();
                            return true;
                        case MotionEvent.ACTION_UP:
                            Log.i("Property", "-----------myset_up");
                            animatorMap.get(v).animator_up.start();
                            OnItemClickListener.onItemClick(v, position);
                            return true;
                        case MotionEvent.ACTION_CANCEL:
                            Log.i("Property", "-----------myset_cancle");
                            animatorMap.get(v).animator_up.start();
                            return true;
                        default:
                            return false;
                    }
                }
            });
        }
        if (OnCheckedChangeListener != null) {
            cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    boolean isRefresh = (Boolean) buttonView.getTag();
                    Log.i("isRefresh", "isRefresh:" + isRefresh);
                    if (isRefresh) {
                        if (isChecked) {
                            viewHolers.price.setTextColor(mContext.getResources().getColor(
                                    R.color.white));
                            viewHolers.price_icon.setTextColor(mContext.getResources()
                                    .getColor(R.color.white));
                            viewHolers.price_group.setBackgroundDrawable(mContext
                                    .getResources().getDrawable(
                                            R.drawable.dish_item_price_s));
                            Log.i("LitePal", "dishesEntity.addOrderGoods");
                            // dishesEntity.addOrderGoods(mScreen.mDataItems.get(position));
                            dishesEntity.updateOrderGoods(
                                    mScreen.mDataItems.get(position), 1);
                        } else {
//                            viewHolers.price_group.setBackgroundDrawable(mContext
//                                    .getResources().getDrawable(
//                                            R.drawable.dish_item_price_n));
                            viewHolers.price_group.setBackgroundDrawable(null);
                            viewHolers.price.setTextColor(mContext.getResources().getColor(
                                    R.color.dish_item_price_text));
                            viewHolers.price_icon.setTextColor(mContext.getResources()
                                    .getColor(R.color.dish_item_price_text));
                            dishesEntity.decreaseAllOrderGoods(mScreen.mDataItems
                                    .get(position));
                        }
                        OnCheckedChangeListener.onCheckedChanged(buttonView,
                                isChecked, mScreen.mDataItems.get(position));
                    } else {
                        //false的时候，表明是主动调用，只刷新视图，不传递事件
                        if (isChecked) {
                            viewHolers.price.setTextColor(mContext.getResources().getColor(
                                    R.color.white));
                            viewHolers.price_icon.setTextColor(mContext.getResources()
                                    .getColor(R.color.white));
                            viewHolers.price_group.setBackgroundDrawable(mContext
                                    .getResources().getDrawable(
                                            R.drawable.dish_item_price_s));
                        } else {
//                            viewHolers.price_group.setBackgroundDrawable(mContext
//                                    .getResources().getDrawable(
//                                            R.drawable.dish_item_price_n));
                            viewHolers.price_group.setBackgroundDrawable(null);
                            viewHolers.price.setTextColor(mContext.getResources().getColor(
                                    R.color.dish_item_price_text));
                            viewHolers.price_icon.setTextColor(mContext.getResources()
                                    .getColor(R.color.dish_item_price_text));
                        }
                    }
                }
            });
        }
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        this.OnItemClickListener = OnItemClickListener;
    }

    public void setOnCheckedChangeListener(
            OnSlidCheckedChangeListener OnCheckedChangeListener) {
        this.OnCheckedChangeListener = OnCheckedChangeListener;
    }

    /**
     * 若内存缓存为空，则去文件缓存中获取 若文件中没有则返回null， 若返回null，则设置默认图片
     */
    @SuppressLint("NewApi")
    private void setImageView(final ImageView imageView, String url) {
        Bitmap bitmap = loader.getBitmapCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);// 原图片大小放入
            // imageView.setBackground(new
            // BitmapDrawable(mContext.getResources(), bitmap));//背景拉伸铺展

        } else {
            // imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_logo));
            imageView.setBackground(mContext.getResources().getDrawable(
                    R.drawable.ic_logo));
            // 防止滚动时多次下载，但是如果有2张图片url相同，则第二站图片会被过滤而不下载，也不能通过接口回调加载
            // 解决思路，通过url与imageview做双重缓存判断来避免此种情况
            if (!loader.getTaskCollection().containsKey(url)) {
                loader.loadImage(url, 300 * density, 175 * density,
                        new ImagesLoader.AsyncImageLoaderListener() {
                            @Override
                            public void onImageLoader(Bitmap bitmap, String url) {
                                System.out.println("imageloader:"
                                        + imageView.getTag(R.id.tag_first)
                                        .toString());
                                if (imageView != null
                                        && bitmap != null
                                        && imageView.getTag(R.id.tag_first)
                                        .toString().equals(url)) {
                                    // imageView.setImageBitmap(bitmap);
                                    imageView.setBackground(new BitmapDrawable(
                                            mContext.getResources(), bitmap));

                                }
                            }
                        });
            } else {
                System.out.println("url:" + url + "在下载队列中---");
            }
        }
    }

    /**
     * 取消下载任务
     */
    /*
	 * public void cancelTasks() { loader.cancelTasks(); }
	 * 
	 * public void clearLruCache() { Log.i("LruCache",
	 * "loader.clearLruCache()"); if (loader != null) loader.clearLruCache(); }
	 */

    private Property<View, Float> view_click_down = new Property<View, Float>(
            Float.class, "view_click_down") {

        @Override
        public void set(View layout, Float value) {
            Log.i("Property", "view_click_down value:" + value);
            float alpha_value = 0.7f + (value - 0.9f) * 3;
            Log.i("Property", "view_click_down alpha_value:" + alpha_value);
            layout.setAlpha(alpha_value);
            layout.setScaleX(value);
            layout.setScaleY(value);
        }

        @Override
        public Float get(View layout) {
            return layout.getAlpha();
        }
    };

    private Property<View, Float> view_click_up = new Property<View, Float>(
            Float.class, "view_click_up") {

        @Override
        public void set(View layout, Float value) {
            Log.i("Property", "view_click_up value:" + value);
            float alpha_value = 0.7f + (value - 0.9f) * 3;
            Log.i("Property", "view_click_up alpha_value:" + alpha_value);
            layout.setAlpha(alpha_value);
            layout.setScaleX(value);
            layout.setScaleY(value);
        }

        @Override
        public Float get(View layout) {
            return layout.getAlpha();
        }
    };

    public void addAnimatorToMap(ImageView image) {
        if (!animatorMap.containsKey(image)) {
            ImageAnimator imageanimator = new ImageAnimator();
            ObjectAnimator animator = ObjectAnimator.ofFloat(image, view_click_down,
                    1.0f, 0.9f).setDuration(40);
            imageanimator.animator_down = animator;
            animator = ObjectAnimator.ofFloat(image, view_click_up,
                    0.9f, 1.0f).setDuration(10);
            imageanimator.animator_up = animator;
            animatorMap.put(image, imageanimator);
        } else {
            Log.i("Property", "该View动画已存在view:" + image);
        }
    }

    static class ImageAnimator {
        private ObjectAnimator animator_up;
        private ObjectAnimator animator_down;
    }

    ImageLoadingListener mImageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String arg0, View arg1) {
        }

        @Override
        public void onLoadingFailed(String arg0, View img, FailReason failReason) {
            DishesInfo dishesInfo = (DishesInfo) img.getTag(R.id.tag_three);
            switch (failReason.getType()) {
                case IO_ERROR:
                    Log.d(TAG, "图片加载IO异常");
                    dishesInfo.setDishesUrl(null);
                    dishesEntity.updateDishesUrl(dishesInfo);
                    break;
                case DECODING_ERROR:
                    Log.d(TAG, "图片加载编码异常");
                    break;
                case NETWORK_DENIED:
                    Log.d(TAG, "图片加载网络异常");
                    break;
                case OUT_OF_MEMORY:
                    Log.d(TAG, "图片加载内存溢出");
                    break;
                case UNKNOWN:
                    Log.d(TAG, "图片加载未知异常");
                    dishesInfo.setDishesUrl(null);
                    dishesEntity.updateDishesUrl(dishesInfo);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {
        }
    };

    class ViewHolers{
        ImageView imageView;
        TextView name;
        TextView price,member_price;
        View price_group;
        TextView price_icon,member_price_icon,member_price_title;
        CheckBox cb;
    }
}
