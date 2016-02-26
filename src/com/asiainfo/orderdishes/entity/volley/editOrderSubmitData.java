package com.asiainfo.orderdishes.entity.volley;

import com.asiainfo.orderdishes.entity.litepal.DishesOrder;

import java.util.ArrayList;

public class editOrderSubmitData {
    public ArrayList<DishesOrder> getOrderInfo() {
        return info;
    }

    public void setOrderInfo(ArrayList<DishesOrder> orderInfo) {
        this.info = orderInfo;
    }

    ArrayList<DishesOrder> info;
}
