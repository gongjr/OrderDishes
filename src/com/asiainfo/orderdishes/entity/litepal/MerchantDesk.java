package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 桌子实体
 *
 * @author ouyangyj
 *         <p/>
 *         2015年3月31日
 */
public class MerchantDesk extends DataSupport implements Serializable {

    private int id;

    /**
     * 分店ID
     */
    private Long childMerchantId;

    /**
     * 餐桌名称
     */
    private String deskId;

    /**
     * 餐桌号
     */
    private String deskName;

    /**
     * 最多容纳人数
     */
    private Integer maxNum;

    /**
     * 区域位置编码
     */
    private Long locationCode;

    /**
     * 餐桌类型
     */
    private String deskType;

    /**
     * 桌子状态名称
     */
    private String deskState;

    /**
     * 桌子状态值
     */
    private int deskStateValue;

    public String getDeskState() {
        return deskState;
    }

    public void setDeskState(String deskState) {
        this.deskState = deskState;
    }

    public int getDeskStateValue() {
        return deskStateValue;
    }

    public void setDeskStateValue(int deskStateValue) {
        this.deskStateValue = deskStateValue;
    }

    public Long getChildMerchantId() {
        return childMerchantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChildMerchantId(Long childMerchantId) {
        this.childMerchantId = childMerchantId;
    }

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Long getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Long locationCode) {
        this.locationCode = locationCode;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    // public Boolean getIsOpened() {
    // return isOpened;
    // }
    //
    // public void setIsOpened(Boolean isOpened) {
    // this.isOpened = isOpened;
    // }
}
