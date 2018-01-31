package com.it.hgad.logisticsmanager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4.
 */
public class Material implements Serializable{

    private int id;
    private String name;
    private String type;
    private int number;
    private boolean isChecked;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Material(int id, String name, String type, int number, boolean isChecked, String price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.number = number;
        this.isChecked = isChecked;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Material{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", number=" + number +
                ", isChecked=" + isChecked +
                '}';
    }
}
