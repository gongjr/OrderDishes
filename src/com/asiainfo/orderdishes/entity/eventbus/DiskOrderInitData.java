package com.asiainfo.orderdishes.entity.eventbus;

import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;

public class DiskOrderInitData {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DishesOrder getDishesOrder() {
        return dishesOrder;
    }

    public void setDishesOrder(DishesOrder dishesOrder) {
        this.dishesOrder = dishesOrder;
    }

    public DishesEntity getDishesEntity() {
        return dishesEntity;
    }

    public void setDishesEntity(DishesEntity dishesEntity) {
        this.dishesEntity = dishesEntity;
    }

    private DishesOrder dishesOrder;
    private DishesEntity dishesEntity;
    private int type;

}
