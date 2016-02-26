package com.asiainfo.orderdishes.helper;

import android.os.HandlerThread;
import android.util.Log;

import com.asiainfo.orderdishes.entity.eventbus.ChildEventBusData;
import com.asiainfo.orderdishes.entity.eventbus.Event;
import com.asiainfo.orderdishes.entity.litepal.MerchantCompDishesInfo;

import de.greenrobot.event.EventBus;

public class ChildEventBusThread extends HandlerThread {


    private String Threadname;


    public ChildEventBusThread(String name) {
        super(name);
        this.Threadname = name;
        EventBus.getDefault().register(this);
    }

    @Override
    public void run() {
        super.run();
    }

    public void onEventBackgroundThread(ChildEventBusData event) {
        event.getDishesEntity().getCompDishesEntity().saveCompDishes(event.getBaseUtils());
        MerchantCompDishesInfo dd = event.getDishesEntity().getCompDishesEntity().getCompDishes("1000898", "");
        Event<MerchantCompDishesInfo> eventComp = new Event<MerchantCompDishesInfo>();
        eventComp.setData(dd);
        eventComp.setType(2);
        EventBus.getDefault().post(eventComp);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void destroy() {
        Log.i("ChildEventBusThread", "线程注销name:" + Threadname);
        super.destroy();
        EventBus.getDefault().unregister(this);
    }

}
