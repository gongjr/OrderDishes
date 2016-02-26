package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单商品内容子表
 *
 * @author gjr
 */
public class OrderGoods extends DataSupport implements Serializable {

    private int id;

    private Long orderId;

    private Long salesId;

    private String salesName;

    private Integer salesNum;

    private Long salesPrice;

    private String remark;

    private String dishesSurl;

    private Long dishesPrice;

    private String dishesTypeCode;

    private String dishesTypeName;

    private String salesState;

    private String createTime;

    private String tradeStaffId;

    private String tradeRemark;

    private Long interferePrice;

    private Integer exportId;

    private String instanceId;

    private String isComp;

    /**
     * 用来存放该数据接下来的处理类型
     * 0 删除
     * 1 新增
     */
    private String dataType;

    private String dishesCode;

    private Long deskId;

    private boolean isCompDish;

    private Long compId;

    private String isZdzk;

    public Long getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Long memberPrice) {
        this.memberPrice = memberPrice;
    }

    public String getIsZdzk() {
        return isZdzk;
    }

    public void setIsZdzk(String isZdzk) {
        this.isZdzk = isZdzk;
    }

    private Long memberPrice;

    private ArrayList<RemarkItem> remarkItem = new ArrayList<RemarkItem>();

    public String getTradeStaffId() {
        return tradeStaffId;
    }

    public void setTradeStaffId(String tradeStaffId) {
        this.tradeStaffId = tradeStaffId;
    }

    public String getTradeRemark() {
        return tradeRemark;
    }

    public void setTradeRemark(String tradeRemark) {
        this.tradeRemark = tradeRemark;
    }

    public Long getInterferePrice() {
        return interferePrice;
    }

    public void setInterferePrice(Long interferePrice) {
        this.interferePrice = interferePrice;
    }

    public Integer getExportId() {
        return exportId;
    }

    public void setExportId(Integer exportId) {
        this.exportId = exportId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getIsComp() {
        return isComp;
    }

    public void setIsComp(String isComp) {
        this.isComp = isComp;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDishesCode() {
        return dishesCode;
    }

    public void setDishesCode(String dishesCode) {
        this.dishesCode = dishesCode;
    }

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public Integer getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(Integer salesNum) {
        this.salesNum = salesNum;
    }

    public Long getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Long salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDishesSurl() {
        return dishesSurl;
    }

    public void setDishesSurl(String dishesSurl) {
        this.dishesSurl = dishesSurl;
    }

    public Long getDishesPrice() {
        return dishesPrice;
    }

    public void setDishesPrice(Long dishesPrice) {
        this.dishesPrice = dishesPrice;
    }

    public String getDishesTypeCode() {
        return dishesTypeCode;
    }

    public void setDishesTypeCode(String dishesTypeCode) {
        this.dishesTypeCode = dishesTypeCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private DishesOrder order;

    public DishesOrder getOrder() {
        return order;
    }

    public void setOrder(DishesOrder order) {
        this.order = order;
    }

    public String getDishesTypeName() {
        return dishesTypeName;
    }

    public void setDishesTypeName(String dishesTypeName) {
        this.dishesTypeName = dishesTypeName;
    }

    public String getSalesState() {
        return salesState;
    }

    public void setSalesState(String salesState) {
        this.salesState = salesState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public ArrayList<RemarkItem> getRemarkItem() {
        return remarkItem;
    }

    public void setRemarkItem(ArrayList<RemarkItem> remarkItem) {
        this.remarkItem = remarkItem;
    }

    public List<RemarkItem> getItemlistDb() {
        this.remarkItem = (ArrayList<RemarkItem>) DataSupport.where("ordergoods_id = ?", String.valueOf(id)).find(RemarkItem.class);
        return remarkItem;
    }

    public boolean isCompDish() {
        return isCompDish;
    }

    public void setCompDish(boolean isCompDish) {
        this.isCompDish = isCompDish;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }

}
