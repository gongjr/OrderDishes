package com.asiainfo.orderdishes.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;

/**
 * 桌台自定义控件
 *
 * @author ouyangyj
 *         <p/>
 *         2015年3月28日
 */
public class TableItemView extends TextView {

    private static final int DEFAULT_BORDER_WIDTH = 4;
    private static final int DEFAULT_BORDER_COLOR = Color.parseColor("#8c7e73");
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mModeBorderWidth = 0;

    private static final int DEFAULT_FRONT_COLOR = Color.parseColor("#6d5f5f"); //灰色
    private static final int DEFAULT_OPENED_FRONT_COLOR = Color.parseColor("#f5a623"); //橙黄色
    private static final int DEFAULT_SELECTED_FRONT_COLOR = Color.parseColor("#f06f6f"); //粉红色

    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#cfc9bb");
    private static final int OPENED_TEXT_COLOR = Color.parseColor("#ffffff");
    private static final int SELECTED_TEXT_COLOR = Color.parseColor("#ffffff");

    private int mDefualtFrontColor = DEFAULT_FRONT_COLOR;
    private int mOpenedFrontColor = DEFAULT_OPENED_FRONT_COLOR;
    private int mSelectedFrontColor = DEFAULT_SELECTED_FRONT_COLOR;

    private int mDefaultTextColor = DEFAULT_TEXT_COLOR;
    private int mOpenedTextColor = OPENED_TEXT_COLOR;
    private int mSelectedTextColor = SELECTED_TEXT_COLOR;

    private int mFrontColor = DEFAULT_FRONT_COLOR;
    private int mTextColor = mDefaultTextColor;

    public final static int VIEW_MODE_NORMAL = 1;
    public final static int VIEW_MODE_OPENED = 2;
    public final static int VIEW_MODE_SELECTED = 3;
    private int mViewMode = VIEW_MODE_NORMAL;

    private final RectF mContentRect = new RectF();
    private final RectF mBorderRect = new RectF();
    private final Paint mFrontColorPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private float mContentRadius;
    private float mBorderRadius;


    public TableItemView(Context context) {
        super(context);
    }

    public TableItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableItemView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.TableItemView_table_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.TableItemView_table_border_color, DEFAULT_BORDER_COLOR);
        mDefualtFrontColor = a.getColor(R.styleable.TableItemView_default_front_color, DEFAULT_FRONT_COLOR);
        mOpenedFrontColor = a.getColor(R.styleable.TableItemView_opened_front_color, DEFAULT_OPENED_FRONT_COLOR);
        mSelectedFrontColor = a.getColor(R.styleable.TableItemView_selected_front_color, DEFAULT_SELECTED_FRONT_COLOR);
        mDefaultTextColor = a.getColor(R.styleable.TableItemView_default_text_color, DEFAULT_TEXT_COLOR);
        mOpenedTextColor = a.getColor(R.styleable.TableItemView_opened_text_color, OPENED_TEXT_COLOR);
        mSelectedTextColor = a.getColor(R.styleable.TableItemView_selected_text_color, SELECTED_TEXT_COLOR);

//		System.out.println("mBorderWidth: " + mBorderWidth);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        build();

        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mContentRadius, mFrontColorPaint);
        super.onDraw(canvas);
    }

    public void build() {
        setTextColor(mTextColor);
        setViewModeInfo();

        mBorderPaint.setStyle(Paint.Style.STROKE); //空心
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor); //颜色
        mBorderPaint.setStrokeWidth(mBorderWidth); //边框宽

        mFrontColorPaint.setStyle(Paint.Style.FILL); //空心
        mFrontColorPaint.setAntiAlias(true);
        mFrontColorPaint.setColor(mFrontColor); //颜色

//	    mBorderRect.set(0, 0, getWidth(), getHeight());
// 	    mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
// 	    mContentRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
//	    mContentRadius = Math.min(mContentRect.height() / 2, mContentRect.width() / 2);

        if (mViewMode == VIEW_MODE_NORMAL) {
            mBorderRect.set(0, 0, getWidth(), getHeight());
            mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
            mContentRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
            mContentRadius = Math.min(mContentRect.height() / 2, mContentRect.width() / 2);
        } else {
            mBorderRect.set(0, 0, getWidth(), getHeight());
            mBorderRadius = Math.min((mBorderRect.height() - mModeBorderWidth) / 2, (mBorderRect.width() - mModeBorderWidth) / 2);
            mContentRect.set(mModeBorderWidth, mModeBorderWidth, mBorderRect.width() - mModeBorderWidth, mBorderRect.height() - mModeBorderWidth);
            mContentRadius = Math.min(mContentRect.height() / 2, mContentRect.width() / 2);
        }
    }

    private void setViewModeInfo() {
        if (mViewMode == VIEW_MODE_NORMAL) {
            mFrontColor = DEFAULT_FRONT_COLOR;
        } else if (mViewMode == VIEW_MODE_OPENED) {
            mFrontColor = DEFAULT_OPENED_FRONT_COLOR;
        } else if (mViewMode == VIEW_MODE_SELECTED) {
            mFrontColor = DEFAULT_SELECTED_FRONT_COLOR;
        }
    }

    public void setViewMode(int viewMode) {
        if (viewMode == VIEW_MODE_NORMAL) {
            mViewMode = VIEW_MODE_NORMAL;
            mTextColor = DEFAULT_TEXT_COLOR;
        } else if (viewMode == VIEW_MODE_OPENED) {
            mViewMode = VIEW_MODE_OPENED;
            mTextColor = mOpenedTextColor;
        } else if (viewMode == VIEW_MODE_SELECTED) {
            mViewMode = VIEW_MODE_SELECTED;
            mTextColor = mSelectedTextColor;
        } else {
            throw new IllegalArgumentException(String.format("view mode %s not supported.", viewMode));
        }


        invalidate();
    }

    public int getmBorderWidth() {
        return mBorderWidth;
    }

    public void setmBorderWidth(int mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        invalidate();
    }

    public int getmBorderColor() {
        return mBorderColor;
    }

    public void setmBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        invalidate();
    }

    public int getmDefualtFrontColor() {
        return mDefualtFrontColor;
    }

    public void setmDefualtFrontColor(int mDefualtFrontColor) {
        this.mDefualtFrontColor = mDefualtFrontColor;
        invalidate();
    }

    public int getmOpenedFrontColor() {
        return mOpenedFrontColor;
    }

    public void setmOpenedFrontColor(int mOpenedFrontColor) {
        this.mOpenedFrontColor = mOpenedFrontColor;
        invalidate();
    }

    public int getmSelectedFrontColor() {
        return mSelectedFrontColor;
    }

    public void setmSelectedFrontColor(int mSelectedFrontColor) {
        this.mSelectedFrontColor = mSelectedFrontColor;
        invalidate();
    }

    public int getmViewMode() {
        return mViewMode;
    }


}