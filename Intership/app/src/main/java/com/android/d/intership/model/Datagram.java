package com.android.d.intership.model;

public class Datagram {

    private String type;
    private String request;
    private String jsonStream;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getJsonStream() {
        return jsonStream;
    }

    public void setJsonStream(String jsonStream) {
        this.jsonStream = jsonStream;
    }

    @Override
    public String toString() {
        return "Datagram{" +
                "type='" + type + '\'' +
                ", request='" + request + '\'' +
                ", jsonStream='" + jsonStream + '\'' +
                '}';
    }
}
