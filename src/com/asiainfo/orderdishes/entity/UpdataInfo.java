package com.asiainfo.orderdishes.entity;

/**
 * Created by gjr on 2015/8/17.
 */
public class UpdataInfo {
    private int version;
    private String url;
    private String description;
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
