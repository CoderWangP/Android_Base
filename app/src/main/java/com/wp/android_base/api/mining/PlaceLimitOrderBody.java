package com.wp.android_base.api.mining;

/**
 * Created by wangpeng on 2018/7/12.
 */

public class PlaceLimitOrderBody {

    private String access_id;
    private String amount;
    private String market;
    private String price;
    private String tonce;
    private String type;

    public PlaceLimitOrderBody(String access_id, String amount, String market, String price, String tonce, String type) {
        this.access_id = access_id;
        this.amount = amount;
        this.market = market;
        this.price = price;
        this.tonce = tonce;
        this.type = type;
    }

    public String getAccess_id() {
        return access_id;
    }

    public void setAccess_id(String access_id) {
        this.access_id = access_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTonce() {
        return tonce;
    }

    public void setTonce(String tonce) {
        this.tonce = tonce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
