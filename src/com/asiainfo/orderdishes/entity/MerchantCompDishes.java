package com.asiainfo.orderdishes.entity;

import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesType;

/**
 * 一条套餐单品菜--对应一条普通单品菜，但是有一些特殊属性
 * MerchantDishesInfo dishesInfo
 * 例如:咖喱鸡排饭
 * compId所属的套餐类型
 * dishesType套餐类型名称
 * 套餐单品菜的id:dishesId=dishesInfo.getDishesId(),他们对应的dishesid是相同的
 * 对应一条普通的单品菜MerchantDishesInfo dishesInfo
 * 2015-5-26 14:00
 *
 * @author gjr
 *         注：5.28日，不与服务器逻辑类同步，使用本地dishesinfo合并MerchantCompDishes和MerchantDishesInfo的逻辑实现
 */
public class MerchantCompDishes {

    private String compId;

    private String dishesType;

    private String dishesId;

    private long dishesNum;

    private long merchantId;

    private MerchantDishesInfo dishesInfo;

    private MerchantCompDishesType merchantCompDishesType;

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

    public String getDishesId() {
        return dishesId;
    }

    public void setDishesId(String dishesId) {
        this.dishesId = dishesId;
    }

    public long getDishesNum() {
        return dishesNum;
    }

    public void setDishesNum(long dishesNum) {
        this.dishesNum = dishesNum;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public MerchantDishesInfo getDishesInfo() {
        return dishesInfo;
    }

    public void setDishesInfo(MerchantDishesInfo dishesInfo) {
        this.dishesInfo = dishesInfo;
    }

    public MerchantCompDishesType getMerchantCompDishesType() {
        return merchantCompDishesType;
    }

    public void setMerchantCompDishesType(
            MerchantCompDishesType merchantCompDishesType) {
        this.merchantCompDishesType = merchantCompDishesType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;


}
