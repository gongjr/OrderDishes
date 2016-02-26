package com.asiainfo.orderdishes.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.asiainfo.orderdishes.BaseApplication;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.adapter.OneScreenAdapter;
import com.asiainfo.orderdishes.entity.DishesData;
import com.asiainfo.orderdishes.entity.DishesData.DishesDataOneScreen;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.helper.OnItemClickListener;
import com.asiainfo.orderdishes.helper.OnScreenChangeListener;
import com.asiainfo.orderdishes.helper.OnSlidCheckedChangeListener;
import com.asiainfo.orderdishes.helper.OnSlideItemClickListener;
import android.view.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 该部分是ViewSwitcher的重载，用该类实现两个屏的切换和切换的动画实现
 */
public class SlideSwitcher extends ViewSwitcher {
    private DishesData mMenuData;
    private int mCurrentScreen;
    private Context mContext;
    private float touchDownX, touchUpX;
    int index = 0;
    private OnTouchListener mOnTouchListener;
    GestureDetector gestureDetector;
    private OnSlideItemClickListener OnSlideItemClickListener;
    private OnScreenChangeListener OnScreenChangeListener;
    private OnSlidCheckedChangeListener OnCheckedChangeListener;
    private OneScreenAdapter adapter;
    private int ScreenNumber = 0;
    private BaseApplication baseApp;

    private Animation switcher_in_left_parallel;
    private Animation switcher_out_left_parallel;
    private Animation switcher_in_right_parallel;
    private Animation switcher_out_right_parallel;
    private AnimationListener animationListener;
    private int currentWhich;

    public SlideSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFactory(new SlideViewFactory(context));
        mContext = context;
        mCurrentScreen = 0;
        this.currentWhich=-1;
        this.gestureDetector = new GestureDetector(context,
                new MyOnGestureListener());
        this.switcher_in_left_parallel = AnimationUtils.loadAnimation(mContext, R.anim.switcher_in_left_parallel);
        this.switcher_out_left_parallel = AnimationUtils.loadAnimation(mContext, R.anim.switcher_out_left_parallel);
        this.switcher_in_right_parallel = AnimationUtils.loadAnimation(mContext, R.anim.switcher_in_right_parallel);
        this.switcher_out_right_parallel = AnimationUtils.loadAnimation(mContext, R.anim.switcher_out_right_parallel);
    }

    public void setOnSlideItemClickListener(
            OnSlideItemClickListener OnSlideItemClickListener) {
        this.OnSlideItemClickListener = OnSlideItemClickListener;
    }

    public void setOnScreenChangeListener(
            OnScreenChangeListener OnScreenChangeListener) {
        this.OnScreenChangeListener = OnScreenChangeListener;
    }

    OnItemClickListener OnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            OnSlideItemClickListener.onItemClick(view, mCurrentScreen,
                    position, mMenuData.getNumberInOneScreen());
        }
    };

    /**
     * 设置业务实体类
     *
     * @param dishesEntity
     */
    public void setDishesEntity(DishesEntity dishesEntity,
                                BaseApplication baseapp) {
        this.baseApp = baseapp;
        adapter = new OneScreenAdapter(mContext, dishesEntity,
                baseapp.getImagesLoader());
        animationListener= new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                baseApp.setDishesHomeTeachState(true);
            }
        };
    }

    public void setOnCheckedChangeListener(
            OnSlidCheckedChangeListener OnCheckedChangeListener) {
        this.OnCheckedChangeListener = OnCheckedChangeListener;
    }

    public void init(ArrayList<DishesInfo> dataItems){
        currentWhich=getDisplayedChild();
            LinearLayout view = (LinearLayout) getCurrentView();
            final MyGridView listView = (MyGridView) view
                    .findViewById(R.id.slidelistview_gridview);
            listView.setGestureDetector(gestureDetector);
            adapter.setOnItemClickListener(OnItemClickListener);
            adapter.setOnCheckedChangeListener(OnCheckedChangeListener);
            // 对于不同品类间切换的时候，主动清一下内存缓存，高清图片太多，LruCache后期也因无法及时处理而OOM
            // adapter.clearLruCache();
            setData(dataItems);
            listView.setAdapter(adapter);
    }

    /**
     * 通过该方法将数据赋值进去，并且将初始的屏显示出来
     */
    public void setData(ArrayList<DishesInfo> dataItems) {
        index = 0;
        mCurrentScreen = 0;
        mMenuData = new DishesData();
        mMenuData.setMenuItems(dataItems);
        ScreenNumber = mMenuData.getScreenNumber();
        if (ScreenNumber != 0) {
            adapter.setScreenData(mMenuData.getScreen(mCurrentScreen));
            baseApp.setCurDishesData(adapter.getScreenData());
        } else {
            adapter.setScreenData(new DishesDataOneScreen());
            baseApp.setCurDishesData(adapter.getScreenData());
        }
    }

    public int getScreenNumber() {
        return ScreenNumber;
    }
    /**
     * 该方法用于显示下一屏
     */
    public void showNextScreen() {
        if (mCurrentScreen < mMenuData.getScreenNumber() - 1) {
            mCurrentScreen++;
            setNextAnimation();
        } else {
            return;
        }
        int curIndex = mCurrentScreen - 1;
        OnScreenChangeListener.onItemClick(curIndex, mCurrentScreen,
                ScreenNumber);
        setViewData(mCurrentScreen);
        showScreem();
    }

    /**
     * 该方法用于显示上一屏
     */
    public void showPreviousScreen() {
        System.out.println("showPreviousScreen;mCurrentScreen:"
                + mCurrentScreen);
        if (mCurrentScreen > 0) {
            mCurrentScreen--;
            setPreviousAnimation();
        } else {
            return;
        }
        int curIndex = mCurrentScreen + 1;
        OnScreenChangeListener.onItemClick(curIndex, mCurrentScreen,
                ScreenNumber);
        setViewData(mCurrentScreen);
        showScreem();
    }

    /**
     * 该方法用于显示某一屏
     */
    public int showScreen(int sIndex) {
        System.out.println("index:" + sIndex + "mCurrentScreen:"
                + mCurrentScreen + "ScreenNumber:" + ScreenNumber);
        if (sIndex < mCurrentScreen && sIndex >= 0) {
            OnScreenChangeListener.onItemClick(mCurrentScreen, sIndex,
                    ScreenNumber);
            mCurrentScreen = sIndex;
            index = sIndex;
            setPreviousAnimation();
            setViewData(mCurrentScreen);
            showScreem();
            return 1;
        } else if (sIndex > mCurrentScreen && sIndex < ScreenNumber) {
            OnScreenChangeListener.onItemClick(mCurrentScreen, sIndex,
                    ScreenNumber);
            mCurrentScreen = sIndex;
            index = sIndex;
            setNextAnimation();
            setViewData(mCurrentScreen);
            showScreem();
            return 1;
        } else if (sIndex == mCurrentScreen) {
            return 0;
        } else {
            return -1;
        }
    }

    private void setViewData(int index) {
        View view = getNextView();
        MyGridView listView = (MyGridView) view
                .findViewById(R.id.slidelistview_gridview);
        listView.setGestureDetector(gestureDetector);
        adapter.setScreenData(mMenuData.getScreen(index));
        baseApp.setCurDishesData(adapter.getScreenData());
        listView.setAdapter(adapter);
    }

    public void setViewOnTouchListener(OnTouchListener mOnTouchListener) {
        this.mOnTouchListener = mOnTouchListener;
    }

    private void showScreem() {
        int mWhichChild = getDisplayedChild();
        int which = mWhichChild == 0 ? 1 : 0;
        currentWhich=which;
        setDisplayedChild(which);
    }

    OnTouchListener vOnTouchListener = new OnTouchListener() {
        public boolean onTouch(View arg0, MotionEvent arg1) {
            if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                touchDownX = arg1.getX();
                return true;
            } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                touchUpX = arg1.getX();
                if (touchDownX - touchUpX > 100)// 左滑
                {
                    if (index < 4) {
                        showNextScreen();
                        index++;
                    }
                } else if (touchUpX - touchDownX > 100) {
                    if (index > 0) {
                        showPreviousScreen();
                        index--;
                    }
                }
                return true;
            }
            return false;
        }
    };

    private String getActionName(int action) {
        String name = "";
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                name = "ACTION_DOWN";
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                name = "ACTION_MOVE";
                break;
            }
            case MotionEvent.ACTION_UP: {
                name = "ACTION_UP";
                break;
            }
            default:
                break;
        }
        return name;
    }

    /**
     * 创建一个Listener来实时监听当前面板操作手势。
     */
    class MyOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onSingleTapUp-----" + getActionName(e.getAction()));
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onLongPress-----" + getActionName(e.getAction()));
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (e1 != null && e2 != null) {
                Log.i(getClass().getName(),
                        "onScroll-----" + getActionName(e2.getAction()) + ",("
                                + e1.getX() + "," + e1.getY() + ") ,(" + e2.getX()
                                + "," + e2.getY() + ")");
            }
			/*
			 * if (e2.getX() - e1.getX() > 100)//左滑 { if (index < 4 ) {
			 * showNextScreen(); index++; } } else if(e1.getX() -e2.getX() >
			 * 100) { if (index >0) { showPreviousScreen(); index--; } }
			 */
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            Log.i(getClass().getName(),
                    "onFling-----" + getActionName(e2.getAction()) + ",("
                            + e1.getX() + "," + e1.getY() + ") ,(" + e2.getX()
                            + "," + e2.getY() + ")");
            if (e1.getX() - e2.getX() > 40) {// 左滑
                System.out.println("index:" + index);
                if (index < ScreenNumber - 1) {
                    if (baseApp.isDishesHomeTeachState()) {
                        baseApp.setDishesHomeTeachState(false);
                        showNextScreen();
                        index++;
                    } else {
                        System.out.println("触摸事件冲突，以忽视");
                    }
                } else {
                    baseApp.setDishesHomeTeachState(true);
                }
            } else if (e2.getX() - e1.getX() > 40) {// 右滑
                // index为0，判断算法有误
                System.out.println("index:" + index);
                if (index > 0) {
                    if (baseApp.isDishesHomeTeachState()) {
                        baseApp.setDishesHomeTeachState(false);
                        showPreviousScreen();
                        index--;
                    } else {
                        System.out.println("触摸事件冲突，以忽视");
                    }
                } else {
                    baseApp.setDishesHomeTeachState(true);
                }
            }
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onShowPress-----" + getActionName(e.getAction()));
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onDown-----" + getActionName(e.getAction()));
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onDoubleTap-----" + getActionName(e.getAction()));
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i(getClass().getName(), "onDoubleTapEvent-----"
                    + getActionName(e.getAction()));
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i(getClass().getName(), "onSingleTapConfirmed-----"
                    + getActionName(e.getAction()));
            return true;
        }
    }

    private void setNextAnimation() {
		/*
		 * setInAnimation(mContext, R.anim.switcher_in_right_scale);
		 * setOutAnimation(mContext, R.anim.switcher_out_right_scale);
		 */
            setInAnimation(switcher_in_right_parallel);
            getInAnimation().setAnimationListener(animationListener);
            setOutAnimation(switcher_out_right_parallel);
            getOutAnimation().setAnimationListener(animationListener);
    }

    private void setPreviousAnimation() {
		/*
		 * setInAnimation(mContext, R.anim.switcher_in_left_scale);
		 * setOutAnimation(mContext, R.anim.switcher_out_left_scale);
		 */
        if(getInAnimation()!=null){
        setInAnimation(switcher_in_left_parallel);
        getInAnimation().setAnimationListener(animationListener);
        }
        if(getOutAnimation()!=null){
        setOutAnimation(switcher_out_left_parallel);
        getOutAnimation().setAnimationListener(animationListener);
        }
    }
}