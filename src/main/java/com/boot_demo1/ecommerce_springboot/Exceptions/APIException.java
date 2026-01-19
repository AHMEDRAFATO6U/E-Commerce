package com.boot_demo1.ecommerce_springboot.Exceptions;

public class APIException extends RuntimeException {
    public APIException() {

    }
    public APIException(String message) {
        super(message);
    }
}
