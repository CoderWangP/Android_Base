package com.wp.android_base.model;

/**
 * Created by wp on 2019/1/22.
 * <p>
 * Description:
 */

public class Balance {

    private String balance;

    private String available;    // 可用
    private String locked;       // 解绑中
    private String locked_warehouse;    // 锁仓中
    private String not_recovered;  // 未取回收益
    private String pending_order;  // 挂单中
    private String share;        // 委托中
    private String bancor_pending;//bancor市场

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getLocked_warehouse() {
        return locked_warehouse;
    }

    public void setLocked_warehouse(String locked_warehouse) {
        this.locked_warehouse = locked_warehouse;
    }

    public String getNot_recovered() {
        return not_recovered;
    }

    public void setNot_recovered(String not_recovered) {
        this.not_recovered = not_recovered;
    }

    public String getPending_order() {
        return pending_order;
    }

    public void setPending_order(String pending_order) {
        this.pending_order = pending_order;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getBancor_pending() {
        return bancor_pending;
    }

    public void setBancor_pending(String bancor_pending) {
        this.bancor_pending = bancor_pending;
    }
}
