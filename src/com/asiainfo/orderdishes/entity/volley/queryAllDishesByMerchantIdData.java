package com.asiainfo.orderdishes.entity.volley;

import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesType;

import java.util.ArrayList;

public class queryAllDishesByMerchantIdData {
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

    ArrayList<DishesType> info;
    ArrayList<DishesInfo> dishes;
}
