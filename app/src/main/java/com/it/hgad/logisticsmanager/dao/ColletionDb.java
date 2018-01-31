package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/26.
 */
@Table(name = ColletionDb.TABLE_NAME)
public class ColletionDb implements Serializable {
    public static final String TABLE_NAME = "ColletionTable";
    @Id
    @NoAutoIncrement
    private int id;
    @Column(column = "title")
    private String title;
    @Column(column = "path")
    private String path;
    @Column(column = "isTop")
    private boolean isTop;

    public ColletionDb() {
    }

    public ColletionDb(int id, String title, String path, boolean isTop) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.isTop = isTop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
