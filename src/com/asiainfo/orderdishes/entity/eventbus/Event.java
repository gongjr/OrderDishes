package com.asiainfo.orderdishes.entity.eventbus;

public class Event<T> {
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T data;

    public int type;

}
