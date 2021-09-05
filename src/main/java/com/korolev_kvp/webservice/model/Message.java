package com.korolev_kvp.webservice.model;

public class Message {

    private String key;
    private String data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "id: " + key +
                ",\nname: " + data + '"';
    }
}
