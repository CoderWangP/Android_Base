package com.wp.android_base.model;

/**
 * Created by wp on 2019/7/30.
 * <p>
 * Description:
 */

public class WidData {
    private int code;
    private Wid data;
    private String message;


    public Wid getData() {
        return data;
    }

    public void setData(Wid data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Wid {
        private String w_id;

        public String getW_id() {
            return w_id;
        }

        public void setW_id(String w_id) {
            this.w_id = w_id;
        }
    }
}
