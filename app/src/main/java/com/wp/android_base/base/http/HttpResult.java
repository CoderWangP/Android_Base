package com.wp.android_base.base.http;

import android.support.annotation.Keep;

/**
 *
 * Created by wangpeng on 2018/6/26
 *
 * 网络框架的基类封装
 * 数据格式如下：
 * {
 *     code : 0,
 *     data:{},
 *     message
 * }
 *
 */

@Keep
public class HttpResult<T> {
	private int code;
	private T data;
	private String message;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
