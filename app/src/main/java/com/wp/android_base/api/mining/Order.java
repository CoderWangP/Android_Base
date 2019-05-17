package com.wp.android_base.api.mining;

/**
 * Created by wangpeng on 2018/7/13.
 */

public class Order {

    private String price;
    private String orderNumber;

    public Order(String price, String orderNumber) {
        this.price = price;
        this.orderNumber = orderNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object obj) {
        return this.orderNumber.equals(((Order)obj).getOrderNumber());
    }
}
