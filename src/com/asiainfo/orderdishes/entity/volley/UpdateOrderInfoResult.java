package com.asiainfo.orderdishes.entity.volley;

/**
 * Created by gjr on 2015/8/31.
 */
public class UpdateOrderInfoResult {

    String state;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    String error;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
