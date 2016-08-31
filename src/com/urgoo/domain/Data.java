package com.urgoo.domain;

import java.io.Serializable;

/**
 * Created by lijie on 2016/4/14.
 */
public class Data implements Serializable {
    private String key;
    private String value;

    @Override
    public String toString() {
        return "Data{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
