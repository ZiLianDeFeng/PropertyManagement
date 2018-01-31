package com.it.hgad.logisticsmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.bean.YValueFormatter;
import com.it.hgad.logisticsmanager.bean.request.CheckCensusRequest;
import com.it.hgad.logisticsmanager.bean.request.MaintainCensusRequest;
import com.it.hgad.logisticsmanager.bean.response.MaintainCensusResponse;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/11.
 */
public class PerformanceFragment extends BaseFragment implements OnChartValueSelectedListener {

    private View mView;
    private HorizontalBarChart mHorizontalBarChart;
    private YValueFormatter valueFormatter;
    private XAxis xl;
    private ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
    private boolean isMaintain;
    private String type;
    private String startDate = "";
    private String endDate;
    private RadioGroup rg;
    private RadioGroup rg_title;
    private TextView tv_title;

    @Override
    protected void initData() {
//        getMaintainCount();
        ((RadioButton) rg_title.getChildAt(0)).setChecked(true);
    }

    public void getMaintainCount() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            MaintainCensusRequest maintainCensusRequest = new MaintainCensusRequest(startDate, endDate);
            sendRequest(maintainCensusRequest, MaintainCensusResponse.class);
        }
    }

    private List<String> names = new ArrayList<>();

    @Override
    protected void initView() {
        mHorizontalBarChart = (HorizontalBarChart) mView.findViewById(R.id.mHorizontalBarChart);
        //设置相关属性
        mHorizontalBarChart.setOnChartValueSelectedListener(this);
        mHorizontalBarChart.setDrawBarShadow(false);
        mHorizontalBarChart.setDrawValueAboveBar(true);
        mHorizontalBarChart.getDescription().setEnabled(false);
        mHorizontalBarChart.setMaxVisibleValueCount(60);
        mHorizontalBarChart.setPinchZoom(false);
        mHorizontalBarChart.setDrawGridBackground(false);


        //x轴
        xl = mHorizontalBarChart.getXAxis();
        valueFormatter = new YValueFormatter();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        xl.setValueFormatter(valueFormatter);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);
        xl.setTextSize(11f);
        xl.setAxisLineColor(R.color.white);

        //y轴
        YAxis yl = mHorizontalBarChart.getAxisLeft();
        yl.setEnabled(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0.1f);
        yl.setZeroLineColor(R.color.white);

        //y轴
        new YValueFormatter();
        YAxis yr = mHorizontalBarChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(true);
        yr.setAxisMinimum(0.1f);
        yr.setZeroLineColor(R.color.white);
        yr.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "" + (int) value;//这句是重点!
            }
        });

        mHorizontalBarChart.setFitBars(true);
        mHorizontalBarChart.animateY(2500);
        mHorizontalBarChart.setNoDataText(getResources().getString(R.string.no_check_data));


        Legend l = mHorizontalBarChart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setFormSize(10f);
//        l.setXEntrySpace(4f);
        rg = (RadioGroup) mView.findViewById(R.id.rg_performance);
        rg.setOnCheckedChangeListener(checkedChangeListener);


        rg_title = (RadioGroup) mView.findViewById(R.id.rg_title);
        rg_title.setOnCheckedChangeListener(titleCheckChangeListener);

        tv_title = (TextView) mView.findViewById(R.id.tv_title);
    }

    private RadioGroup.OnCheckedChangeListener titleCheckChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_maintain:
                    isMaintain = true;
                    tv_title.setText("维修工单量前十名排行");
                    RadioButton childAt = (RadioButton) rg.getChildAt(0);
                    if (childAt.isChecked()) {
                        getData(type);
                    } else {
                        childAt.setChecked(true);
                    }
                    break;
                case R.id.rb_check:
                    isMaintain = false;
                    tv_title.setText("巡检工单量前十名排行");
                    RadioButton childAtCheck = (RadioButton) rg.getChildAt(0);
                    if (childAtCheck.isChecked()) {
                        getData(type);
                    } else {
                        childAtCheck.setChecked(true);
                    }
                    break;
            }
        }
    };


    private void getData(String type) {
        getRange(type);
        if (isMaintain) {
            getMaintainCount();
        } else {
            getCheckCount();
        }
    }

    private void getCheckCount() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            CheckCensusRequest checkCensusRequest = new CheckCensusRequest(startDate, endDate);
            sendRequest(checkCensusRequest, MaintainCensusResponse.class);
        }
    }

    private void getRange(String type) {
        String currentTime = CommonUtils.getCurrentTime();
        currentTime = currentTime.substring(0, 10);
        int year = Integer.parseInt(currentTime.substring(0, 4));
        int month = Integer.parseInt(currentTime.substring(5, 7));
        if ("day".equals(type)) {
            startDate = currentTime;
            endDate = currentTime;
        } else if ("month".equals(type)) {
            Date beginDayofMonth = CommonUtils.getSupportBeginDayofMonth(year, month);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String beginDate = dateFormat.format(beginDayofMonth);
            startDate = beginDate;
            Date endDayofMonth = CommonUtils.getSupportEndDayofMonth(year, month);
            String endDate = dateFormat.format(endDayofMonth);
            this.endDate = endDate;
        } else if ("total".equals(type)) {
            startDate = "";
            endDate = "";
        }
    }

    RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_day:
                    getData("day");
                    break;
                case R.id.rb_month:
                    getData("month");
                    break;
                case R.id.rb_total:
                    getData("total");
                    break;
            }
        }
    };
    float spaceForBar = 10f;


    private void setData() {
        float barWidth = 8f;
        valueFormatter.setmValues(names);
        BarDataSet set1;
        if (mHorizontalBarChart.getData() != null &&
                mHorizontalBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mHorizontalBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mHorizontalBarChart.getData().notifyDataChanged();
            mHorizontalBarChart.notifyDataSetChanged();
            mHorizontalBarChart.invalidate();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(getColors());
            set1.setDrawValues(true);
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    int n = (int) value;
                    return n + "";
                }
            });
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            mHorizontalBarChart.setData(data);
            mHorizontalBarChart.invalidate();
        }
    }

    private int[] getColors() {
        int stacksize = 1;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];
        colors[0] = ColorTemplate.MATERIAL_COLORS[0];
        return colors;
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_performance, null);
        return mView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof MaintainCensusRequest) {
            MaintainCensusResponse maintainCensusResponse = (MaintainCensusResponse) response;
            if (maintainCensusResponse.getData() != null) {
                List<MaintainCensusResponse.DataEntity> datas = maintainCensusResponse.getData();
                names.clear();
                yVals1.clear();
                for (int i = datas.size() - 1, j = 0; i >= 0; i--, j++) {
                    MaintainCensusResponse.DataEntity data = datas.get(i);
                    MaintainCensusResponse.DataEntity dataEntity = datas.get(j);
                    int count = dataEntity.getCount();
                    String username = data.getUsername();
                    names.add(username);
                    yVals1.add(new BarEntry(i * spaceForBar, count));
                }
                xl.setLabelCount(names.size());
                setData();
            }
        } else if (request instanceof CheckCensusRequest) {
            MaintainCensusResponse maintainCensusResponse = (MaintainCensusResponse) response;
            if (maintainCensusResponse.getData() != null) {
                List<MaintainCensusResponse.DataEntity> datas = maintainCensusResponse.getData();
                names.clear();
                yVals1.clear();
                for (int i = datas.size() - 1, j = 0; i >= 0; i--, j++) {
                    MaintainCensusResponse.DataEntity data = datas.get(i);
                    MaintainCensusResponse.DataEntity dataEntity = datas.get(j);
                    int count = dataEntity.getCount();
                    String username = data.getUsername();
                    names.add(username);
                    yVals1.add(new BarEntry(i * spaceForBar, count));
                }
                xl.setLabelCount(names.size());
                setData();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
