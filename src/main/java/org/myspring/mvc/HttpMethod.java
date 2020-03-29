package org.myspring.mvc;

public enum HttpMethod {

    GET("get"),

    POST("post"),

    DELETE("delete"),

    PUT("put");

    String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HttpMethod parse(String name) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name.equalsIgnoreCase(name)) {
                return httpMethod;
            }
        }

        return null;
    }
}