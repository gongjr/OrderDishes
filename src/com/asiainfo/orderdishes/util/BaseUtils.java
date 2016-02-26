package com.asiainfo.orderdishes.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.asiainfo.orderdishes.entity.ServerOrder;
import com.asiainfo.orderdishes.entity.ServerOrderGoods;
import com.asiainfo.orderdishes.entity.litepal.DishesOrder;
import com.asiainfo.orderdishes.entity.litepal.OrderGoods;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseUtils {
    public Context mContext;

    public BaseUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            System.out.println("serviceName:"
                    + serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().equals(className) == true) {

                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * app是运行
     *
     * @param context
     * @return
     */
    public boolean isrun(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(
                    "com.linkage.mealdealer")
                    && info.baseActivity.getPackageName().equals(
                    "com.linkage.mealdealer")) {
                return true;
                // find it, break
            }
        }
        return false;
    }

    // NETWORK STATE
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else// get all network info
        {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测某ActivityUpdate是否在当前Task的栈顶
     */
    public boolean isTopActivy(String cmdName) {
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if (null != runningTaskInfos) {
            cmpNameTemp = runningTaskInfos.get(0).topActivity.toString();
            System.out.println("cmpNameTemp:" + cmpNameTemp);
            System.out.println("cmdName:" + cmdName);

        }
        if (null == cmpNameTemp)
            return false;
        System.out.println("cmpNameTemp.equals(cmdName):"
                + cmpNameTemp.equals(cmdName));
        return cmpNameTemp.equals(cmdName);
    }

    /**
     * 提交数据到服务器之前过滤本地订单数据中的id
     * 由于不能继承实现，所有采用全部读取数据转换
     *
     * @param order
     * @return
     */
    public ServerOrder DishesOrderToServerOrder(DishesOrder order) {
        ServerOrder sOrder = new ServerOrder();
        sOrder.setOrderId(order.getOrderId());
        sOrder.setOrderType(order.getOrderType());
        sOrder.setOrderTypeName(order.getOrderTypeName());
        sOrder.setCreateTime(order.getCreateTime());
        sOrder.setStrCreateTime(order.getStrCreateTime());
        sOrder.setUserId(order.getUserId());
        sOrder.setTimeStr(order.getTimeStr());
        sOrder.setOrderState(order.getOrderState());
        sOrder.setOrderStateName(order.getOrderStateName());
        sOrder.setRemark(order.getRemark());
        sOrder.setOriginalPrice(order.getOriginalPrice());
        sOrder.setPayState(order.getPayState());
        sOrder.setPayType(order.getPayType());
        sOrder.setPayTypeName(order.getPayTypeName());
        sOrder.setFinishTime(order.getFinishTime());
        sOrder.setIsNeedInvo(order.getIsNeedInvo());
        sOrder.setInvoPrice(order.getInvoPrice());
        sOrder.setInvoId(order.getInvoId());
        sOrder.setInvoTitle(order.getInvoTitle());
        sOrder.setMerchantId(order.getMerchantId());
        sOrder.setMerchantName(order.getMerchantName());
        sOrder.setPhoneNumber(order.getPhoneNumber());
        sOrder.setPaidPrice(order.getPaidPrice());
        sOrder.setPostAddrId(order.getPostAddrId());
        sOrder.setPostAddrInfo(order.getPostAddrInfo());
        sOrder.setLinkPhone(order.getLinkPhone());
        sOrder.setLinkName(order.getLinkName());
        sOrder.setServiceTime(order.getServiceTime());
        sOrder.setInMode(order.getInMode());
        sOrder.setInMode(order.getInMode());
        sOrder.setDinnerDesk(order.getDinnerDesk());
        sOrder.setAllGoodsNum(order.getAllGoodsNum());
        sOrder.setDeskId(order.getDeskId());
        sOrder.setGeneralSitauation(order.getGeneralSitauation());
        sOrder.setChildMerchantId(order.getChildMerchantId());
        sOrder.setSendBusi(order.getSendBusi());
        sOrder.setIsUseGift(order.getIsUseGift());
        sOrder.setGiftMoney(order.getGiftMoney());
        sOrder.setFromCode(order.getFromCode());
        sOrder.setFromId(order.getFromId());
        sOrder.setPersonNum(order.getPersonNum());
        sOrder.setDeskId(order.getDeskId());
        sOrder.setTradeStsffId(order.getTradeStaffId());
        for (OrderGoods orderGoods : order.getOrderGoods()) {
            ServerOrderGoods servergoods = new ServerOrderGoods();
            servergoods.setOrderId(orderGoods.getOrderId());
            servergoods.setSalesId(orderGoods.getSalesId());
            servergoods.setSalesName(orderGoods.getSalesName());
            servergoods.setSalesNum(orderGoods.getSalesNum());
            servergoods.setSalesPrice(orderGoods.getDishesPrice());
            if(orderGoods.getRemark()!=null)
			servergoods.setOldremark(orderGoods.getRemark());
            servergoods.setDishesSurl(orderGoods.getDishesSurl());
            servergoods.setDishesPrice(orderGoods.getDishesPrice());
            servergoods.setDishesTypeCode(orderGoods.getDishesTypeCode());
            servergoods.setDishesTypeName(orderGoods.getDishesTypeName());
//			servergoods.setCreateTime(orderGoods.getCreateTime());
            servergoods.setSalesState(orderGoods.getSalesState());
            servergoods.setCompDish(orderGoods.isCompDish());
            servergoods.setTradeStaffId(orderGoods.getTradeStaffId());
            servergoods.setInstanceId(orderGoods.getInstanceId());
            servergoods.setInterferePrice(orderGoods.getInterferePrice());
            servergoods.setExportId(orderGoods.getExportId());
            servergoods.setCompId(orderGoods.getCompId());
            servergoods.setMemberPrice(orderGoods.getMemberPrice());
            servergoods.setIsZdzk(orderGoods.getIsZdzk());
            sOrder.getOrderGoods().add(servergoods);

        }
        return sOrder;
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentData() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String curtime = dd.format(date);
        return curtime;

    }

    public String getAssetsTxt(String name) {
        String txt = "";
        try {
            InputStream in = mContext.getAssets().open(name);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            txt = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //移除空格、换行
        String abc = txt.replaceAll(" ", "");
        String ss = abc.replaceAll("\r|\n|\t", "");
        return ss;
    }
}
