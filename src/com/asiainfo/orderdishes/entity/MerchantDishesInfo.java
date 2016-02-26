package com.asiainfo.orderdishes.entity;

import com.asiainfo.orderdishes.entity.litepal.DishesItemType;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * List<DishesItemType> dishesItemList
 * 一条普通单品菜，点餐主页上面的非套餐菜品使用这个数据集来展示，
 * 本身可以加一些套餐约束属性在套餐中复用展示
 * 2015-5-26 14:00
 *
 * @author gjr
 *         *注：5.28日，不与服务器逻辑类同步，使用本地dishesinfo合并MerchantCompDishes和MerchantDishesInfo的逻辑实现
 */
public class MerchantDishesInfo {

    private String dishesName;

    private long dishesId;

    private String dishesOldPrice;

    private long dishesPrice;

    private String dishesUrl;

    private String dishesTypeName;

    private String isShow;

    private String isShowName;

    private long merchantId;

    private String hasItems;

    private String dishesTypeCode;

    private String isFeature;

    private String remark;

    private String dishesCode;

    private String isComp;

    private Integer exportId;

    private Long memberPrice;

    private MerchantCompDishes merchantCompDishes;

    public Integer getExportId() {
        return exportId;
    }

    public void setExportId(Integer exportId) {
        this.exportId = exportId;
    }

    public Long getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Long memberPrice) {
        this.memberPrice = memberPrice;
    }

    private ArrayList<DishesItemType> dishesItemList = new ArrayList<DishesItemType>();

    public ArrayList<DishesItemType> getDishesItemList() {
        return dishesItemList;
    }

    public void setDishesItemList(ArrayList<DishesItemType> dishesItemList) {
        this.dishesItemList = dishesItemList;
    }

    public String getDishesName() {
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public long getDishesId() {
        return dishesId;
    }

    public void setDishesId(long dishesId) {
        this.dishesId = dishesId;
    }

    public String getDishesOldPrice() {
        return dishesOldPrice;
    }

    public void setDishesOldPrice(String dishesOldPrice) {
        this.dishesOldPrice = dishesOldPrice;
    }

    public long getDishesPrice() {
        return dishesPrice;
    }

    public void setDishesPrice(long dishesPrice) {
        this.dishesPrice = dishesPrice;
    }

    public String getDishesUrl() {
        return dishesUrl;
    }

    public void setDishesUrl(String dishesUrl) {
        this.dishesUrl = dishesUrl;
    }

    public String getDishesTypeName() {
        return dishesTypeName;
    }

    public void setDishesTypeName(String dishesTypeName) {
        this.dishesTypeName = dishesTypeName;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsShowName() {
        return isShowName;
    }

    public void setIsShowName(String isShowName) {
        this.isShowName = isShowName;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getHasItems() {
        return hasItems;
    }

    public void setHasItems(String hasItems) {
        this.hasItems = hasItems;
    }

    public String getDishesTypeCode() {
        return dishesTypeCode;
    }

    public void setDishesTypeCode(String dishesTypeCode) {
        this.dishesTypeCode = dishesTypeCode;
    }

    public String getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(String isFeature) {
        this.isFeature = isFeature;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDishesCode() {
        return dishesCode;
    }

    public void setDishesCode(String dishesCode) {
        this.dishesCode = dishesCode;
    }

    public String getIsComp() {
        return isComp;
    }

    public void setIsComp(String isComp) {
        this.isComp = isComp;
    }

    public MerchantCompDishes getMerchantCompDishes() {
        return merchantCompDishes;
    }

    public void setMerchantCompDishes(MerchantCompDishes merchantCompDishes) {
        this.merchantCompDishes = merchantCompDishes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    /**
     * 查询填充对应的ArrayList<DishesItemType>
     *
     * @return
     */
    public ArrayList<DishesItemType> getItemlistDb() {
        this.dishesItemList = (ArrayList<DishesItemType>) DataSupport.where("merchantdishesinfo_id = ?", String.valueOf(id)).find(DishesItemType.class);
        return dishesItemList;
    }

}
