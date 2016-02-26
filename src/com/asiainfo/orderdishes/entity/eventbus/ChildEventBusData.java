package com.asiainfo.orderdishes.entity.eventbus;

import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesType;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;
import com.asiainfo.orderdishes.util.BaseUtils;

import java.util.ArrayList;

public class ChildEventBusData {
    private DishesInfo dishesInfo;
    private DishesEntity dishesEntity;
    private BaseUtils baseUtils;
    private int type;
    private ArrayList<DishesType> info;
    private ArrayList<DishesInfo> dishes;
    private MerchantCompDishesInfo merchantCompDishesInfo;

    public DishesInfo getDishesInfo() {
        return dishesInfo;
    }

    public void setDishesInfo(DishesInfo dishesInfo) {
        this.dishesInfo = dishesInfo;
    }

    public DishesEntity getDishesEntity() {
        return dishesEntity;
    }

    public void setDishesEntity(DishesEntity dishesEntity) {
        this.dishesEntity = dishesEntity;
    }

    public BaseUtils getBaseUtils() {
        return baseUtils;
    }

    public void setBaseUtils(BaseUtils baseUtils) {
        this.baseUtils = baseUtils;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<DishesType> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<DishesType> info) {
        this.info = info;
    }

    public ArrayList<DishesInfo> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<DishesInfo> dishes) {
        this.dishes = dishes;
    }

    public MerchantCompDishesInfo getMerchantCompDishesInfo() {
        return merchantCompDishesInfo;
    }

    public void setMerchantCompDishesInfo(
            MerchantCompDishesInfo merchantCompDishesInfo) {
        this.merchantCompDishesInfo = merchantCompDishesInfo;
    }

}
