package com.asiainfo.orderdishes.ui.widget;

import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.ui.base.EnsureDialogFragmentBase;

public class MakeOrderSuccessDialog extends EnsureDialogFragmentBase {

    public MakeOrderSuccessDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTypeIconRes(R.drawable.person_02);
        setLeftBtnText("返回大厅");
        setRightBtnText("打印");
        //setShowText("下单成功！厨师正精心为您准备呢~"); 
        tv_showText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv_showText2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv_showText.setText(Html.fromHtml(String.format(mActivity.getResources().getString(R.string.make_order_success_dialog_text))));
        tv_showText2.setText("厨师正精心为您准备呢~");
    }

    @Override
    public void clickLeftButtonEvent() {
        super.clickLeftButtonEvent();
        System.out.println("下单成功对话框  点击加菜");
        dismiss();
    }

    @Override
    public void clickRightButtonEvent() {
        super.clickRightButtonEvent();
        System.out.println("下单对话框  点击打印");
        dismiss();
    }
}
