package org.example.Response;

import java.util.List;

public class Response<T> {
    private int statusCode;
    private Object meta;
    private boolean succeeded;
    private String message;
    private List<String> errors;
    private T data;

    public Response() {
    }

    public Response(T data) {
        this.succeeded = true;
        this.data = data;
    }

    public Response(T data, String message) {
        this.succeeded = true;
        this.message = message;
        this.data = data;
    }

    public Response(String message) {
        this.succeeded = false;
        this.message = message;
    }

    public Response(String message, boolean succeeded) {
        this.succeeded = succeeded;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
