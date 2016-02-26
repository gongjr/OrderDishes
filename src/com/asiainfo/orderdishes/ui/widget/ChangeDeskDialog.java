package com.asiainfo.orderdishes.ui.widget;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.litepal.MerchantDesk;
import com.asiainfo.orderdishes.ui.base.EnsureDialogFragmentBase;

public class ChangeDeskDialog extends EnsureDialogFragmentBase {

    MerchantDesk changeFromDesk, changeToDesk;

    public ChangeDeskDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTypeIconRes(R.drawable.person_01);
        setLeftBtnText("取消");
        setRightBtnText("确定");
        changeFromDesk = (MerchantDesk) getArguments().get("changeFromDesk");
        changeToDesk = (MerchantDesk) getArguments().get("changeToDesk");
//		setShowText("您是要换到"+changeToDesk.getDeskId()+"号桌吗？");
        setShowText(Html.fromHtml(String.format(mActivity.getResources().getString(R.string.hall_change_table_dialog_text), changeToDesk.getDeskId())));
    }

    @Override
    public void clickLeftButtonEvent() {
        super.clickLeftButtonEvent();
        System.out.println("换桌对话框  点击取消： ");
        dismiss();
    }

    @Override
    public void clickRightButtonEvent() {
        super.clickRightButtonEvent();
        if (changeFromDesk != null && changeToDesk != null) {
            System.out.println("换桌对话框  点击确定： ");
            System.out.print("从 " + changeFromDesk.getLocationCode() + "-" + changeFromDesk.getDeskId());
            System.out.println(" 换到" + changeToDesk.getLocationCode() + "-" + changeToDesk.getDeskId());
        }
        dismiss();
    }

}
