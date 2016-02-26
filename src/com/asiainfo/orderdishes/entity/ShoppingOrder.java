package com.asiainfo.orderdishes.entity;

import com.asiainfo.orderdishes.entity.litepal.OrderGoods;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingOrder implements Serializable {

    public ArrayList<String> getmHeaderNames() {
        return mHeaderNames;
    }

    public void setmHeaderNames(ArrayList<String> mHeaderNames) {
        this.mHeaderNames = mHeaderNames;
    }

    public ArrayList<Integer> getmHeaderPositions() {
        return mHeaderPositions;
    }

    public void setmHeaderPositions(ArrayList<Integer> mHeaderPositions) {
        this.mHeaderPositions = mHeaderPositions;
    }

    public ArrayList<OrderGoods> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(ArrayList<OrderGoods> orderGoods) {
        this.orderGoods = orderGoods;
    }

    ArrayList<String> mHeaderNames;
    ArrayList<Integer> mHeaderPositions;
    ArrayList<OrderGoods> orderGoods;

    public ShoppingOrder() {
        mHeaderNames = new ArrayList<String>();
        mHeaderPositions = new ArrayList<Integer>();
        orderGoods = new ArrayList<OrderGoods>();
    }


}
