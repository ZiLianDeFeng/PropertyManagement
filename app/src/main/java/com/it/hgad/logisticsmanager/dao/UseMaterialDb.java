package com.it.hgad.logisticsmanager.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/23.
 */
@Table(name = UseMaterialDb.TABLE_NAME)
public class UseMaterialDb implements Serializable{
    public static final String TABLE_NAME = "UseMaterialTable";

    private int id;
    @Column(column = "materialId")
    private List<Integer> materialId;
    @Column(column = "name")
    private List<String> name;
    @Column(column = "type")
    private List<String> type;
    @Column(column = "number")
    private List<Integer> number;
    @Column(column = "price")
    private List<String> price;

    public UseMaterialDb() {
    }

    public UseMaterialDb(int id, List<Integer> materialId, List<String> name, List<String> type, List<Integer> number, List<String> price) {
        this.id = id;
        this.materialId = materialId;
        this.name = name;
        this.type = type;
        this.number = number;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getMaterialId() {
        return materialId;
    }

    public void setMaterialId(List<Integer> materialId) {
        this.materialId = materialId;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<Integer> getNumber() {
        return number;
    }

    public void setNumber(List<Integer> number) {
        this.number = number;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }
}
