package com.asiainfo.orderdishes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.asiainfo.orderdishes.entity.DishesData.DishesDataOneScreen;
import com.asiainfo.orderdishes.entity.MerchantRegister;
import com.asiainfo.orderdishes.entity.ShoppingOrder;
import com.asiainfo.orderdishes.helper.CrashHandler;
import com.asiainfo.orderdishes.http.imageloader.ImagesLoader;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseApplication extends LitePalApplication {

    /**
     * 用于判断每次打开应用后，进行一次菜单更新
     */
    private boolean isLoadDishes;
    /**
     * 网络访问请求的队列
     */
    private RequestQueue requestQueue;
    /**
     * 图片加载类,统一缓存
     */
    private ImagesLoader imagesLoader;
    /**
     * 点餐页面事件冲突判定
     */
    private boolean dishesHomeTeachState = true;
    /**
     * 缓存点餐页面最新显示的数据项，用于判定某菜品是否在已显示
     */
    private DishesDataOneScreen curDishesData;
    /**
     * 全局缓存登陆商户信息
     */
    private MerchantRegister LoginInfo;

    private ShoppingOrder curShoppingOrder;

    /**
     * activity栈，自维护，用于结束全栈退出应用。
     */
    private List<Activity> activityList = new ArrayList<Activity>();

    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        imagesLoader = new ImagesLoader(this, "OrderDishes");
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext(),this);
        initImageLoader(getApplicationContext());
    }

    public RequestQueue getmQueue() {
        return requestQueue;
    }

    public ImagesLoader getImagesLoader() {
        return imagesLoader;
    }

    public void setImagesLoader(ImagesLoader loader) {
        this.imagesLoader = loader;
    }

    public boolean isDishesHomeTeachState() {
        return dishesHomeTeachState;
    }

    public void setDishesHomeTeachState(boolean dishesHomeTeachState) {
        this.dishesHomeTeachState = dishesHomeTeachState;
    }

    public DishesDataOneScreen getCurDishesData() {
        return curDishesData;
    }

    public void setCurDishesData(DishesDataOneScreen curDishesData) {
        this.curDishesData = curDishesData;
    }

    public MerchantRegister getLoginInfo() {
        return LoginInfo;
    }

    public void setLoginInfo(MerchantRegister loginInfo) {
        LoginInfo = loginInfo;
    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // 建造者模式，创建默认的ImageLoader配置参数
        //LruMemoryCache（这个类就是这个开源框架默认的内存缓存类，缓存的是bitmap的强引用,自动内存管理，上限时，先清理最近最少使用对象
        File cacheDir = StorageUtils.getCacheDirectory(context);
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memoryCacheSize = maxMemory / 8;
        if (maxMemory / 8 > 16 * 1024 * 1024) {
            memoryCacheSize = 16 * 1024 * 1024;
        }
        Log.i("memoryCacheSize","memoryCacheSize:"+memoryCacheSize);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // 设置自定义加载和显示图片的线程池
                .taskExecutor(DefaultConfigurationFactory.createExecutor(3, Thread.NORM_PRIORITY
                        - 1, QueueProcessingType.FIFO))
                        // 设置自定义加载和显示内存缓存或者硬盘缓存图片的线程池
                .taskExecutorForCachedImages(DefaultConfigurationFactory.createExecutor(3, Thread.NORM_PRIORITY
                        - 2, QueueProcessingType.FIFO))
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LRULimitedMemoryCache(memoryCacheSize))
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public boolean isLoadDishes() {
        return isLoadDishes;
    }

    public void setLoadDishes(boolean isLoadDishes) {
        this.isLoadDishes = isLoadDishes;
    }

    public ShoppingOrder getCurShoppingOrder() {
        return curShoppingOrder;
    }

    public void setCurShoppingOrder(ShoppingOrder curShoppingOrder) {
        this.curShoppingOrder = curShoppingOrder;
    }

    /**
     * activity管理：从列表中移除activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * activity管理：添加activity到列表
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * activity管理：结束所有activity，彻底关闭应用
     */
    public void finishProgram() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * activity管理：结束所有activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    /**
     *重启应用
     */
    public void restartApp() {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), "com.asiainfo.orderdishes.ui.LoginActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent); // 1秒钟后重启应用
        finishProgram();
    }
}