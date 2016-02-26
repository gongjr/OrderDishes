package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据模型分层级别:菜品分类最高 1
 * 类型名:dishesTypeName
 * 类型编码:dishesTypeCode
 * 多条普通单品菜:ArrayList<DishesInfo> dishesInfoList
 * 多条套餐菜:ArrayList<MerchantCompDishesInfo> dishesInfoList
 * 看接口更新菜品情况，套餐是否也是DishesInfo，根据iscomp判断是否套餐
 * 1是套餐，根据dishesid，查询出一条MerchantCompDishesInfo
 *
 * @author gjr
 */
public class DishesType extends DataSupport {

    private String dishesTypeName;
    private String dishesTypeCode;
    private String startDate;
    private String endDate;
    private String merchantId;
    private ArrayList<DishesInfo> dishesInfoList = new ArrayList<DishesInfo>();

    //	private ArrayList<MerchantCompDishesInfo> CompDishesInfo;
    public String getDishesTypeName() {
        return dishesTypeName;
    }

    public void setDishesTypeName(String dishesTypeName) {
        this.dishesTypeName = dishesTypeName;
    }

    public String getDishesTypeCode() {
        return dishesTypeCode;
    }

    public void setDishesTypeCode(String dishesTypeCode) {
        this.dishesTypeCode = dishesTypeCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public ArrayList<DishesInfo> getDishesInfoList() {
        return dishesInfoList;
    }

    public void setDishesInfoList(ArrayList<DishesInfo> dishesInfoList) {
        this.dishesInfoList = dishesInfoList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    /**
     * 默认懒加载，查询出关联表中的数据，来实现表的关联表的继续迭代查询关联表数据
     * 连缀查询，我们就可以将关联表数据的查询延迟
     * 这种写法会比激进查询更加高效也更加合理
     *
     * @return
     */
    public List<DishesInfo> getItemlistDb() {
        this.dishesInfoList = (ArrayList<DishesInfo>) DataSupport.where("dishestype_id = ?", String.valueOf(id)).find(DishesInfo.class);
        return dishesInfoList;
    }
}
