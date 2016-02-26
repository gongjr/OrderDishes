package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * 数据模型分层级别：套餐3
 * 一种套餐组合类型，例如(主食【四选二】)
 * 里面规范了本条套餐组合的所属类型compId，名称dishesType
 * 约束规范，可选最大值：maxSelect 组合总数:dishesCount
 * 以及对应的套餐单菜品集合ArrayList<DishesInfo> compDishesList
 * 例如:dishesType=“主食”，对应的套餐单品菜，（猪排饭、鸡排饭、牛排饭、咖喱鸡排饭）
 * dishesCount=compDishesList.size()
 * 2015-5-26 14:00
 *
 * @author gjr
 */
public class MerchantCompDishesType extends DataSupport {

    private String compId;

    private String dishesType;

    private long maxSelect;

    private long dishesCount;

    private long merchantId;

    private String dishesTypeName;

    private ArrayList<DishesInfo> dishesInfoList = new ArrayList<DishesInfo>();

    private MerchantCompDishesInfo merchantCompDishesInfo;

    public MerchantCompDishesInfo getMerchantCompDishesInfo() {
        return merchantCompDishesInfo;
    }

    public void setMerchantCompDishesInfo(
            MerchantCompDishesInfo merchantCompDishesInfo) {
        this.merchantCompDishesInfo = merchantCompDishesInfo;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getDishesType() {
        return dishesType;
    }

    public void setDishesType(String dishesType) {
        this.dishesType = dishesType;
    }

    public long getMaxSelect() {
        return maxSelect;
    }

    public void setMaxSelect(long maxSelect) {
        this.maxSelect = maxSelect;
    }

    public long getDishesCount() {
        return dishesCount;
    }

    public void setDishesCount(long dishesCount) {
        this.dishesCount = dishesCount;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getDishesTypeName() {
        return dishesTypeName;
    }

    public void setDishesTypeName(String dishesTypeName) {
        this.dishesTypeName = dishesTypeName;
    }

    public ArrayList<DishesInfo> getDishesInfoList() {
        return dishesInfoList;
    }

    public void setDishesInfoList(ArrayList<DishesInfo> compDishesList) {
        this.dishesInfoList = compDishesList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    /**
     * 查询填充对应的ArrayList<DishesInfo>
     *
     * @return
     */
    public ArrayList<DishesInfo> getItemlistDb() {
        this.dishesInfoList = (ArrayList<DishesInfo>) DataSupport.where("merchantcompdishestype_id = ?", String.valueOf(id)).find(DishesInfo.class);
        return dishesInfoList;
    }
}
