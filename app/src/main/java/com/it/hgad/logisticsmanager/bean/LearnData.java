package com.it.hgad.logisticsmanager.bean;

/**
 * Created by Administrator on 2017/5/25.
 */
public class LearnData {
    private int id;
    private String type;
    private String path;
    private boolean isTop;
    private String pic;
    private String state;
    private int times;
    private String pubdate;

    public LearnData(int id, String type, String path, boolean isTop) {
        this.id = id;
        this.type = type;
        this.path = path;
        this.isTop = isTop;
    }

    public LearnData(int id, String type, String path, boolean isTop, String pic, String state, int times, String pubdate) {
        this.id = id;
        this.type = type;
        this.path = path;
        this.isTop = isTop;
        this.pic = pic;
        this.state = state;
        this.times = times;
        this.pubdate = pubdate;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

}
