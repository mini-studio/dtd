package com.mini.core.api.data;

/**
 * Created by Wuquancheng on 15/12/5.
 *  type: "3",
 *  type_id: "10",
 *  url: "http://api.dtd.la/uploads/photo/1.jpg"
 */
public class ImageInfo {

    private String type;
    private String type_id;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
