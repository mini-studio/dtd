package com.dtdinc.dtd.core.model;

import com.dtdinc.dtd.core.api.data.City;

/**
 * Created by Wuquancheng on 16/1/3.
 */
public class LineInfo {

    private String name;
    private City from;
    private City to;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getFrom() {
        return from;
    }

    public void setFrom(City from) {
        this.from = from;
    }

    public City getTo() {
        return to;
    }

    public void setTo(City to) {
        this.to = to;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(from.getName()).append("<—>").append(to.getName());
        return stringBuilder.toString();
    }
}
