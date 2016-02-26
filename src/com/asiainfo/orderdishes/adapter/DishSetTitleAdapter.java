package com.asiainfo.orderdishes.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.helper.OnItemClickListener;

import java.util.ArrayList;

public class DishSetTitleAdapter extends BaseAdapter {
    private static final String TAG = DishSetTitleAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<String> mDataset;
    OnItemClickListener mOnItemClickListener;
    int selectedPos = 0;
    Resources mRes;
    public DisplayMetrics gMetrice;

    public DishSetTitleAdapter(Context mContext, ArrayList<String> mDataSet,
                               OnItemClickListener mOnItemClickListener, DisplayMetrics gMetrice) {
        this.mContext = mContext;
        this.mDataset = mDataSet;
        this.mOnItemClickListener = mOnItemClickListener;
        this.gMetrice = gMetrice;
        mRes = mContext.getResources();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.dishset_title_view_item, null);
            viewHolder.img_nav = (ImageView) convertView
                    .findViewById(R.id.item_image);
            viewHolder.tv_title = (TextView) convertView
                    .findViewById(R.id.item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText(mDataset.get(position));

        if (selectedPos == position) {
            viewHolder.tv_title.setTextColor(mRes.getColor(R.color.pink));
            viewHolder.tv_title.setTextSize(18);
            switch (isWhere(position)) {
                case 1:
                    viewHolder.tv_title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.img_nav.getLayoutParams();
                    lp.setMargins((int) (20 * gMetrice.density), 0, 0, 0);
                    viewHolder.img_nav.setImageResource(R.drawable.selected_left_s);
                    break;
                case 2:
                    viewHolder.img_nav.setImageResource(R.drawable.selected_middle_s);
                    viewHolder.tv_title.setGravity(Gravity.CENTER);
                    break;
                case 3:
                    LinearLayout.LayoutParams rp = (LinearLayout.LayoutParams) viewHolder.img_nav.getLayoutParams();
                    rp.setMargins(0, 0, (int) (20 * gMetrice.density), 0);
                    viewHolder.tv_title.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    viewHolder.img_nav.setImageResource(R.drawable.selected_right_s);
                    break;
                default:
                    break;
            }
        } else {
            switch (isWhere(position)) {
                case 1:
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.img_nav.getLayoutParams();
                    lp.setMargins((int) (20 * gMetrice.density), 0, 0, 0);
                    viewHolder.img_nav.setImageResource(R.drawable.selected_left_n);
                    viewHolder.tv_title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//				viewHolder.img_nav.setBackground(mContext.getResources().getDrawable(R.drawable.step_selected_left_n));
                    break;
                case 2:
                    viewHolder.tv_title.setGravity(Gravity.CENTER);
                    viewHolder.img_nav.setImageResource(R.drawable.selected_middle_n);
                    break;
                case 3:
                    LinearLayout.LayoutParams rp = (LinearLayout.LayoutParams) viewHolder.img_nav.getLayoutParams();
                    rp.setMargins(0, 0, (int) (20 * gMetrice.density), 0);
                    viewHolder.tv_title.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    viewHolder.img_nav.setImageResource(R.drawable.selected_right_n);
                    break;
                default:
                    break;
            }
            viewHolder.tv_title.setTextSize(14);
            viewHolder.tv_title.setTextColor(Color.parseColor("#5E5C5D"));
        }

		/*if (mOnItemClickListener != null) {
            convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickListener.onItemClick(v, position);
					Log.d(TAG, "you clicked Pos: " + position);
				}
			});
		}*/

        return convertView;
    }

    public void setSelectedPosNotify(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    private int isWhere(int position) {
        if (mDataset != null && mDataset.size() == 1 && position == 0) {
            // 只有1项的时候，显示中间选中
            return 2;
        }

        if (mDataset != null && mDataset.size() >= 2 && position == 0) {
            // 2项及以上，第一个
            return 1;
        }

        if (mDataset != null && mDataset.size() >= 2 && position != 0
                && position != mDataset.size() - 1) {
            // 2项及以上，不是第一个也不是最后一个，中间项
            return 2;
        }

        if (mDataset != null && mDataset.size() >= 2
                && position == mDataset.size() - 1) {
            // 2项以上，最后一个
            return 3;
        }

        return -1;
    }

    public void setSelectedPos(int position) {
        this.selectedPos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDataset != null && mDataset.size() > 0) {
            return mDataset.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDataset != null && mDataset.size() > 0) {
            return mDataset.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView img_nav;
        TextView tv_title;
    }
}
