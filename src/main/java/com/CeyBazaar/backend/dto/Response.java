package com.CeyBazaar.backend.dto;

public class Response<T> {

    private Integer responseCode;
    private String responseDesc;
    private T responseObject;

    public Response() {
    }

    public Response(Integer responseCode, String responseDesc, T responseObject) {
        this.responseCode = responseCode;
        this.responseDesc = responseDesc;
        this.responseObject = responseObject;
    }



    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }
}