package com.asiainfo.orderdishes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.litepal.RemarkItem;
import com.asiainfo.orderdishes.ui.widget.FlowLayoutMargin;

import java.util.ArrayList;

public class DishesSubmitAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<OrderGoods> dishesList = new ArrayList<OrderGoods>();
    private DishesEntity dishesEntity;

    public DishesSubmitAdapter(Context context, DishesEntity dishesentity) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dishesEntity = dishesentity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dish_selected_lv_item,
                    parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.item_name);
            viewHolder.count = (TextView) convertView
                    .findViewById(R.id.item_count);
            viewHolder.price = (TextView) convertView
                    .findViewById(R.id.item_price);
            viewHolder.state = (TextView) convertView
                    .findViewById(R.id.item_state);
            viewHolder.remarkGroup = (LinearLayout) convertView
                    .findViewById(R.id.ordergoods_detail);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.state.setVisibility(View.GONE);
        viewHolder.price
                .setText("￥" + dishesList.get(position).getDishesPrice());
        String remarkold=dishesList.get(position).getRemark();
        if(remarkold!=null&&!remarkold.equals("")&&!remarkold.equals("[]")&&!dishesList.get(position).getIsComp().equals("1")){
            String remark=remarkold.replace("[","").replace("]","");
            viewHolder.name.setText(dishesList.get(position).getSalesName()+" ("+remark+")");
        }else viewHolder.name.setText(dishesList.get(position).getSalesName());
        viewHolder.count.setText("* " + dishesList.get(position).getSalesNum());
        adddishesDetail(mInflater, viewHolder.remarkGroup,
                dishesList.get(position));
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
     * 获取adapter绑定的数据
     */
    public ArrayList<OrderGoods> getData() {
        return dishesList;
    }

    /**
     * 设置adapter绑定的数据
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
        if (iscomp != null) {
            if (iscomp.equals("0")) {

            } else if (iscomp.equals("1")) {
                ArrayList<OrderGoods> compList = dishesEntity
                        .getCompDishesEntity().findAllOrderGoods(orderGoods);
                LinearLayout layout = (LinearLayout) mInflater.inflate(
                        R.layout.dishes_selected_item_remark, null);
                item_group.addView(layout);
                FlowLayoutMargin item = (FlowLayoutMargin) layout
                        .findViewById(R.id.childdishesinfo_flowLayout);
                for (OrderGoods ordergoods : compList) {
                    TextView tv = (TextView) mInflater.inflate(
                            R.layout.dishes_selected_item_remark_title, item,
                            false);
                    if(ordergoods.getRemark()!=null&&!ordergoods.getRemark().equals("")&&!ordergoods.getRemark().equals("[]")){
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
