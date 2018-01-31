package com.it.hgad.logisticsmanager.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10.
 */
public class ParamDbEntity {


    /**
     * data : [{"ReferenceUnit":"Mpa","ReferenceEndValue":"1.0","ReferenceStartValue":"0.7","Id":2337,"ReferenceName":"冷却泵进水压力","Remark":""},{"ReferenceUnit":"摄氏度","ReferenceEndValue":"16","ReferenceStartValue":"8","Id":2335,"ReferenceName":"蒸发器回水温度","Remark":""},{"ReferenceUnit":"","ReferenceEndValue":"5","ReferenceStartValue":"1","Id":2331,"ReferenceName":"冷冻泵运行泵号","Remark":"运行哪台冷冻泵填写哪台冷冻泵号码"},{"ReferenceUnit":"Mpa","ReferenceEndValue":"1.2","ReferenceStartValue":"0.75","Id":2348,"ReferenceName":"冷却泵出水压力","Remark":""},{"ReferenceUnit":"A","ReferenceEndValue":"270","ReferenceStartValue":"250","Id":2326,"ReferenceName":"冷却泵电流","Remark":""},{"ReferenceUnit":"摄氏度","ReferenceEndValue":"35","ReferenceStartValue":"25","Id":2344,"ReferenceName":"冷凝器出水温度","Remark":""},{"ReferenceUnit":"V","ReferenceEndValue":"410","ReferenceStartValue":"380","Id":2325,"ReferenceName":"冷却冻电压","Remark":""},{"ReferenceUnit":"","ReferenceEndValue":"2","ReferenceStartValue":"1","Id":2347,"ReferenceName":"冷却水补水泵运行泵号","Remark":"运行哪台冷却水补水泵填写哪台冷却水补水泵号码"},{"ReferenceUnit":"","ReferenceEndValue":"1","ReferenceStartValue":"0","Id":2332,"ReferenceName":"集水器总阀门开/停状态","Remark":"集水器总阀门停：0；开：1。"},{"ReferenceUnit":"Kpa","ReferenceEndValue":"190","ReferenceStartValue":"130","Id":2334,"ReferenceName":"油压差","Remark":""},{"ReferenceUnit":"Mpa","ReferenceEndValue":"1.1","ReferenceStartValue":"0.86","Id":2343,"ReferenceName":"分水器压力","Remark":""},{"ReferenceUnit":"摄氏度","ReferenceEndValue":"15","ReferenceStartValue":"7","Id":2350,"ReferenceName":"蒸发器出水温度","Remark":""},{"ReferenceUnit":"摄氏度","ReferenceEndValue":"32","ReferenceStartValue":"22","Id":2339,"ReferenceName":"冷凝器回水温度","Remark":""},{"ReferenceUnit":"摄氏度","ReferenceEndValue":"13","ReferenceStartValue":"8","Id":2345,"ReferenceName":"分水器温度","Remark":""},{"ReferenceUnit":"A","ReferenceEndValue":"120","ReferenceStartValue":"100","Id":2330,"ReferenceName":"冷却冻电流","Remark":""},{"ReferenceUnit":"摄氏度","ReferenceEndValue":"14","ReferenceStartValue":"9","Id":2333,"ReferenceName":"集水器温度","Remark":""},{"ReferenceUnit":"","ReferenceEndValue":"3","ReferenceStartValue":"1","Id":2338,"ReferenceName":"冷却泵运行泵号","Remark":"运行哪台冷却泵填写哪台冷却泵号码"},{"ReferenceUnit":"","ReferenceEndValue":"2","ReferenceStartValue":"1","Id":2340,"ReferenceName":"软水水箱液位","Remark":"低位：0（异常）；中位：1； 高位：2。"},{"ReferenceUnit":"摄氏度","ReferenceEndValue":"12","ReferenceStartValue":"7","Id":2329,"ReferenceName":"目前蒸发器设定温度","Remark":""},{"ReferenceUnit":"V","ReferenceEndValue":"410","ReferenceStartValue":"380","Id":2349,"ReferenceName":"冷却泵电压","Remark":""},{"ReferenceUnit":"","ReferenceEndValue":"2","ReferenceStartValue":"1","Id":2327,"ReferenceName":"冷冻软水补水泵运行泵号","Remark":"运行哪台冷冻软水补水泵填写哪台冷冻软水补水泵号码"},{"ReferenceUnit":"Mpa","ReferenceEndValue":"1.0","ReferenceStartValue":"0.85","Id":2342,"ReferenceName":"集水器压力","Remark":""},{"ReferenceUnit":"Mpa","ReferenceEndValue":"0.9","ReferenceStartValue":"0.6","Id":2336,"ReferenceName":"冷冻泵进水压力","Remark":""},{"ReferenceUnit":"","ReferenceEndValue":"1","ReferenceStartValue":"0","Id":2328,"ReferenceName":"分水器总阀门开/停状态","Remark":"分水器总阀门停：0；开：1。"},{"ReferenceUnit":"Mpa","ReferenceEndValue":"1.0","ReferenceStartValue":"0.7","Id":2346,"ReferenceName":"冷冻泵出水压力","Remark":""},{"ReferenceUnit":"Mpa","ReferenceEndValue":"1.0","ReferenceStartValue":"0.85","Id":2341,"ReferenceName":"冷却水补水泵压力","Remark":""}]
     */
    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public class DataEntity {
//        /**
//         * ReferenceUnit : Mpa
//         * ReferenceEndValue : 1.0
//         * ReferenceStartValue : 0.7
//         * Id : 2337
//         * ReferenceName : 冷却泵进水压力
//         * Remark :
//         */
//        private String ReferenceUnit;
//        private String ReferenceEndValue;
//        private String ReferenceStartValue;
//        private int Id;
//        private String ReferenceName;
//        private String Remark;
//
//        public void setReferenceUnit(String ReferenceUnit) {
//            this.ReferenceUnit = ReferenceUnit;
//        }
//
//        public void setReferenceEndValue(String ReferenceEndValue) {
//            this.ReferenceEndValue = ReferenceEndValue;
//        }
//
//        public void setReferenceStartValue(String ReferenceStartValue) {
//            this.ReferenceStartValue = ReferenceStartValue;
//        }
//
//        public void setId(int Id) {
//            this.Id = Id;
//        }
//
//        public void setReferenceName(String ReferenceName) {
//            this.ReferenceName = ReferenceName;
//        }
//
//        public void setRemark(String Remark) {
//            this.Remark = Remark;
//        }
//
//        public String getReferenceUnit() {
//            return ReferenceUnit;
//        }
//
//        public String getReferenceEndValue() {
//            return ReferenceEndValue;
//        }
//
//        public String getReferenceStartValue() {
//            return ReferenceStartValue;
//        }
//
//        public int getId() {
//            return Id;
//        }
//
//        public String getReferenceName() {
//            return ReferenceName;
//        }
//
//        public String getRemark() {
//            return Remark;
//        }
        /**
         * referenceUnit : 摄氏度
         * referenceEndValue : 35
         * referenceStartValue : 20
         * actualValue : null
         * remark :
         * id : 2090
         * referenceName : 冷凝器出水温度
         * status : null
         */
        private String ReferenceUnit;
        private String ReferenceEndValue;
        private String ReferenceStartValue;
        private String ActualValue;
        private String Remark;
        private int Id;
        private String ReferenceName;
        private String Status;

        public String getReferenceUnit() {
            return ReferenceUnit;
        }

        public void setReferenceUnit(String referenceUnit) {
            ReferenceUnit = referenceUnit;
        }

        public String getReferenceEndValue() {
            return ReferenceEndValue;
        }

        public void setReferenceEndValue(String referenceEndValue) {
            ReferenceEndValue = referenceEndValue;
        }

        public String getReferenceStartValue() {
            return ReferenceStartValue;
        }

        public void setReferenceStartValue(String referenceStartValue) {
            ReferenceStartValue = referenceStartValue;
        }

        public String getActualValue() {
            return ActualValue;
        }

        public void setActualValue(String actualValue) {
            ActualValue = actualValue;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getReferenceName() {
            return ReferenceName;
        }

        public void setReferenceName(String referenceName) {
            ReferenceName = referenceName;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }
    }

}
