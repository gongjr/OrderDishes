package com.asiainfo.orderdishes.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.orderdishes.BaseActivity;
import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.adapter.DishesSelectionAdapter;
import com.asiainfo.orderdishes.adapter.DishesSubmitAdapter;
import com.asiainfo.orderdishes.adapter.SimpleSectionedListAdapter;
import com.asiainfo.orderdishes.adapter.SimpleSectionedListAdapter.Section;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.ServerOrder;
import com.asiainfo.orderdishes.entity.ServerOrderGoods;
import com.asiainfo.orderdishes.entity.ShoppingOrder;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.litepal.RemarkItem;
import com.asiainfo.orderdishes.entity.volley.UpdateOrderInfoResultData;
import com.asiainfo.orderdishes.entity.volley.SubmitOrderId;
import com.asiainfo.orderdishes.entity.volley.VolleyErrors;
import com.asiainfo.orderdishes.helper.DialogDelayListener;
import com.asiainfo.orderdishes.helper.OnDishItemClickListener;
import com.asiainfo.orderdishes.http.volley.ResultMapRequest;
import com.asiainfo.orderdishes.http.volley.VolleyErrorHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DishesSelectionActivity extends BaseActivity {
    /**
     * 所选菜品容器listview
     */
    ListView lv_Dishes, submit_list;

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {
        super.revokeUriPermission(uri, modeFlags);
    }

    /**
     * lv_Dishes的适配器
     */
    DishesSelectionAdapter mAdapter;
    DishesSelectionAdapter editAdapter;
    SimpleSectionedListAdapter mSimpleSectionedListAdapter;
    SimpleSectionedListAdapter editSimpleSectionedListAdapter;
    /**
     * submit_list，已保存订单展示列表
     */
    DishesSubmitAdapter submitAdapter;
    SimpleSectionedListAdapter submitSimpleSectionedListAdapter;
    /**
     * 装载数据头部节点的list
     */
    ArrayList<Section> sections = new ArrayList<Section>();
    /**
     * 装载已下单数据头部节点的list
     */
    ArrayList<Section> submit_sections = new ArrayList<Section>();
    /**
     * 控制listview的状态的按钮
     */
    CheckBox chk_editBtn;
    /**
     * 下单按钮
     */
    Button btn_orderSettle;
    /**
     * 口味要求
     */
    EditText edit_fearures;
    /**
     * 总价钱
     */
    TextView tv_totalPrice;
    /**
     * 总价钱
     */
    TextView dish_total_num;
    /**
     * 总价钱
     */
    TextView dd_diskid;
    TextView dish_selected_top_left;
    /**
     * 左边半透明的视图布局
     */
    LinearLayout dish__selected_middle_left;
    /**
     * 左边半透明的视图布局
     */
    LinearLayout dish_selected_left;
    /**
     * 业务实体类，处理点餐相关业务逻辑
     */
    private DishesEntity dishesEntity, dishesSubmitEntity;

    /**
     * 提示用dialog
     */
    private Dialog dialog;
    private View dd_content, settle_group, dish_selected_top_right;
    private int id;
    private int orderOperateState, submit_id;
    private String add_old_totalPrice;
    private String notityKitchen = Constants.notityKitchen_wait;
    private String changeDesk = Constants.changeDesk_no;
    private DishesOrder dishesOrder;
    private boolean isEdit = false;
    private String deskName;
    //	private ShoppingOrder curShoppingOrder;
    private ShoppingOrder submitShoppingOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_dish_selected);
        getWindow().setLayout(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        deskName = this.getIntent().getStringExtra("deskName");
        id = this.getIntent().getIntExtra("order_id", 1);
        Log.i("DishesSelection  onCreate---", "id:" + id);
        orderOperateState = this.getIntent()
                .getIntExtra("orderOperateState", 1);
        Log.i("DishesSelection  onCreate---", "orderOperateState:" + orderOperateState);
        add_old_totalPrice = this.getIntent().getStringExtra(
                "add_old_totalPrice");
        Log.i("DishesSelection  onCreate---", "add_old_totalPrice:" + add_old_totalPrice);
        submit_id = this.getIntent().getIntExtra("submit_id", 0);
        submitShoppingOrder = (ShoppingOrder) this.getIntent().getSerializableExtra("submitShoppingOrder");
//		curShoppingOrder=(ShoppingOrder) this.getIntent().getSerializableExtra("curShoppingOrder");

        initView();
        initData(id);
        initListener();
    }

    public void initView() {
        lv_Dishes = (ListView) findViewById(R.id.list);
        submit_list = (ListView) findViewById(R.id.submit_list);
        chk_editBtn = (CheckBox) findViewById(R.id.editbtn);
        btn_orderSettle = (Button) findViewById(R.id.settle);
        edit_fearures = (EditText) findViewById(R.id.requires);
        tv_totalPrice = (TextView) findViewById(R.id.sum);
        dish_total_num = (TextView) findViewById(R.id.dish_selected_top_right_num);
        dd_diskid = (TextView) findViewById(R.id.dd_diskid);
        dd_content = (View) findViewById(R.id.dd_content);
        settle_group = (View) findViewById(R.id.settle_group);
        dish__selected_middle_left = (LinearLayout) findViewById(R.id.dish__selected_middle_left);
        dish_selected_left = (LinearLayout) findViewById(R.id.dish_selected_left);
        dish_selected_top_left = (TextView) findViewById(R.id.dish_selected_top_left);
        dish_selected_top_right = (View) findViewById(R.id.dish_selected_top_right);
    }

    public void initData(int id) {
        if (orderOperateState != Constants.DishesHome_orderOperateState_add) {
            dish__selected_middle_left.setVisibility(View.GONE);
            dish_selected_top_left.setVisibility(View.GONE);
        } else {
            System.out.println("submit_id:" + submit_id);
            dishesSubmitEntity = new DishesEntity(submit_id);
            if (submitShoppingOrder == null) {
                submitShoppingOrder = dishesSubmitEntity.findShoppingOrder();
            }
            submit_sections.clear();
            for (int i = 0; i < submitShoppingOrder.getmHeaderPositions().size(); i++) {
                submit_sections.add(new Section(submitShoppingOrder.getmHeaderPositions().get(i),
                        submitShoppingOrder.getmHeaderNames().get(i)));
            }
            submitAdapter = new DishesSubmitAdapter(this, dishesSubmitEntity);
            submitSimpleSectionedListAdapter = new SimpleSectionedListAdapter(
                    this, submitAdapter, R.layout.dish_selected_lv_item_header,
                    R.id.view_header);
            submitAdapter.setData(submitShoppingOrder.getOrderGoods());
            submitSimpleSectionedListAdapter
                    .setSections(submit_sections.toArray(new Section[0]));
            submit_list.setAdapter(submitSimpleSectionedListAdapter);
            dish_selected_top_left.setText(Html.fromHtml(String.format(mContext.getResources().getString(R.string.dishes_selected_top_title), dishesSubmitEntity.SumOrderGoodsNumByorderid(), add_old_totalPrice)));
        }
        Log.i("LitePal", "id:" + id);
        dishesEntity = new DishesEntity(id);
        dishesEntity.getOrderEntity();
        dd_diskid.setText(deskName);
        if (baseApp.getCurShoppingOrder() != null) {
            Log.i("log","baseApp.getCurShoppingOrder() 1 :"+baseApp.getCurShoppingOrder().getOrderGoods().get(0).getSalesName());
            dishesLVItemView(baseApp.getCurShoppingOrder());
        } else {
//			curShoppingOrder = dishesEntity.findShoppingOrder();
            baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
            Log.i("log","baseApp.getCurShoppingOrder() 2 :"+baseApp.getCurShoppingOrder().getOrderGoods().size());
            dishesLVItemView(baseApp.getCurShoppingOrder());
        }
    }

    public void initListener() {
        dd_content.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dd_content.setFocusable(true);
                edit_fearures.clearFocus();
                return false;
            }
        });
        lv_Dishes.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dd_content.setFocusable(true);
                edit_fearures.clearFocus();
                return false;
            }
        });

        chk_editBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                edit_fearures.clearFocus();
//				curShoppingOrder = dishesEntity.findShoppingOrder();
                baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
                if (isChecked) {
                    isEdit = true;
                    chk_editBtn.setText("完成");
                    chk_editBtn.setTextColor(getApplicationContext()
                            .getResources()
                            .getColor(R.color.dishes_lv_edit_btn));
                    dishesLVEditItem(baseApp.getCurShoppingOrder());
                } else {
                    chk_editBtn.setText("编辑");
                    chk_editBtn.setTextColor(getApplicationContext()
                            .getResources().getColor(R.color.black));
                    dishesLVItemView(baseApp.getCurShoppingOrder());
                }
            }
        });
        dish_selected_top_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    Intent intent = getIntent();
                    Bundle data = new Bundle();
                    data.putSerializable("dishesOrder",
                            dishesEntity.getOrderEntity());
//				data.putSerializable("curShoppingOrder",
//						curShoppingOrder);
                    intent.putExtras(data);
                    setResult(Constants.DishesSelection_resultcode_return, intent);
                }
                finish();
            }
        });
        dish_selected_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    Intent intent = getIntent();
                    Bundle data = new Bundle();
                    data.putSerializable("dishesOrder",
                            dishesEntity.getOrderEntity());
                /*data.putSerializable("curShoppingOrder",
						curShoppingOrder);*/
                    intent.putExtras(data);
                    setResult(Constants.DishesSelection_resultcode_return, intent);
                }
                finish();
            }
        });
        btn_orderSettle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_orderSettle.setFocusable(true);
                edit_fearures.clearFocus();
                if (dishesEntity.SumOrderGoodsNumByorderid() > 0) {
                    AlertDialog();
                } else {
                    showShortTip("您尚未点菜，无法下单提交!");
                }
            }
        });
        edit_fearures.setOnFocusChangeListener(mOnFocusChangeListener);

    }

    /**
     * 菜单展示列表
     *
     * @param shopping
     */
    public void dishesLVItemView(ShoppingOrder shopping) {
        updateTotalPriceShow(shopping.getOrderGoods());
        UpdateShopping_Nums(shopping);
        sections.clear();
        for (int i = 0; i < shopping.getmHeaderPositions().size(); i++) {
            sections.add(new Section(shopping.getmHeaderPositions().get(i),
                    shopping.getmHeaderNames().get(i)));
        }
        if (mAdapter == null) {
            initLvAdapter();
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
        UpdateShopping_Nums(shopping);
        sections.clear();
        for (int i = 0; i < shopping.getmHeaderPositions().size(); i++) {
            sections.add(new Section(shopping.getmHeaderPositions().get(i),
                    shopping.getmHeaderNames().get(i)));
        }
        if (editAdapter == null) {
            initEditLvAdapter();
        }

//		editAdapter.refreshData(shopping.getOrderGoods());
        editAdapter.setData(shopping.getOrderGoods());
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

    /**
     * 更新编辑list菜单列表
     */
    public void updateEditDishesList() {
//		curShoppingOrder = dishesEntity.findShoppingOrder();
        baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
        sections.clear();
        for (int i = 0; i < baseApp.getCurShoppingOrder().getmHeaderPositions().size(); i++) {
            sections.add(new Section(baseApp.getCurShoppingOrder().getmHeaderPositions().get(i),
                    baseApp.getCurShoppingOrder().getmHeaderNames().get(i)));
        }
        editAdapter.setData(baseApp.getCurShoppingOrder().getOrderGoods());
        ;
        editSimpleSectionedListAdapter
                .setSections(sections.toArray(new Section[0]));
        lv_Dishes.setAdapter(editSimpleSectionedListAdapter);
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
        positive.setText("重新提交");
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
        context.setText(info);
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
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.submit_notity_now) {
                    notityKitchen = Constants.notityKitchen_now;
                } else if (checkedId == R.id.submit_notity_wait) {
                    notityKitchen = Constants.notityKitchen_wait;
                }
            }
        });
    }

    @SuppressLint("NewApi")
    private void OKAlertDialog(final DishesOrder dishesOrder) {
        dialog.dismiss();
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.submitdialog);
        Button negative = (Button) window
                .findViewById(R.id.submitdialog_negative);
        negative.setVisibility(View.INVISIBLE);
        Button positive = (Button) window
                .findViewById(R.id.submitdialog_positive);
        positive.setVisibility(View.VISIBLE);
        positive.setText("查看订单");
        RadioGroup group = (RadioGroup) window
                .findViewById(R.id.submit_notity_radioGroup);
        group.setVisibility(View.GONE);
        positive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = getIntent();
                Bundle data = new Bundle();
                data.putSerializable("dishesOrder", dishesOrder);
                intent.putExtras(data);
                setResult(Constants.DishesSelection_resultcode_check, intent);
                finish();
            }
        });
        TextView context = (TextView) window
                .findViewById(R.id.submitdialog_content);
        context.setText(Html.fromHtml(String
                .format("<font color=\"#db2023\">下单成功!</font>")) + "正在准备中···");
        ImageView submitdialog_icon = (ImageView) window
                .findViewById(R.id.submitdialog_icon);
        submitdialog_icon.setBackground(mContext.getResources().getDrawable(
                R.drawable.submitdialog_sucess));
        ImageButton close = (ImageButton) window
                .findViewById(R.id.submitdialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setResult(Constants.DishesSelection_resultcode_back);
                finish();
            }
        });

    }


    /**
     * 服务器无响应异常或者订单状态异常，退出操作
     */
    @SuppressLint("NewApi")
    private void unkownAlertDialog(String errorMsg) {
        dialog.dismiss();
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.submitdialog);
        Button negative = (Button) window
                .findViewById(R.id.submitdialog_negative);
        negative.setVisibility(View.INVISIBLE);
        Button positive = (Button) window
                .findViewById(R.id.submitdialog_positive);
        positive.setVisibility(View.VISIBLE);
        positive.setText("确定");
        RadioGroup group = (RadioGroup) window
                .findViewById(R.id.submit_notity_radioGroup);
        group.setVisibility(View.GONE);
        positive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setResult(Constants.DishesSelection_resultcode_back);
                finish();
            }
        });
        TextView context = (TextView) window
                .findViewById(R.id.submitdialog_content);
        context.setText(Html.fromHtml(String
                .format("<font color=\"#db2023\">"+errorMsg+"!</font>")));
        ImageView submitdialog_icon = (ImageView) window
                .findViewById(R.id.submitdialog_icon);
        submitdialog_icon.setBackground(mContext.getResources().getDrawable(
                R.drawable.submitdialog_sucess));
        ImageButton close = (ImageButton) window
                .findViewById(R.id.submitdialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setResult(Constants.DishesSelection_resultcode_back);
                finish();
            }
        });

    }

    private void loadingAlertDialog() {
        dialog.dismiss();
        // showLoadingActivity();
        showLoadingDialog(new DialogDelayListener() {

            @Override
            public void onexecute() {
//				VolleyEditOrderSubmit();
                switch (orderOperateState) {
                    case Constants.DishesHome_orderOperateState_open:
                        notityKitchen = Constants.notityKitchen_wait;
                        VolleysubmitOrderInfo();
                        break;
                    case Constants.DishesHome_orderOperateState_add:
                        notityKitchen = Constants.notityKitchen_now;
                        VolleyupdateOrderInfo();
                        break;
                    case Constants.DishesHome_orderOperateState_edit:
                        notityKitchen = Constants.notityKitchen_wait;
                        VolleyUpdateKeepOrderInfo();
                        break;
                    default:
                        break;
                }
            }
        }, 300);
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
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.submit_notity_now) {
                    notityKitchen = Constants.notityKitchen_now;
                } else if (checkedId == R.id.submit_notity_wait) {
                    notityKitchen = Constants.notityKitchen_wait;
                }
            }
        });
        switch (orderOperateState) {
            case Constants.DishesHome_orderOperateState_open:
                group.setVisibility(View.GONE);
                break;
            case Constants.DishesHome_orderOperateState_add:
                group.setVisibility(View.GONE);
                break;
            case Constants.DishesHome_orderOperateState_edit:
                group.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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
        context.setText("您已完成点菜，确认菜单吗?");
        ImageButton close = (ImageButton) window
                .findViewById(R.id.submitdialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 更新购物车价钱
     */
    void UpdateShopping_Nums(ShoppingOrder shopping) {
        int totalnum = 0;
        for (OrderGoods orderGoods : shopping.getOrderGoods()) {
            if (!orderGoods.isCompDish()) totalnum += orderGoods.getSalesNum();
        }
        dish_total_num.setText("新选" + totalnum + "道菜");

    }

    /**
     * 计算总价
     *
     * @param dishesList
     */
    public long sumPrice(ArrayList<OrderGoods> dishesList) {
        long total = 0;
        if (dishesList != null && dishesList.size() > 0) {
            for (int i = 0; i < dishesList.size(); i++) {
                OrderGoods dm = dishesList.get(i);
                total = total + dm.getSalesNum() * dm.getDishesPrice();
            }
        }
        return total;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    /**
     * 初始化普通订单列表适配器
     */
    void initLvAdapter() {
        mAdapter = new DishesSelectionAdapter(this,
                DishesSelectionAdapter.DISHES_LV_TYPE_VIEW, dishesEntity);
        mSimpleSectionedListAdapter = new SimpleSectionedListAdapter(
                this, mAdapter, R.layout.dish_selected_lv_item_header,
                R.id.view_header);
    }

    /**
     * 初始化可编辑订单列表适配器
     */
    void initEditLvAdapter() {

        editAdapter = new DishesSelectionAdapter(this,
                DishesSelectionAdapter.DISHES_LV_TYPE_EDIT, dishesEntity);
        editAdapter.setOnDishItemClickListener(new OnDishItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int clickType) {
                OrderGoods ordergood = (OrderGoods) v.getTag(R.id.tag_first);
                TextView count = (TextView) v.getTag(R.id.tag_second);
                switch (clickType) {
                    case OnDishItemClickListener.ClickType_DELETE:
                        dishesEntity.decreaseAllOrderGoods(ordergood);
                        dishesEntity.delRemarkItemList(ordergood.getSalesId() + "",ordergood.getInstanceId());
                        if (ordergood.getIsComp().equals("1"))
                            dishesEntity.getCompDishesEntity().decreaseCompOrderGoods(ordergood);
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
                            dishesEntity.delRemarkItemList(ordergood.getSalesId() + "",ordergood.getInstanceId());
                            if (ordergood.getIsComp().equals("1"))
                                dishesEntity.getCompDishesEntity().decreaseCompOrderGoods(ordergood);
                            updateEditDishesList();
                        }
                        break;
                    default:
                        break;
                }
//				curShoppingOrder = dishesEntity.findShoppingOrder();
                baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
                updateTotalPriceShow(baseApp.getCurShoppingOrder().getOrderGoods());
                UpdateShopping_Nums(baseApp.getCurShoppingOrder());
            }
        });

        editSimpleSectionedListAdapter = new SimpleSectionedListAdapter(
                DishesSelectionActivity.this, editAdapter,
                R.layout.dish_selected_lv_item_edit_header, R.id.edit_header);
    }


    /**
     * 向服务器新增订单，开桌
     */
    public void VolleysubmitOrderInfo() {
        String param = "/appController/submitOrderInfo.do?";
        System.out.println("submitOrderInfo:" + Constants.address + param);
        ResultMapRequest<SubmitOrderId> ResultMapRequest = new ResultMapRequest<SubmitOrderId>(
                Method.POST, Constants.address + param, SubmitOrderId.class,
                new Response.Listener<SubmitOrderId>() {
                    @Override
                    public void onResponse(
                            SubmitOrderId response) {
                        try {
                            if (response.getState() == 1) {
                                dishesEntity.upDishesOrder(response.getOrderId());
                                dismissLoadingDialog();
                                OKAlertDialog(dishesEntity.getOrderEntity());
                                baseApp.setCurShoppingOrder(null);
                            }
                            else if(response.getState()==0) {
                                dismissLoadingDialog();
                                repeatAlertDialog(response.getError());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrors errors=VolleyErrorHelper.getVolleyErrors(error,
                        mContext);
                switch (errors.getErrorType()){
                    case 1:
                        dismissLoadingDialog();
                        Log.e(VolleyLogTag,
                                "VolleyError:" + errors.getErrorMsg(), error);
                        unkownAlertDialog(errors.getErrorMsg());
                        break;
                    default:
                        dismissLoadingDialog();
                        Log.e(VolleyLogTag,
                                "VolleyError:" + errors.getErrorMsg(), error);
                        repeatAlertDialog(errors.getErrorMsg());
                        break;
                }
                // dismissLoadingActivity();
                /*dismissLoadingDialog();
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                repeatAlertDialog(VolleyErrorHelper.getMessage(error,
                        mContext));*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                Gson gson = new Gson();
                DishesOrder order = dishesEntity.getOrderEntity();
                order.setRemark(edit_fearures.getEditableText().toString());
                ServerOrder serverOrder = baseUtils
                        .DishesOrderToServerOrder(order);
                String createTime = baseUtils.getCurrentData();
                serverOrder.setTradeStsffId(baseApp.getLoginInfo().getStaffId());
                serverOrder.setCreateTime(createTime);
                serverOrder.setMerchantId(Long.valueOf(baseApp.getLoginInfo().getMerchantId()));
                serverOrder.setOriginalPrice(Long.valueOf(tv_totalPrice.getText()
                        .toString()));
                for (ServerOrderGoods good : serverOrder.getOrderGoods()) {
                    if (notityKitchen.equals(Constants.notityKitchen_now)) {
                        good.setSalesState(Constants.notityKitchen_now);
                    } else if (notityKitchen
                            .equals(Constants.notityKitchen_wait)) {
                        good.setSalesState(Constants.notityKitchen_wait);
                    }
                    good.setAction(1);
                    good.setTradeStaffId(baseApp.getLoginInfo().getStaffId());
                    good.setDeskId(dishesEntity.getOrderEntity().getDeskId());
                    good.setOrderId(serverOrder.getOrderId());
                    ArrayList<RemarkItem> kk = dishesEntity.findRemarkItemList(good);
                    ArrayList<String> oo = new ArrayList<String>();
                    for (int i = 0; i < kk.size(); i++) {
                        oo.add(kk.get(i).getItemName());
                    }
                    good.setSalesPrice(good.getDishesPrice()*good.getSalesNum());
                    good.setRemark(oo);
                    dishesEntity.persistServerOrderGoods(good);
                }
                if (notityKitchen.equals(Constants.notityKitchen_now)) {
                    serverOrder.setOrderState("0");
                } else if (notityKitchen
                        .equals(Constants.notityKitchen_wait)) {
                    serverOrder.setOrderState("B");
                }
                dishesEntity.persistServerOrder(serverOrder);
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
        ResultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                1.0f));
        requestQueue.add(ResultMapRequest);
    }


    /**
     * 修改订单，加菜
     */
    public void VolleyupdateOrderInfo() {
        String param = "/appController/updateOrderInfo.do?";
        System.out.println("submitOrderInfo:" + Constants.address + param);
        ResultMapRequest<UpdateOrderInfoResultData> ResultMapRequest = new ResultMapRequest<UpdateOrderInfoResultData>(
                Method.POST, Constants.address + param, UpdateOrderInfoResultData.class,
                new Response.Listener<UpdateOrderInfoResultData>() {
                        @Override
                    public void onResponse(
                            UpdateOrderInfoResultData response) {
                        if(response.getState()==1) try {
                            DishesOrder dishesorder = dishesEntity.addDishesOrder(dishesSubmitEntity.getOrderEntity(), notityKitchen);
                            dismissLoadingDialog();
                            OKAlertDialog(dishesorder);
                            baseApp.setCurShoppingOrder(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        else if(response.getState()==0) {
                            dismissLoadingDialog();
                            repeatAlertDialog(response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrors errors=VolleyErrorHelper.getVolleyErrors(error,
                        mContext);
                switch (errors.getErrorType()){
                    case 1:
                        dismissLoadingDialog();
                        Log.e(VolleyLogTag,
                                "VolleyError:" + errors.getErrorMsg(), error);
                        unkownAlertDialog(errors.getErrorMsg());
                        break;
                    default:
                        dismissLoadingDialog();
                        Log.e(VolleyLogTag,
                                "VolleyError:" + errors.getErrorMsg(), error);
                        repeatAlertDialog(errors.getErrorMsg());
                        break;
                }
                // dismissLoadingActivity();
                /*dismissLoadingDialog();
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                repeatAlertDialog(VolleyErrorHelper.getMessage(error,
                        mContext));*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                Gson gson = new Gson();
                DishesOrder order = dishesEntity.getOrderEntity();
                order.setRemark(edit_fearures.getEditableText().toString());
                ServerOrder serverOrder = baseUtils
                        .DishesOrderToServerOrder(order);
                String createTime = baseUtils.getCurrentData();
                serverOrder.setTradeStsffId(baseApp.getLoginInfo().getStaffId());
                serverOrder.setCreateTime(createTime);
                serverOrder.setMerchantId(Long.valueOf(baseApp.getLoginInfo().getMerchantId()));
                System.out.println("add_old_totalPrice:" + add_old_totalPrice);
                if (add_old_totalPrice != null)
                    serverOrder.setOriginalPrice(Long.valueOf(tv_totalPrice.getText()
                            .toString())
                            + Long.valueOf(add_old_totalPrice));
                else
                    serverOrder.setOriginalPrice(Long.valueOf(tv_totalPrice.getText()
                            .toString()));
                for (ServerOrderGoods good : serverOrder.getOrderGoods()) {
                    if (notityKitchen.equals(Constants.notityKitchen_now)) {
                        good.setSalesState(Constants.notityKitchen_now);
                    } else if (notityKitchen
                            .equals(Constants.notityKitchen_wait)) {
                        good.setSalesState(Constants.notityKitchen_wait);
                    }
                    good.setAction(1);
                    good.setTradeStaffId(baseApp.getLoginInfo().getStaffId());
                    good.setOrderId(serverOrder.getOrderId());
                    good.setDeskId(dishesEntity.getOrderEntity().getDeskId());
                    ArrayList<RemarkItem> kk = dishesEntity.findRemarkItemList(good);
                    ArrayList<String> oo = new ArrayList<String>();
                    for (int i = 0; i < kk.size(); i++) {
                        oo.add(kk.get(i).getItemName());
                    }
                    good.setRemark(oo);
                    good.setSalesPrice(good.getDishesPrice()*good.getSalesNum());
                    dishesEntity.persistServerOrderGoods(good);
                }
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
        ResultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                1.0f));
        requestQueue.add(ResultMapRequest);
    }

    /**
     * 修改订单，编辑
     */
    public void VolleyUpdateKeepOrderInfo() {
        String param = "/appController/updateKeepOrderInfo.do?";
        System.out.println("submitOrderInfo:" + Constants.address + param);
        ResultMapRequest<UpdateOrderInfoResultData> ResultMapRequest = new ResultMapRequest<UpdateOrderInfoResultData>(
                Method.POST, Constants.address + param, UpdateOrderInfoResultData.class,
                new Response.Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            UpdateOrderInfoResultData response) {
                        try {
                            if (response.getState()==1) {
//									dishesEntity.upDishesOrder(response.getOrderId());
                                dismissLoadingDialog();
                                Log.i("DishesEntity(DishesOrder)", "dishesEntity.getOrderEntity().getOriginalPrice():" + dishesEntity.getOrderEntity().getOriginalPrice());

                                OKAlertDialog(dishesEntity.getOrderEntity());
                                baseApp.setCurShoppingOrder(null);
                            } else if(response.getState()==0) {
                                dismissLoadingDialog();
                                repeatAlertDialog(response.getError());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrors errors=VolleyErrorHelper.getVolleyErrors(error,
                        mContext);
                switch (errors.getErrorType()){
                    case 1:
                        dismissLoadingDialog();
                        Log.e(VolleyLogTag,
                                "VolleyError:" + errors.getErrorMsg(), error);
                        unkownAlertDialog(errors.getErrorMsg());
                        break;
                    default:
                        dismissLoadingDialog();
                        Log.e(VolleyLogTag,
                                "VolleyError:" + errors.getErrorMsg(), error);
                        repeatAlertDialog(errors.getErrorMsg());
                        break;
                }
                // dismissLoadingActivity();
                /*dismissLoadingDialog();
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                repeatAlertDialog(VolleyErrorHelper.getMessage(error,
                        mContext));*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                Gson gson = new Gson();
                DishesOrder order = dishesEntity.getOrderEntity();
                order.setRemark(edit_fearures.getEditableText().toString());
                ServerOrder serverOrder = baseUtils
                        .DishesOrderToServerOrder(order);
                String createTime = baseUtils.getCurrentData();
                serverOrder.setTradeStsffId(baseApp.getLoginInfo().getStaffId());
                serverOrder.setCreateTime(createTime);
                serverOrder.setMerchantId(Long.valueOf(baseApp.getLoginInfo().getMerchantId()));
                serverOrder.setOriginalPrice(Long.valueOf(tv_totalPrice.getText()
                        .toString()));
                for (ServerOrderGoods good : serverOrder.getOrderGoods()) {
                    good.setSalesState(Constants.notityKitchen_wait);
                    good.setAction(1);
                    good.setTradeStaffId(baseApp.getLoginInfo().getStaffId());
                    good.setDeskId(dishesEntity.getOrderEntity().getDeskId());
                    good.setOrderId(serverOrder.getOrderId());
                    ArrayList<RemarkItem> kk = dishesEntity.findRemarkItemList(good);
                    ArrayList<String> oo = new ArrayList<String>();
                    for (int i = 0; i < kk.size(); i++) {
                        oo.add(kk.get(i).getItemName());
                    }
                    String remarkold=good.getOldremark();
                    if(remarkold!=null&&!remarkold.equals("")&&!remarkold.equals("[]")){
                        String remark=remarkold.replace("[","").replace("]","");
                    oo.add(remark);
                    }
                    Log.i("i",good.getInstanceId()+":"+good.getRemark());
                    good.setRemark(oo);
                    good.setSalesPrice(good.getDishesPrice()*good.getSalesNum());
                    dishesEntity.persistServerOrderGoods(good);
                }
                if (notityKitchen.equals(Constants.notityKitchen_now)) {
                    serverOrder.setOrderState("0");
                } else if (notityKitchen
                        .equals(Constants.notityKitchen_wait)) {
                    serverOrder.setOrderState("B");
                }
                dishesEntity.persistServerOrder(serverOrder);
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
        ResultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                1.0f));
        requestQueue.add(ResultMapRequest);
    }
}
