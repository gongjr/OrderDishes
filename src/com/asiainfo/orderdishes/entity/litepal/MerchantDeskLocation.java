package com.asiainfo.orderdishes.entity.litepal;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 桌子区域实体
 *
 * @author ouyangyj
 *         <p/>
 *         2015年3月31日
 */
public class MerchantDeskLocation extends DataSupport implements Serializable {

    private int id;

    /**
     * 分店ID
     */
    private Long childMerchantId;

    /**
     * 位置编码
     */
    private String locationCode;

    /**
     * 位置名称
     */
    private String locationName;

    /**
     * 桌子区域下面的桌子列表
     */
    private List<MerchantDesk> merchantDeskList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getChildMerchantId() {
        return childMerchantId;
    }

    public void setChildMerchantId(Long childMerchantId) {
        this.childMerchantId = childMerchantId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<MerchantDesk> getMerchantDeskList() {
        return merchantDeskList;
    }

    public void setMerchantDeskList(List<MerchantDesk> merchantDeskList) {
        this.merchantDeskList = merchantDeskList;
    }
}
