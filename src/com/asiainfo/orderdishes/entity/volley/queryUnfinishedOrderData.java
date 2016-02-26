package com.asiainfo.orderdishes.entity.volley;

import com.asiainfo.orderdishes.entity.litepal.DishesOrder;

import java.util.ArrayList;

public class queryUnfinishedOrderData {
    ArrayList<DishesOrder> info;

    public ArrayList<DishesOrder> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<DishesOrder> info) {
        this.info = info;
    }
}
