package com.asiainfo.orderdishes.entity;

import android.util.Log;

import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesItem;
import com.asiainfo.orderdishes.entity.litepal.DishesItemType;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesType;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.litepal.RemarkItem;
import com.asiainfo.orderdishes.util.BaseUtils;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 将点套餐的业务流程和点普通菜品分离，统一保存数据库缓存，只在提交过程中统一展示
 *
 * @author gjr
 */
public class CompDishesEntity {
    /**
     * 用于绑定表对象，完成MerchantCompDishesInfo对象对应表的操作
     */
    public DataBinder<MerchantCompDishesInfo> dataSetCompDishesInfo;
    /**
     * 用于绑定表对象，完成MerchantCompDishesType对象对应表的操作
     */
    public DataBinder<MerchantCompDishesType> dataSetCompDishesType;
    /**
     * 用于绑定表对象，完成ChildDishesInfo对象对应表的操作
     */
    public DataBinder<DishesInfo> dataSetChildDishesInfo;
    /**
     * 订单业务实体,保存当前订单详情
     */
    private DishesOrder orderEntity;
    /**
     * 用于绑定表对象，完成DishesItemType对象对应表的操作
     */
    public DataBinder<DishesItemType> dataSetItemType;
    /**
     * 用于绑定表对象，完成DishesItem对象对应表的操作
     */
    public DataBinder<DishesItem> dataSetItem;
    /**
     * 用于绑定表对象，完成OrderGoods对象对应表的操作
     */
    public DataBinder<OrderGoods> dataSetorderGoods;
    /**
     * 用于绑定表对象，完成DishesOrder对象对应表的操作
     */
    public DataBinder<DishesOrder> dataSetorder;
    /**
     * 用于绑定表对象，完成RemarkItem对象对应表的操作
     */
    public DataBinder<RemarkItem> dataSetRemarkItem;

    public CompDishesEntity(DishesOrder orderentity) {
        this.orderEntity = orderentity;
        initDataBinder();
    }

    public void initDataBinder() {
        dataSetCompDishesInfo = new DataBinder<MerchantCompDishesInfo>();
        dataSetCompDishesType = new DataBinder<MerchantCompDishesType>();
        dataSetChildDishesInfo = new DataBinder<DishesInfo>();
        dataSetItemType = new DataBinder<DishesItemType>();
        dataSetItem = new DataBinder<DishesItem>();
        dataSetorderGoods = new DataBinder<OrderGoods>();
        dataSetorder = new DataBinder<DishesOrder>();
        dataSetRemarkItem = new DataBinder<RemarkItem>();

    }

    /**
     * 保存套餐详情
     *
     * @param baseUtils
     */
    public void saveCompDishes(BaseUtils baseUtils) {
        String txt = baseUtils.getAssetsTxt("compdishesinfotest.txt");
        Gson gson = new Gson();
        MerchantCompDishesInfo compDishesInfo = gson.fromJson(txt,
                MerchantCompDishesInfo.class);
        compDishesInfo.save();
        DataSupport.saveAll(compDishesInfo.getCompDishesTypeList());
        for (MerchantCompDishesType aa : compDishesInfo.getCompDishesTypeList()) {
            DataSupport.saveAll(aa.getDishesInfoList());
            for (DishesInfo ss : aa.getDishesInfoList()) {
                DataSupport.saveAll(ss.getDishesItemList());
                for (DishesItemType ItemType : ss.getDishesItemList()) {
                    DataSupport.saveAll(ItemType.getItemlist());
                    Log.i("DishesItem",
                            "ItemType:" + ItemType.getItemTypeName());
                    for (DishesItem dishesItem : ItemType.getItemlist()) {
                        Log.i("DishesItem",
                                "DishesItem:" + dishesItem.getItemName());
                    }
                }
            }
        }
        Log.i("MerchantCompDishesInfo",
                "dishesname:" + compDishesInfo.getDishesName());
        Log.i("MerchantCompDishesInfo",
                "dishesid:" + compDishesInfo.getDishesId());
        Log.i("MerchantCompDishesInfo",
                "dishesprice:" + compDishesInfo.getDishesPrice());
        Log.i("MerchantCompDishesInfo",
                "dishesTypeName:" + compDishesInfo.getDishesTypeName());
        Log.i("MerchantCompDishesInfo",
                "dishesTypeCode:" + compDishesInfo.getDishesTypeCode());
        Log.i("MerchantCompDishesInfo", "remark:" + compDishesInfo.getRemark());
        Log.i("MerchantCompDishesInfo",
                "dishesCode:" + compDishesInfo.getDishesCode());
        Log.i("MerchantCompDishesInfo",
                "exportId:" + compDishesInfo.getExportId());
        Log.i("MerchantCompDishesInfo",
                "compDishesTypeList:" + compDishesInfo.getCompDishesTypeList());
        Log.i("MerchantCompDishesInfo", "CompDishesType.CompId:"
                + compDishesInfo.getCompDishesTypeList().get(0).getCompId());
        Log.i("MerchantCompDishesInfo", "CompDishesType.dishesTypeName:"
                + compDishesInfo.getCompDishesTypeList().get(0)
                .getDishesTypeName());
        Log.i("MerchantCompDishesInfo", "CompDishesType.maxSelect:"
                + compDishesInfo.getCompDishesTypeList().get(0).getMaxSelect());
        Log.i("MerchantCompDishesInfo", "CompDishesType.dishesCount:"
                + compDishesInfo.getCompDishesTypeList().get(0)
                .getDishesCount());
        Log.i("MerchantCompDishesInfo", "CompDishesType.CompDishesList:"
                + compDishesInfo.getCompDishesTypeList().get(0)
                .getDishesInfoList());
        Log.i("MerchantCompDishesInfo",
                "CompDishesType.CompDishesList.dishesName:"
                        + compDishesInfo.getCompDishesTypeList().get(0)
                        .getDishesInfoList().get(0).getDishesName());
        Log.i("MerchantCompDishesInfo",
                "CompDishesType.CompDishesList.dishesItemList:"
                        + compDishesInfo.getCompDishesTypeList().get(0)
                        .getDishesInfoList().get(0).getDishesItemList());
        Log.i("MerchantCompDishesInfo",
                "CompDishesType.CompDishesList.dishesItemList.itemTypeName:"
                        + compDishesInfo.getCompDishesTypeList().get(0)
                        .getDishesInfoList().get(0).getDishesItemList()
                        .get(0).getItemTypeName());
        Log.i("MerchantCompDishesInfo",
                "CompDishesType.CompDishesList.dishesItemList.itemlist:"
                        + compDishesInfo.getCompDishesTypeList().get(0)
                        .getDishesInfoList().get(0).getDishesItemList()
                        .get(0).getItemlist());
        Log.i("MerchantCompDishesInfo",
                "CompDishesType.CompDishesList.dishesItemList.itemlist.itemName:"
                        + compDishesInfo.getCompDishesTypeList().get(0)
                        .getDishesInfoList().get(0).getDishesItemList()
                        .get(0).getItemlist().get(0).getItemName());

    }

    /**
     * 查询套餐详情
     *
     * @param dishesid
     * @return
     */
    public MerchantCompDishesInfo getCompDishes(String dishesid,
                                                String merchantid) {
        List<MerchantCompDishesInfo> compDishesInfoList = new ArrayList<MerchantCompDishesInfo>();
        compDishesInfoList = dataSetCompDishesInfo.findWithWhere(
                MerchantCompDishesInfo.class, "merchantid=? and dishesid=? ",
                merchantid, dishesid);
        if (compDishesInfoList.size() >= 1) {
            // 此处4层传递查询，比较耗时查询耗时430毫秒
            MerchantCompDishesInfo compDishesInfo = compDishesInfoList
                    .get(compDishesInfoList.size() - 1);
            System.out.println("compDishesInfo.id:" + compDishesInfo.getId());
            compDishesInfo.getItemlistDb();
            for (MerchantCompDishesType merchantCompDishesType : compDishesInfo
                    .getCompDishesTypeList()) {
                merchantCompDishesType.getItemlistDb();
                for (DishesInfo childDishesInfo : merchantCompDishesType
                        .getDishesInfoList()) {
                    childDishesInfo.getItemlistDb();
                    for (DishesItemType dishesItemType : childDishesInfo
                            .getDishesItemList()) {
                        dishesItemType.getItemlistDb();
                    }
                }
            }
            return compDishesInfo;
        } else {
            return null;
        }
    }

    /**
     * 查询套餐详情
     *
     * @return
     */
    public MerchantCompDishesInfo getCompDishes(int compdishesinfo_id) {
        List<MerchantCompDishesInfo> compDishesInfoList = new ArrayList<MerchantCompDishesInfo>();
        compDishesInfoList = dataSetCompDishesInfo.findWithWhere(
                MerchantCompDishesInfo.class, "id=?", compdishesinfo_id + "");
        if (compDishesInfoList.size() >= 1) {
            // 此处4层传递查询，比较耗时查询耗时430毫秒
            MerchantCompDishesInfo compDishesInfo = compDishesInfoList
                    .get(compDishesInfoList.size() - 1);
            System.out.println("compDishesInfo.id:" + compDishesInfo.getId());
            compDishesInfo.getItemlistDb();
            for (MerchantCompDishesType merchantCompDishesType : compDishesInfo
                    .getCompDishesTypeList()) {
                merchantCompDishesType.getItemlistDb();
                for (DishesInfo childDishesInfo : merchantCompDishesType
                        .getDishesInfoList()) {
                    childDishesInfo.getItemlistDb();
                    for (DishesItemType dishesItemType : childDishesInfo
                            .getDishesItemList()) {
                        dishesItemType.getItemlistDb();
                    }
                }
            }
            return compDishesInfo;
        } else {
            return null;
        }
    }

    /**
     * 增加套餐父项
     */
    public OrderGoods addCompOrderGoods(
            MerchantCompDishesInfo merchantCompDishesInfo,
            CompDishesInfo compDishesInfo) {
        OrderGoods good = newCompOrderGoods(merchantCompDishesInfo,
                compDishesInfo);
        String[] select = {"salesnum"};
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithCluster(
                OrderGoods.class, select,
                "salesid = ? and dishesorder_id=? and instanceid =?", good
                        .getSalesId().toString(), orderEntity.getId() + "",
                compDishesInfo.getInstanceid());
        if (salesOrderGoods.size() == 0) {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        } else if (salesOrderGoods.size() == 1) {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        }
        return good;
    }

    /**
     * 增加套餐选中的子项菜
     */
    public void addOrderGoods(DishesInfo dishesinfo,
                              CompDishesInfo compDishesInfo) {
        OrderGoods good = newOrderGoods(dishesinfo, compDishesInfo);
        String[] select = {"salesnum"};
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithCluster(
                OrderGoods.class, select,
                "salesid = ? and dishesorder_id=? and instanceid =?", good
                        .getSalesId().toString(), orderEntity.getId() + "",
                compDishesInfo.getInstanceid());
        if (salesOrderGoods.size() == 0) {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        } else if (salesOrderGoods.size() == 1) {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        }
    }

    /**
     * 删除套餐选中的子项菜
     */
    public void decreaseOrderGoods(DishesInfo dishesinfo,
                                   CompDishesInfo compDishesInfo) {
        Log.i("decreaseOrderGoods", "compid:" + compDishesInfo.getCompid());
        Log.i("decreaseOrderGoods",
                "instanceid:" + compDishesInfo.getInstanceid());
        Log.i("decreaseOrderGoods", "salesid:" + dishesinfo.getDishesId());
        Log.i("decreaseOrderGoods", "dishesorder_id:" + orderEntity.getId());

        int i = dataSetorderGoods
                .deleteAll(
                        OrderGoods.class,
                        "compid = ? and instanceid = ? and salesid = ? and dishesorder_id = ?",
                        compDishesInfo.getCompid() + "",
                        compDishesInfo.getInstanceid() + "",
                        dishesinfo.getDishesId(), orderEntity.getId() + "");

        Log.i("decreaseOrderGoods", "decreaseOrderGoods--i:" + i);
    }

    /**
     * 查询套餐选中的子项菜
     */
    public ArrayList<OrderGoods> findOrderGoods(DishesInfo dishesinfo,
                                                CompDishesInfo compDishesInfo) {
        ArrayList<OrderGoods> ss = (ArrayList<OrderGoods>) dataSetorderGoods
                .findWithWhere(
                        OrderGoods.class,
                        "compid = ? and instanceid = ? and salesid = ? and dishesorder_id = ?",
                        compDishesInfo.getCompid() + "",
                        compDishesInfo.getInstanceid(),
                        dishesinfo.getDishesId(), orderEntity.getId() + "");
        return ss;
    }

    /**
     * 删除的子项菜
     */
    public void decreaseOrderGoodsRemarkItem() {

    }

    /**
     * 新增菜品子项
     *
     * @param dishesInfo
     * @return
     */
    public OrderGoods newOrderGoods(DishesInfo dishesInfo,
                                    CompDishesInfo compDishesInfo) {
        OrderGoods good = new OrderGoods();
        Long j = (long) 0;
        good.setSalesPrice(j);
        good.setDishesPrice(j);
        good.setSalesName(dishesInfo.getDishesName());
        good.setSalesId(Long.valueOf(dishesInfo.getDishesId()));
        good.setDishesTypeCode(dishesInfo.getDishesTypeCode());
        good.setDishesTypeName(dishesInfo.getDishesTypeName());
        good.setExportId(dishesInfo.getExportId());
        good.setSalesState("0");
        good.setRemark("");
        good.setCompDish(true);
        good.setInstanceId("" + compDishesInfo.getInstanceid());
        good.setCompId(compDishesInfo.getCompid());
        good.setInterferePrice(j);
        good.setOrder(orderEntity);
        good.setMemberPrice(dishesInfo.getMemberPrice());
        good.setIsZdzk(dishesInfo.getIsZdzk());
        // 由于orderEntity是从本地数据库中查找出来，由于序列化相关判断，再次保存的时候报错，无法对象引用，关联父对象
        // 考虑传递的时候直接传递orderEntity对象，而不是id重新查找
        // 最后问题确定，不是上述问题，需要在save之前先访问查询解锁一下，否则新生成报错
        System.out.println("orderEntity.id():" + orderEntity.getId());
        return good;
    }

    /**
     * 新增菜品套餐父菜
     *
     * @param dishesInfo
     * @return
     */
    public OrderGoods newCompOrderGoods(
            MerchantCompDishesInfo merchantCompDishesInfo,
            CompDishesInfo compDishesInfo) {
        OrderGoods good = new OrderGoods();
        good.setSalesPrice(merchantCompDishesInfo.getDishesPrice());
        good.setDishesPrice(merchantCompDishesInfo.getDishesPrice());
        good.setSalesName(merchantCompDishesInfo.getDishesName());
        good.setSalesId(Long.valueOf(merchantCompDishesInfo.getDishesId()));
        good.setDishesTypeCode(merchantCompDishesInfo.getDishesTypeCode());
        good.setDishesTypeName(merchantCompDishesInfo.getDishesTypeName());
        good.setExportId(merchantCompDishesInfo.getExportId());
        good.setSalesState("0");
        good.setRemark("");
        good.setCompDish(false);
        good.setInstanceId("" + compDishesInfo.getInstanceid());
        Long j = (long) 0;
        good.setInterferePrice(j);
        good.setOrder(orderEntity);
        good.setIsComp("1");
        good.setMemberPrice(merchantCompDishesInfo.getMemberPrice());
        good.setIsZdzk(merchantCompDishesInfo.getIsZdzk());
        // 由于orderEntity是从本地数据库中查找出来，由于序列化相关判断，再次保存的时候报错，无法对象引用，关联父对象
        // 考虑传递的时候直接传递orderEntity对象，而不是id重新查找
        // 最后问题确定，不是上述问题，需要在save之前先访问查询解锁一下，否则新生成报错
        System.out.println("orderEntity.id():" + orderEntity.getId());
        return good;
    }

    /**
     * 根据dishesid,itemtype,itemid,compid,instanceid五个字段来唯一识别remarkitem表中选中标签集，
     * 并且只有一条这样的数据 只能通过此方法增加 dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public void addDishesItem(DishesItem dishesItem,
                              CompDishesInfo compDishesInfo) {
        RemarkItem remarkItem = new RemarkItem();
        remarkItem.setDishesId(dishesItem.getDishesId());
        remarkItem.setItemType(dishesItem.getItemType());
        remarkItem.setItemId(dishesItem.getItemId());
        remarkItem.setItemName(dishesItem.getItemName());
        remarkItem.setItemTypeName(dishesItem.getItemTypeName());
        remarkItem.setDishesorderid(orderEntity.getId());
        remarkItem.setCompid(compDishesInfo.getCompid());
        remarkItem.setInstanceid(compDishesInfo.getInstanceid());
        List<RemarkItem> alldishesitem = dataSetRemarkItem
                .findWithWhere(
                        RemarkItem.class,
                        "dishesid=? and itemtype =? and itemid = ? and dishesorderid= ? and compid = ? and instanceid = ?",
                        dishesItem.getDishesId(), dishesItem.getItemType(),
                        dishesItem.getItemId() + "", orderEntity.getId() + "",
                        compDishesInfo.getCompid() + "",
                        compDishesInfo.getInstanceid());
        if (alldishesitem.size() == 0)
            dataSetRemarkItem.save(remarkItem);
        else
            System.out.println("已经存在此条标签属性记录，已忽视，请查找问题");
    }

    /**
     * 根据dishesid,itemtype,itemid,compid,instanceid五个字段来唯一识别remarkitem表中选中标签集，
     * 并且只有一条这样的数据 通过此方法删除单条标签 dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public int delDishesItem(DishesItem dishesItem,
                             CompDishesInfo compDishesInfo) {
        return dataSetRemarkItem
                .deleteAll(
                        RemarkItem.class,
                        "dishesid=? and itemtype =? and itemid = ? and dishesorderid=? and compid = ? and instanceid = ? ",
                        dishesItem.getDishesId(), dishesItem.getItemType(),
                        dishesItem.getItemId() + "", orderEntity.getId() + "",
                        compDishesInfo.getCompid() + "",
                        compDishesInfo.getInstanceid());
    }

    /**
     * 根据dishesid,compid,instanceid五个字段来唯一识别remarkitem表中选中标签集，并且只有一条这样的数据
     * 通过此方法删除单条标签 dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public int delallDishesItem(String dishesId, CompDishesInfo compDishesInfo) {
        return dataSetRemarkItem
                .deleteAll(
                        RemarkItem.class,
                        "dishesid=?  and dishesorderid=? and compid = ? and instanceid = ? ",
                        dishesId, orderEntity.getId() + "",
                        compDishesInfo.getCompid() + "",
                        compDishesInfo.getInstanceid());
    }

    /**
     * 根据dishesid,itemtype,itemid,compid,instanceid五个字段来唯一识别remarkitem表中选中标签集，
     * 并且只有一条这样的数据 通过此方法删除单条标签 dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public ArrayList<RemarkItem> findDishesItem(DishesItem dishesItem,
                                                CompDishesInfo compDishesInfo) {
        ArrayList<RemarkItem> ss = (ArrayList<RemarkItem>) dataSetRemarkItem
                .findWithWhere(
                        RemarkItem.class,
                        "dishesid=? and itemtype =? and itemid = ? and dishesorderid=? and compid = ? and instanceid = ? ",
                        dishesItem.getDishesId(), dishesItem.getItemType(),
                        dishesItem.getItemId() + "", orderEntity.getId() + "",
                        compDishesInfo.getCompid() + "",
                        compDishesInfo.getInstanceid());
        return ss;
    }

    /**
     * 根据dishesid,compid,instanceid三个字段来唯一识别remarkitem表中选中标签集，一条套餐子菜的标签
     * 通过此方法删除单条标签 dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public ArrayList<RemarkItem> findDishesItem(OrderGoods orderGoods) {
        ArrayList<RemarkItem> ss = (ArrayList<RemarkItem>) dataSetRemarkItem
                .findWithWhere(
                        RemarkItem.class,
                        "dishesid=? and dishesorderid=? and compid = ? and instanceid = ? ",
                        orderGoods.getSalesId() + "", orderEntity.getId() + "",
                        orderGoods.getCompId() + "", orderGoods.getInstanceId());
        return ss;
    }

    /**
     * 根据instanceid，dishesorder_id，compid 删除一条套餐的所选子菜 同时删除子菜的所有标签
     */
    public void decreaseCompOrderGoods(OrderGoods good) {
        Log.i("deleteAll", "instanceid:" + good.getInstanceId());
        Log.i("deleteAll", "id:" + orderEntity.getId());
        Log.i("deleteAll", "compid:" + good.getSalesId());
        int i = dataSetorderGoods.deleteAll(OrderGoods.class,
                "instanceid = ? and dishesorder_id=? and compid=?",
                good.getInstanceId(), orderEntity.getId() + "",
                good.getSalesId() + "");
        Log.i("deleteAll", "删除行i:" + i);
        int j = dataSetRemarkItem.deleteAll(RemarkItem.class,
                "dishesorderid=? and compid = ? and instanceid = ? ",
                orderEntity.getId() + "", good.getCompId() + "",
                good.getInstanceId());
    }

    /**
     * 根据instanceid，dishesorder_id，compid 删除一条套餐的所选子菜 同时删除子菜的所有标签
     */
    public void decreaseCompOrderGoods(CompDishesInfo compDishesInfo) {
        int i = dataSetorderGoods.deleteAll(OrderGoods.class,
                "instanceid = ? and dishesorder_id=? and compid=?",
                compDishesInfo.getInstanceid(), orderEntity.getId() + "",
                compDishesInfo.getCompid() + "");
        int j = dataSetRemarkItem.deleteAll(RemarkItem.class,
                "dishesorderid=? and compid = ? and instanceid = ? ",
                orderEntity.getId() + "", compDishesInfo.getCompid() + "",
                compDishesInfo.getInstanceid());
    }

    /**
     * 查询套餐选中所有的子项菜
     */
    public ArrayList<OrderGoods> findAllOrderGoods(OrderGoods orderGoods) {
        ArrayList<OrderGoods> ss = (ArrayList<OrderGoods>) dataSetorderGoods
                .findWithWhere(OrderGoods.class,
                        "compid = ? and instanceid = ? and dishesorder_id = ?",
                        orderGoods.getSalesId() + "",
                        orderGoods.getInstanceId(), orderEntity.getId() + "");
        return ss;
    }

    public void saveMerchantCompDishesInfo(
            MerchantCompDishesInfo merchantCompDishesInfo) {
        dataSetCompDishesInfo.save(merchantCompDishesInfo);
        dataSetCompDishesType.saveAllMerchantCompDishesType(merchantCompDishesInfo.getCompDishesTypeList());
        for (MerchantCompDishesType iterable_element : merchantCompDishesInfo.getCompDishesTypeList()) {
            dataSetChildDishesInfo.saveAllDishesInfo(iterable_element.getDishesInfoList());
            for (DishesInfo dishesInfo : iterable_element.getDishesInfoList()) {
                dataSetItemType.saveAllDishesItemType(dishesInfo.getDishesItemTypelist());
                for (DishesItemType dishesItenType : dishesInfo.getDishesItemTypelist()) {
                    dataSetItem.saveAllDishesItem(dishesItenType.getItemlist());
                    Log.i("TAG", "ItemTypeName" + dishesItenType.getItemTypeName());
                }
            }
        }

    }
}
