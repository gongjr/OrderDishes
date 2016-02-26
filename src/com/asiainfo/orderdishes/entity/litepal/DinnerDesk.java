package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

/**
 * 商家餐桌表
 *
 * @author zhouwx
 */
public class DinnerDesk extends DataSupport {

    private Long deskId;

    private Long merchantId;

    private String mininumCharge;

    private Integer personNum;

    private String deskNum;

    private String twoDimensionCode;

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMininumCharge() {
        return mininumCharge;
    }

    public void setMininumCharge(String mininumCharge) {
        this.mininumCharge = mininumCharge;
    }

    public Integer getPersonNum() {
        return personNum;
    }

    public void setPersonNum(Integer personNum) {
        this.personNum = personNum;
    }

    public String getDeskNum() {
        return deskNum;
    }

    public void setDeskNum(String deskNum) {
        this.deskNum = deskNum;
    }

    public String getTwoDimensionCode() {
        return twoDimensionCode;
    }

    public void setTwoDimensionCode(String twoDimensionCode) {
        this.twoDimensionCode = twoDimensionCode;
    }

    public String toString() {
        StringBuffer sbf = new StringBuffer();
        sbf.append("deskId=").append(deskId).append(", ");
        sbf.append("merchantId=").append(merchantId).append(", ");
        sbf.append("mininumCharge=").append(mininumCharge).append(", ");
        sbf.append("personNum=").append(personNum).append(", ");
        sbf.append("deskNum=").append(deskNum).append(", ");
        sbf.append("twoDimensionCode=").append(twoDimensionCode);
        return sbf.toString();
    }

    private DishesOrder order;

    public DishesOrder getOrder() {
        return order;
    }

    public void setOrder(DishesOrder order) {
        this.order = order;
    }
}
