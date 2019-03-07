package com.wp.android_base.api.mining;

import java.util.List;

/**
 * Created by wangpeng on 2018/7/12.
 */

public class PriceData {

    private String last;
    private List<String[]> asks;
    private List<String[]> bids;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }


    public List<String[]> getAsks() {
        return asks;
    }

    public void setAsks(List<String[]> asks) {
        this.asks = asks;
    }

    public List<String[]> getBids() {
        return bids;
    }

    public void setBids(List<String[]> bids) {
        this.bids = bids;
    }
}
