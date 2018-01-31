package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/20.
 */
@Table(name = MaterialDb.TABLE_NAME)
public class MaterialDb implements Serializable{
    public static final String TABLE_NAME = "MaterialTable";
    public static String MATERIAL_NAME = "name";
    @Id
    @NoAutoIncrement
    private int materialId;
    @Column(column = "name")
    private String name;
    @Column(column = "type")
    private String type;
    @Column(column = "number")
    private int number;
    @Column(column = "price")
    private String price;

    public MaterialDb() {
    }

    public MaterialDb(int materialId, String name, String type, int number, String price) {
        this.materialId = materialId;
        this.name = name;
        this.type = type;
        this.number = number;
        this.price = price;
    }


    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
