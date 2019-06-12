package com.taotao.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class ItemCat implements Serializable {
    @JsonProperty("u")
    private String url;
    @JsonProperty("n")
    private String name;
    @JsonProperty("i")
    private List<?> item; // ?:字符串、对象

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<?> getItem() {
        return item;
    }

    public void setItem(List<?> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "ItemCat{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", item=" + item +
                '}';
    }
}
