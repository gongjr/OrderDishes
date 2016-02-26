package com.asiainfo.orderdishes.entity;

import android.content.ContentValues;
import android.util.Log;

import com.asiainfo.orderdishes.Constants;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesItem;
import com.asiainfo.orderdishes.entity.litepal.DishesItemType;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.DishesType;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.litepal.RemarkItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 将数据库操作和视图activity分离 在此实体类中统一处理菜品相关的业务逻辑 通过DataBinder来绑定表对象，进行数据库操作
 *
 * @author gjr
 */
public class DishesEntity {
    /**
     * 用于绑定表对象，完成DishesType对象对应表的操作
     */
    public DataBinder<DishesType> dataSetDishesType;
    /**
     * 用于绑定表对象，完成DishesInfo对象对应表的操作
     */
    public DataBinder<DishesInfo> dataSetDishesInfo;
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

    /**
     * 订单业务实体,保存当前订单详情
     */
    private DishesOrder orderEntity;
    private CompDishesEntity compDishesEntity;

    public DishesEntity(String username) {
        orderEntity = new DishesOrder();
        initDataBinder();
        initOrderData(username, "5");
        compDishesEntity = new CompDishesEntity(orderEntity);
    }

    public DishesEntity(String username, String diskid,int number) {
        orderEntity = new DishesOrder();
        initDataBinder();
        initOrderData(username, diskid,number);
        compDishesEntity = new CompDishesEntity(orderEntity);
    }

    public DishesEntity(String username, String diskid, String orderid) {
        orderEntity = new DishesOrder();
        initDataBinder();
        initOrderData(username, diskid, orderid);
        compDishesEntity = new CompDishesEntity(orderEntity);
    }

    public DishesEntity(DishesOrder dishesOrder, boolean issave) {
        initDataBinder();
        orderEntity = dishesOrder;
        Log.i("DishesEntity(DishesOrder)", "dishesOrder.getOriginalPrice():" + dishesOrder.getOriginalPrice());
        ////此处每次过来都是新生成了一单，考虑是否可以优化为一单使用,只有没有缓存单的时候才新生成一单
        if (issave) {
            dataSetorder.save(orderEntity);
            dataSetorderGoods.saveAllOrderGoods(orderEntity.getOrderGoods());
        }
        compDishesEntity = new CompDishesEntity(orderEntity);
    }

    public DishesEntity(int id) {
        initDataBinder();
        orderEntity = dataSetorder.find(DishesOrder.class, id);
        orderEntity.getItemlistDb();
        compDishesEntity = new CompDishesEntity(orderEntity);
    }

    public DishesEntity(String username,String orderid,DishesOrder add_dishesOrder) {
        orderEntity = new DishesOrder();
        initDataBinder();
        initOrderData(username, orderid,add_dishesOrder);
        compDishesEntity = new CompDishesEntity(orderEntity);
    }


    /**
     * 初始开单生产order订单
     *
     * @param username
     */
    public void initOrderData(String username, String orderid,DishesOrder add_dishesOrder) {
        orderEntity.setChildMerchantId(username);
        orderEntity.setOrderId(Long.valueOf(orderid));
        orderEntity.setOrderType(add_dishesOrder.getOrderType());
        orderEntity.setOrderTypeName(add_dishesOrder.getOrderTypeName());
        orderEntity.setCreateTime(add_dishesOrder.getCreateTime());
        orderEntity.setStrCreateTime(add_dishesOrder.getStrCreateTime());
        orderEntity.setUserId(add_dishesOrder.getUserId());
        orderEntity.setTimeStr(add_dishesOrder.getTimeStr());
        orderEntity.setOrderState(add_dishesOrder.getOrderState());
        orderEntity.setOrderStateName(add_dishesOrder.getOrderStateName());
        orderEntity.setRemark(add_dishesOrder.getRemark());
        orderEntity.setOriginalPrice(add_dishesOrder.getOriginalPrice());
        orderEntity.setPayState(add_dishesOrder.getPayState());
        orderEntity.setPayType(add_dishesOrder.getPayType());
        orderEntity.setPayTypeName(add_dishesOrder.getPayTypeName());
        orderEntity.setFinishTime(add_dishesOrder.getFinishTime());
        orderEntity.setIsNeedInvo(add_dishesOrder.getIsNeedInvo());
        orderEntity.setInvoPrice(add_dishesOrder.getInvoPrice());
        orderEntity.setInvoId(add_dishesOrder.getInvoId());
        orderEntity.setInvoTitle(add_dishesOrder.getInvoTitle());
        orderEntity.setMerchantId(add_dishesOrder.getMerchantId());
        orderEntity.setMerchantName(add_dishesOrder.getMerchantName());
        orderEntity.setPhoneNumber(add_dishesOrder.getPhoneNumber());
        orderEntity.setPaidPrice(add_dishesOrder.getPaidPrice());
        orderEntity.setPostAddrId(add_dishesOrder.getPostAddrId());
        orderEntity.setPostAddrInfo(add_dishesOrder.getPostAddrInfo());
        orderEntity.setLinkPhone(add_dishesOrder.getLinkPhone());
        orderEntity.setLinkName(add_dishesOrder.getLinkName());
        orderEntity.setServiceTime(add_dishesOrder.getServiceTime());
        orderEntity.setInMode(add_dishesOrder.getInMode());
        orderEntity.setInMode(add_dishesOrder.getInMode());
        orderEntity.setDinnerDesk(add_dishesOrder.getDinnerDesk());
        orderEntity.setAllGoodsNum(add_dishesOrder.getAllGoodsNum());
        orderEntity.setDeskId(add_dishesOrder.getDeskId());
        orderEntity.setGeneralSitauation(add_dishesOrder.getGeneralSitauation());
        orderEntity.setChildMerchantId(add_dishesOrder.getChildMerchantId());
        orderEntity.setSendBusi(add_dishesOrder.getSendBusi());
        orderEntity.setIsUseGift(add_dishesOrder.getIsUseGift());
        orderEntity.setGiftMoney(add_dishesOrder.getGiftMoney());
        orderEntity.setFromCode(add_dishesOrder.getFromCode());
        orderEntity.setFromId(add_dishesOrder.getFromId());
        orderEntity.setPersonNum(add_dishesOrder.getPersonNum());
        orderEntity.setDeskId(add_dishesOrder.getDeskId());
        orderEntity.setRemark(add_dishesOrder.getRemark());
        orderEntity.setTradeStaffId(add_dishesOrder.getTradeStaffId());
        dataSetorder.save(orderEntity);
    }


    public void initDataBinder() {
        dataSetDishesType = new DataBinder<DishesType>();
        dataSetDishesInfo = new DataBinder<DishesInfo>();
        dataSetItemType = new DataBinder<DishesItemType>();
        dataSetItem = new DataBinder<DishesItem>();
        dataSetorderGoods = new DataBinder<OrderGoods>();
        dataSetorder = new DataBinder<DishesOrder>();
        dataSetRemarkItem = new DataBinder<RemarkItem>();
    }

    /**
     * 更新本地数据库菜品表信息 增加的时候同表可以批量插入，但是多表不能关联插入， 多表关联的只能取其对应表列自行单表插入
     * 删除的时候会将其所关联的多条数据一起删除 但是也只能删除一级关联表，二级三级关联表无法自动删除，所以
     * 所以有多级关联表时，必须要自己查询其多级关联关系，来删除多级以免数据混乱
     */
    public void updateDb(ArrayList<DishesType> dishesTypeList,
                         ArrayList<DishesInfo> all) {
        ArrayList<DishesInfo> cur = new ArrayList<DishesInfo>();
        for (DishesType dishesType : dishesTypeList) {
            getDishesInfobyType(all, cur, dishesType.getDishesTypeCode());
            for (DishesInfo dishesInfo : cur) {
                dataSetItemType.saveAllDishesItemType(dishesInfo
                        .getDishesItemList());
                for (DishesItemType dishesItemType : dishesInfo
                        .getDishesItemList()) {
                    dataSetItem.saveAllDishesItem(dishesItemType.getItemlist());
                }
            }
            dishesType.setDishesInfoList(cur);
            dataSetDishesInfo.saveAllDishesInfo(cur);
            // String gson_typeList=gson_dishes.toJson(typeList);//此处gson包报错
            // Log.i("LitePal", "dishes222222:"+dishes222222);
            // 有中不确切说法:LitePal存储过程中实体类序列化等等过程引起gson.toJson解析时内存报错，以业务类与表类绑定分化来共用
            dataSetDishesType.saveThrows(dishesType);
        }
    }

    /**
     * 从菜品列表中检索出特点type类型的菜品列表
     *
     * @param type
     * @return
     */
    private List<DishesInfo> getDishesInfobyType(ArrayList<DishesInfo> all,
                                                 ArrayList<DishesInfo> cur, String type) {
        if (cur == null) {
            cur = new ArrayList<DishesInfo>();
        } else {
            cur.clear();
        }
        for (int i = 0; i < all.size(); i++) {
            DishesInfo dish = all.get(i);
            if (dish.getDishesTypeCode().equals(type)) {
                cur.add(dish);
            }
        }
        return cur;
    }

    /**
     * 获取所有的菜品类型
     */
    public ArrayList<DishesType> getAllDishesType() {
        ArrayList<DishesType> dishesTypeList = (ArrayList<DishesType>) dataSetDishesType.findAll(DishesType.class);
        return dishesTypeList;
    }

    /**
     * 填充某类型菜品列表
     *
     * @param dishesType
     * @return
     */
    public void getDishesTypeItemlist(DishesType dishesType) {
        dishesType.getItemlistDb();
        for (DishesInfo dishesInfo : dishesType.getDishesInfoList()) {
            dishesInfo.getItemlistDb();
            for (DishesItemType dishesItemType : dishesInfo
                    .getDishesItemList()) {
                dishesItemType.getItemlistDb();
            }
        }
    }

    /**
     * 从菜品数据库中检索出特点type类型的菜品列表
     *
     * @param index 分组位置
     * @return DishesType
     */
    public DishesType getDishesTypebyIndex(int index) {
        DishesType dishesType = dataSetDishesType.find(DishesType.class,
                dataSetDishesType.findFirst(DishesType.class).getId() + index);
        dishesType.getItemlistDb();
        for (DishesInfo dishesInfo : dishesType.getDishesInfoList()) {
            dishesInfo.getItemlistDb();
            for (DishesItemType dishesItemType : dishesInfo
                    .getDishesItemList()) {
                dishesItemType.getItemlistDb();
            }
        }
        return dishesType;
    }

    /**
     * 根据OrderGoods的salesId判断唯一性， 如果存在将对应的这条数据的salesNum数量加1
     */
    public OrderGoods addOrderGoods(DishesInfo dishesInfo) {
        OrderGoods good = newOrderGoods(dishesInfo);
        String[] select = {"salesnum"};
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithCluster(
                OrderGoods.class, select, "salesid = ? and dishesorder_id=? and iscompdish=?",
                good.getSalesId().toString(), orderEntity.getId() + "", "0");
        if (salesOrderGoods.size() == 0) {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        } else if (salesOrderGoods.size() == 1) {
            good.setSalesNum(salesOrderGoods.get(0).getSalesNum() + 1);
            dataSetorderGoods.update(good, "salesid = ? and dishesorder_id=? and iscompdish=?", good
                    .getSalesId().toString(), orderEntity.getId() + "", "0");
        }
        return good;

    }

    /**
     * 根据OrderGoods的salesId判断唯一性， 如果存在将对应的这条数据的salesNum数量加1
     */
    public OrderGoods addOrderGoodsItem(DishesInfo dishesInfo,long instanceid,String remark) {
        OrderGoods good = newOrderGoodsWithInstance(dishesInfo,instanceid,remark);
        String[] select = {"salesnum"};
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithCluster(
                OrderGoods.class, select, "salesid = ? and dishesorder_id=? and iscompdish=? and instanceid = ? ",
                good.getSalesId().toString(), orderEntity.getId() + "", "0",good.getInstanceId());
        if (salesOrderGoods.size() == 0) {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        } else  {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        }
        return good;

    }


    /**
     * 根据OrderGoods的salesId判断唯一性， 如果存在将对应的这条数据的salesNum数量加1
     */
    public void addOrderGoods(OrderGoods good) {
        String[] select = {"salesnum"};
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithCluster(
                OrderGoods.class, select, "salesid = ? and dishesorder_id=? and iscompdish=? and instanceid = ? ",
                good.getSalesId().toString(), orderEntity.getId() + "", "0",good.getInstanceId());
        if (salesOrderGoods.size() == 0) {
            good.setSalesNum(1);
            dataSetorderGoods.save(good);
        } else if (salesOrderGoods.size() == 1) {
            good.setSalesNum(salesOrderGoods.get(0).getSalesNum() + 1);
            dataSetorderGoods.update(good, "salesid = ? and dishesorder_id=? and iscompdish=? and instanceid = ? ", good
                    .getSalesId().toString(), orderEntity.getId() + "", "0",good.getInstanceId());
        }

    }

    /**
     * 根据OrderGoods的salesId判断唯一性， 如果存在将对应的这条数据的salesNum数量减1
     */
    public void decreaseOrderGoods(DishesInfo dishesInfo) {
        OrderGoods good = newOrderGoods(dishesInfo);
        String[] select = {"salesnum"};
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithCluster(
                OrderGoods.class, select, "salesid = ? and dishesorder_id=? and iscompdish=?",
                good.getSalesId().toString(), orderEntity.getId() + "", "0");
        if (salesOrderGoods.size() > 0) {
            if (salesOrderGoods.get(0).getSalesNum() > 1) {
                good.setSalesNum(salesOrderGoods.get(0).getSalesNum() - 1);
                dataSetorderGoods.update(good, "salesid = ? and dishesorder_id=? and iscompdish=?",
                        good.getSalesId().toString(), orderEntity.getId() + "", "0");
            } else if (salesOrderGoods.get(0).getSalesNum() == 1) {
                dataSetorderGoods.deleteAll(OrderGoods.class,
                        "salesid = ? and dishesorder_id=? and iscompdish=?", good.getSalesId()
                                .toString(), orderEntity.getId() + "", "0");

            }
        } else {
            // 有错误
        }

    }

    /**
     * 根据OrderGoods的salesId判断唯一性， 如果存在将对应的这条数据的salesNum数量减1
     */
    public void decreaseOrderGoods(OrderGoods good) {
        String[] select = {"salesnum"};
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithCluster(
                OrderGoods.class, select, "salesid = ? and dishesorder_id=? and iscompdish=? and instanceid = ? ",
                good.getSalesId().toString(), orderEntity.getId() + "", "0", good.getInstanceId());
        if (salesOrderGoods.size() > 0) {
            if (salesOrderGoods.get(0).getSalesNum() > 1) {
                good.setSalesNum(salesOrderGoods.get(0).getSalesNum() - 1);
                dataSetorderGoods.update(good, "salesid = ? and dishesorder_id=? and iscompdish=? and instanceid = ?",
                        good.getSalesId().toString(), orderEntity.getId() + "", "0", good.getInstanceId());
            } else if (salesOrderGoods.get(0).getSalesNum() == 1) {
                dataSetorderGoods.deleteAll(OrderGoods.class,
                        "salesid = ? and dishesorder_id=? and iscompdish=? and instanceid = ?", good.getSalesId()
                                .toString(), orderEntity.getId() + "", "0", good.getInstanceId());
            }
        } else {
            // 有错误
        }

    }

    /**
     * 删除对应good的所有子项数据
     */
    public void decreaseAllOrderGoods(DishesInfo dishesInfo) {
        OrderGoods good = newOrderGoods(dishesInfo);
        dataSetorderGoods.deleteAll(OrderGoods.class,
                "salesid = ? and dishesorder_id=? and iscompdish=?", good.getSalesId()
                        .toString(), orderEntity.getId() + "", "0");

    }

    /**
     * 删除对应good的所有子项数据
     */
    public void decreaseAllOrderGoods(OrderGoods good) {
        dataSetorderGoods.deleteAll(OrderGoods.class,
                "salesid = ? and dishesorder_id=? and iscompdish=? and instanceid = ?", good.getSalesId()
                        .toString(), orderEntity.getId() + "", "0", good.getInstanceId());

    }

    /**
     * 根据OrderGoods的salesId判断唯一性， 如果存在将对应的这条数据的salesNum数量设置为num
     */
    public void updateOrderGoods(DishesInfo dishesInfo, int num) {
        OrderGoods good = newOrderGoods(dishesInfo);
        good.setSalesNum(num);
        List<OrderGoods> salesOrderGoods = dataSetorderGoods.findWithWhere(
                OrderGoods.class, "salesid = ? and dishesorder_id=? and iscompdish=?", good
                        .getSalesId().toString(), orderEntity.getId() + "", "0");
        if (salesOrderGoods.size() > 0) {
            dataSetorderGoods.update(good, "salesid = ? and dishesorder_id=? and iscompdish=?", good
                    .getSalesId().toString(), orderEntity.getId() + "", "0");
        } else {
            dataSetorderGoods.save(good);
        }
    }

    /**
     * 根据dishesInfo查询当前类别菜品总数
     */
    public int SumOrderGoodsNumByIndex(DishesInfo dishesInfo) {
        return dataSetorderGoods.sumWhere(OrderGoods.class, "salesnum", int.class,
                "dishestypecode=? and dishesorder_id=? and iscompdish=?",
                dishesInfo.getDishesTypeCode(), orderEntity.getId() + "", "0");

    }

    /**
     * 根据dishestypecode查询当前类别菜品总数
     */
    public int SumOrderGoodsNumByIndex(String dishestypecode) {
        return dataSetorderGoods.sumWhere(OrderGoods.class, "salesnum", int.class,
                "dishestypecode=? and dishesorder_id=? and iscompdish=?", dishestypecode,
                orderEntity.getId() + "", "0");

    }

    /**
     * 根据订单id查询当前类别菜品总数
     */
    public int SumOrderGoodsNumByorderid() {
        System.out.println("orderEntity.getId():" + orderEntity.getId());
        return dataSetorderGoods.sumWhere(OrderGoods.class, "salesnum", int.class,
                " dishesorder_id=? and iscompdish=?", orderEntity.getId() + "", "0");

    }

    /**
     * 查询dishesinfo菜品的salesnum
     */
    public int findOrderGoodsNumByDishesInfo(DishesInfo dishesInfo) {
        String[] select = {"salesnum"};
        List<OrderGoods> me = dataSetorderGoods.findWithCluster(OrderGoods.class,
                select, "salesid=? and dishesorder_id=? and iscompdish =?", dishesInfo
                        .getDishesId().toString(), orderEntity.getId() + "", "0");
        if (me.size() == 1) {
            return me.get(0).getSalesNum();
        } else if (me.size() > 1) {
            int sum = 0;
            for (int i = 0; i < me.size(); i++) {
                sum += me.get(i).getSalesNum();
            }
            return sum;
        } else {
            return 0;
        }
    }

    /**
     * 查询dishesinfo菜品的salesnum
     */
    public int findOrderGoodsNumByDishesInfo(OrderGoods good) {
        String[] select = {"salesnum"};
        List<OrderGoods> me = dataSetorderGoods.findWithCluster(OrderGoods.class,
                select, "salesid=? and dishesorder_id=? and iscompdish =?", good
                        .getSalesId().toString(), orderEntity.getId() + "", "0");
        if (me.size() == 1) {
            return me.get(0).getSalesNum();
        } else {
            return 0;
        }

    }

    /**
     * 初始开单生产order订单
     *
     * @param username
     */
    public void initOrderData(String username, String diskid) {
        orderEntity.setOrderType("0");
        orderEntity.setOrderTypeName("点餐");
        orderEntity.setDeskId(Long.valueOf(diskid));
//		orderEntity.setMerchantId(Long.valueOf(username));
        orderEntity.setChildMerchantId(username);
//        orderEntity.setUserId(Long.valueOf("1234567890"));
        orderEntity.setInMode("2");
        orderEntity.setPayType("0");
        orderEntity.setOrderState("0");
        dataSetorder.save(orderEntity);
    }

    /**
     * 初始开单生产order订单
     *
     * @param username
     */
    public void initOrderData(String username, String diskid, String orderid) {
        orderEntity.setOrderId(Long.valueOf(orderid));
        orderEntity.setOrderType("0");
        orderEntity.setOrderTypeName("点餐");
        orderEntity.setDeskId(Long.valueOf(diskid));
        orderEntity.setChildMerchantId(username);
//        orderEntity.setUserId(Long.valueOf("1234567890"));
        orderEntity.setInMode("2");
        orderEntity.setPayType("0");
        orderEntity.setOrderState("0");
        dataSetorder.save(orderEntity);
    }

    /**
     * 初始开单生产order订单
     *
     * @param username
     */
    public void initOrderData(String username, String diskid, int number) {
        orderEntity.setOrderType("0");
        orderEntity.setOrderTypeName("点餐");
        orderEntity.setDeskId(Long.valueOf(diskid));
        orderEntity.setChildMerchantId(username);
//        orderEntity.setUserId(Long.valueOf("1234567890"));
        orderEntity.setInMode("2");
        orderEntity.setPayType("0");
        orderEntity.setOrderState("0");
        orderEntity.setPersonNum(number);
        dataSetorder.save(orderEntity);
    }

    /**
     * 更新已经保存的订单数据
     *
     */
    public void upOrderData() {
        dataSetorder.update(orderEntity, orderEntity.getId());
    }

    /**
     * 新增菜品子项
     *
     * @param dishesInfo
     * @return
     */
    public OrderGoods newOrderGoods(DishesInfo dishesInfo) {
        Date date = new Date();
        long InstanceId = date.getTime();
        OrderGoods good = new OrderGoods();
        good.setSalesPrice(dishesInfo.getDishesPrice());
        good.setDishesPrice(dishesInfo.getDishesPrice());
        good.setSalesName(dishesInfo.getDishesName());
        good.setSalesId(Long.valueOf(dishesInfo.getDishesId()));
        good.setDishesTypeCode(dishesInfo.getDishesTypeCode());
        good.setDishesTypeName(dishesInfo.getDishesTypeName());
        good.setExportId(dishesInfo.getExportId());
        good.setSalesState("0");
        good.setRemark("[]");
        good.setCompDish(false);
        good.setInstanceId("" + InstanceId);
        long j = 0;
        good.setInterferePrice(j);
        good.setCompId(j);
        good.setIsComp("0");
        good.setOrder(orderEntity);
        good.setMemberPrice(dishesInfo.getMemberPrice());
        good.setIsZdzk(dishesInfo.getIsZdzk());
        Log.i("orderEntity", "orderEntity:" + orderEntity);
        return good;
    }

    /**
     * 新增菜品子项
     *
     * @param dishesInfo
     * @return
     */
    public OrderGoods newOrderGoodsWithInstance(DishesInfo dishesInfo,long InstanceId,String remark) {
        OrderGoods good = new OrderGoods();
        good.setSalesPrice(dishesInfo.getDishesPrice());
        good.setDishesPrice(dishesInfo.getDishesPrice());
        good.setSalesName(dishesInfo.getDishesName());
        good.setSalesId(Long.valueOf(dishesInfo.getDishesId()));
        good.setDishesTypeCode(dishesInfo.getDishesTypeCode());
        good.setDishesTypeName(dishesInfo.getDishesTypeName());
        good.setExportId(dishesInfo.getExportId());
        good.setSalesState("0");
        good.setRemark(remark);
        good.setCompDish(false);
        good.setInstanceId("" + InstanceId);
        long j = 0;
        good.setInterferePrice(j);
        good.setCompId(j);
        good.setIsComp("0");
        good.setOrder(orderEntity);
        good.setMemberPrice(dishesInfo.getMemberPrice());
        good.setIsZdzk(dishesInfo.getIsZdzk());
        Log.i("orderEntity", "orderEntity:" + orderEntity);
        return good;
    }

    /**
     * 获取当前业务实体类下的订单变量
     *
     * @return
     */
    public DishesOrder getOrderEntity() {
        orderEntity.getItemlistDb();
        return orderEntity;
    }

    /**
     * 给当前业务实体设置已有订单数据，刷新修改当前订单内容
     *
     * @return
     */
    public void setOrderEntity(DishesOrder dishesOrder) {
        this.orderEntity = dishesOrder;
    }

    /**
     * 查询出用于我的订单列表展示用的数据集，组合生成ShoppingOrder返回
     *
     * @return
     */
    public ShoppingOrder findShoppingOrder() {
        ShoppingOrder shopping = new ShoppingOrder();
        ArrayList<DishesType> dishesTypeList = (ArrayList<DishesType>) dataSetDishesType
                .findAll(DishesType.class);
        for (DishesType dishesType : dishesTypeList) {
            ArrayList<OrderGoods> goods = (ArrayList<OrderGoods>) dataSetorderGoods
                    .findWithWhere(OrderGoods.class,
                            "dishesorder_id=? and dishestypecode=? and iscompdish=?",
                            orderEntity.getId() + "",
                            dishesType.getDishesTypeCode(), "0");
            if (goods.size() > 0) {
                shopping.getmHeaderPositions().add(shopping.getOrderGoods().size());
                shopping.getmHeaderNames().add(dishesType.getDishesTypeName());
            }
            for (OrderGoods ordergoods : goods) {
                shopping.getOrderGoods().add(ordergoods);
            }
        }
        return shopping;

    }

    /**
     * 提交服务器成功后删除本地order缓存，保存服务器订单到本地,并且合并本地订单
     *
     * @param dishesorder
     */
    public DishesOrder addDishesOrder(DishesOrder dishesorder, String notityKitchen) {
        for (OrderGoods orderGoods : orderEntity.getOrderGoods()) {
            //保存对应的orderGoods到新加的dishesorder
            if (notityKitchen.equals(Constants.notityKitchen_now)) {
                orderGoods.setSalesState(Constants.notityKitchen_now);
            } else if (notityKitchen.equals(Constants.notityKitchen_wait)) {
                orderGoods.setSalesState(Constants.notityKitchen_wait);
            }
            dishesorder.getOrderGoods().add(orderGoods);
            //保存删除对应菜品的标签，避免数据遗漏
            dataSetRemarkItem.deleteAll(RemarkItem.class, "dishesid=? and dishesorderid=? and compid=?", orderGoods.getSalesId() + "", orderEntity.getId() + "", "0");
        }
        Log.i("upDishesOrder", "dishesorder.getOrderGoods().size():" + dishesorder.getOrderGoods().size());
        dataSetorderGoods.deleteAll(OrderGoods.class, "id = ? ", "" + orderEntity.getId());
        orderEntity.delete();
        dataSetorder.save(dishesorder);
        dataSetorderGoods.saveAllOrderGoods(dishesorder.getOrderGoods());
        return dishesorder;
    }

    /**
     * 提交服务器成功后删除本地order缓存，更新服务器orderid到本地缓存
     */
    public void upDishesOrder(String orderid) {
        ContentValues values = new ContentValues();
        values.put("orderid", orderid);
        int i=dataSetorder.updateAll(DishesOrder.class, values, "id = ? ", "" + orderEntity.getId());
        orderEntity.setOrderId(Long.valueOf(orderid));
    }

    /**
     * 通知后厨后更新当前订单的菜品子项
     */
    public void upDishesOrderGoods(List<OrderGoods> ordergoods) {
        dataSetorderGoods.deleteAll(OrderGoods.class, "id = ? ", "" + orderEntity.getId());
        orderEntity.setOrderGoods(ordergoods);
        dataSetorderGoods.saveAllOrderGoods(ordergoods);
    }

    /**
     * 清除当前订单的缓存数据
     */
    public void delDishesOrder() {
        orderEntity.delete();
    }

    /**
     * 判断本条记录操作实体下的所有子菜品是否都已经通知后厨
     * 全部通知过了返回true，否则返回false
     *
     * @return
     */
    public boolean isnotityKitchen() {
        ArrayList<OrderGoods> ss = (ArrayList<OrderGoods>) dataSetorderGoods.findWithWhere(OrderGoods.class, "dishesorder_id = ? and salesstate = ? ", "" + orderEntity.getId(), "0");
        for (OrderGoods goods : ss) {
            if (goods.getSalesState().equals("0")) {
                System.out.println("orderEntity.id:" + orderEntity.getId());
                System.out.println("goods.getSalesName：" + goods.getSalesName());
                return false;
            }
        }
        return true;
    }

    /**
     * 查询出本条记录操作实体下的子菜品中未打印的菜品，即salesState==0
     *
     * @return
     */
    public ArrayList<OrderGoods> notityKitchen() {
        ArrayList<OrderGoods> NotifyList = new ArrayList<OrderGoods>();
        for (OrderGoods goods : orderEntity.getOrderGoods()) {
            if (goods.getSalesState().equals("0")) {
                NotifyList.add(goods);
            }
        }
        return NotifyList;
    }

    /**
     * 通知服务器打印订单菜品
     * 修改ordergoods子菜品的状态从0--》2
     */
    public void notityKitchenSuccess() {
        ContentValues values = new ContentValues();
        values.put("salesstate", "2");
        dataSetorderGoods.updateAll(OrderGoods.class, values, "dishesorder_id = ? and salesstate = ? ", "" + orderEntity.getId(), "0");

        ContentValues valuesOrder = new ContentValues();
        orderEntity.setOrderState("0");
        valuesOrder.put("orderstate",orderEntity.getOrderState());
        dataSetorder.updateAll(DishesOrder.class, valuesOrder, "id = ? ", "" + orderEntity.getId());
    }

    /**
     * 获取套餐操作业务实体类
     * 不允许直接设置，只能通过先实例化的DishesEntity来获取对应CompDishEntity
     *
     * @return
     */
    public CompDishesEntity getCompDishesEntity() {
        return compDishesEntity;
    }

    /**
     * 根据dishesid,itemtype,itemid三个字段来唯一识别remarkitem表中选中标签集，并且只有一条这样的数据
     * 只能通过此方法增加
     * dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public void addDishesItem(DishesItem dishesItem,String instanceid) {
        RemarkItem remarkItem = new RemarkItem();
        remarkItem.setDishesId(dishesItem.getDishesId());
        remarkItem.setItemType(dishesItem.getItemType());
        remarkItem.setItemId(dishesItem.getItemId());
        remarkItem.setItemName(dishesItem.getItemName());
        remarkItem.setItemTypeName(dishesItem.getItemTypeName());
        Long j = (long) 0;
        remarkItem.setCompid(j);
        remarkItem.setDishesorderid(orderEntity.getId());
        remarkItem.setInstanceid(instanceid);
        List<RemarkItem> alldishesitem = dataSetRemarkItem.findWithWhere(RemarkItem.class, "dishesid=? and itemtype =? and itemid = ? and dishesorderid= ? and compid =?", dishesItem.getDishesId(), dishesItem.getItemType(), dishesItem.getItemId() + "", orderEntity.getId() + "", "0");
        if (alldishesitem.size() == 0)
            dataSetRemarkItem.save(remarkItem);
        else
            dataSetRemarkItem.save(remarkItem);
    }

    /**
     * 根据dishesid,itemtype,itemid三个字段来唯一识别remarkitem表中选中标签集，并且只有一条这样的数据
     * 通过此方法删除单条标签
     * dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public int delDishesItem(DishesItem dishesItem,String instanceid) {
        return dataSetRemarkItem.deleteAll(RemarkItem.class, "dishesid=? and itemtype =? and itemid = ? and dishesorderid=? and compid=? and instanceid = ?", dishesItem.getDishesId(), dishesItem.getItemType(), dishesItem.getItemId() + "", orderEntity.getId() + "", "0",instanceid);
    }

    /**
     * 根据dishesid,dishesorderid2个字段来删除某菜品的所有标签
     * dishesorderid其挂靠的订单本地缓存id
     *
     */
    public int delDishesInfoItem(DishesInfo dishesInfo) {

        return dataSetRemarkItem.deleteAll(RemarkItem.class, "dishesid=? and dishesorderid=? and compid=?", dishesInfo.getDishesId(), orderEntity.getId() + "", "0");
    }

    /**
     * 根据dishesid,itemtype,itemid三个字段来唯一识别remarkitem表中选中标签集，并且只有一条这样的数据
     * 查看是否存在此标签的选中记录
     * dishesorderid其挂靠的订单本地缓存id
     *
     * @param dishesItem
     */
    public boolean findRemarkItem(DishesItem dishesItem) {
        List<RemarkItem> alldishesitem = dataSetRemarkItem.findWithWhere(RemarkItem.class, "dishesid=? and itemtype =? and itemid = ? and dishesorderid =? and compid=?", dishesItem.getDishesId(), dishesItem.getItemType(), dishesItem.getItemId() + "", orderEntity.getId() + "", "0");
        if (alldishesitem.size() == 0) return false;
        else return true;
    }

    /**
     * 查询某道菜品的所有所选属性
     * dishesorderid其挂靠的订单本地缓存id
     *
     */
    public ArrayList<RemarkItem> findRemarkItemList(String dishesId,String instanceid) {
        ArrayList<RemarkItem> alldishesitem = (ArrayList<RemarkItem>) dataSetRemarkItem.findWithWhere(RemarkItem.class, "dishesid=? and dishesorderid =? and compid=? and instanceid = ?", dishesId, orderEntity.getId() + "", "0",instanceid);
        return alldishesitem;
    }

    /**
     * 查询某道菜品的所有所选属性
     * dishesorderid其挂靠的订单本地缓存id
     *
     */
    public ArrayList<RemarkItem> findRemarkItemList(ServerOrderGoods orderGoods) {
        ArrayList<RemarkItem> alldishesitem = (ArrayList<RemarkItem>) dataSetRemarkItem.findWithWhere(RemarkItem.class, "dishesid=? and dishesorderid =? and compid=? and instanceid = ?", orderGoods.getSalesId()+"", orderEntity.getId() + "", orderGoods.getCompId()+"",orderGoods.getInstanceId()+"");
        return alldishesitem;
    }

    /**
     * 删除某道菜品的所有所选属性
     * dishesorderid其挂靠的订单本地缓存id
     *
     */
    public void delRemarkItemList(String dishesId,String instanceid) {
        dataSetRemarkItem.deleteAll(RemarkItem.class, "dishesid=? and dishesorderid =? and compid=? and instanceid = ?", dishesId, orderEntity.getId() + "", "0",instanceid);
    }

    /**
     * 提交服务器成功后删除本地order缓存，保存服务器订单到本地
     *
     * @param dishesorder
     */
    public void addDishesOrder(DishesOrder dishesorder) {
        ContentValues values = new ContentValues();
        values.put("orderid", dishesorder.getOrderId());
        values.put("strcreatetime", dishesorder.getStrCreateTime());
        values.put("originalprice", dishesorder.getOriginalPrice());
        values.put("timestr", dishesorder.getTimeStr());
        values.put("orderstate", dishesorder.getOrderState());
        values.put("orderstatename", dishesorder.getOrderStateName());
        values.put("invoid", dishesorder.getTimeStr());
        values.put("merchantname", dishesorder.getMerchantName());
        values.put("allgoodsnum", dishesorder.getAllGoodsNum());
        dataSetorder.updateAll(DishesOrder.class, values, "id = ? ", "" + orderEntity.getId());
        dataSetorderGoods.deleteAll(OrderGoods.class, "id = ? ", "" + orderEntity.getId());
        orderEntity.setOrderGoods(dishesorder.getOrderGoods());
        dataSetorderGoods.saveAllOrderGoods(dishesorder.getOrderGoods());
    }

    /**
     * 保留订单重新保存生成保留订单
     * 注：只能在第一次下单开桌的时候选择保留订单
     *
     */
    public void persistServerOrder(ServerOrder serverOrder) {
        orderEntity.setRemark(serverOrder.getRemark());
        orderEntity.setPersonNum(serverOrder.getPersonNum());
        orderEntity.setTradeStaffId(serverOrder.getTradeStsffId());
        orderEntity.setCreateTime(serverOrder.getCreateTime());
        orderEntity.setMerchantId(serverOrder.getMerchantId());
        orderEntity.setOriginalPrice(serverOrder.getOriginalPrice());
        orderEntity.setOrderState(serverOrder.getOrderState());
        ContentValues values = new ContentValues();
        values.put("remark", serverOrder.getRemark());
        values.put("personnum", serverOrder.getPersonNum());
        values.put("tradestaffid", serverOrder.getTradeStsffId());
        values.put("createtime", serverOrder.getCreateTime());
        values.put("merchantid", serverOrder.getMerchantId());
        values.put("originalprice", serverOrder.getOriginalPrice());
        values.put("orderstate",serverOrder.getOrderState());
        dataSetorder.updateAll(DishesOrder.class, values, "id = ? ", "" + orderEntity.getId());
    }

    /**
     * 保留订单，更新生成订单中未保存数据值到缓存中
     * 注：只能在第一次下单开桌的时候选择保留订单
     */
    public void persistServerOrderGoods(ServerOrderGoods serverOrderGoods) {
        ContentValues values = new ContentValues();
        values.put("tradestaffid", serverOrderGoods.getTradeStaffId());
        values.put("remark", serverOrderGoods.getRemark().toString().replace("[","").replace("]",""));
        values.put("salesstate", serverOrderGoods.getSalesState());
        values.put("orderid", serverOrderGoods.getOrderId());
        values.put("deskid", serverOrderGoods.getDeskId());
        values.put("salesprice", serverOrderGoods.getSalesPrice());

        dataSetorderGoods.updateAll(OrderGoods.class, values, "salesid = ? and dishesorder_id = ? and instanceid=?", serverOrderGoods.getSalesId() + "", "" + orderEntity.getId(), serverOrderGoods.getInstanceId());
    }

    public void updateDishesUrl(DishesInfo dishesInfo) {
        ContentValues values = new ContentValues();
        values.put("dishesUrl", dishesInfo.getDishesUrl());
        dataSetDishesInfo.updateAll(DishesInfo.class, values, "dishesid = ? ", dishesInfo.getDishesId() + "");

    }

}
