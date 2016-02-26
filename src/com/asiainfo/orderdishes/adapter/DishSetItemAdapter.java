package com.asiainfo.orderdishes.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.orderdishes.R;
import com.asiainfo.orderdishes.entity.CompDishesEntity;
import com.asiainfo.orderdishes.entity.CompDishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesItem;
import com.asiainfo.orderdishes.entity.litepal.DishesItemType;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesType;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.litepal.RemarkItem;
import com.asiainfo.orderdishes.helper.OnItemClickListener;
import com.asiainfo.orderdishes.ui.widget.FlowLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class DishSetItemAdapter extends Adapter<DishSetItemAdapter.ViewHolder> {
    /**
     * 菜单显示图片的加载参数
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.dishes_default)
            .showImageOnFail(R.drawable.dishes_default)
            .showImageForEmptyUri(R.drawable.dishes_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .displayer(new RoundedBitmapDisplayer(20))
            .displayer(new FadeInBitmapDisplayer(100))
            .build();
    private static final String TAG = DishSetItemAdapter.class.getName();
    private Context mContext;
    private Resources mRes;
    LayoutInflater mInflater;
    //	List<Object> mDataList;
    OnItemClickListener mOnItemClickListener;
    //	OnItemClickListener mOnDishSetItemFlagClickListener;
    OnPropertyItemCheckedChangedListener mOnPropertyItemCheckedChangedListener;
    private int selectedPos = -1;
    private int oldSelectedPos = -1;
    private MerchantCompDishesType merchantCompDishesType;
    //	private ArrayList<HashMap<DishesItem, CompoundButton>> checkDishesItem=new ArrayList<HashMap<DishesItem, CompoundButton>>();
    private boolean CheckType;
    private SparseArray<View> viewHolders = new SparseArray<View>();
    private SparseArray<View> checkDishes = new SparseArray<View>();
    private CompDishesEntity compDishesEntity;
    private CompDishesInfo compDishesInfo;

    public DishSetItemAdapter(MerchantCompDishesType merchantcompDishesType, Context mContext, CompDishesEntity dishesEntity, CompDishesInfo compDishesInfo) {
        this.merchantCompDishesType = merchantcompDishesType;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mRes = mContext.getResources();
        this.compDishesEntity = dishesEntity;
        this.compDishesInfo = compDishesInfo;
        if (merchantCompDishesType.getMaxSelect() > 1) CheckType = false;
        else CheckType = true;
        Log.i("CheckType", "CheckType:" + CheckType);
    }

    public void setOnDataSetItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnPropertyItemCheckedChangedListener(OnPropertyItemCheckedChangedListener mOnPropertyItemCheckedChangedListener) {
        this.mOnPropertyItemCheckedChangedListener = mOnPropertyItemCheckedChangedListener;
    }

    @Override
    public int getItemCount() {
        return merchantCompDishesType.getDishesInfoList().size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginTop = TypedValue.COMPLEX_UNIT_DIP * 3;
        ll_params.setMargins(0, marginTop, 0, 0);
        holder.ll_properties.removeAllViews();
        ImageLoader.getInstance().displayImage(
                merchantCompDishesType.getDishesInfoList().get(position).getDishesUrl(), holder.img_image, options);
        holder.tv_name.setText(merchantCompDishesType.getDishesInfoList().get(position).getDishesName());
        ;
        holder.tv_price.setText("￥ " + merchantCompDishesType.getDishesInfoList().get(position).getDishesPrice());
        ConcurrentHashMap<DishesItem, CompoundButton> position_checkDishesItem = new ConcurrentHashMap<DishesItem, CompoundButton>();
        ArrayList<OrderGoods> selectedOrderGoods = compDishesEntity.findOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
        if (selectedOrderGoods.size() >= 1) {
            if (CheckType && selectedOrderGoods.size() == 1 && merchantCompDishesType.getDishesInfoList().get(position).getDishesId().equals(selectedOrderGoods.get(0).getSalesId() + ""))
                selectedPos = position;
            checkDishes.put(position, holder.ll_properties);
            holder.mView.setTag(R.id.tag_first, true);
            holder.ll_properties.setTag(R.id.tag_first, true);
            holder.mView.setBackground(mRes.getDrawable(R.anim.dish_set_item_selected_bg));
            holder.img_isSelected.setImageResource(R.drawable.checkbox_select_s);
            holder.img_isSelected.setVisibility(View.VISIBLE);
        } else {
            holder.mView.setBackground(mRes.getDrawable(R.anim.dish_set_item_normal_bg));
            holder.img_isSelected.setImageResource(R.drawable.checkbox_select_n);
            holder.img_isSelected.setVisibility(View.GONE);
            holder.mView.setTag(R.id.tag_first, false);
            holder.ll_properties.setTag(R.id.tag_first, false);
        }


        holder.mView.setTag(R.id.tag_second, holder.img_isSelected);
        holder.mView.setTag(R.id.tag_three, holder.ll_properties);
        holder.mView.setTag(R.id.tag_four, position_checkDishesItem);
        holder.mView.setTag(R.id.tag_five, merchantCompDishesType);
        viewHolders.put(position, holder.mView);


        holder.ll_properties.setTag(R.id.tag_second, holder.img_isSelected);
        holder.ll_properties.setTag(R.id.tag_three, holder.mView);
        holder.ll_properties.setTag(R.id.tag_four, position_checkDishesItem);
        holder.ll_properties.setTag(R.id.tag_five, merchantCompDishesType);

        //套餐项整体设置监听
        holder.mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = (Boolean) v.getTag(R.id.tag_first);
                ImageView isSelected = (ImageView) v.getTag(R.id.tag_second);
                LinearLayout ll_properties = (LinearLayout) v.getTag(R.id.tag_three);
                ConcurrentHashMap<DishesItem, CompoundButton> ss = (ConcurrentHashMap<DishesItem, CompoundButton>) v.getTag(R.id.tag_four);
                MerchantCompDishesType merchantCompDishesType = (MerchantCompDishesType) v.getTag(R.id.tag_five);
                if (!flag) {
                    if (CheckType) {
                        //单选
                        oldSelectedPos = selectedPos;
                        selectedPos = position;
                        //如果旧的已选中菜项不为-1，表示当前单选菜有以选中项，应该先取消该菜，删除数据
                        if (oldSelectedPos != -1) {
                            View old_view = viewHolders.get(oldSelectedPos);
//							删除旧菜，刷新已选中视图为未选中
                            boolean old_flag = (Boolean) old_view.getTag(R.id.tag_first);
                            ImageView old_isSelected = (ImageView) old_view.getTag(R.id.tag_second);
                            LinearLayout old_ll_properties = (LinearLayout) old_view.getTag(R.id.tag_three);
                            ConcurrentHashMap<DishesItem, CompoundButton> old_ss = (ConcurrentHashMap<DishesItem, CompoundButton>) old_view.getTag(R.id.tag_four);
                            MerchantCompDishesType old_merchantCompDishesType = (MerchantCompDishesType) old_view.getTag(R.id.tag_five);
                            if (old_flag) {
                                checkDishes.delete(position);
                                compDishesEntity.decreaseOrderGoods(old_merchantCompDishesType.getDishesInfoList().get(oldSelectedPos), compDishesInfo);
                                old_view.setTag(R.id.tag_first, !old_flag);
                                old_ll_properties.setTag(R.id.tag_first, !old_flag);
                                old_view.setBackground(mRes.getDrawable(R.anim.dish_set_item_normal_bg));
                                old_isSelected.setImageResource(R.drawable.checkbox_select_n);
                                old_isSelected.setVisibility(View.GONE);
                                Iterator<Entry<DishesItem, CompoundButton>> iter = old_ss.entrySet().iterator();
                                while (iter.hasNext()) {
//								compDishesEntity.decreaseOrderGoodsRemarkItem();
                                    iter.next().getValue().setChecked(false);
                                }
                                old_ss.clear();
                            }
                        }
//						compDishesEntity.addOrderGoods(merchantCompDishesType.getCompDishesList().get(position));
                        checkDishes.put(position, v);
                        compDishesEntity.addOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                        v.setTag(R.id.tag_first, !flag);
                        ll_properties.setTag(R.id.tag_first, !flag);
                        v.setBackground(mRes.getDrawable(R.anim.dish_set_item_selected_bg));
                        isSelected.setImageResource(R.drawable.checkbox_select_s);
                        isSelected.setVisibility(View.VISIBLE);
                    } else {
                        if (checkDishes.size() < merchantCompDishesType.getMaxSelect()) {
                            compDishesEntity.addOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                            checkDishes.put(position, v);
                            v.setTag(R.id.tag_first, !flag);
                            ll_properties.setTag(R.id.tag_first, !flag);
                            v.setBackground(mRes.getDrawable(R.anim.dish_set_item_selected_bg));
                            isSelected.setImageResource(R.drawable.checkbox_select_s);
                            isSelected.setVisibility(View.VISIBLE);
                        } else {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(v, position);
//								 Toast.makeText(mContext, "超出最大可选数", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //多选
                    }
                } else {
                    //取消选中状态的时候，统一清除其视图和数据，不用区分
                    compDishesEntity.decreaseOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                    checkDishes.delete(position);
                    v.setTag(R.id.tag_first, !flag);
                    ll_properties.setTag(R.id.tag_first, !flag);
                    v.setBackground(mRes.getDrawable(R.anim.dish_set_item_normal_bg));
                    isSelected.setImageResource(R.drawable.checkbox_select_n);
                    isSelected.setVisibility(View.GONE);
                    Iterator<Entry<DishesItem, CompoundButton>> iter = ss.entrySet().iterator();
                    while (iter.hasNext()) {
                        iter.next().getValue().setChecked(false);
                    }
                    ss.clear();
                }
                /*if(mOnItemClickListener!=null){
					 mOnItemClickListener.onItemClick(v, position);
//					 Toast.makeText(mContext, "click entire", Toast.LENGTH_SHORT).show();
				}*/
            }
        });
        holder.ll_properties.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = (Boolean) v.getTag(R.id.tag_first);
                ImageView isSelected = (ImageView) v.getTag(R.id.tag_second);
                View ll_properties = (View) v.getTag(R.id.tag_three);
                ConcurrentHashMap<DishesItem, CompoundButton> ss = (ConcurrentHashMap<DishesItem, CompoundButton>) v.getTag(R.id.tag_four);
                MerchantCompDishesType merchantCompDishesType = (MerchantCompDishesType) v.getTag(R.id.tag_five);
                if (!flag) {

                    if (CheckType) {
                        //单选
                        oldSelectedPos = selectedPos;
                        selectedPos = position;
                        //如果旧的已选中菜项不为-1，表示当前单选菜有以选中项，应该先取消该菜，删除数据
                        if (oldSelectedPos != -1) {
                            View old_view = viewHolders.get(oldSelectedPos);
//							删除旧菜，刷新已选中视图为未选中
                            boolean old_flag = (Boolean) old_view.getTag(R.id.tag_first);
                            ImageView old_isSelected = (ImageView) old_view.getTag(R.id.tag_second);
                            LinearLayout old_ll_properties = (LinearLayout) old_view.getTag(R.id.tag_three);
                            ConcurrentHashMap<DishesItem, CompoundButton> old_ss = (ConcurrentHashMap<DishesItem, CompoundButton>) old_view.getTag(R.id.tag_four);
                            MerchantCompDishesType old_merchantCompDishesType = (MerchantCompDishesType) old_view.getTag(R.id.tag_five);
                            if (old_flag) {
                                checkDishes.delete(position);
                                compDishesEntity.decreaseOrderGoods(old_merchantCompDishesType.getDishesInfoList().get(oldSelectedPos), compDishesInfo);
                                old_view.setTag(R.id.tag_first, !old_flag);
                                old_ll_properties.setTag(R.id.tag_first, !old_flag);
                                old_view.setBackground(mRes.getDrawable(R.anim.dish_set_item_normal_bg));
                                old_isSelected.setImageResource(R.drawable.checkbox_select_n);
                                old_isSelected.setVisibility(View.GONE);
                                Iterator<Entry<DishesItem, CompoundButton>> iter = old_ss.entrySet().iterator();
                                while (iter.hasNext()) {
                                    iter.next().getValue().setChecked(false);
                                }
                                old_ss.clear();
                            }
                        }
                        checkDishes.put(position, v);
                        compDishesEntity.addOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                        v.setTag(R.id.tag_first, !flag);
                        ll_properties.setTag(R.id.tag_first, !flag);
                        ll_properties.setBackground(mRes.getDrawable(R.anim.dish_set_item_selected_bg));
                        isSelected.setImageResource(R.drawable.checkbox_select_s);
                        isSelected.setVisibility(View.VISIBLE);

                    } else {
                        if (checkDishes.size() < merchantCompDishesType.getMaxSelect()) {
                            compDishesEntity.addOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                            checkDishes.put(position, ll_properties);
                            v.setTag(R.id.tag_first, !flag);
                            ll_properties.setTag(R.id.tag_first, !flag);
                            ll_properties.setBackground(mRes.getDrawable(R.anim.dish_set_item_selected_bg));
                            isSelected.setImageResource(R.drawable.checkbox_select_s);
                            isSelected.setVisibility(View.VISIBLE);
                            //多选
                        } else {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(v, position);
//								 Toast.makeText(mContext, "超出最大可选数", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else {
//					checkDishes.put(position, ll_properties);
                    compDishesEntity.decreaseOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                    checkDishes.delete(position);
                    v.setTag(R.id.tag_first, !flag);
                    ll_properties.setTag(R.id.tag_first, !flag);
                    ll_properties.setBackground(mRes.getDrawable(R.anim.dish_set_item_normal_bg));
                    isSelected.setImageResource(R.drawable.checkbox_select_n);
                    isSelected.setVisibility(View.GONE);
                    Iterator<Entry<DishesItem, CompoundButton>> iter = ss.entrySet().iterator();
                    while (iter.hasNext()) {
                        iter.next().getValue().setChecked(false);
                    }
                    ss.clear();
                }
			/*if(mOnItemClickListener!=null){
				 mOnItemClickListener.onItemClick(v, position);
//				 Toast.makeText(mContext, "click entire", Toast.LENGTH_SHORT).show();
			}*/
            }
        });
        ArrayList<DishesItemType> pp = merchantCompDishesType.getDishesInfoList().get(position).getDishesItemList();
        for (int j = 0; j < pp.size(); j++) {
            ViewGroup propertyItemView = (ViewGroup) mInflater.inflate(R.layout.property_item, null);
            FlowLayout propItemsView = (FlowLayout) propertyItemView.findViewById(R.id.property_items);
            ViewGroup.LayoutParams vl_params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ArrayList<DishesItem> ll = pp.get(j).getItemlist();
            TextView itemType = (TextView) propertyItemView.findViewById(R.id.peoperty_title);
            itemType.setText(pp.get(j).getItemTypeName() + ":");
            for (int m = 0; m < ll.size(); m++) {
                CheckBox tasetItemView = (CheckBox) mInflater.inflate(R.layout.taste_item, null);
                tasetItemView.setTag(R.id.tag_first, holder.mView);
                tasetItemView.setTag(R.id.tag_second, holder.img_isSelected);
                tasetItemView.setTag(R.id.tag_three, holder.ll_properties);
                ll.get(m).setDishesId(merchantCompDishesType.getDishesInfoList().get(position).getDishesId());
                ll.get(m).setItemType(pp.get(j).getItemType());
                ll.get(m).setItemTypeName(pp.get(j).getItemTypeName());
                tasetItemView.setTag(R.id.tag_four, ll.get(m));
                tasetItemView.setTag(R.id.tag_five, position_checkDishesItem);
                ArrayList<RemarkItem> ss = compDishesEntity.findDishesItem(ll.get(m), compDishesInfo);
                if (ss.size() >= 1) {
                    tasetItemView.setChecked(true);
                    position_checkDishesItem.put(ll.get(m), tasetItemView);
                }
                tasetItemView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        View mView = (View) buttonView.getTag(R.id.tag_first);
                        ImageView img_isSelected = (ImageView) buttonView.getTag(R.id.tag_second);
                        LinearLayout ll_properties = (LinearLayout) buttonView.getTag(R.id.tag_three);
                        boolean flag = (Boolean) mView.getTag(R.id.tag_first);
                        DishesItem dishesItme = (DishesItem) buttonView.getTag(R.id.tag_four);
                        ConcurrentHashMap<DishesItem, CompoundButton> ss = (ConcurrentHashMap<DishesItem, CompoundButton>) buttonView.getTag(R.id.tag_five);
                        //当标签项变为选中的时候，如果该菜没有被选中，则将显示状态改为选中
                        if (isChecked) {

                            //保持同类型口味单选检测，之后再做其他判断
                            Iterator<Entry<DishesItem, CompoundButton>> iter_ss = ss.entrySet().iterator();
                            while (iter_ss.hasNext()) {
                                Entry<DishesItem, CompoundButton> curiter=iter_ss.next();
                                if(curiter.getKey().getItemType().equals(dishesItme.getItemType()))
                                    curiter.getValue().setChecked(false);
                            }

                            if (!flag) {
                                if (CheckType) {
                                    //单选
                                    oldSelectedPos = selectedPos;
                                    selectedPos = position;
                                    //如果旧的已选中菜项不为-1，表示当前单选菜有以选中项，应该先取消该菜，删除数据
                                    if (oldSelectedPos != -1) {
                                        View old_view = viewHolders.get(oldSelectedPos);
//									删除旧菜，刷新已选中视图为未选中
                                        boolean old_flag = (Boolean) old_view.getTag(R.id.tag_first);
                                        ImageView old_isSelected = (ImageView) old_view.getTag(R.id.tag_second);
                                        LinearLayout old_ll_properties = (LinearLayout) old_view.getTag(R.id.tag_three);
                                        ConcurrentHashMap<DishesItem, CompoundButton> old_ss = (ConcurrentHashMap<DishesItem, CompoundButton>) old_view.getTag(R.id.tag_four);
                                        MerchantCompDishesType old_merchantCompDishesType = (MerchantCompDishesType) old_view.getTag(R.id.tag_five);
                                        if (old_flag) {
                                            checkDishes.delete(position);
                                            compDishesEntity.decreaseOrderGoods(merchantCompDishesType.getDishesInfoList().get(oldSelectedPos), compDishesInfo);
                                            old_view.setTag(R.id.tag_first, !old_flag);
                                            old_ll_properties.setTag(R.id.tag_first, !old_flag);
                                            old_view.setBackground(mRes.getDrawable(R.anim.dish_set_item_normal_bg));
                                            old_isSelected.setImageResource(R.drawable.checkbox_select_n);
                                            old_isSelected.setVisibility(View.GONE);
                                            Iterator<Entry<DishesItem, CompoundButton>> iter = old_ss.entrySet().iterator();
                                            while (iter.hasNext()) {
                                                iter.next().getValue().setChecked(false);
                                            }
                                            compDishesEntity.delallDishesItem(merchantCompDishesType.getDishesInfoList().get(oldSelectedPos).getDishesId(), compDishesInfo);
                                            old_ss.clear();
                                        }
                                    }
                                    checkDishes.put(position, ll_properties);
                                    compDishesEntity.addOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                                    mView.setTag(R.id.tag_first, !flag);
                                    ll_properties.setTag(R.id.tag_first, !flag);
                                    mView.setBackground(mRes.getDrawable(R.anim.dish_set_item_selected_bg));
                                    img_isSelected.setImageResource(R.drawable.checkbox_select_s);
                                    img_isSelected.setVisibility(View.VISIBLE);

                                } else {

                                    if (checkDishes.size() < merchantCompDishesType.getMaxSelect()) {
                                        compDishesEntity.addOrderGoods(merchantCompDishesType.getDishesInfoList().get(position), compDishesInfo);
                                        checkDishes.put(position, ll_properties);
                                        mView.setTag(R.id.tag_first, !flag);
                                        ll_properties.setTag(R.id.tag_first, !flag);
                                        mView.setBackground(mRes.getDrawable(R.anim.dish_set_item_selected_bg));
                                        img_isSelected.setImageResource(R.drawable.checkbox_select_s);
                                        img_isSelected.setVisibility(View.VISIBLE);
                                        //多选
                                    } else {
                                        if (mOnItemClickListener != null) {
                                            mOnPropertyItemCheckedChangedListener.onCheckedChanged(buttonView, null, isChecked, position);
//										 Toast.makeText(mContext, "超出最大可选数", Toast.LENGTH_SHORT).show();
                                        }
                                        buttonView.setChecked(!isChecked);
                                        if (ss.containsKey(dishesItme))
                                            ss.remove(dishesItme);
                                        compDishesEntity.delDishesItem(dishesItme, compDishesInfo);

                                    }
                                    //多选
                                }
                            } else {
							/*mView.setTag(R.id.tag_first,!flag);
							ll_properties.setTag(R.id.tag_first,!flag);
							mView.setBackground(mRes.getDrawable(R.anim.dish_set_item_normal_bg));
							img_isSelected.setImageResource(R.drawable.checkbox_select_n);
							img_isSelected.setVisibility(View.GONE);*/
                            }
                            if (!ss.containsKey(dishesItme))
                                ss.put(dishesItme, buttonView);
                            compDishesEntity.addDishesItem(dishesItme, compDishesInfo);
                            Log.i("checkDishesItem", "dishesItme:" + dishesItme);
                        } else {
                            if (ss.containsKey(dishesItme))
                                ss.remove(dishesItme);
                            compDishesEntity.delDishesItem(dishesItme, compDishesInfo);
                            Log.i("checkDishesItem", "dishesItme:" + dishesItme);
                        }
						/*if(mOnPropertyItemCheckedChangedListener!=null){
							 mOnPropertyItemCheckedChangedListener.onCheckedChanged(buttonView, null, isChecked, position);
//							 Toast.makeText(mContext, "click state isChecked: " + isChecked, Toast.LENGTH_SHORT).show();
						 }*/
                    }
                });
                tasetItemView.setText(ll.get(m).getItemName());
                propItemsView.addView(tasetItemView, vl_params);
            }
            holder.ll_properties.addView(propertyItemView, ll_params);
        }
    }

    void setData(MerchantCompDishesType merchantcompDishesType) {
        this.merchantCompDishesType = merchantcompDishesType;
    }

    public void refreshbyData(MerchantCompDishesType merchantcompDishesType) {
        this.merchantCompDishesType = merchantcompDishesType;
        this.oldSelectedPos = -1;
        checkDishes.clear();
//		this.selectedPos=-1;
        if (merchantCompDishesType.getMaxSelect() > 1) CheckType = false;
        else CheckType = true;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.meal_set_dish_item, null);
        ViewHolder holder = new ViewHolder(view);
        holder.setIsRecyclable(true);
        return holder;
    }

    /**
     * 刷新数据前，先删除对应的视图
     *
     * @param v
     * @param startRemoveIndex
     */
    private void removeViewsBeforeRefresh(ViewGroup v, int startRemoveIndex) {
        int count = v.getChildCount();
        if (count > 0 && count > startRemoveIndex) {
            v.removeViews(startRemoveIndex, count - startRemoveIndex);
        }
    }

    /**
     * 只刷新特定区间的数据显示项，提升效率
     *
     * @param pos
     */
    public void setSelectedPos(int pos) {
        int startItem = 0;
        int changeCount = 0;
        if (merchantCompDishesType.getDishesInfoList() != null && merchantCompDishesType.getDishesInfoList().size() > 0) {
            changeCount = merchantCompDishesType.getDishesInfoList().size();
        }
        if (selectedPos != pos) {
            if (selectedPos > pos) {
                startItem = pos;
                changeCount = selectedPos - pos + 1;
            } else {
                startItem = selectedPos;
                changeCount = pos - selectedPos + 1;
            }
            selectedPos = pos;
//			notifyDataSetChanged();
//			notifyItemChanged(pos);
            notifyItemRangeChanged(startItem, changeCount);
        }
    }

    /**
     * 套餐属性项状态监听器
     */
    public interface OnPropertyItemCheckedChangedListener<T> {
        public void onCheckedChanged(View v, T t, Boolean checked, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView img_image;
        public TextView tv_name;
        public TextView tv_price;

        public LinearLayout ll_properties;
        public LinearLayout ll_consists;
        public TextView tv_consistsTitle;
        public TextView tv_consistsContent;
        public ImageView img_isSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.dish_content);
            img_image = (ImageView) itemView.findViewById(R.id.dish_img);
            tv_name = (TextView) itemView.findViewById(R.id.dish_name);
            tv_price = (TextView) itemView.findViewById(R.id.dish_price);
            ll_properties = (LinearLayout) itemView.findViewById(R.id.ll_properties);
            ll_consists = (LinearLayout) itemView.findViewById(R.id.consists_ll);
            tv_consistsTitle = (TextView) itemView.findViewById(R.id.consists_title);
            tv_consistsContent = (TextView) itemView.findViewById(R.id.consists_content);
            img_isSelected = (ImageView) itemView.findViewById(R.id.isSelected);
        }
    }

    /**
     * @return
     */
    public boolean isSelectComplte() {
        if (checkDishes.size() == merchantCompDishesType.getMaxSelect()) return true;
        else return false;
    }

}