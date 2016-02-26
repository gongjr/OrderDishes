package com.asiainfo.orderdishes.ui;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.adapter.HallChangeDeskAdapter;
import com.asiainfo.orderdishes.entity.litepal.MerchantDesk;
import com.asiainfo.orderdishes.entity.volley.ResultMap;
import com.asiainfo.orderdishes.entity.volley.queryFreeDeskData;
import com.asiainfo.orderdishes.helper.OnItemClickListener;
import com.asiainfo.orderdishes.http.volley.ResultMapRequest;
import com.asiainfo.orderdishes.http.volley.VolleyErrorHelper;
import com.asiainfo.orderdishes.ui.base.DialogFragmentBase;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectedDeskChangeToDialog extends DialogFragmentBase {
    private static final String TAG = SelectedDeskChangeToDialog.class.getName();
    private String childMerchantId;
    private MerchantDesk currentSelectedDesk, changeToDesk;
    private String orderId;
    private View view;
    private TextView tv_showText;
    private EditText edit_selectDesk;  //正在获取桌子数据..., 选择换桌, 换桌到-XXXX
    private CheckBox chk_showSelectableDesks;
    private Button btn_confirm;
    private ImageView btn_close;
    private View desksRootView;
    private PopupWindow pop_dropDownPopup;
    private ListView lv_dropdown;
    private View v_cover_layer;
    private int showTxtColor1 = Color.parseColor("#70453B3B"); //正在获取桌子数据..., 选择换桌
    private int showTxtColor2 = Color.parseColor("#453B3B"); //换桌到-XXXX
    private List<MerchantDesk> listMData;
    private HallChangeDeskAdapter deskAdapter;
    private OnChangeDeskListener mOnChangeDeskListener;

    public interface OnChangeDeskListener {
        public void onSuccess(MerchantDesk changeToDesk);
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        view = inflater.inflate(R.layout.hall_change_desk_dialog, container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = mActivity.getFragmentManager().findFragmentByTag("change_desk_dialog").getArguments();
        currentSelectedDesk = bundle.getSerializable("changeFromDesk") == null ? null : (MerchantDesk) bundle.getSerializable("changeFromDesk");
        childMerchantId = bundle.getString("childMerchantId");
        orderId = bundle.getString("orderId");
        Log.d(TAG, "childMerchantId" + currentSelectedDesk.getDeskName());
        initView();
        initListener();
        initData();
        initDropDownPop();
    }

    public void initView() {
        btn_close = (ImageView) view.findViewById(R.id.close_btn);
        tv_showText = (TextView) view.findViewById(R.id.text_show);
        edit_selectDesk = (EditText) view.findViewById(R.id.desk_selection);
        chk_showSelectableDesks = (CheckBox) view.findViewById(R.id.show_selected_desk_btn);
        btn_confirm = (Button) view.findViewById(R.id.confirm_btn);
        v_cover_layer = view.findViewById(R.id.cover_layer);
        v_cover_layer.setVisibility(View.GONE);
    }

    public void initListener() {
        chk_showSelectableDesks.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showDeskDropDown();
                } else {
                    hideDeskDropDown();
                }
            }
        });

        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeToDesk == null) {
                    showShortTip("请先选择目标桌子");
                } else {
                    Log.d(TAG, "确定换桌");
                    Log.d(TAG, currentSelectedDesk.getDeskId() + " 换到  " + changeToDesk.getDeskId());
                    updateOrderDesk();
                }
            }
        });
    }

    public void initData() {
        setCancelable(false);
        listMData = new ArrayList<MerchantDesk>();
        showLoadingDeskData();
        getSelectableDesks();
    }

    /**
     * 初始化弹出下拉框的视图数据
     */
    private void initDropDownPop() {
        desksRootView = mActivity.getLayoutInflater().inflate(R.layout.hall_change_desk_dropdown_pop, null);
        lv_dropdown = (ListView) desksRootView.findViewById(R.id.lv_dropdown);
        deskAdapter = new HallChangeDeskAdapter(listMData, mActivity);
        deskAdapter.setMOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                changeToDesk = deskAdapter.getSelectedDesk();
                showSelectedDesk();
                chk_showSelectableDesks.setChecked(false);
            }
        });
        Log.d(TAG, "lv_dropdown / deskAdapter " + lv_dropdown + "-" + deskAdapter);
        lv_dropdown.setAdapter(deskAdapter);
        pop_dropDownPopup = new PopupWindow(desksRootView);
        pop_dropDownPopup.setFocusable(true);
        pop_dropDownPopup.setOutsideTouchable(true);
        pop_dropDownPopup.setBackgroundDrawable(new BitmapDrawable());
        pop_dropDownPopup.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (chk_showSelectableDesks.isChecked()) {
                    chk_showSelectableDesks.setChecked(false);
                }
            }
        });
    }

    /**
     * 获取可换的桌子数据
     * http://115.29.35.199:19890/buildsite/appController/queryFreeDesk.do?
     * childMerchantId=10000344&deskId=1&locationCode=1
     */
    private void getSelectableDesks() {
        if (currentSelectedDesk == null) {
            Log.d(TAG, "currentSelectedDesk==null");
            return;
        }

        String param = "/appController/queryFreeDesk.do?childMerchantId=" + childMerchantId
                + "&deskId=" + currentSelectedDesk.getDeskId() + "&locationCode=" + currentSelectedDesk.getLocationCode();
        Log.d(TAG, "childMerchantId: " + Constants.BuildSite + param);
        Type type = new TypeToken<ResultMap<queryFreeDeskData>>() {
        }.getType();
        ResultMapRequest<ResultMap<queryFreeDeskData>> resultMapRequest = new ResultMapRequest<ResultMap<queryFreeDeskData>>(
                Constants.address + param, type,
                new Response.Listener<ResultMap<queryFreeDeskData>>() {
                    @Override
                    public void onResponse(ResultMap<queryFreeDeskData> response) {
                        Log.d(TAG, "queryFreeDesk:" + response.getData());
                        if (response.getData() != null && response.getData().getInfo().size() > 0) {
                            listMData = response.getData().getInfo();
                            showSelectingDesk("选择桌子");
                        } else {
                            listMData.clear();
                            showSelectingDesk("没有空桌可换");
                            Log.d(TAG, "没有可换的桌子");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError:" + error.getMessage(), error);
                showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
            }
        });
        resultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        requestQueue.add(resultMapRequest);
    }

    /**
     * 换桌操作
     * http://192.168.1.151:8080/buildsite/appController/orderChangeDesk.do?
     * childMerchantId=10000344&orderId=2015041600010277&fromDeskId=1&toDeskId=4
     */
    private void updateOrderDesk() {
        if (currentSelectedDesk == null || changeToDesk == null) {
            Log.d(TAG, "currentSelectedDesk== " + currentSelectedDesk);
            Log.d(TAG, "changeToDesk== " + changeToDesk);
            return;
        }
        onCommittingState();
        String param = "/appController/orderChangeDesk.do?childMerchantId=" + childMerchantId
                + "&orderId=" + orderId + "&fromDeskId=" + currentSelectedDesk.getDeskId() + "&toDeskId=" + changeToDesk.getDeskId();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
                Constants.address + param, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onCommitSuccessState();
                Log.d(TAG, "换桌  response:" + response.toString());
                try {
                    String errcode = response.getString("errcode");
                    Log.d(TAG, "换桌响应 errorcode : " + errcode);
                    String msg = response.getString("msg");
                    if (msg != null && msg.equals("ok")) {
                        Log.d(TAG, "换桌成功！" + msg);
                        onChangeDeskSuccess(changeToDesk); //换桌成功，回调返回到桌台大厅，刷新数据
                    } else {
                        Log.d(TAG, "换桌失败：" + msg);
                        showShortTip("换桌失败-" + msg);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "换桌结果数据解析失败 response：" + response);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onCommitFailState();
                Log.e(TAG, "VolleyError:" + error.getMessage(), error);
            }
        });
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        requestQueue.add(jsonObjRequest);
    }

    /**
     * 显示选桌的下拉
     */
    private void showDeskDropDown() {
        Log.d(TAG, "显示 pop_dropDownPopup");
        deskAdapter.refreshDataWithPos(listMData, -1);

//		int dip = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, mActivity.getResources().getDisplayMetrics());
        int width = edit_selectDesk.getWidth();
        int height = edit_selectDesk.getHeight();
        if (listMData.size() < 7) {
            height = height * listMData.size();
            pop_dropDownPopup.setHeight(height);
        } else {
            pop_dropDownPopup.setHeight(700);
        }
        Log.d(TAG, "real height " + pop_dropDownPopup.getHeight());
        pop_dropDownPopup.setWidth(width);
        if (chk_showSelectableDesks.isChecked()) {
            pop_dropDownPopup.showAsDropDown(edit_selectDesk, 0, 0); //btn_confirm
        }
    }

    /**
     * 隐藏选桌的下拉
     */
    private void hideDeskDropDown() {
        if (pop_dropDownPopup.isShowing()) {
            pop_dropDownPopup.dismiss();
        }
    }

    private void onChangeDeskSuccess(MerchantDesk changeToDesk) {
        if (mOnChangeDeskListener != null && changeToDesk != null) {
            mOnChangeDeskListener.onSuccess(changeToDesk);
            dismiss();
        }
    }

    private void showLoadingDeskData() {
        edit_selectDesk.setText("正在获取桌子数据...");
        edit_selectDesk.setTextColor(showTxtColor1);
    }

    private void showSelectingDesk(String datars) {
        edit_selectDesk.setText(datars);
        edit_selectDesk.setTextColor(showTxtColor1);
    }

    private void showSelectedDesk() {
        if (changeToDesk != null) {
            edit_selectDesk.setText("" + changeToDesk.getDeskName());
        } else {
            edit_selectDesk.setText("选择桌子");
            Log.d(TAG, "showSelectedDesk()中 changeToDesk==null");
        }
        edit_selectDesk.setTextColor(showTxtColor2);
        hideDeskDropDown();
    }

    public void setOnChangeDeskListener(OnChangeDeskListener mOnChangeDeskListener) {
        this.mOnChangeDeskListener = mOnChangeDeskListener;
    }

    /**
     * 正在提交状态
     */
    private void onCommittingState() {
        Log.d(TAG, "正在提交换桌状态");
        chk_showSelectableDesks.setEnabled(false);
        btn_confirm.setEnabled(false);
        btn_confirm.setText("正在提交...");
        v_cover_layer.setVisibility(View.VISIBLE);
    }

    /**
     * 提交成功状态
     */
    private void onCommitSuccessState() {
        Log.d(TAG, "换桌成功状态");
        showShortTip("换桌成功");
        chk_showSelectableDesks.setEnabled(true);
        btn_confirm.setEnabled(true);
        btn_confirm.setText("确认");
        v_cover_layer.setVisibility(View.GONE);
    }

    /**
     * 提交失败状态
     */
    private void onCommitFailState() {
        Log.d(TAG, "换桌失败状态");
        showShortTip("换桌失败");
        chk_showSelectableDesks.setEnabled(true);
        btn_confirm.setEnabled(true);
        btn_confirm.setText("确认");
        v_cover_layer.setVisibility(View.GONE);
    }
}