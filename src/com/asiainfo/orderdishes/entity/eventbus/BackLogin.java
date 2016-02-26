package com.asiainfo.orderdishes.entity.eventbus;

import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesType;
import com.asiainfo.orderdishes.helper.DbEntity;

import java.util.ArrayList;

public class BackLogin {
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

    public DbEntity getDbEntity() {
        return dbEntity;
    }

    public void setDbEntity(DbEntity dbEntity) {
        this.dbEntity = dbEntity;
    }

    private int type;
    private ArrayList<DishesType> info;
    private ArrayList<DishesInfo> dishes;
    private DbEntity dbEntity;
}
