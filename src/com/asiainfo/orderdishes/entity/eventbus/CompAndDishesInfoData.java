package com.asiainfo.orderdishes.entity.eventbus;

import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;

public class CompAndDishesInfoData {
    public DishesInfo getDishesInfo() {
        return dishesInfo;
    }

    public void setDishesInfo(DishesInfo dishesInfo) {
        this.dishesInfo = dishesInfo;
    }

    public MerchantCompDishesInfo getCerchantCompDishesInfo() {
        return cerchantCompDishesInfo;
    }

    public void setCerchantCompDishesInfo(
            MerchantCompDishesInfo cerchantCompDishesInfo) {
        this.cerchantCompDishesInfo = cerchantCompDishesInfo;
    }

    private DishesInfo dishesInfo;
    private MerchantCompDishesInfo cerchantCompDishesInfo;

}
