package com.asiainfo.orderdishes.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.adapter.NumTipsAdapter;
import com.asiainfo.orderdishes.entity.CompPopupHolder;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.PopupHolder;
import com.asiainfo.orderdishes.entity.ShoppingOrder;
import com.asiainfo.orderdishes.entity.eventbus.ChildEventBusData;
import com.asiainfo.orderdishes.entity.eventbus.CompAndDishesInfoData;
import com.asiainfo.orderdishes.entity.eventbus.Event;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.DishesType;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;
import com.asiainfo.orderdishes.entity.litepal.MerchantDesk;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.volley.ItemPopupHolder;
import com.asiainfo.orderdishes.entity.volley.ResultMap;
import com.asiainfo.orderdishes.entity.volley.queryAllDishesByMerchantIdData;
import com.asiainfo.orderdishes.helper.DialogDelayListener;
import com.asiainfo.orderdishes.helper.OnItemClickListener;
import com.asiainfo.orderdishes.helper.OnScreenChangeListener;
import com.asiainfo.orderdishes.helper.OnSlidCheckedChangeListener;
import com.asiainfo.orderdishes.helper.OnSlideItemClickListener;
import com.asiainfo.orderdishes.helper.PwdEncryptor;
import com.asiainfo.orderdishes.helper.SharedPreferencesUtils;
import com.asiainfo.orderdishes.http.volley.ResultMapRequest;
import com.asiainfo.orderdishes.http.volley.VolleyErrorHelper;
import com.asiainfo.orderdishes.ui.base.AnimatedDoorActivity;
import com.asiainfo.orderdishes.ui.widget.SlideSwitcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.viewbadger.BadgeView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Home界面，点餐主页
 *
 * @author gjr 2014-3-6
 */
public class DishesHomeActivity extends AnimatedDoorActivity {
    /**
     * 自定义滑动分屏控件
     */
    private SlideSwitcher switcher;
    /**
     * 分屏联动提示图标
     */
    private GridView numTips;
    /**
     * 分屏联动提示图标视图适配器
     */
    private NumTipsAdapter numIcon;
    /**
     * 菜品分类组
     */
    private RadioGroup catagoriesContainer;
    /**
     * 商户用户名
     */
    private String username;
    /**
     * 存放菜品选中数目的提示角标 (由于角标与RadioButton存在时间冲突BUG，暂时用TextView透明覆盖实现)
     */
    private LinearLayout catagoriesTips;
    /**
     * 实时更新头部菜单的Index
     */
    private int catagoriesIndex;
    /**
     * 菜品详情弹窗
     */
    private static PopupWindow dishesPopup;
    /**
     * 业务实体类，处理点餐相关业务逻辑
     */
    private DishesEntity dishesEntity;
    /**
     * 创建一个公有popup弹窗视图，每次点击时取视图，更新信息复用
     */
    private PopupHolder popupHolder;
    private CompPopupHolder compPopupHolder;
    private ItemPopupHolder itemPopupHolder;
    /**
     * 购物车组视图
     */
    private View home_top_shopping_group;
    /**
     * 已点菜品购物车查看视图
     */
    private TextView tv_selectedBtn;
    /**
     * 返回大厅
     */
    private TextView home_top_home_back;
    /**
     * 已点菜品购物车
     */
    private ImageView shopping;
    /**
     * 已点菜品购物车数量
     */
    private BadgeView shopping_Num;

    /**
     * 大厅选择的桌子信息
     */
    private static MerchantDesk selectedDesk;
    /**
     * 对本订单的操作类型，1开桌，2加菜，3编辑
     */
    private int orderOperateState, submit_id;
    /**
     * 加菜时订单id
     */
    private String orderId;
    /**
     * 加菜时传入原来有的价格，提交是结算加入
     */
    private String add_old_totalPrice;
    /**
     * 当前页面展示菜品数据缓存列表，进行一些回调数据验证
     */
    private ArrayList<DishesInfo> curData;

    private View home_dish;
    /**
     * 加菜时的旧订单
     */
    private DishesOrder dishesOrder;
    //	private ShoppingOrder curShoppingOrder;
    private ShoppingOrder submitShoppingOrder;
    private ArrayList<Integer> shoppingIndex = new ArrayList<Integer>();
    private String people_number;

    @Override
    protected int layoutResId() {
        return R.layout.activity_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        //统一获取初始化传递数据
        people_number=getIntent().getStringExtra("people_number");
        selectedDesk = (MerchantDesk) getIntent().getSerializableExtra(
                "hall_selected_desk");
        Log.i("DishesHome  onCreate---", "selectedDesk:" + selectedDesk);
        System.out.println("baseApp:" + baseApp);
        System.out.println("baseApp.getLoginInfo():" + baseApp.getLoginInfo());
//		username = baseApp.getLoginInfo().getChildMerchantId();
//        baseApp=null;
        username = baseApp.getLoginInfo() == null ? "0" : baseApp.getLoginInfo().getChildMerchantId();
        Log.i("DishesHome  onCreate---", "username:" + username);
        add_old_totalPrice = this.getIntent().getStringExtra("totalprice");
        Log.i("DishesHome  onCreate---", "add_old_totalPrice:" + add_old_totalPrice);
        orderId = this.getIntent().getStringExtra("order_id");
        Log.i("DishesHome  onCreate---", "orderId:" + orderId);
        dishesOrder = (DishesOrder) this.getIntent().getSerializableExtra("dishesOrder");
        Log.i("DishesHome  onCreate---", "dishesOrder:" + dishesOrder);
        String diskid = this.getIntent().getStringExtra("diskid");
        Log.i("DishesHome  onCreate---", "diskid:" + diskid);
        submit_id = this.getIntent().getIntExtra("submit_id", 0);
        submitShoppingOrder = (ShoppingOrder) this.getIntent().getSerializableExtra("curShoppingOrder");
        //统一判断初始化传递数据
        if (orderId != null) {
            //先判断Orderid是否为空，不为空则是加菜，加菜对应的orderid和dishesOrder展示原订单信息都不能缺
            orderOperateState = Constants.DishesHome_orderOperateState_add;
            DishesOrder add_dishesOrder = (DishesOrder) this.getIntent().getSerializableExtra("add_dishesOrder");
            dishesEntity = new DishesEntity(username,orderId,add_dishesOrder);
        } else if (dishesOrder != null) {
            System.out.println("dishesOrder:" + dishesOrder);
            //orderid为空，直接传递dishesOrder过来的，表示对本条进行编辑修改
            orderOperateState = Constants.DishesHome_orderOperateState_edit;
            dishesEntity = new DishesEntity(dishesOrder, true);
        } else if (selectedDesk != null) {
            //当orderid和dishesOrder都没有的情况下，有桌子信息，表示新开桌下单
            orderOperateState = Constants.DishesHome_orderOperateState_open;
            if(people_number!=null)
            dishesEntity = new DishesEntity(username, selectedDesk.getDeskId(),Integer.valueOf(people_number));
            else
            dishesEntity = new DishesEntity(username, selectedDesk.getDeskId(),1);
        } else {
            orderOperateState = Constants.DishesHome_orderOperateState_open;
            dishesEntity = new DishesEntity(username);
        }
        mActivity = this;
        initView();
        initData();
        showDelay(new DialogDelayListener() {

            @Override
            public void onexecute() {
                Event<String> event = new Event<String>();
                event.setData("NotifyInitData");
                event.setType(1);
                EventBus.getDefault().post(event);
            }
        }, 220);

    }

    public void onEventMainThread(Event event) {
        switch (event.getType()) {
            case 1:
                Log.i("isLoadDishes", "baseApp.isLoadDishes():" + baseApp.isLoadDishes());
                if (baseApp.isLoadDishes()) {
                    showLoadingDialog(new DialogDelayListener() {

                        @Override
                        public void onexecute() {
                            VolleyQueryAllDishes();
                        }
                    }, 300);
                } else {
                    initCatagoriesData(true);
                }
                break;
            case 2:
                dismissLoadingDialog();
                CompAndDishesInfoData dd = (CompAndDishesInfoData) event.getData();
                View comp = compPopupHolder.get(dd.getDishesInfo(), dd.getCerchantCompDishesInfo(), catagoriesIndex);
                dishesPopup = (PopupWindow) comp.getTag();
                if (!dishesPopup.isShowing()) {
                    dishesPopup.showAtLocation(comp, Gravity.CENTER, 0, 0);
                }
                break;
            case 3:
                dismissLoadingDialog();
                initCatagoriesData(false);
                baseApp.setLoadDishes(false);
                break;
            case 4:
                System.err.println("套餐没有本地缓存");
                CompAndDishesInfoData compAndDishesInfoData = (CompAndDishesInfoData) event.getData();
                VolleyQueryComboInfoForApp(compAndDishesInfoData.getDishesInfo());
                break;
            default:
                break;
        }
    }

    public void onEventBackgroundThread(ChildEventBusData event) {
        switch (event.getType()) {
            case 1:
//			event.getDishesEntity().getCompDishesEntity().saveCompDishes(event.getBaseUtils());
                Event<CompAndDishesInfoData> eventComp = new Event<CompAndDishesInfoData>();
                MerchantCompDishesInfo dd = event.getDishesEntity().getCompDishesEntity().getCompDishes(event.getDishesInfo().getDishesId(), baseApp.getLoginInfo().getChildMerchantId());
                System.out.println("dd:" + dd);
                if (dd != null) {
                    CompAndDishesInfoData compAndDishesInfoData = new CompAndDishesInfoData();
                    compAndDishesInfoData.setCerchantCompDishesInfo(dd);
                    dd.setDishesId(Long.valueOf(event.getDishesInfo().getDishesId()));
                    dd.setDishesName(event.getDishesInfo().getDishesName());
                    dd.setDishesPrice(event.getDishesInfo().getDishesPrice());
                    dd.setDishesTypeCode(event.getDishesInfo().getDishesTypeCode());
                    dd.setDishesTypeName(event.getDishesInfo().getDishesTypeName());
                    dd.setDishesType(event.getDishesInfo().getDishesType());
                    dd.setDishesUrl(event.getDishesInfo().getDishesUrl());
                    dd.setIsComp(event.getDishesInfo().getIsComp());
                    dd.setExportId(event.getDishesInfo().getExportId());
                    dd.setMemberPrice(event.getDishesInfo().getMemberPrice());
                    compAndDishesInfoData.setDishesInfo(event.getDishesInfo());
                    eventComp.setData(compAndDishesInfoData);
                    eventComp.setType(2);
                    EventBus.getDefault().post(eventComp);
                } else {
                    Event<CompAndDishesInfoData> eventinfo = new Event<CompAndDishesInfoData>();
                    CompAndDishesInfoData ss = new CompAndDishesInfoData();
                    ss.setDishesInfo(event.getDishesInfo());
                    eventinfo.setData(ss);
                    eventinfo.setType(4);
                    EventBus.getDefault().post(eventinfo);
                }
                break;
            case 2:
                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext);
                event.getDishesEntity().updateDb(event.getInfo(),
                        event.getDishes());
                Event<String> msg = new Event<String>();
                msg.setType(3);
                msg.setData("更新到菜品，刷新页面!");
                sharedPreferencesUtils.refreshDishesLatestDate();
                EventBus.getDefault().post(msg);
                break;
            case 3:
                event.getDishesEntity().getCompDishesEntity().saveMerchantCompDishesInfo(event.getMerchantCompDishesInfo());
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

    /**
     * 初始化视图
     */
    private void initView() {
        switcher = (SlideSwitcher) findViewById(R.id.slide_view);
        numTips = (GridView) findViewById(R.id.delivery_left_dishes_gridview);
        catagoriesContainer = (RadioGroup) findViewById(R.id.home_top_catagories_container);
        catagoriesTips = (LinearLayout) findViewById(R.id.home_top_catagories_linear);
        tv_selectedBtn = (TextView) findViewById(R.id.home_top_shopping_tip);
        shopping = (ImageView) findViewById(R.id.home_top_shopping_img_num);
        shopping.setImageDrawable(getApplicationContext().getResources()
                .getDrawable(R.drawable.shopping));
        TextView home_top_shopping_price = (TextView) findViewById(R.id.home_top_shopping_price);
        shopping_Num = new BadgeView(mActivity, home_top_shopping_price);
        home_top_shopping_group = findViewById(R.id.home_top_shopping_group);
        home_top_home_back = (TextView) findViewById(R.id.home_top_home_back);
        home_dish = findViewById(R.id.home_dish);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        baseApp.setDishesHomeTeachState(true);
        numIcon = new NumTipsAdapter(getApplicationContext());
        popupHolder = new PopupHolder(mActivity, dishesEntity,
                baseApp);
        compPopupHolder = new CompPopupHolder(mActivity, dishesEntity,
                baseApp);
        itemPopupHolder = new ItemPopupHolder(mActivity, dishesEntity,
                baseApp);
        switcher.setDishesEntity(dishesEntity,
                baseApp);
        initShoppingBadberView();
        initListener();
        home_dish.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.dishesmenu));
//		initCatagoriesData(true);
//		VolleyQueryAllDishes();
    }

    void initShoppingBadberView() {
        shopping_Num.setBadgeBackgroundColor(Color.RED);
        shopping_Num.setTextColor(Color.WHITE);
        shopping_Num.setTextSize(16);
        shopping_Num.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        shopping_Num.show();

        if (orderOperateState == Constants.DishesHome_orderOperateState_edit) {
            baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
//			curShoppingOrder=dishesEntity.findShoppingOrder();
            tv_selectedBtn.setText(sumPrice(baseApp.getCurShoppingOrder().getOrderGoods()) + "元");
            int totalnum = 0;
            for (OrderGoods orderGoods : baseApp.getCurShoppingOrder().getOrderGoods()) {
                if (!orderGoods.isCompDish()) totalnum += orderGoods.getSalesNum();
            }
            shopping_Num.setText(totalnum + "");
        } else {
            tv_selectedBtn.setText("0 元");
            shopping_Num.setText("0");
        }
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        /**
         * 分屏菜单的点击事件
         */
        switcher.setOnSlideItemClickListener(new OnSlideItemClickListener() {

            @Override
            public void onItemClick(View view, int screemIndex, int position,
                                    int numberInOneScreen) {
                if (baseApp.isDishesHomeTeachState()) {
                    baseApp.setDishesHomeTeachState(false);
                    View ItemView = (View) view.getTag(R.id.tag_second);
                    int index = screemIndex * numberInOneScreen + position;
                    View tvTip = catagoriesTips.getChildAt(catagoriesIndex);
                    BadgeView badge = (BadgeView) tvTip.getTag(R.id.tag_second);
                    DishesInfo dishesInfo = (DishesInfo) ItemView.getTag(R.id.tag_second);
                    int typeSum = dishesEntity.SumOrderGoodsNumByIndex(dishesInfo);
                    Log.i("LitePal", "typeSum:" + typeSum);
                    if (typeSum > 0) {
                        badge.setVisibility(View.VISIBLE);
                        badge.setText("" + typeSum);
                    } else {
                        badge.setText("" + 0);
                        badge.setVisibility(View.INVISIBLE);
                    }

                    if (dishesInfo.getIsComp().equals("1")){
                        ChildEventBusData childEventBusData = new ChildEventBusData();
                        childEventBusData.setType(1);
                        childEventBusData.setBaseUtils(baseUtils);
                        childEventBusData.setDishesEntity(dishesEntity);
                        childEventBusData.setDishesInfo(dishesInfo);
                        EventBus.getDefault().post(childEventBusData);
                        showLoadingDialog(new DialogDelayListener() {
                            @Override
                            public void onexecute() {
                            }
                        }, 1);
                    }  else if(dishesInfo.getDishesItemList()!=null&&dishesInfo.getDishesItemList().size()>=1)
                    {
                        View v = itemPopupHolder.get(dishesInfo, badge, ItemView, shopping_Num, curData, tv_selectedBtn);
                        dishesPopup = (PopupWindow) v.getTag();
                        if (!dishesPopup.isShowing()) {
                            dishesPopup.showAtLocation(v, Gravity.CENTER, 0, 0);
                        }
                    }
                    else{
                        View v = popupHolder.get(dishesInfo, badge, ItemView, shopping_Num, curData, tv_selectedBtn);
                        dishesPopup = (PopupWindow) v.getTag();
                        if (!dishesPopup.isShowing()) {
                            dishesPopup.showAtLocation(v, Gravity.CENTER, 0, 0);
                        }
                    }

                } else {
                    System.out.println("触摸事件冲突，以忽视");
                }
            }
        });
        /**
         * 分屏提示图标的点击监听
         */
        numIcon.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (baseApp.isDishesHomeTeachState()) {
                    baseApp.setDishesHomeTeachState(false);
                    int result = switcher.showScreen(position);
                    if (result == -1) {
                        baseApp.setDishesHomeTeachState(true);
                        showShortTip("跳转失败!");
                    } else if (result == 0) {
                        baseApp.setDishesHomeTeachState(true);
                        showShortTip("就在当前页面!");
                    }
                } else {
                    System.out.println("触摸事件冲突，以忽视");
                }
            }
        });
        /**
         * 同类才分屏切换监听器
         */
        switcher.setOnScreenChangeListener(new OnScreenChangeListener() {
            @Override
            public void onItemClick(int curIndex, int nextIndex,
                                    int screenNumber) {
                numIcon.setSelectedPos(nextIndex);
            }
        });
        switcher.setOnCheckedChangeListener(new OnSlidCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked, DishesInfo dishesinfo) {
                UpdateShopping_Nums();
                UpdateTvTips(catagoriesIndex);
                if (!isChecked) dishesEntity.delDishesInfoItem(dishesinfo);
            }
        });

        /**
         * 点击一点菜查看按钮，进入点菜详情页面
         */
        home_top_shopping_group.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (baseApp.isDishesHomeTeachState()) {
                    baseApp.setDishesHomeTeachState(false);
                    Intent intent = new Intent(mActivity,
                            DishesSelectionActivity.class);
                    intent.putExtra("order_id", dishesEntity.getOrderEntity()
                            .getId());
                    intent.putExtra("submit_id",submit_id);
                    Log.i("log","id:"+ dishesEntity.getOrderEntity()
                            .getId());
                    Log.i("log","submit_id:"+submit_id);
                    intent.putExtra("add_old_totalPrice", add_old_totalPrice);
                    intent.putExtra("deskName", selectedDesk.getDeskName());
                    System.out.println("intent.putExtra add_old_totalPrice:" + add_old_totalPrice);
                    intent.putExtra("orderOperateState", orderOperateState);
                    Bundle ShoppingOrder = new Bundle();
//				ShoppingOrder.putSerializable("curShoppingOrder",baseApp.getCurShoppingOrder());
                    ShoppingOrder.putSerializable("submitShoppingOrder", submitShoppingOrder);
                    intent.putExtras(ShoppingOrder);
                    for (int i = 0; i < catagoriesTips.getChildCount(); i++) {
                        View tips = catagoriesTips.getChildAt(i);
                        if (tips.getVisibility() == View.VISIBLE) {
                            int index = (Integer) tips.getTag(R.id.tag_first);
                            shoppingIndex.add(index);
                        }
                    }
                    startActivityForResult(intent, Constants.DishesHome_requestcode_dishesselect);
                } else {
                    System.out.println("触摸冲突，已忽视");
                }
            }
        });
        home_top_home_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				finish();
//				backFinish();
                WaitterAuthenticate();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.DishesHome_requestcode_dishesselect:
                switch (resultCode) {
                    case Constants.DishesSelection_resultcode_return:
//				curShoppingOrder=(ShoppingOrder) data.getSerializableExtra("curShoppingOrder");
                /*ShoppingOrder shopping=(ShoppingOrder) data.getSerializableExtra("curShoppingOrder");
				baseApp.setCurShoppingOrder(shopping);*/
                        refreshCurrentOrderData();
                        break;
                    case Constants.DishesSelection_resultcode_back:
                        this.finish();
                        break;
                    case Constants.DishesSelection_resultcode_check:
                        DishesOrder dishesOrder = (DishesOrder) data.getSerializableExtra("dishesOrder");
                        this.finish();
                        Intent intent = new Intent(mContext, DiskOrderActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle dishesdata = new Bundle();
                        dishesdata.putSerializable("dishesOrder", dishesOrder);
                        dishesdata.putSerializable("hall_selected_desk", selectedDesk);
                        intent.putExtras(dishesdata);
                        startActivity(intent);
//				 EventBus.getDefault().post(dishesOrder); 
                        break;
                    default:
                        break;
                }
                baseApp.setDishesHomeTeachState(true);
                break;
            case Constants.DishesHome_requestcode_dishesset:
                switch (resultCode) {
                    case Constants.Dishesset_Resultcode_Sucesse:
                        int CurrentIndex = data.getIntExtra("catagoriesIndex", 0);
                        refreshCurrentOrderDataByCurrentIndex(CurrentIndex);
                        break;
                    case Constants.Dishesset_Resultcode_Cancle:
                        int Index = data.getIntExtra("catagoriesIndex", 0);
                        RadioButton currentRadio = (RadioButton) catagoriesContainer.getChildAt(Index);
                        currentRadio.setChecked(true);
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
     * flag用于判断是否是缓存初始化 true表示读取本地数据先显示，false表示读取获得服务器数据后及时更新 初始化菜品分类组的样式
     */
    public void initCatagoriesData(boolean flag) {
        home_dish.setBackgroundColor(mActivity.getResources().getColor(R.color.home_dish_bg));
        if (true) {
            catagoriesContainer.removeAllViews();
            catagoriesTips.removeAllViews();
        }
        float density = mActivity.getResources().getDisplayMetrics().density;
        ArrayList<DishesType> dishesTypeList = dishesEntity.getAllDishesType();
        for (int j = 0; j < dishesEntity.dataSetDishesType
                .count(DishesType.class); j++) {
            DishesType dishesType = dishesTypeList.get(j);
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) (65 * density));
            RadioButton radiobutton = (RadioButton) inflater.inflate(
                    R.layout.catagories_rb, null);
            TextView tv = (TextView) inflater.inflate(R.layout.catagories_tv,
                    null);
            radiobutton.setGravity(Gravity.CENTER);
            if (j == 0) {
                dishesEntity.getDishesTypeItemlist(dishesType);
                radiobutton.setChecked(true);
                catagoriesIndex = j;
                switcher.init(dishesType.getDishesInfoList());
                curData = dishesType.getDishesInfoList();
                setIconNumber();
            }
            radiobutton.setId(j);
            radiobutton.setTag(dishesType);
            radiobutton.setText(dishesType.getDishesTypeName());
            radiobutton.setTextSize(18);
            params_rb.setMargins(0, 0, 0, 0);
            radiobutton.setPadding((int) (30 * density), 0,
                    (int) (30 * density), 0);
            // 选择菜品种类的监听器
            radiobutton
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            System.out.println("isChecked:" + isChecked);
                            if (isChecked) {
                                if (catagoriesIndex != buttonView.getId()) {
                                    buttonView.setTextColor(Color
                                            .parseColor("#ffffff"));
                                    DishesType dishesType = (DishesType) buttonView
                                            .getTag();
                                    if (dishesType.getDishesInfoList().size() == 0) {
                                        dishesEntity
                                                .getDishesTypeItemlist(dishesType);
                                        buttonView.setTag(dishesType);
                                    }
                                    catagoriesIndex = buttonView.getId();
                                    switcher.setData(dishesType
                                            .getDishesInfoList());
                                    curData = dishesType.getDishesInfoList();
                                    setIconNumber();
                                }
                            } else {
                                buttonView.setTextColor(Color
                                        .parseColor("#e7cda1"));
                            }
                        }
                    });
            catagoriesContainer.addView(radiobutton, params_rb);
            catagoriesContainer.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (baseApp.isDishesHomeTeachState()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            tv.setText(dishesType.getDishesTypeName());
            tv.setTextSize(18);
            tv.setPadding((int) (30 * density), 0, (int) (30 * density), 0);
            tv.setVisibility(View.INVISIBLE);
            catagoriesTips.addView(tv);
            BadgeView badge = new BadgeView(mActivity, tv);
            badge.setBadgeBackgroundColor(Color.parseColor("#f1c57c"));
            badge.setTextColor(Color.BLACK);
            badge.setText("0");
            badge.setTextSize(12);
            badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badge.show();
            int typeSum = dishesEntity.SumOrderGoodsNumByIndex(dishesType
                    .getDishesTypeCode());
            Log.i("LitePal", "typeSum:" + typeSum
                    + "  dishesType.getDishesTypeCode()");
            if (typeSum > 0) {
                badge.setVisibility(View.VISIBLE);
                badge.setText(typeSum + "");
            } else if (typeSum == 0) {
                Log.i("LitePal", "badge.setVisibility(View.INVISIBLE);");
                badge.setText("" + 0);
                badge.setVisibility(View.INVISIBLE);
            }
            View tips = catagoriesTips.getChildAt(j);
            tips.setTag(R.id.tag_first, j);
            tips.setTag(R.id.tag_second, badge);
            tips.setTag(R.id.tag_three, dishesType);
        }
    }

    /**
     * 设置初始num提示图标 每次切换菜品种类需要更新
     */
    private void setIconNumber() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < switcher.getScreenNumber(); i++) {
            list.add(i);
        }
        numTips.setNumColumns(switcher.getScreenNumber());
        numIcon.setScreenData(list);
        numIcon.setSelectedPos(0);
        numTips.setAdapter(numIcon);

    }

    /**
     * 获取当前商家的所有菜品信息，并且本地缓存
     */
    public void VolleyQueryAllDishes() {
        String param = "/appController/queryAllDishesInfoByMerchantId.do?childMerchantId=" + username;
        Type type = new TypeToken<ResultMap<queryAllDishesByMerchantIdData>>() {
        }.getType();
        Log.i("Volley", "url:" + Constants.address + param);
        ResultMapRequest<ResultMap<queryAllDishesByMerchantIdData>> resultMapRequest = new ResultMapRequest<ResultMap<queryAllDishesByMerchantIdData>>(
                Constants.address + param,
                type,
                new Response.Listener<ResultMap<queryAllDishesByMerchantIdData>>() {
                    @Override
                    public void onResponse(
                            ResultMap<queryAllDishesByMerchantIdData> response) {
                        switch (Integer.valueOf(response.getErrcode())) {
                            case 0:
                                response.getData().getDishes();
                                Gson gson = new Gson();
                                ChildEventBusData childEventBusData = new ChildEventBusData();
                                childEventBusData.setType(2);
                                childEventBusData.setDishesEntity(dishesEntity);
                                childEventBusData.setDishes(response.getData().getDishes());
                                childEventBusData.setInfo(response.getData().getInfo());
                                EventBus.getDefault().post(childEventBusData);
                                break;
                            default:
                                showShortTip(response.getMsg() + "!");
                                dismissLoadingDialog();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                dismissLoadingDialog();
//						showShortTip(VolleyErrorHelper.getMessage(error, mContext));
                showShortTip("更新菜单失败，请退出重试!");

            }
        });
        requestQueue.add(resultMapRequest);
        resultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1,
                1.0f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("HomeActivity - onRusume");
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

    /**
     * 根据位置更新对应位置的数量提示视图
     *
     * @param index
     */
    void UpdateTvTips(int index) {
        View tvTip = catagoriesTips.getChildAt(index);
        BadgeView badge = (BadgeView) tvTip.getTag(R.id.tag_second);
        DishesType dishesType = (DishesType) tvTip.getTag(R.id.tag_three);
        int typeSum = dishesEntity.SumOrderGoodsNumByIndex(dishesType.getDishesTypeCode());
        if (typeSum > 0) {
            badge.setVisibility(View.VISIBLE);
            badge.setText("" + typeSum);
        } else {
            badge.setText("0");
            badge.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 更新购物车价钱
     */
    void UpdateShopping_Nums() {
        baseApp.setCurShoppingOrder(dishesEntity.findShoppingOrder());
//		curShoppingOrder=dishesEntity.findShoppingOrder();
        tv_selectedBtn.setText(sumPrice(baseApp.getCurShoppingOrder().getOrderGoods()) + "元");
        int totalnum = 0;
        for (OrderGoods orderGoods : baseApp.getCurShoppingOrder().getOrderGoods()) {
            if (!orderGoods.isCompDish()) totalnum += orderGoods.getSalesNum();
        }
        shopping_Num.setText(totalnum + "");
    }

    void refreshCurrentOrderData() {
//		switcher.refreshCurrentScreen();
        tv_selectedBtn.setText(sumPrice(baseApp.getCurShoppingOrder().getOrderGoods()) + "元");
        int totalnum = 0;
        for (OrderGoods orderGoods : baseApp.getCurShoppingOrder().getOrderGoods()) {
            if (!orderGoods.isCompDish()) totalnum += orderGoods.getSalesNum();
        }
        shopping_Num.setText(totalnum + "");
        //此处应该只更新有shopping中有的那几种的头部数据
        for (int i = 0; i < shoppingIndex.size(); i++) {
            UpdateTvTips(shoppingIndex.get(i));
        }
        switcher.setData(curData);
    }


    void refreshCurrentOrderDataByCurrentIndex(int currentIndex) {
        RadioButton currentRadio = (RadioButton) catagoriesContainer.getChildAt(currentIndex);
        currentRadio.setChecked(true);
        UpdateShopping_Nums();
        UpdateTvTips(currentIndex);

    }

    /**
     * 获取当前商家的所有菜品信息，并且本地缓存
     */
    public void VolleyQueryComboInfoForApp(final DishesInfo dishesinfo) {
        String param = "/appController/queryComboInfoForApp.do?dishesId=" + dishesinfo.getDishesId() + "&childMerchantId=" + username;
        Type type = new TypeToken<ResultMap<MerchantCompDishesInfo>>() {
        }.getType();
        Log.i("Volley", "url:" + Constants.address + param);
        ResultMapRequest<MerchantCompDishesInfo> resultMapRequest = new ResultMapRequest<MerchantCompDishesInfo>(
                Constants.address + param,
                MerchantCompDishesInfo.class,
                new Response.Listener<MerchantCompDishesInfo>() {
                    @Override
                    public void onResponse(
                            MerchantCompDishesInfo response) {
                        MerchantCompDishesInfo merchantCompDishesInfo = response;
                        System.err.println("merchantCompDishesInfo:" + merchantCompDishesInfo);

                        merchantCompDishesInfo.setDishesId(Long.valueOf(dishesinfo.getDishesId()));
                        merchantCompDishesInfo.setDishesName(dishesinfo.getDishesName());
                        merchantCompDishesInfo.setDishesPrice(dishesinfo.getDishesPrice());
                        merchantCompDishesInfo.setDishesTypeCode(dishesinfo.getDishesTypeCode());
                        merchantCompDishesInfo.setDishesTypeName(dishesinfo.getDishesTypeName());
                        merchantCompDishesInfo.setDishesType(dishesinfo.getDishesType());
                        merchantCompDishesInfo.setDishesUrl(dishesinfo.getDishesUrl());
                        merchantCompDishesInfo.setIsComp(dishesinfo.getIsComp());
                        merchantCompDishesInfo.setExportId(dishesinfo.getExportId());
                        merchantCompDishesInfo.setMemberPrice(dishesinfo.getMemberPrice());
                        merchantCompDishesInfo.setIsZdzk(dishesinfo.getIsZdzk());
                        merchantCompDishesInfo.setMerchantId(Long.valueOf(baseApp.getLoginInfo().getChildMerchantId()));
                        ChildEventBusData childEventBusData = new ChildEventBusData();
                        childEventBusData.setDishesEntity(dishesEntity);
                        childEventBusData.setMerchantCompDishesInfo(merchantCompDishesInfo);
                        childEventBusData.setType(3);
                        EventBus.getDefault().post(childEventBusData);

                        dismissLoadingDialog();

                        View comp = compPopupHolder.get(dishesinfo, merchantCompDishesInfo, catagoriesIndex);
                        dishesPopup = (PopupWindow) comp.getTag();
                        if (!dishesPopup.isShowing()) {
                            dishesPopup.showAtLocation(comp, Gravity.CENTER, 0, 0);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VolleyLogTag,
                        "VolleyError:" + error.getMessage(), error);
                baseApp.setDishesHomeTeachState(true);
                dismissLoadingDialog();
                showShortTip(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
        requestQueue.add(resultMapRequest);
        resultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1,
                1.0f));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void WaitterAuthenticate() {
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
                    backFinish();
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

}
