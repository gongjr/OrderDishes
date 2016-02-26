package com.asiainfo.orderdishes.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.adapter.DishSetItemAdapter;
import com.asiainfo.orderdishes.adapter.DishSetItemAdapter.OnPropertyItemCheckedChangedListener;
import com.asiainfo.orderdishes.adapter.DishSetTitleAdapter;
import com.asiainfo.orderdishes.entity.CompDishesInfo;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.eventbus.CompDishesEventData;
import com.asiainfo.orderdishes.entity.eventbus.Event;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.helper.OnItemClickListener;
import com.asiainfo.orderdishes.ui.base.AnimatedDoorActivity;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class DishSetActivity extends AnimatedDoorActivity {

    /**
     * 套餐头部数据视图
     */
    private GridView titleGridView;
    /**
     * 套餐头部数据适配器
     */
    private DishSetTitleAdapter titleAdapter;
    /**
     * 套餐内容视图
     */
    private RecyclerView dishesRecyclerView;
    /**
     * 套餐内容适配器
     */
    private DishSetItemAdapter dishesAdapter;

//	private List<Object> mDishSetItems;
    /**
     * 套餐本地id
     */
    private int MerchantCompDishesInfoId;
    private int OrderEntityId;
    private DishesEntity dishesEntity;
    private MerchantCompDishesInfo merchantCompDishesInfo;
    private TextView dishset_name;
    private Button nextAndComplete, previous, remarkbtn, dishesset_back;
    private int currentIndex = -1;
    /**
     * 用于识别不同的套餐配置项，一份套餐共用一个InstanceId值
     */
    private long InstanceId;
    private CompDishesInfo compDishesInfo = new CompDishesInfo();
    private DishesOrder dishesOrder;
    private int LastHomeIndex;

    @Override
    protected int layoutResId() {
        return R.layout.activity_dish_set_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        MerchantCompDishesInfoId = getIntent().getIntExtra("MerchantCompDishesInfoId", 0);
        LastHomeIndex = getIntent().getIntExtra("catagoriesIndex", 0);

        dishset_name = (TextView) findViewById(R.id.dishset_name);
        titleGridView = (GridView) findViewById(R.id.dishset_parts_title);

        dishesRecyclerView = (RecyclerView) findViewById(R.id.part_dishes);
        LinearLayoutManager dishesLayoutManager = new LinearLayoutManager(this);
        dishesLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dishesRecyclerView.setLayoutManager(dishesLayoutManager);
        nextAndComplete = (Button) findViewById(R.id.finishbtn);
        previous = (Button) findViewById(R.id.returnbtn);
        remarkbtn = (Button) findViewById(R.id.remarkbtn);
        dishesset_back = (Button) findViewById(R.id.dishesset_back);

        remarkbtn.setVisibility(View.GONE);
        initListener();
    }


    public void onEventMainThread(Event event) {
        switch (event.getType()) {
            case 1:
                Date date = new Date();
                InstanceId = date.getTime();
                compDishesInfo.setInstanceid(InstanceId + "");
                CompDishesEventData compDishesEventData = (CompDishesEventData) event.getData();
                merchantCompDishesInfo = compDishesEventData.getMerchantCompDishesInfo();
                dishesOrder = compDishesEventData.getDishesOrder();
                dishesEntity = new DishesEntity(dishesOrder, false);

                compDishesInfo.setCompid(merchantCompDishesInfo.getDishesId());
                dishset_name.setText(merchantCompDishesInfo.getDishesName());
                ArrayList<String> tips = new ArrayList<String>();
                for (int i = 0; i < merchantCompDishesInfo.getCompDishesTypeList().size(); i++) {
                    String tip = merchantCompDishesInfo.getCompDishesTypeList().get(i).getDishesTypeName() + "(" + merchantCompDishesInfo.getCompDishesTypeList().get(i).getDishesCount()
                            + "选" + merchantCompDishesInfo.getCompDishesTypeList().get(i).getMaxSelect() + ")";
                    tips.add(tip);
                }
                titleGridView.setNumColumns(tips.size());
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        (int) ((tips.size() * 100) * gMetrice.density), LayoutParams.WRAP_CONTENT);
                rp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                rp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                rp.topMargin = (int) (20 * gMetrice.density);
                titleGridView.setLayoutParams(rp);
                titleAdapter = new DishSetTitleAdapter(DishSetActivity.this, tips, mOnTitleItemClickListener, gMetrice);
                titleGridView.setAdapter(titleAdapter);

                dishesRecyclerView.setBackground(null);
                currentIndex = 0;
                dishesAdapter = new DishSetItemAdapter(merchantCompDishesInfo.getCompDishesTypeList().get(currentIndex), DishSetActivity.this, dishesEntity.getCompDishesEntity(), compDishesInfo);
                dishesAdapter.setOnDataSetItemClickListener(mOnDataSetItemClickListener);
                dishesAdapter.setOnPropertyItemCheckedChangedListener(mOnPropertyItemCheckedChangedListener);
                dishesRecyclerView.setAdapter(dishesAdapter);
//			dishesRecyclerView.getLayoutParams().width=gMetrice.densityDpi*306;
//			nextAndComplete.setText("继续"+merchantCompDishesInfo.getCompDishesTypeList().get(currentIndex+1).getDishesTypeName()+"吧!");
                remarkbtn.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 标题栏点击事件
     */
    OnItemClickListener mOnTitleItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {

        }
    };

    void initListener() {
        dishesset_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dishesEntity.getCompDishesEntity().decreaseCompOrderGoods(compDishesInfo);
                Intent intent = getIntent();
                intent.putExtra("catagoriesIndex", LastHomeIndex);
                setResult(Constants.Dishesset_Resultcode_Cancle, intent);
//				finish();
                backFinish();
            }
        });
        nextAndComplete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dishesAdapter.isSelectComplte()) {
                    if (currentIndex == 0) previous.setVisibility(View.VISIBLE);
                    if (currentIndex < merchantCompDishesInfo.getCompDishesTypeList().size() - 1) {
                        currentIndex++;
                        titleAdapter.setSelectedPos(currentIndex);
                        dishesAdapter.refreshbyData(merchantCompDishesInfo.getCompDishesTypeList().get(currentIndex));
                        if (currentIndex != merchantCompDishesInfo.getCompDishesTypeList().size() - 1) {
                            nextAndComplete.setText("下一步");
//					nextAndComplete.setText("继续"+merchantCompDishesInfo.getCompDishesTypeList().get(currentIndex+1).getDishesTypeName()+"吧!");
                            remarkbtn.setVisibility(View.GONE);
                        } else {
                            nextAndComplete.setText("完成");
                            remarkbtn.setVisibility(View.GONE);
                        }
                    } else {
//					AlertDialog();
                        OrderGoods orderGoods = dishesEntity.getCompDishesEntity().addCompOrderGoods(merchantCompDishesInfo, compDishesInfo);
                    /*ArrayList<OrderGoods>  compList=dishesEntity.getCompDishesEntity().findAllOrderGoods(orderGoods);
					for (OrderGoods ordergoods : compList) {
						ArrayList<RemarkItem> kk =dishesEntity.getCompDishesEntity().findDishesItem(ordergoods);
						String remark="";
						if(kk.size()>=1){
							remark+="(";
						for (int i=0;i<kk.size();i++) {
							remark+=kk.get(i).getItemName();
							if(i!=kk.size()-1)remark+="、";
						}
						remark+=")";
						}
						//更新数据库
					}*/

                        Intent intent = getIntent();
                        intent.putExtra("catagoriesIndex", LastHomeIndex);
                        setResult(Constants.Dishesset_Resultcode_Sucesse, intent);
                        finish();
                    }
                } else {
                    showMyShortTip("请先配置完本页，再下一步!");
                }
            }
        });
        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dishesAdapter.isSelectComplte()) {
                    if (currentIndex == 1) previous.setVisibility(View.GONE);
                    if (currentIndex > 0) {
                        currentIndex--;
                        titleAdapter.setSelectedPos(currentIndex);
                        dishesAdapter.refreshbyData(merchantCompDishesInfo.getCompDishesTypeList().get(currentIndex));
//					nextAndComplete.setText("继续"+merchantCompDishesInfo.getCompDishesTypeList().get(currentIndex+1).getDishesTypeName()+"吧!");
                        nextAndComplete.setText("下一步");
                        remarkbtn.setVisibility(View.GONE);
                    } else {
                        showMyShortTip("第一页，无法继续向前跳转");
                    }
                } else {
                    showMyShortTip("请先配置完本页，再返回!");
                }
            }
        });
    }

    /**
     * 套餐项点击事件监听
     */
    OnItemClickListener mOnDataSetItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            showMyShortTip("超出本项最大可选数!");
        }
    };

    /**
     * 套餐属性项选择状态监听
     */
    OnPropertyItemCheckedChangedListener mOnPropertyItemCheckedChangedListener = new OnPropertyItemCheckedChangedListener() {
        @Override
        public void onCheckedChanged(View v, Object t, Boolean checked, int position) {
            showMyShortTip("超出本项最大可选数!");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                dialog.dismiss();
                dishesEntity.getCompDishesEntity().addCompOrderGoods(merchantCompDishesInfo, compDishesInfo);
                Intent intent = getIntent();
                intent.putExtra("catagoriesIndex", LastHomeIndex);
                setResult(Constants.Dishesset_Resultcode_Sucesse, intent);
                finish();
            }
        });
        TextView context = (TextView) window
                .findViewById(R.id.submitdialog_content);
        context.setText("您已完成套餐配置，确认订单吗?");
        ImageButton close = (ImageButton) window
                .findViewById(R.id.submitdialog_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}