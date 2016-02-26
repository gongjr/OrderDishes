package com.asiainfo.orderdishes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.helper.OnItemClickListener;

import java.util.ArrayList;

public class NumTipsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private OnItemClickListener OnItemClickListener;
    private int selectedpos = 0;
    private Context mContext;
    private ArrayList listoo;

    public NumTipsAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 这里将数据赋予Adapter
     */
    public void setScreenData(ArrayList screenData) {
        listoo = screenData;
    }

    public int getCount() {
        return listoo.size();
    }

    public Object getItem(int position) {
        return listoo.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 该函数中将数据和View进行关联
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.icon_view, null);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_view_img);
        if (selectedpos == position) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.num_icon_n));
        } else {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.num_icon_s));
        }
        if (OnItemClickListener != null) {
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    OnItemClickListener.onItemClick(v, position);
                }
            });
        }
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        this.OnItemClickListener = OnItemClickListener;
    }

    public void setSelectedPos(int seletedpos) {
        selectedpos = seletedpos;
        notifyDataSetChanged();
    }

} 
