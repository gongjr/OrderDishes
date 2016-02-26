package com.asiainfo.orderdishes.adapter;

import android.content.Context;
import android.graphics.Color;
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

public class HallChangeDeskAdapter extends BaseAdapter {
    private static final String TAG = HallChangeDeskAdapter.class.getName();
    Context mContext;
    List<MerchantDesk> mdList;
    OnItemClickListener mOnItemClickListener;
    int selectedPos = -1;

    public HallChangeDeskAdapter(List<MerchantDesk> mdList, Context mContext) {
        this.mdList = mdList;
        this.mContext = mContext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView tv_deskId;
        TextView tv_deskName;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater lf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lf.inflate(R.layout.hall_change_desk_item, parent, false);
        }
        tv_deskId = viewHolder.get(convertView, R.id.desk_item_deskId);
        tv_deskName = viewHolder.get(convertView, R.id.desk_item_deskName);

        MerchantDesk md = mdList.get(position);
        tv_deskId.setText("ID-" + md.getDeskId());
        tv_deskName.setText(md.getDeskName()); //如，小桌2号
        if (position == selectedPos) {
            convertView.setBackgroundColor(Color.parseColor("#E7E6E3"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    setSelectedPos(position);
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

    public void setSelectedPosOnly(int pos) {
        this.selectedPos = pos;
    }

    public void refreshDataWithPos(List<MerchantDesk> mdList, int position) {
        this.selectedPos = position;
        this.mdList = mdList;
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

    public MerchantDesk getSelectedDesk() {
        if (mdList != null && mdList.size() > selectedPos) {
            return mdList.get(selectedPos);
        }
        return null;
    }
}