package com.asiainfo.orderdishes.entity.eventbus;

import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;

public class CompDishesEventData {
    private MerchantCompDishesInfo merchantCompDishesInfo;
    private DishesOrder dishesOrder;

    public MerchantCompDishesInfo getMerchantCompDishesInfo() {
        return merchantCompDishesInfo;
    }

    public void setMerchantCompDishesInfo(
            MerchantCompDishesInfo merchantCompDishesInfo) {
        this.merchantCompDishesInfo = merchantCompDishesInfo;
    }

    public DishesOrder getDishesOrder() {
        return dishesOrder;
    }

    public void setDishesOrder(DishesOrder dishesOrder) {
        this.dishesOrder = dishesOrder;
    }

}
