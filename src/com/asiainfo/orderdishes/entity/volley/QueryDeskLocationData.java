package com.asiainfo.orderdishes.entity.volley;

import com.asiainfo.orderdishes.entity.litepal.MerchantDeskLocation;

import java.util.List;

public class QueryDeskLocationData {

    List<MerchantDeskLocation> info;

    public List<MerchantDeskLocation> getInfo() {
        return info;
    }

    public void setInfo(List<MerchantDeskLocation> info) {
        this.info = info;
    }
}
