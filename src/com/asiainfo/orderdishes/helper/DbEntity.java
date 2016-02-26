package com.asiainfo.orderdishes.helper;

import com.asiainfo.orderdishes.entity.DataBinder;
import com.asiainfo.orderdishes.entity.litepal.DishesInfo;
import com.asiainfo.orderdishes.entity.litepal.DishesItem;
import com.asiainfo.orderdishes.entity.litepal.DishesItemType;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.DishesType;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesType;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;
import com.asiainfo.orderdishes.entity.litepal.RemarkItem;

import java.util.ArrayList;
import java.util.List;

public class DbEntity {
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
     * 用于绑定表对象，完成MerchantCompDishesInfo对象对应表的操作
     */
    public DataBinder<MerchantCompDishesInfo> dataSetCompDishesInfo;
    /**
     * 用于绑定表对象，完成MerchantCompDishesType对象对应表的操作
     */
    public DataBinder<MerchantCompDishesType> dataSetCompDishesType;

    public DbEntity() {
        initDataBinder();
    }

    public void initDataBinder() {
        dataSetDishesType = new DataBinder<DishesType>();
        dataSetDishesInfo = new DataBinder<DishesInfo>();
        dataSetItemType = new DataBinder<DishesItemType>();
        dataSetItem = new DataBinder<DishesItem>();
        dataSetorderGoods = new DataBinder<OrderGoods>();
        dataSetorder = new DataBinder<DishesOrder>();
        dataSetRemarkItem = new DataBinder<RemarkItem>();
        dataSetCompDishesInfo = new DataBinder<MerchantCompDishesInfo>();
        dataSetCompDishesType = new DataBinder<MerchantCompDishesType>();

    }

    public void clearOrder() {
        dataSetorder.deleteAll(DishesOrder.class);
        dataSetorderGoods.deleteAll(OrderGoods.class);
        dataSetRemarkItem.deleteAll(RemarkItem.class);
    }

    public void clearDishes() {
        dataSetDishesType.deleteAll(DishesType.class);
        dataSetDishesInfo.deleteAll(DishesInfo.class);
        dataSetItemType.deleteAll(DishesItemType.class);
        dataSetItem.deleteAll(DishesItem.class);
        dataSetCompDishesInfo.deleteAll(MerchantCompDishesInfo.class);
        dataSetCompDishesType.deleteAll(MerchantCompDishesType.class);
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
                    System.out.println("dishesItemType:" + dishesItemType.getItemTypeName());
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
}
