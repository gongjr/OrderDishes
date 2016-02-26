package com.asiainfo.orderdishes.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.adapter.HallDeskAdapter;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.MerchantDesk;
import com.asiainfo.orderdishes.entity.litepal.MerchantDeskLocation;
import com.asiainfo.orderdishes.entity.volley.QueryDeskLocationData;
import com.asiainfo.orderdishes.entity.volley.ResultMap;
import com.asiainfo.orderdishes.entity.volley.queryUnfinishedOrderData;
import com.asiainfo.orderdishes.helper.DialogDelayListener;
import com.asiainfo.orderdishes.helper.OnItemClickListener;
import com.asiainfo.orderdishes.helper.PwdEncryptor;
import com.asiainfo.orderdishes.helper.SharedPreferencesUtils;
import com.asiainfo.orderdishes.helper.StringUtils;
import com.asiainfo.orderdishes.http.volley.ResultMapRequest;
import com.asiainfo.orderdishes.http.volley.VolleyErrorHelper;
import com.asiainfo.orderdishes.ui.SelectedDeskChangeToDialog.OnChangeDeskListener;
import com.asiainfo.orderdishes.ui.base.AnimatedDoorActivity;
import com.asiainfo.orderdishes.util.BaseUtils;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HallActivity extends AnimatedDoorActivity {
    private static final String TAG = HallActivity.class.getName();
    private String username;
    private String childMerchantId;
    private HallDeskAdapter hallDeskAdapter;
    private List<MerchantDeskLocation> mdlListOnly;
    private List<MerchantDeskLocation> mdlListWithData;
    private List<MerchantDesk> mdList;
    private float mDensity;
    private LayoutInflater mInflater;
    private MerchantDeskLocation selectedDeskLocation;
    private MerchantDesk currentSelectedDesk, changeToDesk;
    private DishesOrder dishesOrder;
    private SharedPreferencesUtils hallSP;
    private static final int INIT_REFRESH = 1;
    private static final int DESK_REFRESH = 2;
    private static int REFRESH_TYPE = INIT_REFRESH;
    /**
     * 视图提取
     */
    private Button btn_takeOrder;
    private RadioGroup grp_deskLocations;
    private TextView tv_changeDeskDesc;
    private Button btn_changeDesk;
    private TextView tv_viewOrder;
    private GridView gridv_Desks;
    private TextView tv_selectedDeskCode;
    private TextView tv_refresh_txt;
    private RelativeLayout rl_refresh_desk;
    private ProgressBar progbar_desk_refresh;
    private TextView tv_desk_notice;
    private View table_fresh;

    @Override
    protected int layoutResId() {
        return R.layout.activity_hall;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Log.d(TAG, "baseApp: " + baseApp);
        Log.d(TAG, "getLoginInfo:" + baseApp.getLoginInfo());
        username = baseApp.getLoginInfo() == null ? "0" : baseApp.getLoginInfo().getChildMerchantId();
        childMerchantId = username;
        initVeiw();
        initListener();
//		initDeskLocationData();
    }

    @Override
    protected void onResume() {
        tv_selectedDeskCode.setText("");
        currentSelectedDesk = null;
        initDeskLocationData();
        super.onResume();
    }

    private void initVeiw() {
        btn_takeOrder = (Button) findViewById(R.id.take_order);
        grp_deskLocations = (RadioGroup) findViewById(R.id.table_types_grp);
        btn_changeDesk = (Button) findViewById(R.id.change_table);
        tv_viewOrder = (TextView) findViewById(R.id.view_order);
        gridv_Desks = (GridView) findViewById(R.id.tables_grid);
        tv_selectedDeskCode = (TextView) findViewById(R.id.selected_table_num);

        tv_refresh_txt = (TextView) findViewById(R.id.deskloc_fresh_txt);
        rl_refresh_desk = (RelativeLayout) findViewById(R.id.desk_refresh_layout);
        progbar_desk_refresh = (ProgressBar) findViewById(R.id.desk_progressbar);
        tv_desk_notice = (TextView) findViewById(R.id.desk_notice_txt);
        table_fresh = findViewById(R.id.table_fresh);
        mDensity = getResources().getDisplayMetrics().density;
        mInflater = LayoutInflater.from(HallActivity.this);
    }

    /**
     * 获取当前商户的桌子类型信息
     */
    public void initDeskLocationData() {
        //仅用于装载桌子区域数据，不包含桌子数据，deskList始终为null
        if (mdlListOnly == null) {
            mdlListOnly = new ArrayList<MerchantDeskLocation>();
        }

        if (mdlListWithData != null) {
            mdlListWithData.clear();
        } else {
            mdlListWithData = new ArrayList<MerchantDeskLocation>(); //空初始化
        }
        if (selectedDeskLocation == null) {
            selectedDeskLocation = new MerchantDeskLocation(); //空初始化, 结合换桌成功回调刷新
        }
        initRefreshing();
        //获取商户桌子区域数据
        if (BaseUtils.isNetworkAvailable(getApplicationContext())) {
            getMerchantDeskLocationData();
        } else {
            /*Log.d(TAG, "网络断开，获取本地数据");
			mdlListWithData = DataSupport.findAll(MerchantDeskLocation.class);
			if(mdlListWithData!=null && mdlListWithData.size()>0){
				resolveDeskLocationData();
				setDeskLocationsData();
				if(selectedDeskLocation==null || !selectedDeskLocation.getLocationCode().equals("")){
					selectedDeskLocation =  mdlListOnly.get(0);
				}
				mdList = DataSupport.where("locationCode = ?", selectedDeskLocation.getLocationCode()).find(MerchantDesk.class);
				Log.d(TAG,"获取桌子的本地数据  locCode-"+selectedDeskLocation.getLocationCode() + " deskSize-" + mdList.size());
				setDeskItemsInfo();
			}else{
				showShortTip("本地无数据");
			}*/
            showShortTip("请检查您的本地网络是否正常!");
        }
    }

    private void setDeskLocationsData() {
        if (grp_deskLocations != null && grp_deskLocations.getChildCount() > 0) {
            grp_deskLocations.removeAllViews();
        }

        for (int i = 0; i < mdlListOnly.size(); i++) {
            MerchantDeskLocation mdl = mdlListOnly.get(i);
            RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams((int) mDensity * 340,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            RadioButton radiobutton = (RadioButton) mInflater.inflate(R.layout.table_type_rdo, null);
            radiobutton.setId(i + 1);  //视图的id
            radiobutton.setTag(i);  //放置的是桌子区域的编码
            radiobutton.setText(mdl.getLocationName());

            if (selectedDeskLocation.getLocationCode() != null
                    && selectedDeskLocation.getLocationCode().equals(mdl.getLocationCode())) {
                Log.d(TAG, "test change keep");
                selectedDeskLocation = mdl;
                radiobutton.setChecked(true);
                radiobutton.setTextColor(Color.parseColor("#FFFFFF"));
            } else if (selectedDeskLocation.getLocationCode() == null && i == 0) {
                selectedDeskLocation = mdl;
                radiobutton.setChecked(true);
                radiobutton.setTextColor(Color.parseColor("#FFFFFF"));
            }
            // 选择菜品种类的监听器
            radiobutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setTextColor(Color.parseColor("#ffffff"));
                        Integer mdlIndex = (Integer) buttonView.getTag();
                        MerchantDeskLocation mdl = mdlListOnly.get(mdlIndex);
                        if (!mdl.getLocationCode().equals(selectedDeskLocation.getLocationCode())) {
                            selectedDeskLocation = mdl;
                            //下一步，根据currentSelectedTableType拿到对应的桌子信息
                            refreshDeskIng();
                            if (BaseUtils.isNetworkAvailable(getApplicationContext())) {
                                updateMerchantDeskData();
                            } else {
                                mdList = DataSupport.where("locationCode = ?", selectedDeskLocation.getLocationCode()).find(MerchantDesk.class);
                                setDeskItemsInfo();
                            }
                        }
                        setSelectedDeskNo();
                    } else {
                        buttonView.setTextColor(getResources().getColor(R.color.hall_mdl_text_normal));
                    }
                }
            });
            grp_deskLocations.addView(radiobutton, params_rb);
        }
    }

    /**
     * 获取商户桌子区域信息
     */
    public void getMerchantDeskLocationData() {
        if (childMerchantId == null || childMerchantId.equals("")) {
            showShortTip("登陆无效，请退出重新登陆!");
            initRefreshFialed();
            return;
        }
        String param = "/appController/queryDeskLocation.do?childMerchantId=" + childMerchantId;
        Log.d(TAG, "queryDeskLocation: " + Constants.address + param);
        Type type = new TypeToken<ResultMap<QueryDeskLocationData>>() {
        }.getType();
        ResultMapRequest<ResultMap<QueryDeskLocationData>> resultMapRequest = new ResultMapRequest<ResultMap<QueryDeskLocationData>>(
                Constants.address + param, type,
                new Response.Listener<ResultMap<QueryDeskLocationData>>() {
                    @Override
                    public void onResponse(ResultMap<QueryDeskLocationData> response) {
                        switch (Integer.valueOf(response.getErrcode())) {
                            case 0:
                                QueryDeskLocationData mdls = response.getData();
                                if (mdls != null && mdls.getInfo() != null && mdls.getInfo().size() > 0) {
                                    mdlListWithData = mdls.getInfo();
                                    resolveDeskLocationData(); //分离桌子区域和桌子数据
                                    setDeskLocationsData();
                                    if (selectedDeskLocation.getLocationCode() == null) {
                                        selectedDeskLocation = mdlListOnly.get(0);
                                        updateMerchantDeskData();
                                    } else {
                                        updateMerchantDeskData();
                                    }
                                    // 如果当天没有刷新过桌子区域信息，则本地数据与服务器进行同步
								/*if(isNeedSynchronizeDeskLocation()){
									synchronizeMerchantDeskLocation();
								}*/
                                } else {
                                    //没数据
                                    initRefreshFialed();
                                }
                                break;
                            case 4:
                                initRefreshFialed();
                                break;
                            default:
                                initRefreshFialed();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VolleyLogTag, "VolleyError:" + error.getMessage(), error);
                initRefreshFialed();
            }
        });
        resultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        requestQueue.add(resultMapRequest);
    }

    /**
     * 从包含桌子区域和区域数据的全数据列表中解析桌子区域数据
     */
    private void resolveDeskLocationData() {
        if (mdlListWithData != null && mdlListWithData.size() > 0) {
            if (mdlListOnly != null) {
                mdlListOnly.clear();
            } else {
                mdlListOnly = new ArrayList<MerchantDeskLocation>();
            }

            for (int i = 0; i < mdlListWithData.size(); i++) {
                MerchantDeskLocation mdl = new MerchantDeskLocation();
                mdl.setChildMerchantId(mdlListWithData.get(i).getChildMerchantId());
                mdl.setLocationCode(mdlListWithData.get(i).getLocationCode());
                mdl.setLocationName(mdlListWithData.get(i).getLocationName());
//        		mdl.setMerchantDeskList(null);
                mdlListOnly.add(mdl);
            }
        }
    }

    private void updateMerchantDeskData() {
        for (int i = 0; i < mdlListWithData.size(); i++) {
            String sdc = selectedDeskLocation.getLocationCode();
            String mdd = mdlListWithData.get(i).getLocationCode();
            if (sdc.equals(mdd)) {
                mdList = mdlListWithData.get(i).getMerchantDeskList();
                setDeskItemsInfo();
                return;
            }
        }
    }

    /**
     * 根据当前选中区域编码设置桌子数据
     */
    public void setDeskItemsInfo() {
        if (mdList == null) {
            mdList = new ArrayList<MerchantDesk>();
        }

        if (mdList.size() == 0) {
            refreshDeskNoData();
            return;
        }

        if (hallDeskAdapter == null) {
            hallDeskAdapter = new HallDeskAdapter(mdList, HallActivity.this);
        } else {
//			hallDeskAdapter.refreshDataWithItems(mdList, currentSelectedDesk, changeToDesk);
            hallDeskAdapter.refreshDataWithPos(mdList, -1);
        }

        if (REFRESH_TYPE == INIT_REFRESH) {
            initRefreshSuccess();
        } else {
            refreshDeskSuccess();
        }

        hallDeskAdapter.setMOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                hallDeskAdapter.setSelectedPos(position);
                currentSelectedDesk = mdList.get(position);
                tv_selectedDeskCode.setText(currentSelectedDesk.getDeskName());
            }
        });

        gridv_Desks.setAdapter(hallDeskAdapter);
    }

    private void initListener() {

        table_fresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				setSelectedDeskNo();
                tv_selectedDeskCode.setText("");
                currentSelectedDesk = null;
                initDeskLocationData();
            }
        });
        //换桌
        btn_changeDesk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedDesk == null || !isAbleToAction()) {
                    showShortTip("请先选择桌子");
                } else {
                    Log.d(TAG, "test btn_changeDesk");
                    showLoadingDialog(new DialogDelayListener() {
                        @Override
                        public void onexecute() {
                            getDeskOrderForAction(Constants.HALL_ACTION_CHANGE_DESK);
                        }
                    }, 300);
                }
            }
        });

        //查看订单
        tv_viewOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedDesk == null || !isAbleToAction()) {
                    showShortTip("请先选择桌子");
                } else {
                    switch (currentSelectedDesk.getDeskStateValue()) {
                        case 0:
                            showShortTip("空桌，无订单!请刷新桌台");
                            break;
                        case 1:
                            Log.d(TAG, "test tv_viewOrder");
                            showLoadingDialog(new DialogDelayListener() {
                                @Override
                                public void onexecute() {
                                    getDeskOrderForAction(Constants.HALL_ACTION_VIEW_ORDER);
                                }
                            }, 300);
                            break;
                        case 2:
                            Log.d(TAG, "test tv_viewOrder");
                            showLoadingDialog(new DialogDelayListener() {
                                @Override
                                public void onexecute() {
                                    getDeskOrderForAction(Constants.HALL_ACTION_VIEW_ORDER);
                                }
                            }, 300);
                            break;
                        default:
                            showShortTip("桌子已锁定，无法操作!请刷新桌台");
                            break;
                    }
                }
            }
        });

        //点菜
        btn_takeOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedDesk == null || !isAbleToAction()) {
                    showShortTip("请先选择桌子");
                } else {
                    Log.d(TAG, "test btn_takeOrder");
                    if (BaseUtils.isNetworkAvailable(getApplicationContext())) {
                        switch (currentSelectedDesk.getDeskStateValue()) {
                            case 0:
                                Log.d(TAG, "在线情况，获取网络数据点菜");
                                showLoadingDialog(new DialogDelayListener() {
                                    @Override
                                    public void onexecute() {
                                        getDeskOrderForAction(Constants.HALL_ACTION_MAKE_ORDER);
                                    }
                                }, 300);
                                break;
                            default:
                                showShortTip("非空桌，无法开桌点菜!请刷新桌台");
                                break;
                        }


                    } else {
						/*Log.d(TAG, "离线情况，获取本地桌子点菜");
						Intent intent = new Intent();
						intent.putExtra("u_name", "" + username);
						intent.setClass(getApplicationContext(), DishesHomeActivity.class);
						Bundle mBundle = new Bundle();     
				        mBundle.putSerializable("hall_selected_desk", currentSelectedDesk);
				        intent.putExtras(mBundle);
						startActivity(intent);*/
                        showShortTip("请检查您的本地网络是否正常!");
                    }
                }
            }
        });
    }

    /**
     * 根据子商户id和桌子id，获取桌子下可操作的订单id，如果有多张订单，默认取第一张订单的id
     * http://192.168.1.151:8080/buildsite/appController/queryUnfinishedOrder.do?
     * childMerchantId=10000272&deskId=6
     */
    public void getDeskOrderForAction(int actionType) {
        Log.d(TAG, "正在获取订单数据......");
        dishesOrder = null;  //操作前先置空
        final int at = actionType;
        String param = "/appController/queryUnfinishedOrder.do?childMerchantId=" + childMerchantId + "&deskId=" + currentSelectedDesk.getDeskId();
        Log.d(TAG, Constants.address + param);
        Type type = new TypeToken<ResultMap<queryUnfinishedOrderData>>() {
        }.getType();
        ResultMapRequest<ResultMap<queryUnfinishedOrderData>> resultMapRequest = new ResultMapRequest<ResultMap<queryUnfinishedOrderData>>(
                Constants.address + param, type, new Response.Listener<ResultMap<queryUnfinishedOrderData>>() {
            @Override
            public void onResponse(ResultMap<queryUnfinishedOrderData> response) {
                dismissLoadingDialog();
                switch (Integer.valueOf(response.getErrcode())) {
                    case 0:
                        queryUnfinishedOrderData data = response.getData();
                        if (data.getInfo() != null && data.getInfo().size() > 0) {
                            dishesOrder = data.getInfo().get(data.getInfo().size() - 1);
                            btnTakeAction(at);
                        }
                        if (data.getInfo() != null && data.getInfo().size() == 0) {
                            Log.d(TAG, "data.getInfo() " + data.getInfo());
                            if (at == Constants.HALL_ACTION_MAKE_ORDER) {
                                btnTakeAction(at);
                            } else {
                                showShortTip("桌子尚未开单");
                            }
                        }
                        break;
                    case 4:
                        break;
                    default:
                        showShortTip("查询出错，请重试！");
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissLoadingDialog();
                Log.e(VolleyLogTag, "VolleyError:" + error.getMessage(), error);
                showShortTip(VolleyErrorHelper.getMessage(error, mContext));
            }
        });
        resultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        requestQueue.add(resultMapRequest);
    }

    /**
     * 对桌子进行操作
     *
     * @param type Constants.HALL_ACTION_CHANGE_DESK换桌，Constants.HALL_ACTION_VIEW_ORDER查看订单
     *             Constants.HALL_ACTION_MAKE_ORDER开单
     */
    private void btnTakeAction(int type) {
        if (type == Constants.HALL_ACTION_CHANGE_DESK) {
            if (dishesOrder != null) {
                SelectedDeskChangeToDialog changeToDeskDialog = new SelectedDeskChangeToDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("changeFromDesk", currentSelectedDesk);
                bundle.putString("childMerchantId", childMerchantId);
                bundle.putString("orderId", dishesOrder.getOrderId() + "");
                changeToDeskDialog.setArguments(bundle);
                changeToDeskDialog.setOnChangeDeskListener(new OnChangeDeskListener() {
                    @Override
                    public void onSuccess(MerchantDesk changeToDesk) {
                        Log.d(TAG, "换桌成功hallactivity回调刷新数据");
                        currentSelectedDesk = changeToDesk;
                        MerchantDeskLocation mdl = new MerchantDeskLocation();
                        mdl.setLocationCode(changeToDesk.getLocationCode() + "");
                        selectedDeskLocation = mdl;
                        REFRESH_TYPE = INIT_REFRESH;
                        dishesOrder = null;
                        setSelectedDeskNo();
                        initDeskLocationData();
                    }
                });
                changeToDeskDialog.show(getFragmentManager(), "change_desk_dialog");
            }
        }
        if (type == Constants.HALL_ACTION_MAKE_ORDER) {
            if (dishesOrder == null) {
                EditPeopleNumber();
//                showPopupWindows();
            } else {
                showShortTip("该桌有未完成订单!");
				/*Intent intent = new Intent();
				intent.putExtra("u_name", "" + username);
				intent.putExtra("dishesOrder", dishesOrder);
				intent.setClass(getApplicationContext(), DishesHomeActivity.class);
				Bundle mBundle = new Bundle();     
		        mBundle.putSerializable("hall_selected_desk", currentSelectedDesk);
		        intent.putExtras(mBundle);
				startActivity(intent);*/
            }
        }
        if (type == Constants.HALL_ACTION_VIEW_ORDER) {
            if (dishesOrder != null) {
                Intent intent = new Intent();
                Bundle order = new Bundle();
                intent.putExtra("ChildMerchantId", "" + username);
                intent.putExtra("orderOperateState", Constants.DishesHome_orderOperateState_edit);
                order.putSerializable("hall_selected_desk", currentSelectedDesk);
                order.putSerializable("dishesOrder", dishesOrder);
                intent.putExtras(order);
                intent.setClass(getApplicationContext(),
                        DiskOrderActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
    }

    private void setSelectedDeskNo() {
        if (selectedDeskLocation != null && currentSelectedDesk != null) {
            if (selectedDeskLocation.getLocationCode().equals(currentSelectedDesk.getLocationCode() + "")) {
                tv_selectedDeskCode.setText(currentSelectedDesk.getDeskName());
            } else {
                tv_selectedDeskCode.setText("");
                currentSelectedDesk = null;
            }
        }
    }

    private Boolean isAbleToAction() {
        if (selectedDeskLocation != null && currentSelectedDesk != null) {
            if (selectedDeskLocation.getLocationCode().equals(currentSelectedDesk.getLocationCode() + "")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否需要同步本地桌子区域数据
     */
    private Boolean isNeedSynchronizeDeskLocation() {
        if (hallSP == null) {
            hallSP = new SharedPreferencesUtils(HallActivity.this);
            return hallSP.isNeedSyncDeskLocData(StringUtils.date2Str(new Date()));
        } else {
            return hallSP.isNeedSyncDeskLocData(StringUtils.date2Str(new Date()));
        }
    }

    /**
     * 本地同步桌子区域数据
     */
    private void synchronizeMerchantDeskLocation() {
        Log.d(TAG, "同步大厅区域数据");
        hallSP.refreshDeskLocLatestDate(StringUtils.date2Str(new Date())); //刷新偏好参数里面的最新一次同步时间
        DataSupport.deleteAll(MerchantDeskLocation.class); //先删除本地数据库中的全部桌子区域数据
        DataSupport.saveAll(mdlListOnly);

        DataSupport.deleteAll(MerchantDesk.class); //先删除本地数据库中的全部桌子区域数据
        for (int i = 0; i < mdlListWithData.size(); i++) {
            DataSupport.saveAll(mdlListWithData.get(i).getMerchantDeskList());
        }
    }

    /**
     * 初始刷新页面，正在刷新
     */
    private void initRefreshing() {
        REFRESH_TYPE = INIT_REFRESH;  //设置刷新类型为，初始刷新
        grp_deskLocations.setVisibility(View.INVISIBLE);
        gridv_Desks.setVisibility(View.INVISIBLE);
        tv_refresh_txt.setText("正在获取数据...");
        tv_refresh_txt.setVisibility(View.VISIBLE);
        tv_desk_notice.setText("正在获取数据...");
        progbar_desk_refresh.setVisibility(View.VISIBLE);
        rl_refresh_desk.setVisibility(View.VISIBLE);
    }

    /**
     * 初始刷新页面，刷新成功
     */
    private void initRefreshSuccess() {
        grp_deskLocations.setVisibility(View.VISIBLE);
        gridv_Desks.setVisibility(View.VISIBLE);
        tv_refresh_txt.setVisibility(View.INVISIBLE);
        rl_refresh_desk.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始刷新页面，刷新失败
     */
    private void initRefreshFialed() {
        showShortTip("服务器没有桌子数据，请重试，或联系开发人员");
        grp_deskLocations.setVisibility(View.INVISIBLE);
        gridv_Desks.setVisibility(View.INVISIBLE);
        tv_refresh_txt.setText("获取失败，请重新登录！");
        tv_refresh_txt.setVisibility(View.VISIBLE);
        tv_desk_notice.setText("获取失败，请重新登录！");
        progbar_desk_refresh.setVisibility(View.GONE);
        rl_refresh_desk.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新桌子数据，正在刷新
     */
    private void refreshDeskIng() {
        REFRESH_TYPE = DESK_REFRESH;  //设置刷新类型为，刷新指定区域的桌子
        tv_refresh_txt.setVisibility(View.INVISIBLE);
        grp_deskLocations.setVisibility(View.VISIBLE);
        gridv_Desks.setVisibility(View.INVISIBLE);
        tv_desk_notice.setText("正在刷新...");
        progbar_desk_refresh.setVisibility(View.VISIBLE);
        rl_refresh_desk.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新桌子数据，刷新成功
     */
    private void refreshDeskSuccess() {
        tv_refresh_txt.setVisibility(View.INVISIBLE);
        grp_deskLocations.setVisibility(View.VISIBLE);
        gridv_Desks.setVisibility(View.VISIBLE);
        rl_refresh_desk.setVisibility(View.INVISIBLE);
    }

    /**
     * 刷新桌子数据成功，没有数据
     */
    private void refreshDeskNoData() {
        tv_refresh_txt.setVisibility(View.INVISIBLE);
        grp_deskLocations.setVisibility(View.VISIBLE);
        gridv_Desks.setVisibility(View.INVISIBLE);
        tv_desk_notice.setText("这里没有桌子哦~");
        progbar_desk_refresh.setVisibility(View.GONE);
        rl_refresh_desk.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新桌子数据失败
     */
    private void refreshDeskFailed() {
        tv_refresh_txt.setVisibility(View.INVISIBLE);
        grp_deskLocations.setVisibility(View.VISIBLE);
        gridv_Desks.setVisibility(View.INVISIBLE);
        tv_desk_notice.setText("刷新失败，请重试...");
        progbar_desk_refresh.setVisibility(View.GONE);
        rl_refresh_desk.setVisibility(View.VISIBLE);
    }


    private void EditPeopleNumber() {
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.order_people_number);
        final EditText number = (EditText) window.findViewById(R.id.people_number_edit);
        number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        });
        Button positive = (Button) window
                .findViewById(R.id.people_number_submit);
        positive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String people_number=number.getEditableText().toString();
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DishesHomeActivity.class);
                intent.putExtra("people_number",people_number);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("hall_selected_desk", currentSelectedDesk);
                intent.putExtras(mBundle);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        ImageButton close = (ImageButton) window
                .findViewById(R.id.people_dialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
