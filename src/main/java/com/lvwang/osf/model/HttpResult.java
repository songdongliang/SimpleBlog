package com.lvwang.osf.model;

public class HttpResult {

    private Integer code;

    private String data;

    public HttpResult() {
    }

    public HttpResult(Integer code, String data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static HttpResult buildServerError() {
        HttpResult httpResult = new HttpResult();
        httpResult.setCode(500);
        httpResult.setData("Server Error");
        return httpResult;
    }
}