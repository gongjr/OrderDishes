package com.asiainfo.orderdishes.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.adapter.DishesOrderAdapter;
import com.asiainfo.orderdishes.adapter.DishesSelectionAdapter;
import com.asiainfo.orderdishes.adapter.SimpleSectionedListAdapter;
import com.asiainfo.orderdishes.adapter.SimpleSectionedListAdapter.Section;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.ServerOrder;
import com.asiainfo.orderdishes.entity.ShoppingOrder;
import com.asiainfo.orderdishes.entity.eventbus.DiskOrderBackToMainData;
import com.asiainfo.orderdishes.entity.eventbus.DiskOrderInitData;
import com.asiainfo.orderdishes.entity.eventbus.Event;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.MerchantDesk;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.volley.UpdateOrderInfoResultData;
import com.asiainfo.orderdishes.entity.volley.NotityKitchenData;
import com.asiainfo.orderdishes.entity.volley.ResultMap;
import com.asiainfo.orderdishes.helper.DialogDelayListener;
import com.asiainfo.orderdishes.helper.OnDishItemClickListener;
import com.asiainfo.orderdishes.helper.PwdEncryptor;
import com.asiainfo.orderdishes.http.volley.ResultMapRequest;
import com.asiainfo.orderdishes.http.volley.VolleyErrorHelper;
import com.asiainfo.orderdishes.ui.base.AnimatedDoorActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class DiskOrderActivity extends AnimatedDoorActivity {

    private Button order_add;
    private Button order_edit;
    private Button order_zhifu;
    private Button back;
    /**
     * 所选菜品容器listview
     */
    ListView lv_Dishes;
    /**
     * lv_Dishes的适配器
     */
    DishesOrderAdapter mAdapter;
    DishesOrderAdapter editAdapter;
    SimpleSectionedListAdapter mSimpleSectionedListAdapter;
    SimpleSectionedListAdapter editSimpleSectionedListAdapter;

    /**
     * 业务实体类，处理点餐相关业务逻辑
     */
    private DishesEntity dishesEntity;
    /**
     * 装载数据头部节点的list
     */
    ArrayList<Section> sections = new ArrayList<Section>();
    /**
     * 总价钱
     */
    private TextView tv_totalPrice;
    private TextView diskid;
    private String username;
    private ImageView order_state_notity_img;
    private CheckBox chk_editBtn;
    /**
     * 大厅选择的桌子信息
     */
    private static MerchantDesk selectedDesk;
    private ShoppingOrder curShoppingOrder;

    @Override
    protected int layoutResId() {
        return R.layout.activity_orderdetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		username = baseApp.getLoginInfo().getChildMerchantId();
        username = baseApp.getLoginInfo() == null ? "0" : baseApp.getLoginInfo().getChildMerchantId();
        DishesOrder dishesOrder = (DishesOrder) this.getIntent().getSerializableExtra("dishesOrder");
//		dishesEntity=new DishesEntity(dishesOrder,true);
        selectedDesk = (MerchantDesk) getIntent().getSerializableExtra(
                "hall_selected_desk");
//		initView();
//		initData();
//		initListener(); 
        EventBus.getDefault().register(this);
        DiskOrderInitData diskOrderInitData = new DiskOrderInitData();
        diskOrderInitData.setDishesOrder(dishesOrder);
        diskOrderInitData.setType(1);
        EventBus.getDefault().post(diskOrderInitData);
    }


    public void onEventMainThread(Event event) {
        switch (event.getType()) {
            case 1:
                DiskOrderBackToMainData diskOrderBackToMainData = (DiskOrderBackToMainData) event.getData();
                dishesEntity = diskOrderBackToMainData.getDishesEntity();
                curShoppingOrder = diskOrderBackToMainData.getShoppingOrder();
                initView();
                initData();
                initListener();
                break;
            default:
                break;
        }
    }

    public void onEventBackgroundThread(DiskOrderInitData event) {
        switch (event.getType()) {
            case 1:
                DishesEntity dishesEntity_s = new DishesEntity(event.getDishesOrder(), true);
                ShoppingOrder shopping = dishesEntity_s.findShoppingOrder();
                DiskOrderBackToMainData diskOrderBackToMainData = new DiskOrderBackToMainData();
                diskOrderBackToMainData.setDishesEntity(dishesEntity_s);
                diskOrderBackToMainData.setShoppingOrder(shopping);
                Event<DiskOrderBackToMainData> msg = new Event<DiskOrderBackToMainData>();
                msg.setType(1);
                msg.setData(diskOrderBackToMainData);
                EventBus.getDefault().post(msg);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void initView() {
        order_add = (Button) findViewById(R.id.order_add);
        order_edit = (Button) findViewById(R.id.order_edit);
        order_zhifu = (Button) findViewById(R.id.order_zhifu);
        back = (Button) findViewById(R.id.order_back);
        lv_Dishes = (ListView) findViewById(R.id.dishes_list);
        tv_totalPrice = (TextView) findViewById(R.id.dishes_totalprice);
        diskid = (TextView) findViewById(R.id.order_diskid);
        order_state_notity_img = (ImageView) findViewById(R.id.order_state_notity_img);
        chk_editBtn = (CheckBox) findViewById(R.id.dishes_order_edit_error);
    }

    void initData() {
//		dishesEntity = new DishesEntity(69);
//		curShoppingOrder = dishesEntity.findShoppingOrder();
        Log.i("DishesEntity(DishesOrder)", "Time:2");
//		diskid.setText(dishesEntity.getOrderEntity().getDeskId()+"号桌订单");
        diskid.setText(selectedDesk.getDeskName() + "订单");
        dishesLVItemView(curShoppingOrder);
        order_state_notity_img.setBackgroundResource(R.drawable.order_detail_top_state_notify_no);
        order_zhifu.setText("通知后厨");
        order_edit.setVisibility(View.GONE);
        boolean allNotify = dishesEntity.isnotityKitchen();
        if (!allNotify) {
            order_state_notity_img.setBackgroundResource(R.drawable.order_detail_top_state_notify_no);
            order_zhifu.setText("通知后厨");
            order_add.setVisibility(View.GONE);
            order_edit.setVisibility(View.VISIBLE);
        } else {
            order_add.setVisibility(View.VISIBLE);
            order_edit.setVisibility(View.GONE);
            order_zhifu.setText("去支付");
            order_zhifu.setVisibility(View.VISIBLE);
            order_state_notity_img.setBackgroundResource(R.drawable.order_detail_top_state_notify_yes);
        }
        chk_editBtn.setVisibility(View.GONE);
    }

    void initListener() {
        chk_editBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                curShoppingOrder = dishesEntity.findShoppingOrder();
                if (isChecked) {
                    chk_editBtn.setText("修改完成");
                    chk_editBtn.setTextColor(getApplicationContext()
                            .getResources()
                            .getColor(R.color.black));
                    dishesLVEditItem(curShoppingOrder);
                } else {
                    chk_editBtn.setText("订单编辑");
                    chk_editBtn.setTextColor(getApplicationContext()
                            .getResources().getColor(R.color.submitdialog_no_text));
                    dishesLVItemView(curShoppingOrder);
                }
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                WaitterAuthenticate(2);
            }
        });
        order_zhifu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean allNotify = dishesEntity.isnotityKitchen();
                if (!allNotify) {
//					AlertDialog();
                    WaitterAuthenticate(1);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("diskName", selectedDesk.getDeskName());
                    intent.putExtra("orderid", dishesEntity.getOrderEntity().getOrderId() + "");
                    intent.putExtra("totalprice", tv_totalPrice.getText());
                    intent.setClass(getApplicationContext(),
                            ElectronicPaymentActivity.class);
                    startActivityForResult(intent, Constants.OrderDetail_requestcode_ElectronicPayment);
                    overridePendingTransition(0, 0);
                }

            }
        });
        order_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				finishNoAnimator();
                Intent intent = new Intent();
                intent.putExtra("submit_id", dishesEntity.getOrderEntity().getId());
                intent.putExtra("u_name", username);
                System.out.println("dishesorder username:" + username);
                intent.putExtra("diskid", dishesEntity.getOrderEntity().getDeskId() + "");
                intent.putExtra("order_id", dishesEntity.getOrderEntity().getOrderId() + "");
                intent.putExtra("totalprice", tv_totalPrice.getText());
                System.out.println("dishe onCreate tv_totalPrice.getText():" + tv_totalPrice.getText());
                intent.setClass(getApplicationContext(), DishesHomeActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("curShoppingOrder", curShoppingOrder);
                data.putSerializable("hall_selected_desk", selectedDesk);
                data.putSerializable("add_dishesOrder", dishesEntity.getOrderEntity());
                intent.putExtras(data);
//                backFinish();
                startActivityForResult(intent, Constants.DiskOrder_requestcode_add);
                overridePendingTransition(0, 0);

            }
        });
        order_edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("submit_id", dishesEntity.getOrderEntity().getId());
                intent.putExtra("u_name", username);
                System.out.println("dishesorder username:" + username);
                intent.putExtra("diskid", dishesEntity.getOrderEntity().getDeskId() + "");
                intent.putExtra("totalprice", tv_totalPrice.getText());
                System.out.println("dishe onCreate tv_totalPrice.getText():" + tv_totalPrice.getText());
                intent.setClass(getApplicationContext(),
                        DishesHomeActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("curShoppingOrder", curShoppingOrder);
                data.putSerializable("hall_selected_desk", selectedDesk);
                data.putSerializable("dishesOrder", dishesEntity.getOrderEntity());
                intent.putExtras(data);
                startActivityForResult(intent, Constants.DiskOrder_requestcode_edit);
                overridePendingTransition(0, 0);
            }
        });
    }

    /**
     * 菜单展示列表
     *
     * @param shopping
     */
    public void dishesLVItemView(ShoppingOrder shopping) {
        updateTotalPriceShow(shopping.getOrderGoods());
        sections.clear();
        for (int i = 0; i < shopping.getmHeaderPositions().size(); i++) {
            sections.add(new Section(shopping.getmHeaderPositions().get(i),
                    shopping.getmHeaderNames().get(i)));
        }

        if (mAdapter == null) {
            initLvAdapter(shopping);
        }
        mAdapter.setData(shopping.getOrderGoods());
        mSimpleSectionedListAdapter
                .setSections(sections.toArray(new Section[0]));
        lv_Dishes.setAdapter(mSimpleSectionedListAdapter);
    }

    /**
     * 设置菜单编辑列表
     *
     * @param shopping
     */
    public void dishesLVEditItem(ShoppingOrder shopping) {
        updateTotalPriceShow(shopping.getOrderGoods());
        sections.clear();
        for (int i = 0; i < shopping.getmHeaderPositions().size(); i++) {
            sections.add(new Section(shopping.getmHeaderPositions().get(i),
                    shopping.getmHeaderNames().get(i)));
        }
        if (editAdapter == null) {
            initEditLvAdapter(shopping);
        }
        editAdapter.setData(shopping.getOrderGoods());
        editSimpleSectionedListAdapter
                .setSections(sections.toArray(new Section[0]));
        lv_Dishes.setAdapter(editSimpleSectionedListAdapter);
    }

    /**
     * 更新编辑list菜单列表
     */
    public void updateEditDishesList() {
        curShoppingOrder = dishesEntity.findShoppingOrder();
        sections.clear();
        for (int i = 0; i < curShoppingOrder.getmHeaderPositions().size(); i++) {
            sections.add(new Section(curShoppingOrder.getmHeaderPositions().get(i),
                    curShoppingOrder.getmHeaderNames().get(i)));
        }
        editAdapter.setData(curShoppingOrder.getOrderGoods());
        ;
        editSimpleSectionedListAdapter
                .setSections(sections.toArray(new Section[0]));
        lv_Dishes.setAdapter(editSimpleSectionedListAdapter);
    }

    /**
     * 更新总价
     *
     * @param dishesList
     */
    public void updateTotalPriceShow(ArrayList<OrderGoods> dishesList) {
        long total = 0;
        if (dishesList != null && dishesList.size() > 0) {
            for (int i = 0; i < dishesList.size(); i++) {
                OrderGoods dm = dishesList.get(i);
                total = total + dm.getSalesNum() * dm.getDishesPrice();
            }
        }
        tv_totalPrice.setText(total + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.OrderDetail_requestcode_ElectronicPayment:
                switch (resultCode) {
                    case Constants.ElectronicPayment_resultcode_back:
                        break;
                    case Constants.ElectronicPayment_resultcode_finish:
                        this.finish();
                        break;
                    default:
                        break;
                }
                break;
            case Constants.DiskOrder_requestcode_add:
                switch (resultCode) {
                    case Constants.DishesHome_resultcode_back:
                        DishesOrder dishes = (DishesOrder) data.getSerializableExtra("dishesOrder");
                        username = data.getStringExtra("ChildMerchantId");
                        dishesEntity = new DishesEntity(dishes, false);
                        initData();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 通知服务器打印
     */
    public void VolleyNotityKitchen() {
        String param = "/appController/notityKitchen.do?";
        Type type = new TypeToken<ResultMap<NotityKitchenData>>() {
        }.getType();
        System.out.println("editOrderSubmit:" + Constants.address + param);
        ResultMapRequest<ResultMap<NotityKitchenData>> ResultMapRequest = new ResultMapRequest<ResultMap<NotityKitchenData>>(
                Method.POST, Constants.address + param, type,
                new Response.Listener<ResultMap<NotityKitchenData>>() {
                    @Override
                    public void onResponse(
                            ResultMap<NotityKitchenData> response) {
                        dismissLoadingDialog();
                        try {
                            if (response.getErrcode().equals("0")) {
                                dishesEntity.notityKitchenSuccess();
                                initData();
                            } else {
                                repeatAlertDialog(response.getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dismissLoadingActivity();
                dismissLoadingDialog();
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                repeatAlertDialog(VolleyErrorHelper.getMessage(error,
                        mContext));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                Gson gson = new Gson();
                DishesOrder order = new DishesOrder();
                order = dishesEntity.getOrderEntity();
                order.setOrderGoods(dishesEntity.notityKitchen());
                for (OrderGoods goods : order.getOrderGoods()) {
                    goods.setSalesState("1");
                }
                ServerOrder serverOrder = baseUtils.DishesOrderToServerOrder(order);
                String inparam = gson.toJson(serverOrder);
                paramList.put("inparam", inparam);
                Log.i("VolleyLogTag", "paramList:" + paramList.toString());
                return paramList;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        ResultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1,
                1.0f));
        requestQueue.add(ResultMapRequest);
    }


    private void AlertDialog() {
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.submitdialog);
        Button negative = (Button) window
                .findViewById(R.id.submitdialog_negative);
        negative.setVisibility(View.VISIBLE);
        negative.setText("再看看");
        Button positive = (Button) window
                .findViewById(R.id.submitdialog_positive);
        positive.setVisibility(View.VISIBLE);
        positive.setText("确定");
        RadioGroup group = (RadioGroup) window
                .findViewById(R.id.submit_notity_radioGroup);
        group.setVisibility(View.GONE);
        negative.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        positive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingAlertDialog();
            }
        });
        TextView context = (TextView) window
                .findViewById(R.id.submitdialog_content);
        context.setText("  通知厨房开始制作吗?");
        ImageButton close = (ImageButton) window
                .findViewById(R.id.submitdialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void loadingAlertDialog() {
        dialog.dismiss();
        showLoadingDialog(new DialogDelayListener() {

            @Override
            public void onexecute() {
//				VolleyNotityKitchen();
                VolleyNotityPersistOrder();
            }
        }, 100);
    }

    private void repeatAlertDialog(String info) {
        dialog.dismiss();
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.submitdialog);
        Button negative = (Button) window
                .findViewById(R.id.submitdialog_negative);
        negative.setVisibility(View.VISIBLE);
        negative.setText("再看看");
        Button positive = (Button) window
                .findViewById(R.id.submitdialog_positive);
        positive.setVisibility(View.VISIBLE);
        positive.setText("重新通知");
        negative.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        positive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingAlertDialog();
            }
        });
        TextView context = (TextView) window
                .findViewById(R.id.submitdialog_content);
        context.setText(info + ",请重新尝试!");
        ImageButton close = (ImageButton) window
                .findViewById(R.id.submitdialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RadioGroup group = (RadioGroup) window
                .findViewById(R.id.submit_notity_radioGroup);
        group.setVisibility(View.GONE);
    }

    /**
     * 初始化普通订单列表适配器
     *
     * @param shopping
     */
    void initLvAdapter(ShoppingOrder shopping) {
        mAdapter = new DishesOrderAdapter(this,
                DishesSelectionAdapter.DISHES_LV_TYPE_VIEW, dishesEntity);
        mAdapter.setData(shopping.getOrderGoods());
        mSimpleSectionedListAdapter = new SimpleSectionedListAdapter(
                this, mAdapter, R.layout.dish_selected_lv_item_header,
                R.id.view_header);


    }

    /**
     * 初始化可编辑订单列表适配器
     *
     * @param shopping
     */
    void initEditLvAdapter(ShoppingOrder shopping) {

        editAdapter = new DishesOrderAdapter(this,
                DishesSelectionAdapter.DISHES_LV_TYPE_EDIT, dishesEntity);
        editAdapter.setOnDishItemClickListener(new OnDishItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int clickType) {
                OrderGoods ordergood = (OrderGoods) v.getTag(R.id.tag_first);
                TextView count = (TextView) v.getTag(R.id.tag_second);
                switch (clickType) {
                    case OnDishItemClickListener.ClickType_DELETE:
                        dishesEntity.decreaseAllOrderGoods(ordergood);
                        updateEditDishesList();
                        break;
                    case OnDishItemClickListener.ClickType_ADD:
                        dishesEntity.addOrderGoods(ordergood);
                        int num_add = Integer.valueOf(count.getText().toString());
                        num_add++;
                        count.setText(num_add + "");
                        break;
                    case OnDishItemClickListener.ClickType_MINUS:
                        int num_minus = Integer.valueOf(count.getText().toString());
                        dishesEntity.decreaseOrderGoods(ordergood);
                        if (num_minus > 1) {
                            num_minus--;
                            count.setText(num_minus + "");
                        } else if (num_minus == 1) {
                            updateEditDishesList();
                        }
                        break;
                    default:
                        break;
                }
                curShoppingOrder = dishesEntity.findShoppingOrder();
                updateTotalPriceShow(curShoppingOrder.getOrderGoods());
            }
        });

        editSimpleSectionedListAdapter = new SimpleSectionedListAdapter(
                DiskOrderActivity.this, editAdapter,
                R.layout.dish_selected_lv_item_edit_header, R.id.edit_header);

    }

    /**
     * 通知服务器打印保留订单
     */
    public void VolleyNotityPersistOrder() {
        String param = "/appController/updateOrderInfo.do?";
        System.out.println("NotityPersistOrder:" + Constants.address + param);
        ResultMapRequest<UpdateOrderInfoResultData> ResultMapRequest = new ResultMapRequest<UpdateOrderInfoResultData>(
                Method.POST, Constants.address + param, UpdateOrderInfoResultData.class,
                new Response.Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            UpdateOrderInfoResultData response) {
                        dismissLoadingDialog();
                        if(response.getState()==1) {
                            dishesEntity.notityKitchenSuccess();
                            curShoppingOrder = dishesEntity.findShoppingOrder();
                            initData();
//                            backFinish();
                        }
                        else if(response.getState()==0) {
                            repeatAlertDialog(response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dismissLoadingActivity();
                dismissLoadingDialog();
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                repeatAlertDialog(VolleyErrorHelper.getMessage(error,
                        mContext));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                Gson gson = new Gson();
                ServerOrder serverOrder = new ServerOrder();
                DishesOrder dishesOrder = dishesEntity.getOrderEntity();

                serverOrder.setDeskId(dishesOrder.getDeskId());
                serverOrder.setOrderId(dishesOrder.getOrderId());
                serverOrder.setPersonNum(dishesOrder.getPersonNum());
                serverOrder.setTradeStsffId(dishesOrder.getTradeStaffId());
                serverOrder.setCreateTime(dishesOrder.getCreateTime());
                serverOrder.setOriginalPrice(dishesOrder.getOriginalPrice());
                serverOrder.setMerchantId(dishesOrder.getMerchantId());
                serverOrder.setChildMerchantId(dishesOrder.getChildMerchantId());
                serverOrder.setRemark(dishesOrder.getRemark());
                String inparam = gson.toJson(serverOrder);
                paramList.put("orderSubmitData", inparam);
                Log.i("VolleyLogTag", "paramList:" + paramList.toString());
                return paramList;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        ResultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1,
                1.0f));
        requestQueue.add(ResultMapRequest);
    }

    private void WaitterAuthenticate(final int type) {
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.waitter_authenticate);
        final EditText password = (EditText) window.findViewById(R.id.waitter_authenticate_edit);
        final TextView password_error = (TextView) window
                .findViewById(R.id.password_error);

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        TextWatcher watcher_password = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (password_error.getVisibility() == View.VISIBLE)
                    password_error.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        password.addTextChangedListener(watcher_password);
        Button positive = (Button) window
                .findViewById(R.id.waitter_dialog_submit);
        positive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String pass = PwdEncryptor.encryptByMD5(password.getText().toString(),
                        baseApp.getLoginInfo().getStaffId());
                if (pass.equals(baseApp.getLoginInfo().getPassword())) {
                    dialog.dismiss();
                    if (password_error.getVisibility() == View.VISIBLE)
                        password_error.setVisibility(View.GONE);
                    if (type == 1)
                        loadingAlertDialog();
                    else {
                        backFinish();
                    }
                } else {
                    password_error.setVisibility(View.VISIBLE);
                }
            }
        });
        ImageButton close = (ImageButton) window
                .findViewById(R.id.waitter_dialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        System.out.println("keyCode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
