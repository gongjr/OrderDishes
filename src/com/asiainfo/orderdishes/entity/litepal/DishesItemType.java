package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * 数据模型分层级别：套餐5，单品菜3
 * List<DishesItem> itemlist
 * 菜品标签属性类型
 * 属性类型:牛肉几分熟/做法/口味
 * dishesId:表示这个标签属性属于哪个单品菜
 * 2015-5-26 14:00
 *
 * @author gjr
 */
public class DishesItemType extends DataSupport {

    private String itemType;

    private String itemTypeName;

    private String dishesId;

    private String merchantId;

    private String limitTag;

    private DishesInfo childDishesInfo;

    // 菜品属性
    private ArrayList<DishesItem> itemlist = new ArrayList<DishesItem>();

    public ArrayList<DishesItem> getItemlist() {
        return itemlist;
    }

    public void setItemlist(ArrayList<DishesItem> itemlist) {
        this.itemlist = itemlist;
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

    public String getDishesId() {
        return dishesId;
    }

    public void setDishesId(String dishesId) {
        this.dishesId = dishesId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getLimitTag() {
        return limitTag;
    }

    public void setLimitTag(String limitTag) {
        this.limitTag = limitTag;
    }

    public DishesInfo getChildDishesInfo() {
        return childDishesInfo;
    }

    public void setChildDishesInfo(DishesInfo childDishesInfo) {
        this.childDishesInfo = childDishesInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public ArrayList<DishesItem> getItemlistDb() {
        itemlist = (ArrayList<DishesItem>) DataSupport.where("dishesitemtype_id = ?", String.valueOf(id)).find(DishesItem.class);
        return itemlist;
    }

}
