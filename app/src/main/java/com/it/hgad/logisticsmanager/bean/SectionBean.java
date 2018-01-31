package com.it.hgad.logisticsmanager.bean;

import com.max.pinnedsectionrefreshlistviewdemo.TimeManagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/4/27.
 */
public class SectionBean {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public final int type;
    public final CheckOrder checkOrder;
    public final int dataCount;

    public SectionBean(int type, CheckOrder checkOrder, int dataCount) {
        this.type = type;
        this.checkOrder = checkOrder;
        this.dataCount = dataCount;
    }

    public int getType() {
        return type;
    }

    public CheckOrder getCheckOrder() {
        return checkOrder;
    }

    public int getDataCount() {
        return dataCount;
    }

    public static ArrayList<SectionBean> getData(
            List<CheckOrder> details) {
//        String currentTime = CommonUtils.getCurrentTime();
//        final String currentDate = currentTime.substring(0, 10);
        ArrayList<SectionBean> list = new ArrayList<>();
        Map<CheckOrder, List<CheckOrder>> map = new TreeMap<>(new Comparator<CheckOrder>() {
            public int compare(CheckOrder key1, CheckOrder key2) {
                return key1.getCurTime().compareTo(key2.getCurTime());
            }});
        CheckOrder checkOrder = new CheckOrder();
        List<CheckOrder> dates = new ArrayList<>();
        dates.add(checkOrder);
        for (int i = 0; i < details.size(); i++) {
            try {
                String key = TimeManagement.exchangeStringDate(details.get(i).getPlanTime());
                if (checkOrder.getCurTime() != null && !"".equals(checkOrder.getCurTime())) {
                    boolean b = !key.equals(checkOrder.getCurTime().toString());
                    if (b) {
                        boolean isHave = false;
                        for (CheckOrder date :
                                dates) {
                            if (date.getCurTime().equals(key)) {
                                checkOrder = date;
                                isHave = true;
                            }
                        }
                        if (!isHave) {
                            checkOrder = new CheckOrder();
                            dates.add(checkOrder);
                        }
                    }
                }
                checkOrder.setCurTime(key);
                List<CheckOrder> curList = map.get(checkOrder);
                if (curList == null) {
                    curList = new ArrayList<CheckOrder>();
                }
                String time = details.get(i).getPlanTime();
                time = TimeManagement.exchangeStringTime(time);
                details.get(i).setCurTime(time);
                curList.add(details.get(i));
                map.put(checkOrder, curList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            CheckOrder key = (CheckOrder) entry.getKey();
            List<CheckOrder> li = (List<CheckOrder>) entry.getValue();
            int size = li.size();
            list.add(new SectionBean(SECTION, key, size));
            for (CheckOrder warnDetail : li) {
                list.add(new SectionBean(ITEM, warnDetail, size));
            }
        }
        return list;
    }
}
