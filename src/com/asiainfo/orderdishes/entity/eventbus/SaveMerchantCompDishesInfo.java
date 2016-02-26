package com.asiainfo.orderdishes.entity.eventbus;

import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;

public class SaveMerchantCompDishesInfo {
    public MerchantCompDishesInfo getMerchantCompDishesInfo() {
        return merchantCompDishesInfo;
    }

    public void setMerchantCompDishesInfo(
            MerchantCompDishesInfo merchantCompDishesInfo) {
        this.merchantCompDishesInfo = merchantCompDishesInfo;
    }

    public DishesEntity getDishesEntity() {
        return dishesEntity;
    }

    public void setDishesEntity(DishesEntity dishesEntity) {
        this.dishesEntity = dishesEntity;
    }

    private MerchantCompDishesInfo merchantCompDishesInfo;
    private DishesEntity dishesEntity;
}
