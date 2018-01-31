package com.it.hgad.logisticsmanager.bean.response;

import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;

/**
 * Created by Administrator on 2017/4/26.
 */
public class NewCheckOrderResponse extends BaseReponse {

    /**
     * result : 0
     * data : {"perpage":10,"recordcount":140,"firstpage":1,"pagecount":14,"currentpage":1,"lastpage":14,"listcount":10,"nextpage":2,"dyntitles":null,"listdata":[{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145442","inspector":" 17, 21, ","deviceName":"2D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600182","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(2D-1  进线柜),日巡检","id":7542,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"40","referenceParamTypeName":"2D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145442","inspector":" 17, 21, ","deviceName":"2D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600182","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(2D-1  进线柜),日巡检","id":7542,"taskStatus":"0"},"description":null,"id":7542,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"300","referenceStartValue":"100","referenceTypeId":"40","actualValue":null,"remark":"","id":68996,"taskResult":null,"referenceName":"电流","status":null},{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"40","actualValue":null,"remark":"","id":68997,"taskResult":null,"referenceName":"电压","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150325","inspector":" 17, 21, ","deviceName":"5D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600140","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(5D-1  进线柜),日巡检","id":7500,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"42","referenceParamTypeName":"5D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150325","inspector":" 17, 21, ","deviceName":"5D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600140","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(5D-1  进线柜),日巡检","id":7500,"taskStatus":"0"},"description":null,"id":7500,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"42","actualValue":null,"remark":"","id":68912,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"1500","referenceStartValue":"0","referenceTypeId":"42","actualValue":null,"remark":"","id":68913,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150921","inspector":" 17, 21, ","deviceName":"6D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600126","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(6D-1  进线柜),日巡检","id":7486,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"44","referenceParamTypeName":"6D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150921","inspector":" 17, 21, ","deviceName":"6D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600126","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(6D-1  进线柜),日巡检","id":7486,"taskStatus":"0"},"description":null,"id":7486,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"44","actualValue":null,"remark":"","id":68884,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"1500","referenceStartValue":"0","referenceTypeId":"44","actualValue":null,"remark":"","id":68885,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145715","inspector":" 17, 21, ","deviceName":"3D-1 进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600168","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(3D-1 进线柜),日巡检","id":7528,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"36","referenceParamTypeName":"3D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145715","inspector":" 17, 21, ","deviceName":"3D-1 进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600168","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(3D-1 进线柜),日巡检","id":7528,"taskStatus":"0"},"description":null,"id":7528,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"36","actualValue":null,"remark":"","id":68968,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"500","referenceStartValue":"125","referenceTypeId":"36","actualValue":null,"remark":"","id":68969,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121045","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T3变压器）G12","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600224","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T3变压器）G12),日巡检","id":7584,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"30","referenceParamTypeName":"G12高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121045","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T3变压器）G12","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600224","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T3变压器）G12),日巡检","id":7584,"taskStatus":"0"},"description":null,"id":7584,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"20","referenceStartValue":"5","referenceTypeId":"30","actualValue":null,"remark":"","id":69053,"taskResult":null,"referenceName":"出线柜电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145139","inspector":" 17, 21, ","deviceName":"1D-1   进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600196","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(1D-1   进线柜),日巡检","id":7556,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"38","referenceParamTypeName":"1D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145139","inspector":" 17, 21, ","deviceName":"1D-1   进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600196","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(1D-1   进线柜),日巡检","id":7556,"taskStatus":"0"},"description":null,"id":7556,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"38","actualValue":null,"remark":"","id":69024,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"425","referenceStartValue":"150","referenceTypeId":"38","actualValue":null,"remark":"","id":69025,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121342","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T5变压器）G11","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600210","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T5变压器）G11),日巡检","id":7570,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"29","referenceParamTypeName":"G11高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121342","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T5变压器）G11","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600210","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T5变压器）G11),日巡检","id":7570,"taskStatus":"0"},"description":null,"id":7570,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"60","referenceStartValue":"0","referenceTypeId":"29","actualValue":null,"remark":"","id":69039,"taskResult":null,"referenceName":"出线柜电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150009","inspector":" 17, 21, ","deviceName":"4D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600154","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(4D-1  进线柜),日巡检","id":7514,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"34","referenceParamTypeName":"4D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150009","inspector":" 17, 21, ","deviceName":"4D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600154","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(4D-1  进线柜),日巡检","id":7514,"taskStatus":"0"},"description":null,"id":7514,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"34","actualValue":null,"remark":"","id":68940,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"600","referenceStartValue":"125","referenceTypeId":"34","actualValue":null,"remark":"","id":68941,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323120758","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T1变压器）G13","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600238","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T1变压器）G13),日巡检","id":7598,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"31","referenceParamTypeName":"G13高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323120758","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T1变压器）G13","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600238","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T1变压器）G13),日巡检","id":7598,"taskStatus":"0"},"description":null,"id":7598,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"17","referenceStartValue":"6","referenceTypeId":"31","actualValue":null,"remark":"","id":69067,"taskResult":null,"referenceName":"出线柜电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323113835","inspector":" 17, 21, ","deviceName":"高压电源进线柜G17","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600252","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压电源进线柜G17),日巡检","id":7612,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"32","referenceParamTypeName":"G17高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323113835","inspector":" 17, 21, ","deviceName":"高压电源进线柜G17","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600252","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压电源进线柜G17),日巡检","id":7612,"taskStatus":"0"},"description":null,"id":7612,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"30","referenceStartValue":"0","referenceTypeId":"32","actualValue":null,"remark":"","id":69094,"taskResult":null,"referenceName":"备进柜电流","status":null},{"referenceUnit":"KV","referenceEndValue":"10.6","referenceStartValue":"10","referenceTypeId":"32","actualValue":null,"remark":"","id":69095,"taskResult":null,"referenceName":"备进柜电压","status":null}],"actualParamTypeId":null}}],"prevpage":1,"dyncolsdata":null,"oddsize":true}
     */
    private String result;
    private DataEntity data;

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public DataEntity getData() {
        return data;
    }

    public class DataEntity {
        /**
         * perpage : 10
         * recordcount : 140
         * firstpage : 1
         * pagecount : 14
         * currentpage : 1
         * lastpage : 14
         * listcount : 10
         * nextpage : 2
         * dyntitles : null
         * listdata : [{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145442","inspector":" 17, 21, ","deviceName":"2D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600182","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(2D-1  进线柜),日巡检","id":7542,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"40","referenceParamTypeName":"2D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145442","inspector":" 17, 21, ","deviceName":"2D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600182","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(2D-1  进线柜),日巡检","id":7542,"taskStatus":"0"},"description":null,"id":7542,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"300","referenceStartValue":"100","referenceTypeId":"40","actualValue":null,"remark":"","id":68996,"taskResult":null,"referenceName":"电流","status":null},{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"40","actualValue":null,"remark":"","id":68997,"taskResult":null,"referenceName":"电压","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150325","inspector":" 17, 21, ","deviceName":"5D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600140","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(5D-1  进线柜),日巡检","id":7500,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"42","referenceParamTypeName":"5D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150325","inspector":" 17, 21, ","deviceName":"5D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600140","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(5D-1  进线柜),日巡检","id":7500,"taskStatus":"0"},"description":null,"id":7500,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"42","actualValue":null,"remark":"","id":68912,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"1500","referenceStartValue":"0","referenceTypeId":"42","actualValue":null,"remark":"","id":68913,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150921","inspector":" 17, 21, ","deviceName":"6D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600126","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(6D-1  进线柜),日巡检","id":7486,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"44","referenceParamTypeName":"6D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150921","inspector":" 17, 21, ","deviceName":"6D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600126","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(6D-1  进线柜),日巡检","id":7486,"taskStatus":"0"},"description":null,"id":7486,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"44","actualValue":null,"remark":"","id":68884,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"1500","referenceStartValue":"0","referenceTypeId":"44","actualValue":null,"remark":"","id":68885,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145715","inspector":" 17, 21, ","deviceName":"3D-1 进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600168","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(3D-1 进线柜),日巡检","id":7528,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"36","referenceParamTypeName":"3D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145715","inspector":" 17, 21, ","deviceName":"3D-1 进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600168","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(3D-1 进线柜),日巡检","id":7528,"taskStatus":"0"},"description":null,"id":7528,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"36","actualValue":null,"remark":"","id":68968,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"500","referenceStartValue":"125","referenceTypeId":"36","actualValue":null,"remark":"","id":68969,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121045","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T3变压器）G12","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600224","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T3变压器）G12),日巡检","id":7584,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"30","referenceParamTypeName":"G12高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121045","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T3变压器）G12","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600224","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T3变压器）G12),日巡检","id":7584,"taskStatus":"0"},"description":null,"id":7584,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"20","referenceStartValue":"5","referenceTypeId":"30","actualValue":null,"remark":"","id":69053,"taskResult":null,"referenceName":"出线柜电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145139","inspector":" 17, 21, ","deviceName":"1D-1   进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600196","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(1D-1   进线柜),日巡检","id":7556,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"38","referenceParamTypeName":"1D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145139","inspector":" 17, 21, ","deviceName":"1D-1   进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600196","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(1D-1   进线柜),日巡检","id":7556,"taskStatus":"0"},"description":null,"id":7556,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"38","actualValue":null,"remark":"","id":69024,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"425","referenceStartValue":"150","referenceTypeId":"38","actualValue":null,"remark":"","id":69025,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121342","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T5变压器）G11","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600210","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T5变压器）G11),日巡检","id":7570,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"29","referenceParamTypeName":"G11高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323121342","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T5变压器）G11","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600210","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T5变压器）G11),日巡检","id":7570,"taskStatus":"0"},"description":null,"id":7570,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"60","referenceStartValue":"0","referenceTypeId":"29","actualValue":null,"remark":"","id":69039,"taskResult":null,"referenceName":"出线柜电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150009","inspector":" 17, 21, ","deviceName":"4D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600154","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(4D-1  进线柜),日巡检","id":7514,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"34","referenceParamTypeName":"4D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323150009","inspector":" 17, 21, ","deviceName":"4D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600154","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(4D-1  进线柜),日巡检","id":7514,"taskStatus":"0"},"description":null,"id":7514,"deviceParamSet":[{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"34","actualValue":null,"remark":"","id":68940,"taskResult":null,"referenceName":"电压","status":null},{"referenceUnit":"A","referenceEndValue":"600","referenceStartValue":"125","referenceTypeId":"34","actualValue":null,"remark":"","id":68941,"taskResult":null,"referenceName":"电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323120758","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T1变压器）G13","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600238","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T1变压器）G13),日巡检","id":7598,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"31","referenceParamTypeName":"G13高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323120758","inspector":" 17, 21, ","deviceName":"高压馈线柜（至T1变压器）G13","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600238","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压馈线柜（至T1变压器）G13),日巡检","id":7598,"taskStatus":"0"},"description":null,"id":7598,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"17","referenceStartValue":"6","referenceTypeId":"31","actualValue":null,"remark":"","id":69067,"taskResult":null,"referenceName":"出线柜电流","status":null}],"actualParamTypeId":null}},{"deviceTask":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323113835","inspector":" 17, 21, ","deviceName":"高压电源进线柜G17","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600252","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压电源进线柜G17),日巡检","id":7612,"taskStatus":"0"},"deviceTaskResult":{"resultStatus":null,"referenceParamTypeId":"32","referenceParamTypeName":"G17高压柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323113835","inspector":" 17, 21, ","deviceName":"高压电源进线柜G17","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600252","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(高压电源进线柜G17),日巡检","id":7612,"taskStatus":"0"},"description":null,"id":7612,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"30","referenceStartValue":"0","referenceTypeId":"32","actualValue":null,"remark":"","id":69094,"taskResult":null,"referenceName":"备进柜电流","status":null},{"referenceUnit":"KV","referenceEndValue":"10.6","referenceStartValue":"10","referenceTypeId":"32","actualValue":null,"remark":"","id":69095,"taskResult":null,"referenceName":"备进柜电压","status":null}],"actualParamTypeId":null}}]
         * prevpage : 1
         * dyncolsdata : null
         * oddsize : true
         */
        private int perpage;
        private int recordcount;
        private int firstpage;
        private int pagecount;
        private int currentpage;
        private int lastpage;
        private int listcount;
        private int nextpage;
        private String dyntitles;
        private List<ListdataEntity> listdata;
        private int prevpage;
        private String dyncolsdata;
        private boolean oddsize;

        public void setPerpage(int perpage) {
            this.perpage = perpage;
        }

        public void setRecordcount(int recordcount) {
            this.recordcount = recordcount;
        }

        public void setFirstpage(int firstpage) {
            this.firstpage = firstpage;
        }

        public void setPagecount(int pagecount) {
            this.pagecount = pagecount;
        }

        public void setCurrentpage(int currentpage) {
            this.currentpage = currentpage;
        }

        public void setLastpage(int lastpage) {
            this.lastpage = lastpage;
        }

        public void setListcount(int listcount) {
            this.listcount = listcount;
        }

        public void setNextpage(int nextpage) {
            this.nextpage = nextpage;
        }

        public void setDyntitles(String dyntitles) {
            this.dyntitles = dyntitles;
        }

        public void setListdata(List<ListdataEntity> listdata) {
            this.listdata = listdata;
        }

        public void setPrevpage(int prevpage) {
            this.prevpage = prevpage;
        }

        public void setDyncolsdata(String dyncolsdata) {
            this.dyncolsdata = dyncolsdata;
        }

        public void setOddsize(boolean oddsize) {
            this.oddsize = oddsize;
        }

        public int getPerpage() {
            return perpage;
        }

        public int getRecordcount() {
            return recordcount;
        }

        public int getFirstpage() {
            return firstpage;
        }

        public int getPagecount() {
            return pagecount;
        }

        public int getCurrentpage() {
            return currentpage;
        }

        public int getLastpage() {
            return lastpage;
        }

        public int getListcount() {
            return listcount;
        }

        public int getNextpage() {
            return nextpage;
        }

        public String getDyntitles() {
            return dyntitles;
        }

        public List<ListdataEntity> getListdata() {
            return listdata;
        }

        public int getPrevpage() {
            return prevpage;
        }

        public String getDyncolsdata() {
            return dyncolsdata;
        }

        public boolean isOddsize() {
            return oddsize;
        }

        public class ListdataEntity {
            /**
             * deviceTask : {"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145442","inspector":" 17, 21, ","deviceName":"2D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600182","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(2D-1  进线柜),日巡检","id":7542,"taskStatus":"0"}
             * deviceTaskResult : {"resultStatus":null,"referenceParamTypeId":"40","referenceParamTypeName":"2D1低压进线柜","task":{"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145442","inspector":" 17, 21, ","deviceName":"2D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600182","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(2D-1  进线柜),日巡检","id":7542,"taskStatus":"0"},"description":null,"id":7542,"deviceParamSet":[{"referenceUnit":"A","referenceEndValue":"300","referenceStartValue":"100","referenceTypeId":"40","actualValue":null,"remark":"","id":68996,"taskResult":null,"referenceName":"电流","status":null},{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"40","actualValue":null,"remark":"","id":68997,"taskResult":null,"referenceName":"电压","status":null}],"actualParamTypeId":null}
             */
            private DeviceTaskEntity deviceTask;
            private DeviceTaskResultEntity deviceTaskResult;

            public void setDeviceTask(DeviceTaskEntity deviceTask) {
                this.deviceTask = deviceTask;
            }

            public void setDeviceTaskResult(DeviceTaskResultEntity deviceTaskResult) {
                this.deviceTaskResult = deviceTaskResult;
            }

            public DeviceTaskEntity getDeviceTask() {
                return deviceTask;
            }

            public DeviceTaskResultEntity getDeviceTaskResult() {
                return deviceTaskResult;
            }

            public class DeviceTaskEntity {
                /**
                 * deviceType : 配电
                 * finishTime : null
                 * deviceCode : DM20170323145442
                 * inspector :  17, 21,
                 * deviceName : 2D-1  进线柜
                 * inspectorName :  杨少华, 杨恒之,
                 * arrageId : 14
                 * taskCode : 2017042600182
                 * planTime : 2017-04-27T08:00:00
                 * responser : 肖虎
                 * shouldTime : 2017-04-27T08:30:00
                 * taskName : (2D-1  进线柜),日巡检
                 * id : 7542
                 * taskStatus : 0
                 */
                private String deviceType;
                private String finishTime;
                private String deviceCode;
                private String inspector;
                private String deviceName;
                private String inspectorName;
                private int arrageId;
                private String taskCode;
                private String planTime;
                private String responser;
                private String shouldTime;
                private String taskName;
                private int id;
                private String taskStatus;

                public void setDeviceType(String deviceType) {
                    this.deviceType = deviceType;
                }

                public void setFinishTime(String finishTime) {
                    this.finishTime = finishTime;
                }

                public void setDeviceCode(String deviceCode) {
                    this.deviceCode = deviceCode;
                }

                public void setInspector(String inspector) {
                    this.inspector = inspector;
                }

                public void setDeviceName(String deviceName) {
                    this.deviceName = deviceName;
                }

                public void setInspectorName(String inspectorName) {
                    this.inspectorName = inspectorName;
                }

                public void setArrageId(int arrageId) {
                    this.arrageId = arrageId;
                }

                public void setTaskCode(String taskCode) {
                    this.taskCode = taskCode;
                }

                public void setPlanTime(String planTime) {
                    this.planTime = planTime;
                }

                public void setResponser(String responser) {
                    this.responser = responser;
                }

                public void setShouldTime(String shouldTime) {
                    this.shouldTime = shouldTime;
                }

                public void setTaskName(String taskName) {
                    this.taskName = taskName;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public void setTaskStatus(String taskStatus) {
                    this.taskStatus = taskStatus;
                }

                public String getDeviceType() {
                    return deviceType;
                }

                public String getFinishTime() {
                    return finishTime;
                }

                public String getDeviceCode() {
                    return deviceCode;
                }

                public String getInspector() {
                    return inspector;
                }

                public String getDeviceName() {
                    return deviceName;
                }

                public String getInspectorName() {
                    return inspectorName;
                }

                public int getArrageId() {
                    return arrageId;
                }

                public String getTaskCode() {
                    return taskCode;
                }

                public String getPlanTime() {
                    return planTime;
                }

                public String getResponser() {
                    return responser;
                }

                public String getShouldTime() {
                    return shouldTime;
                }

                public String getTaskName() {
                    return taskName;
                }

                public int getId() {
                    return id;
                }

                public String getTaskStatus() {
                    return taskStatus;
                }
            }

            public class DeviceTaskResultEntity {
                /**
                 * resultStatus : null
                 * referenceParamTypeId : 40
                 * referenceParamTypeName : 2D1低压进线柜
                 * task : {"deviceType":"配电","finishTime":null,"deviceCode":"DM20170323145442","inspector":" 17, 21, ","deviceName":"2D-1  进线柜","inspectorName":" 杨少华, 杨恒之, ","arrageId":14,"taskCode":"2017042600182","planTime":"2017-04-27T08:00:00","responser":"肖虎","shouldTime":"2017-04-27T08:30:00","taskName":"(2D-1  进线柜),日巡检","id":7542,"taskStatus":"0"}
                 * description : null
                 * id : 7542
                 * deviceParamSet : [{"referenceUnit":"A","referenceEndValue":"300","referenceStartValue":"100","referenceTypeId":"40","actualValue":null,"remark":"","id":68996,"taskResult":null,"referenceName":"电流","status":null},{"referenceUnit":"V","referenceEndValue":"420","referenceStartValue":"380","referenceTypeId":"40","actualValue":null,"remark":"","id":68997,"taskResult":null,"referenceName":"电压","status":null}]
                 * actualParamTypeId : null
                 */
                private String resultStatus;
                private String referenceParamTypeId;
                private String referenceParamTypeName;
                private TaskEntity task;
                private String description;
                private int id;
                private List<DeviceParamSetEntity> deviceParamSet;
                private String actualParamTypeId;

                @Override
                public String toString() {
                    return "DeviceTaskResultEntity{" +
                            "resultStatus='" + resultStatus + '\'' +
                            ", referenceParamTypeId='" + referenceParamTypeId + '\'' +
                            ", referenceParamTypeName='" + referenceParamTypeName + '\'' +
                            ", task=" + task +
                            ", description='" + description + '\'' +
                            ", id=" + id +
                            ", deviceParamSet=" + deviceParamSet +
                            ", actualParamTypeId='" + actualParamTypeId + '\'' +
                            '}';
                }

                public void setResultStatus(String resultStatus) {
                    this.resultStatus = resultStatus;
                }

                public void setReferenceParamTypeId(String referenceParamTypeId) {
                    this.referenceParamTypeId = referenceParamTypeId;
                }

                public void setReferenceParamTypeName(String referenceParamTypeName) {
                    this.referenceParamTypeName = referenceParamTypeName;
                }

                public void setTask(TaskEntity task) {
                    this.task = task;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public void setDeviceParamSet(List<DeviceParamSetEntity> deviceParamSet) {
                    this.deviceParamSet = deviceParamSet;
                }

                public void setActualParamTypeId(String actualParamTypeId) {
                    this.actualParamTypeId = actualParamTypeId;
                }

                public String getResultStatus() {
                    return resultStatus;
                }

                public String getReferenceParamTypeId() {
                    return referenceParamTypeId;
                }

                public String getReferenceParamTypeName() {
                    return referenceParamTypeName;
                }

                public TaskEntity getTask() {
                    return task;
                }

                public String getDescription() {
                    return description;
                }

                public int getId() {
                    return id;
                }

                public List<DeviceParamSetEntity> getDeviceParamSet() {
                    return deviceParamSet;
                }

                public String getActualParamTypeId() {
                    return actualParamTypeId;
                }

                public class TaskEntity {
                    /**
                     * deviceType : 配电
                     * finishTime : null
                     * deviceCode : DM20170323145442
                     * inspector :  17, 21,
                     * deviceName : 2D-1  进线柜
                     * inspectorName :  杨少华, 杨恒之,
                     * arrageId : 14
                     * taskCode : 2017042600182
                     * planTime : 2017-04-27T08:00:00
                     * responser : 肖虎
                     * shouldTime : 2017-04-27T08:30:00
                     * taskName : (2D-1  进线柜),日巡检
                     * id : 7542
                     * taskStatus : 0
                     */
                    private String deviceType;
                    private String finishTime;
                    private String deviceCode;
                    private String inspector;
                    private String deviceName;
                    private String inspectorName;
                    private int arrageId;
                    private String taskCode;
                    private String planTime;
                    private String responser;
                    private String shouldTime;
                    private String taskName;
                    private int id;
                    private String taskStatus;

                    public void setDeviceType(String deviceType) {
                        this.deviceType = deviceType;
                    }

                    public void setFinishTime(String finishTime) {
                        this.finishTime = finishTime;
                    }

                    public void setDeviceCode(String deviceCode) {
                        this.deviceCode = deviceCode;
                    }

                    public void setInspector(String inspector) {
                        this.inspector = inspector;
                    }

                    public void setDeviceName(String deviceName) {
                        this.deviceName = deviceName;
                    }

                    public void setInspectorName(String inspectorName) {
                        this.inspectorName = inspectorName;
                    }

                    public void setArrageId(int arrageId) {
                        this.arrageId = arrageId;
                    }

                    public void setTaskCode(String taskCode) {
                        this.taskCode = taskCode;
                    }

                    public void setPlanTime(String planTime) {
                        this.planTime = planTime;
                    }

                    public void setResponser(String responser) {
                        this.responser = responser;
                    }

                    public void setShouldTime(String shouldTime) {
                        this.shouldTime = shouldTime;
                    }

                    public void setTaskName(String taskName) {
                        this.taskName = taskName;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public void setTaskStatus(String taskStatus) {
                        this.taskStatus = taskStatus;
                    }

                    public String getDeviceType() {
                        return deviceType;
                    }

                    public String getFinishTime() {
                        return finishTime;
                    }

                    public String getDeviceCode() {
                        return deviceCode;
                    }

                    public String getInspector() {
                        return inspector;
                    }

                    public String getDeviceName() {
                        return deviceName;
                    }

                    public String getInspectorName() {
                        return inspectorName;
                    }

                    public int getArrageId() {
                        return arrageId;
                    }

                    public String getTaskCode() {
                        return taskCode;
                    }

                    public String getPlanTime() {
                        return planTime;
                    }

                    public String getResponser() {
                        return responser;
                    }

                    public String getShouldTime() {
                        return shouldTime;
                    }

                    public String getTaskName() {
                        return taskName;
                    }

                    public int getId() {
                        return id;
                    }

                    public String getTaskStatus() {
                        return taskStatus;
                    }
                }

                public class DeviceParamSetEntity {
                    /**
                     * referenceUnit : A
                     * referenceEndValue : 300
                     * referenceStartValue : 100
                     * referenceTypeId : 40
                     * actualValue : null
                     * remark :
                     * id : 68996
                     * taskResult : null
                     * referenceName : 电流
                     * status : null
                     */
                    private String referenceUnit;
                    private String referenceEndValue;
                    private String referenceStartValue;
                    private String referenceTypeId;
                    private String actualValue;
                    private String remark;
                    private int id;
                    private String taskResult;
                    private String referenceName;
                    private String status;

                    public void setReferenceUnit(String referenceUnit) {
                        this.referenceUnit = referenceUnit;
                    }

                    public void setReferenceEndValue(String referenceEndValue) {
                        this.referenceEndValue = referenceEndValue;
                    }

                    public void setReferenceStartValue(String referenceStartValue) {
                        this.referenceStartValue = referenceStartValue;
                    }

                    public void setReferenceTypeId(String referenceTypeId) {
                        this.referenceTypeId = referenceTypeId;
                    }

                    public void setActualValue(String actualValue) {
                        this.actualValue = actualValue;
                    }

                    public void setRemark(String remark) {
                        this.remark = remark;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public void setTaskResult(String taskResult) {
                        this.taskResult = taskResult;
                    }

                    public void setReferenceName(String referenceName) {
                        this.referenceName = referenceName;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }

                    public String getReferenceUnit() {
                        return referenceUnit;
                    }

                    public String getReferenceEndValue() {
                        return referenceEndValue;
                    }

                    public String getReferenceStartValue() {
                        return referenceStartValue;
                    }

                    public String getReferenceTypeId() {
                        return referenceTypeId;
                    }

                    public String getActualValue() {
                        return actualValue;
                    }

                    public String getRemark() {
                        return remark;
                    }

                    public int getId() {
                        return id;
                    }

                    public String getTaskResult() {
                        return taskResult;
                    }

                    public String getReferenceName() {
                        return referenceName;
                    }

                    public String getStatus() {
                        return status;
                    }
                }
            }
        }
    }
}
