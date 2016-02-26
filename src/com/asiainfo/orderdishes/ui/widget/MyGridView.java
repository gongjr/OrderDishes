package com.asiainfo.orderdishes.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

public class MyGridView extends GridView {
    int index = 0;
    GestureDetector gestureDetector;
    private boolean mHandlerClick;
    private View mHitView;

    public MyGridView(Context context) {
        super(context);
        this.gestureDetector = new GestureDetector(context, new
                MyOnGestureListener());
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gestureDetector = new GestureDetector(context, new
                MyOnGestureListener());
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public GestureDetector getGestureDetector() {
        return this.gestureDetector;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        gestureDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev == null) {
            Log.i("monkey", "MyGridView dispatchTouchEvent:MotionEvent ev==null 忽视返回false");
            return false;
        } else {
            gestureDetector.onTouchEvent(ev);
            super.dispatchTouchEvent(ev);
            return true;
        }
    }

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

    class MyOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onSingleTapUp-----" + getActionName(e.getAction()));
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onLongPress-----" + getActionName(e.getAction()));
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i(getClass().getName(),
                    "onScroll-----" + getActionName(e2.getAction()) + ",("
                            + e1.getX() + "," + e1.getY() + ") ,(" + e2.getX()
                            + "," + e2.getY() + ")");
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            Log.i(getClass().getName(),
                    "onFling-----" + getActionName(e2.getAction()) + ",("
                            + e1.getX() + "," + e1.getY() + ") ,(" + e2.getX()
                            + "," + e2.getY() + ")");
            return false;
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
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i(getClass().getName(),
                    "onDoubleTap-----" + getActionName(e.getAction()));
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i(getClass().getName(), "onDoubleTapEvent-----"
                    + getActionName(e.getAction()));
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i(getClass().getName(), "onSingleTapConfirmed-----"
                    + getActionName(e.getAction()));
            return false;
        }
    }

    private class OnGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // 如果按下，并且移动，就标志取消onItemClick事件
            if (e2.getAction() == MotionEvent.ACTION_MOVE) {

                if (Math.abs(distanceX) > 5 || Math.abs(distanceY) > 5) {
                    mHandlerClick = false;
                    if (mHitView != null) {
                        mHitView.setPressed(false);
                        mHitView.setSelected(false);
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // Finalise scrolling
            // 按下时，取得当前点击的item view
            mHandlerClick = true;
            int x = (int) e.getX();
            int y = (int) e.getY();
            int pos = MyGridView.this.pointToPosition(x, y);
            mHitView = MyGridView.this.getChildAt(pos);
            if (mHitView != null) {
                mHitView.setPressed(true);
                mHitView.setSelected(true);
            }
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // 按下时，取得当前点击的item view
            mHandlerClick = true;
            int x = (int) e.getX();
            int y = (int) e.getY();
            int pos = MyGridView.this.pointToPosition(x, y);
            mHitView = MyGridView.this.getChildAt(pos);
            if (mHitView != null) {
                mHitView.setPressed(true);
                mHitView.setSelected(true);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Reset fling state
            if (mHitView != null) {
                mHitView.setPressed(false);
                mHitView.setSelected(false);
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // 按下后，如果事件没有被取消，调用onItemClick的处理过程
            if (mHitView != null) {
                mHitView.setPressed(false);
                mHitView.setSelected(false);
            }

            if (mHandlerClick && mHitView != null) {
                if (MyGridView.this.getOnItemClickListener() != null)
                    MyGridView.this.getOnItemClickListener().onItemClick(null,
                            mHitView, 0, 0);
            }
            return true;
        }
    }

}
