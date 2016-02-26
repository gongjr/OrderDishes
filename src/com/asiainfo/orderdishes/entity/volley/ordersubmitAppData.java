package com.asiainfo.orderdishes.entity.volley;

import com.asiainfo.orderdishes.entity.litepal.DishesOrder;

public class ordersubmitAppData {
    public DishesOrder getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(DishesOrder orderInfo) {
        this.orderInfo = orderInfo;
    }

    DishesOrder orderInfo;
}
