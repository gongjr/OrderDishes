package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 将子菜品的所有属性拼入ordergoods的remark字段
 * 通过ordergoods_id来寻找对应的ordergoods
 * 通过dishesId、itemType、itemId来在套餐配置过程中唯一判断一个DishesItem子项
 * 对RemarkItem项进行增删，来判断其是否选中
 *
 * @author gjr
 */
public class RemarkItem extends DataSupport implements Serializable {

    private String dishesId;
    private Long itemId;
    private String itemName;
    private String itemType;
    private String itemTypeName;
    private OrderGoods ordergoods;
    private int dishesorderid;
    private Long compid;
    private String instanceid;

    public String getDishesId() {
        return dishesId;
    }

    public void setDishesId(String dishesId) {
        this.dishesId = dishesId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public OrderGoods getOrdergoods() {
        return ordergoods;
    }

    public void setOrdergoods(OrderGoods ordergoods) {
        this.ordergoods = ordergoods;
    }

    public int getDishesorderid() {
        return dishesorderid;
    }

    public void setDishesorderid(int dishesorderid) {
        this.dishesorderid = dishesorderid;
    }

    public Long getCompid() {
        return compid;
    }

    public void setCompid(Long compid) {
        this.compid = compid;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }
}
