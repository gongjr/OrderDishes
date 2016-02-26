package com.asiainfo.orderdishes.entity;

import com.asiainfo.orderdishes.entity.litepal.DinnerDesk;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单主表
 *
 * @author zhouwx
 */
public class ServerOrder {

    private Long orderid;

    private String orderType;

    private String orderTypeName;

    private String createTime;

    private String strCreateTime;

    private Long userId;

    private String timeStr;//为了pad端展示用的字段，截取时间字段的时分
    /**
     * 订单状态
     * /0 待商家收单 1 已收单待寄送 2 配送中 3 配送完成 4 订单取消 5 订单支付失败 6 自送 7 帮帮送 9 订单完成
     */
    private String orderState;

    private String orderStateName;
    private String remark;

    private Long originalPrice;

    private String payState;

    private String payType;

    private String payTypeName;

    public Timestamp finishTime;

    private String isNeedInvo;

    private Long invoPrice;

    private Long invoId;

    private String invoTitle;

    private Long merchantId;

    private String merchantName;
    private String phoneNumber;

    private Long paidPrice;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private Long postAddrId;
    private String postAddrInfo;

    private String linkPhone;

    private String linkName;
    private String serviceTime;
    private String inMode;//订单渠道来源  0是微信， 1是收银机,2pad端

    public String getInMode() {
        return inMode;
    }

    public void setInMode(String inMode) {
        this.inMode = inMode;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    private List<ServerOrderGoods> OrderGoods = new ArrayList<ServerOrderGoods>();

    private DinnerDesk dinnerDesk;

    private Integer allGoodsNum = 0;

    private Long deskId;

    //订单概况 该字段记录该笔订单大致内容，用于app端展示
    private String generalSitauation;

    private String childMerchantId;

    private String sendBusi;

    private String isUseGift;

    private Long giftMoney;

    private String fromCode;

    private String fromId;

    private int personNum;

    private String tradeStsffId;

    public String getGeneralSitauation() {
        return generalSitauation;
    }

    public void setGeneralSitauation(String generalSitauation) {
        this.generalSitauation = generalSitauation;
    }

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

    public Long getOrderId() {
        return orderid;
    }

    public void setOrderId(Long orderId) {
        this.orderid = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getStrCreateTime() {
        return strCreateTime;
    }


    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public String getIsNeedInvo() {
        return isNeedInvo;
    }

    public void setIsNeedInvo(String isNeedInvo) {
        this.isNeedInvo = isNeedInvo;
    }

    public Long getInvoPrice() {
        return invoPrice;
    }

    public void setInvoPrice(Long invoPrice) {
        this.invoPrice = invoPrice;
    }

    public Long getInvoId() {
        return invoId;
    }

    public void setInvoId(Long invoId) {
        this.invoId = invoId;
    }

    public String getInvoTitle() {
        return invoTitle;
    }

    public void setInvoTitle(String invoTitle) {
        this.invoTitle = invoTitle;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }


    public Long getPostAddrId() {
        return postAddrId;
    }

    public void setPostAddrId(Long postAddrId) {
        this.postAddrId = postAddrId;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public List<ServerOrderGoods> getOrderGoods() {
        return OrderGoods;
    }

    public void setOrderGoods(List<ServerOrderGoods> orderGoods) {
        for (int i = 0; i < orderGoods.size(); i++) {
            this.allGoodsNum += (orderGoods.get(i).getSalesNum());
        }
        this.OrderGoods = orderGoods;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public DinnerDesk getDinnerDesk() {
        return dinnerDesk;
    }

    public void setDinnerDesk(DinnerDesk dinnerDesk) {
        this.dinnerDesk = dinnerDesk;
    }

    public Integer getAllGoodsNum() {
        return allGoodsNum;
    }

    public void setAllGoodsNum(Integer allGoodsNum) {
        this.allGoodsNum = allGoodsNum;
    }

    public String toString() {
        StringBuffer sbf = new StringBuffer();
        sbf.append("orderId=").append(orderid).append(", ");
        sbf.append("orderType=").append(orderType).append(", ");
        sbf.append("orderTypeName=").append(orderTypeName).append(", ");
        sbf.append("createTime=").append(createTime).append(", ");
        sbf.append("userId=").append(userId).append(", ");
        sbf.append("orderState=").append(orderState).append(", ");
        sbf.append("remark=").append(remark).append(", ");
        sbf.append("originalPrice=").append(originalPrice).append(", ");
        sbf.append("payState=").append(payState).append(", ");
        sbf.append("payType=").append(payType).append(", ");
        sbf.append("payTypeName=").append(payTypeName).append(", ");
        sbf.append("finishTime=").append(finishTime).append(", ");
        sbf.append("isNeedInvo=").append(isNeedInvo).append(", ");
        sbf.append("invoPrice=").append(invoPrice).append(", ");
        sbf.append("invoId=").append(invoId).append(", ");
        sbf.append("merchantId=").append(merchantId).append(", ");
        sbf.append("postAddrId=").append(postAddrId).append(", ");
        sbf.append("linkPhone=").append(linkPhone).append(", ");
        sbf.append("orderGoods=").append(OrderGoods).append(", ");
        sbf.append("deskId=").append(deskId).append(", ");
        sbf.append("invoPrice=").append(invoPrice);
        return sbf.toString();
    }

    public String getPostAddrInfo() {
        return postAddrInfo;
    }

    public void setPostAddrInfo(String postAddrInfo) {
        this.postAddrInfo = postAddrInfo;
    }

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }

    public Long getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(Long paidPrice) {
        this.paidPrice = paidPrice;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getChildMerchantId() {
        return childMerchantId;
    }

    public void setChildMerchantId(String childMerchantId) {
        this.childMerchantId = childMerchantId;
    }

    public String getSendBusi() {
        return sendBusi;
    }

    public void setSendBusi(String sendBusi) {
        this.sendBusi = sendBusi;
    }

    public String getIsUseGift() {
        return isUseGift;
    }

    public void setIsUseGift(String isUseGift) {
        this.isUseGift = isUseGift;
    }

    public Long getGiftMoney() {
        return giftMoney;
    }

    public void setGiftMoney(Long giftMoney) {
        this.giftMoney = giftMoney;
    }

    public String getFromCode() {
        return fromCode;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public String getTradeStsffId() {
        return tradeStsffId;
    }

    public void setTradeStsffId(String tradeStsffId) {
        this.tradeStsffId = tradeStsffId;
    }


}
