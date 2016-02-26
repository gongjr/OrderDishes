package com.asiainfo.orderdishes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.litepal.RemarkItem;
import com.asiainfo.orderdishes.helper.OnDishItemClickListener;
import com.asiainfo.orderdishes.ui.widget.FlowLayoutMargin;

import java.util.ArrayList;

public class DishesSelectionAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<OrderGoods> dishesList = new ArrayList<OrderGoods>();

    /**
     * listview的类型 DISHES_LV_TYPE_VIEW 查看 DISHES_LV_TYPE_EDIT 编辑
     */
    public static final int DISHES_LV_TYPE_VIEW = 1;
    public static final int DISHES_LV_TYPE_EDIT = 2;
    public int DISHES_LV_TYPE = DISHES_LV_TYPE_VIEW;

    /**
     * 点餐菜品专用回调接口
     */
    OnDishItemClickListener mOnDishItemClickListener;
    private DishesEntity dishesEntity;

    public DishesSelectionAdapter(Context context, int lvType,
                                  DishesEntity dishesentity) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.DISHES_LV_TYPE = lvType;
        this.dishesEntity = dishesentity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.dish_selected_lv_item_edit, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.name);
            viewHolder.count = (TextView) convertView
                    .findViewById(R.id.count);
            viewHolder.price = (TextView) convertView
                    .findViewById(R.id.price);
            viewHolder.state = (TextView) convertView
                    .findViewById(R.id.state);
            viewHolder.remarkGroup = (LinearLayout) convertView
                    .findViewById(R.id.ordergoods_edit_detail);
            viewHolder.del = (TextView) convertView.findViewById(R.id.del);
            viewHolder.add = (TextView) convertView.findViewById(R.id.add);
            viewHolder.minus = (TextView) convertView
                    .findViewById(R.id.minus);
            viewHolder.editGroup = (LinearLayout) convertView
                    .findViewById(R.id.edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (DISHES_LV_TYPE == DISHES_LV_TYPE_VIEW) {
            String remarkold=dishesList.get(position).getRemark();
            if(remarkold!=null&&!remarkold.equals("")&&!remarkold.equals("[]")&&!dishesList.get(position).getIsComp().equals("1")){
                String remark=remarkold.replace("[","").replace("]","");
                viewHolder.name.setText(dishesList.get(position).getSalesName()+" ("+remark+")");
            }else viewHolder.name.setText(dishesList.get(position).getSalesName());
            viewHolder.editGroup.setVisibility(View.GONE);
            viewHolder.state.setVisibility(View.VISIBLE);
            viewHolder.price.setText("￥"
                    + dishesList.get(position).getDishesPrice());
            viewHolder.state.setText("* "
                    + dishesList.get(position).getSalesNum());
            adddishesDetail(mInflater, viewHolder.remarkGroup,
                    dishesList.get(position));
            viewHolder.del.setVisibility(View.INVISIBLE);
        } else if (DISHES_LV_TYPE == DISHES_LV_TYPE_EDIT) {
            viewHolder.state.setVisibility(View.GONE);
            viewHolder.price.setText("￥"
                    + dishesList.get(position).getDishesPrice());
            viewHolder.del.setTag(R.id.tag_first, dishesList.get(position));
            viewHolder.del.setTag(R.id.tag_second, viewHolder.count);
            viewHolder.add.setTag(R.id.tag_first, dishesList.get(position));
            viewHolder.add.setTag(R.id.tag_second, viewHolder.count);
            viewHolder.minus.setTag(R.id.tag_first, dishesList.get(position));
            viewHolder.minus.setTag(R.id.tag_second, viewHolder.count);
            viewHolder.del.setVisibility(View.VISIBLE);
            String remarkold=dishesList.get(position).getRemark();
            if(remarkold!=null&&!remarkold.equals("")&&!remarkold.equals("[]")&&!dishesList.get(position).getIsComp().equals("1")){
                String remark=remarkold.replace("[","").replace("]","");
                viewHolder.name.setText(dishesList.get(position).getSalesName()+" ("+remark+")");
            }else viewHolder.name.setText(dishesList.get(position).getSalesName());
            viewHolder.count.setText(dishesList.get(position).getSalesNum()
                    + "");

            if(dishesList.get(position).getIsComp().equals("1"))
            viewHolder.editGroup.setVisibility(View.GONE);

            adddishesDetail(mInflater, viewHolder.remarkGroup,
                    dishesList.get(position));

            viewHolder.del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDishItemClickListener != null) {
                        mOnDishItemClickListener.onItemClick(v, position,
                                OnDishItemClickListener.ClickType_DELETE);
                    }
                }
            });
            viewHolder.add.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDishItemClickListener != null) {
                        mOnDishItemClickListener.onItemClick(v, position,
                                OnDishItemClickListener.ClickType_ADD);
                    }
                }
            });
            viewHolder.minus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDishItemClickListener != null) {
                        mOnDishItemClickListener.onItemClick(v, position,
                                OnDishItemClickListener.ClickType_MINUS);
                    }
                }
            });
        }

        return convertView;
    }

    @Override
    public int getCount() {
        if (dishesList != null && dishesList.size() > 0) {
            return dishesList.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (dishesList != null && dishesList.size() > 0) {
            return dishesList.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 设置菜品项点击回调监听器
     *
     * @param mOnDishItemClickListener
     */
    public void setOnDishItemClickListener(
            OnDishItemClickListener mOnDishItemClickListener) {
        this.mOnDishItemClickListener = mOnDishItemClickListener;
    }

    /**
     * 重新设置数据，通知刷新
     */
    public void onDataRefreshNotify(ArrayList<OrderGoods> dishesList, int lvType) {
        this.dishesList = dishesList;
        this.DISHES_LV_TYPE = lvType;
        notifyDataSetChanged();
    }

    /**
     * 获取adapter绑定的数据
     */
    public ArrayList<OrderGoods> getData() {
        return dishesList;
    }

    /**
     * 获取adapter绑定的数据
     */
    public void setData(ArrayList<OrderGoods> orderGoods) {
        this.dishesList = orderGoods;
    }

    /**
     * 加入单个菜品的标签集合
     */
    public void adddishesDetail(LayoutInflater mInflater,
                                LinearLayout item_group, OrderGoods orderGoods) {
        item_group.removeAllViews();
        String iscomp = orderGoods.getIsComp();
        Log.i("adddishesDetail",
                orderGoods.getSalesName() + ":" + orderGoods.getIsComp());
        if (iscomp != null) {
            if (iscomp.equals("0")) {
                ArrayList<RemarkItem> remarkItemList = dishesEntity
                        .findRemarkItemList(orderGoods.getSalesId() + "",orderGoods.getInstanceId());
                LinearLayout layout = (LinearLayout) mInflater.inflate(
                        R.layout.dishes_selected_item_remark, null);
                item_group.addView(layout);
                FlowLayoutMargin item = (FlowLayoutMargin) layout
                        .findViewById(R.id.childdishesinfo_flowLayout);
                for (RemarkItem remarkItem : remarkItemList) {
                    TextView tv = (TextView) mInflater.inflate(
                            R.layout.dishes_selected_item_remark_title, item,
                            false);
                    tv.setText(remarkItem.getItemName());
                    item.addView(tv);
                }
            } else if (iscomp.equals("1")) {
                ArrayList<OrderGoods> compList = dishesEntity
                        .getCompDishesEntity().findAllOrderGoods(orderGoods);
                Log.i("adddishesDetail", orderGoods.getSalesName() + ":"
                        + compList.size());

                LinearLayout layout = (LinearLayout) mInflater.inflate(
                        R.layout.dishes_selected_item_remark, null);
                item_group.addView(layout);
                FlowLayoutMargin item = (FlowLayoutMargin) layout
                        .findViewById(R.id.childdishesinfo_flowLayout);
                for (OrderGoods ordergoods : compList) {
                    TextView tv = (TextView) mInflater.inflate(
                            R.layout.dishes_selected_item_remark_title, item,
                            false);
                    ArrayList<RemarkItem> kk = dishesEntity
                            .getCompDishesEntity().findDishesItem(ordergoods);
                    String remark = "";
                    if (kk.size() >= 1) {
                        remark += "(";
                        for (int i = 0; i < kk.size(); i++) {
                            remark += kk.get(i).getItemName();
                            if (i != kk.size() - 1)
                                remark += "、";
                        }
                        remark += ")";
                        tv.setText(ordergoods.getSalesName() + remark);
                    }else if(ordergoods.getRemark()!=null&&!ordergoods.getRemark().equals("")&&!ordergoods.getRemark().equals("[]")){
                        tv.setText(ordergoods.getSalesName() + "("+ordergoods.getRemark()+")");
                    }else{
                        tv.setText(ordergoods.getSalesName());
                    }
                    item.addView(tv);
                }
            }
        }
    }

    /**
     * DATA数据刷新
     */
    public void refreshData(ArrayList<OrderGoods> orderGoods) {
        this.dishesList = orderGoods;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView name;
        TextView count;
        TextView price;
        TextView state;
        TextView del;
        TextView add;
        TextView minus;
        LinearLayout remarkGroup, editGroup;

    }
}
