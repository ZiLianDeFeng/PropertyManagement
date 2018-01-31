package com.it.hgad.logisticsmanager.bean.response;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/1/12.
 */
public class UpLoadPicResponse extends BaseReponse {

    /**
     * path : 文件路径
     * size : 文件大小
     */
    private String path;
    private String size;

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public String getSize() {
        return size;
    }
}
