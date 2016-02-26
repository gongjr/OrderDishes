package com.asiainfo.orderdishes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.litepal.MerchantDesk;
import com.asiainfo.orderdishes.helper.OnItemClickListener;

import java.util.List;

public class HallDeskAdapter extends BaseAdapter {
    // 餐桌状态: 0空闲可用，1正在点菜，2正在用餐，3预定中,5并桌
    LayoutInflater lf;
    Context mContext;
    List<MerchantDesk> mdList;
    OnItemClickListener mOnItemClickListener;
    int selectedPos = -1;
    int changeDeskToIndex = -1;

    public HallDeskAdapter(List<MerchantDesk> mdList, Context mContext) {
        this.mdList = mdList;
        this.mContext = mContext;
        lf = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView merchantDesk;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = lf.inflate(R.layout.hall_table_item, null);
        }
        merchantDesk = viewHolder.get(convertView, R.id.table_item);

        MerchantDesk md = mdList.get(position);

        if (md.getDeskType() != null && md.getDeskType().equals("包间")) {
            String dn = md.getDeskName();
            if (dn != null && dn.length() > 2) {
                merchantDesk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                String dh = dn.substring(0, 2);
                String dt = dn.substring(2);
                merchantDesk.setText(dh + "\n" + dt);
            } else {
                merchantDesk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                merchantDesk.setText(md.getDeskName());
            }
        } else {
            merchantDesk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            merchantDesk.setText(md.getDeskName());
        }

        if (position == selectedPos) {
            merchantDesk.setBackgroundResource(R.drawable.table_item_bg_s);
            merchantDesk.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            // 餐桌状态: 0空闲可用，1正在点菜，2正在用餐，3预定中,5并桌
            switch (md.getDeskStateValue()) {
                case 0:
                    merchantDesk.setBackgroundResource(R.drawable.table_item_bg_n);
                    merchantDesk.setTextColor(mContext.getResources().getColor(
                            R.color.table_table_t_normal));
                    break;
                case 1:
                    merchantDesk.setBackgroundResource(R.drawable.table_item_bg_yes);
                    merchantDesk.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 2:
                    merchantDesk.setBackgroundResource(R.drawable.table_item_bg_yes);
                    merchantDesk.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 3:
                    merchantDesk.setBackgroundResource(R.drawable.table_item_bgtablelock);
                    merchantDesk.setTextColor(Color.parseColor("#bfbfbf"));
                    break;
                default:
                    merchantDesk.setBackgroundResource(R.drawable.table_item_bgtablelock);
                    merchantDesk.setTextColor(Color.parseColor("#bfbfbf"));
                    break;
            }
        }
        merchantDesk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        if (mdList != null && mdList.size() > 0) {
            return mdList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mdList != null && mdList.size() > 0) {
            return mdList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void refreshDataWithPos(List<MerchantDesk> mdList, int position) {
        this.selectedPos = position;
        this.mdList = mdList;
        notifyDataSetChanged();
    }

    /**
     * 不同区域换桌，刷新数据时，选中桌子
     *
     * @param tiList
     * @param curItem
     * @param toItem
     */
    public void refreshDataWithItems(List<MerchantDesk> mdList,
                                     MerchantDesk curDesk, MerchantDesk toDesk) {
        this.mdList = mdList;
        selectedPos = -1;
        changeDeskToIndex = -1;
        if (mdList != null && mdList.size() > 0) {
            for (int i = 0; i < mdList.size(); i++) {
                MerchantDesk md = mdList.get(i);
                if (curDesk != null
                        && curDesk.getLocationCode().equals(
                        md.getLocationCode())
                        && curDesk.getDeskName().equals(md.getDeskName())) {
                    selectedPos = i;
                }
                if (toDesk != null
                        && toDesk.getLocationCode()
                        .equals(md.getLocationCode())
                        && toDesk.getDeskName().equals(md.getDeskName())) {
                    changeDeskToIndex = i;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void refreshData(List<MerchantDesk> mdList) {
        this.mdList = mdList;
        notifyDataSetChanged();
    }

    public void setMOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setSelectedPos(int pos) {
        this.selectedPos = pos;
        notifyDataSetChanged();
    }

    /**
     * @param position
     * @return
     */
    public MerchantDesk changeDeskSelectSecond(int position) {
        changeDeskToIndex = position;
        if (mdList != null && mdList.size() > position) {
            notifyDataSetChanged();
            return mdList.get(position);
        }
        return null;
    }

    /**
     * 清空选择
     */
    public void clearSelectedChangeTables() {
        changeDeskToIndex = -1;
        notifyDataSetChanged();
    }
}