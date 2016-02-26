package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * 数据模型分层级别：套餐2
 * 单条套餐菜品，例如(缤纷套餐)
 * 内含多种套餐组合类型，例如(主食【四选二】、甜品【二选一】、饮料【三选一】)
 * ArrayList<MerchantCompDishesType>
 * MerchantCompDishesType:套餐组合类型
 * dishesTypeName，配置的时候店员可以讲套餐配到“冷菜”、“热菜”任意分类下面
 * 2015-5-26 14:00
 *
 * @author gjr
 */
public class MerchantCompDishesInfo extends DataSupport {

    private String dishesName;

    private long dishesId;

    private String dishesOldPrice;

    private long dishesPrice;

    private String dishesUrl;

    private String dishesSurl;

    private String dishesTypeName;

    private String dishesType;

    private String isShow;

    private String isShowName;

    private long merchantId;

    private String hasItems;

    private String dishesTypeCode;

    private String isFeature;

    private String remark;

    private String dishesCode;

    private String isComp;

    private String dishesDiscnt;

    private String isDelivery;

    private String menuId;

    private Integer exportId;

    private Long memberPrice;

    public String getIsZdzk() {
        return isZdzk;
    }

    public void setIsZdzk(String isZdzk) {
        this.isZdzk = isZdzk;
    }

    private String isZdzk;

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

    private ArrayList<MerchantCompDishesType> compDishesTypeList = new ArrayList<MerchantCompDishesType>();

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

    public String getDishesSurl() {
        return dishesSurl;
    }

    public void setDishesSurl(String dishesSurl) {
        this.dishesSurl = dishesSurl;
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

    public ArrayList<MerchantCompDishesType> getCompDishesTypeList() {
        return compDishesTypeList;
    }

    public void setCompDishesTypeList(
            ArrayList<MerchantCompDishesType> compDishesTypeList) {
        this.compDishesTypeList = compDishesTypeList;
    }

    public void setDishesDiscnt(String dishesDiscnt) {
        this.dishesDiscnt = dishesDiscnt;
    }

    public String getDishesDiscnt() {
        return dishesDiscnt;
    }

    public String getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(String isDelivery) {
        this.isDelivery = isDelivery;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getDishesType() {
        return dishesType;
    }

    public void setDishesType(String dishesType) {
        this.dishesType = dishesType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    /**
     * 查询填充对应的ArrayList<MerchantCompDishesType>
     *
     * @return
     */
    public ArrayList<MerchantCompDishesType> getItemlistDb() {
        this.compDishesTypeList = (ArrayList<MerchantCompDishesType>) DataSupport.where("merchantcompdishesinfo_id = ?", String.valueOf(id)).find(MerchantCompDishesType.class);
        return compDishesTypeList;
    }


}
