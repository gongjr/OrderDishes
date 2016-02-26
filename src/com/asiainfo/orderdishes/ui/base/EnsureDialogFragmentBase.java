package com.asiainfo.orderdishes.ui.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;

/**
 * @author ouyangyj
 *         <p/>
 *         2015年4月22日
 */
public class EnsureDialogFragmentBase extends DialogFragment {
    View view; //全局view
    ImageView btn_close; //关闭按钮
    ImageView img_typeIcon; //类型图标
    protected TextView tv_showText; //展示文本
    protected TextView tv_showText2; //展示文本
    Button btn_left; //左边按钮
    Button btn_right; //右边按钮
    CallBackListener mCallBackListener;
    protected Activity mActivity;

    public interface CallBackListener {
        public void onLeftBtnFinish();

        public void onRightBtnFinish();

        public void onCloseBtnFinish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_base, container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        btn_close = (ImageView) view.findViewById(R.id.close_btn);
        img_typeIcon = (ImageView) view.findViewById(R.id.typed_img);
        tv_showText = (TextView) view.findViewById(R.id.text_show);
        tv_showText2 = (TextView) view.findViewById(R.id.text_show2);
        btn_left = (Button) view.findViewById(R.id.btn1);
        btn_right = (Button) view.findViewById(R.id.btn2);

        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBackListener.onCloseBtnFinish();
                dismiss();
            }
        });

        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeftButtonEvent();
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRightButtonEvent();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    protected void setTypeIconRes(int resId) {
        img_typeIcon.setImageResource(resId);
    }

    protected void setLeftBtnText(String txt) {
        btn_left.setText(txt);
    }

    protected void setRightBtnText(String txt) {
        btn_right.setText(txt);
    }

    protected void setShowText(String txt) {
        tv_showText.setText(txt);
    }

    protected void setShowText(Spanned spanned) {
        tv_showText.setText(spanned);
    }

    protected void clickLeftButtonEvent() {
        mCallBackListener.onLeftBtnFinish();
    }

    protected void clickRightButtonEvent() {
        mCallBackListener.onRightBtnFinish();
    }

    public void setOnCallBackListener(CallBackListener mCallBackListener) {
        this.mCallBackListener = mCallBackListener;
    }
}
