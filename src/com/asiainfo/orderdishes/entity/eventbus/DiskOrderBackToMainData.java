package com.asiainfo.orderdishes.entity.eventbus;

import com.asiainfo.orderdishes.entity.DishesEntity;
import com.asiainfo.orderdishes.entity.ShoppingOrder;

public class DiskOrderBackToMainData {
    public DishesEntity getDishesEntity() {
        return dishesEntity;
    }

    public void setDishesEntity(DishesEntity dishesEntity) {
        this.dishesEntity = dishesEntity;
    }

    public ShoppingOrder getShoppingOrder() {
        return shoppingOrder;
    }

    public void setShoppingOrder(ShoppingOrder shoppingOrder) {
        this.shoppingOrder = shoppingOrder;
    }

    private DishesEntity dishesEntity;
    private ShoppingOrder shoppingOrder;

}
