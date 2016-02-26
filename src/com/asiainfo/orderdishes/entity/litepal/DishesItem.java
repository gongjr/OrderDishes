package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

/**
 * 数据模型分层级别：套餐6，单品菜4
 * 分类属性下面对应的单个属性标签
 * 牛肉几分熟:三分熟、五分熟、七分熟、全熟
 * 做法:清蒸、红烧
 * 口味:不辣、微辣、特辣、咸、清淡
 * itemType:表示其对应的属性分类的编码
 * itemTypeName:表示其对应的属性分类的名称
 * 2015-5-26 14:00
 *
 * @author gjr
 */
public class DishesItem extends DataSupport {
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int price;
    private String dishesId;
    private Long itemId;
    private String itemName;
    private String itemType;
    private String itemTypeName;
    private DishesItemType dishesItemType;

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
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

    private String merchantId;
    private String limitTag;

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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public DishesItemType getDishesItemType() {
        return dishesItemType;
    }

    public void setDishesItemType(DishesItemType dishesItemType) {
        this.dishesItemType = dishesItemType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

}
