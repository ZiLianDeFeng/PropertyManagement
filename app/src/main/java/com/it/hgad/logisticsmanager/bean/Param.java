package com.it.hgad.logisticsmanager.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/14.
 */
public class Param implements Serializable{
    private String referenceName;
    private String referenceStartValue;
    private String referenceEndValue;
    private String actualValue;
    private String referenceUnit;
    private String remark;
    private String referenceTypeId;

    public Param(String referenceName, String referenceStartValue, String referenceEndValue, String actualValue, String referenceUnit, String remark) {
        this.referenceName = referenceName;
        this.referenceStartValue = referenceStartValue;
        this.referenceEndValue = referenceEndValue;
        this.actualValue = actualValue;
        this.referenceUnit = referenceUnit;
        this.remark = remark;
    }

    public Param(String referenceName, String referenceStartValue, String referenceEndValue, String actualValue, String referenceUnit, String remark, String referenceTypeId) {
        this.referenceName = referenceName;
        this.referenceStartValue = referenceStartValue;
        this.referenceEndValue = referenceEndValue;
        this.actualValue = actualValue;
        this.referenceUnit = referenceUnit;
        this.remark = remark;
        this.referenceTypeId = referenceTypeId;
    }

    public String getReferenceTypeId() {
        return referenceTypeId;
    }

    public void setReferenceTypeId(String referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getReferenceStartValue() {
        return referenceStartValue;
    }

    public void setReferenceStartValue(String referenceStartValue) {
        this.referenceStartValue = referenceStartValue;
    }

    public String getReferenceEndValue() {
        return referenceEndValue;
    }

    public void setReferenceEndValue(String referenceEndValue) {
        this.referenceEndValue = referenceEndValue;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getReferenceUnit() {
        return referenceUnit;
    }

    public void setReferenceUnit(String referenceUnit) {
        this.referenceUnit = referenceUnit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
