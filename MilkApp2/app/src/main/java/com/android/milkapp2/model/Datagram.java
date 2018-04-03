package com.android.milkapp2.model;

public class Datagram {

    private String Type;
    private String Request;
    private String JsonStream;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        this.Request = request;
    }

    public String getJsonStream() {
        return JsonStream;
    }

    public void setJsonStream(String jsonStream) {
        this.JsonStream = jsonStream;
    }

    @Override
    public String toString() {
        return "Datagram{" +
                "Type='" + Type + '\'' +
                ", Request='" + Request + '\'' +
                ", JsonStream='" + JsonStream + '\'' +
                '}';
    }
}
