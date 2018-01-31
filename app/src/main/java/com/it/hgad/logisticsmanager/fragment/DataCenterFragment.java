package com.it.hgad.logisticsmanager.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.it.hgad.logisticsmanager.R;
import com.it.hgad.logisticsmanager.application.LocalApp;
import com.it.hgad.logisticsmanager.bean.request.AllCheckSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.AllOrderSizeRequest;
import com.it.hgad.logisticsmanager.bean.request.TodaySizeRequest;
import com.it.hgad.logisticsmanager.bean.response.AllCheckSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.AllOrderSizeResponse;
import com.it.hgad.logisticsmanager.bean.response.TodaySizeResponse;
import com.it.hgad.logisticsmanager.constants.SPConstants;
import com.it.hgad.logisticsmanager.dao.CheckOrderDb;
import com.it.hgad.logisticsmanager.dao.OrderDb;
import com.it.hgad.logisticsmanager.util.CommonUtils;
import com.it.hgad.logisticsmanager.util.SPUtils;
import com.it.hgad.logisticsmanager.util.Utils;
import com.it.hgad.logisticsmanager.view.CustomProgressDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.ecshop.net.BaseReponse;
import cn.itcast.ecshop.net.BaseRequest;

/**
 * Created by Administrator on 2017/4/11.
 */
public class DataCenterFragment extends BaseFragment implements OnChartValueSelectedListener {

    private View mView;
    private PieChart mPieChart_check;
    private PieChart mPieChart_maintain;
    private int userId;
    private CustomProgressDialog customProgressDialog;
    private DbUtils db;
    private Handler handler = new Handler();
    private LineChart lineChart;
    private XAxis xAxis;
    private Description description;
    //    private ListView lv;
//    private List<PersonalData> listData = new ArrayList<>();
//    private DataCenterAdapter dataCenterAdapter;

    @Override
    protected void initData() {
        if (db == null) {
            db = LocalApp.getDb();
        }
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        refreshData();
    }

    private List<Entry> repairList = new ArrayList<>();
    private List<Entry> checkList = new ArrayList<>();
    private String maintain = "维修任务量";
    private String check = "巡检任务量";

    @Override
    protected void initView() {
//        lv = (ListView) mView.findViewById(R.id.lv_data_center);
//        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
//        dataCenterAdapter = new DataCenterAdapter(listData);
//        lv.setAdapter(dataCenterAdapter);
//        lv.setEmptyView(tv_empty);
//        for (int i = 0; i < 10; i++) {
//            repairList.add(new Entry(i, 10 + (float) Math.random()));
//            checkList.add(new Entry(i, 5 + (float) Math.random()));
//            xValuesProcess.add(i + 1 + "");
//        }
        initCheckPieChart();
        initMaintainPieChart();
        initLineChart();
    }

    private void initLineChart() {
        lineChart = (LineChart) mView.findViewById(R.id.lineChart);
        // 不显示数据描述
        lineChart.getDescription().setEnabled(true);
        description = new Description();
        description.setTextSize(11f);
        lineChart.setDescription(description);
        // 没有数据的时候，显示“暂无数据”
        lineChart.setNoDataText("暂无数据");
        // 不显示表格颜色
        lineChart.setDrawGridBackground(false);
        // 不可以缩放
        lineChart.setScaleXEnabled(true);
        lineChart.setDragEnabled(true);

        // 不显示y轴右边的值
        lineChart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        lineChart.setExtraLeftOffset(-15);

        xAxis = lineChart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(true);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(11f);
//        xAxis.setLabelCount(10);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(5);
//        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setAxisMinimum(0);


        YAxis yAxis = lineChart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(true);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(true);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "" + (int) value;//这句是重点!
            }
        });
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(15f);
        yAxis.setYOffset(-1);
        yAxis.setAxisMinimum(0);

//        Matrix matrix = new Matrix();
//        // x轴缩放1.5倍
//        matrix.postScale(1.5f, 1f);
//        // 在图表动画显示之前进行缩放
//        lineChart.getViewPortHandler().refresh(matrix, lineChart, false);
        // x轴执行动画
        lineChart.animateX(2000);
//        lineChart.invalidate();
    }

    private void initMaintainPieChart() {
        mPieChart_maintain = (PieChart) mView.findViewById(R.id.mPieChart_maintain);
        mPieChart_maintain.setUsePercentValues(true);
        mPieChart_maintain.getDescription().setEnabled(false);
        mPieChart_maintain.setExtraOffsets(5, 10, 5, 5);
        mPieChart_maintain.setDragDecelerationFrictionCoef(0.95f);

        mPieChart_maintain.setDrawSliceText(false);
        mPieChart_maintain.setDrawHoleEnabled(true);
        mPieChart_maintain.setHoleColor(Color.WHITE);

        mPieChart_maintain.setTransparentCircleColor(Color.WHITE);
        mPieChart_maintain.setTransparentCircleAlpha(110);

        mPieChart_maintain.setCenterTextSize(16f);
        mPieChart_maintain.setCenterTextColor(R.color.light_black);

        mPieChart_maintain.setHoleRadius(58f);
        mPieChart_maintain.setTransparentCircleRadius(61f);

        mPieChart_maintain.setDrawCenterText(true);

        mPieChart_maintain.setRotationAngle(0);
        // 触摸旋转
        mPieChart_maintain.setRotationEnabled(false);
        mPieChart_maintain.setHighlightPerTapEnabled(true);

        mPieChart_maintain.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        mPieChart_maintain.setNoDataText(getResources().getString(R.string.no_maintain_data));

        //变化监听
        mPieChart_maintain.setOnChartValueSelectedListener(this);
        Legend mLegend_maintain = mPieChart_maintain.getLegend();  //设置比例图
        mLegend_maintain.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);  //左下边显示
        mLegend_maintain.setFormSize(8f);//比例块字体大小
        mLegend_maintain.setXEntrySpace(2f);//设置距离饼图的距离，防止与饼图重合
        mLegend_maintain.setYEntrySpace(2f);
        //设置比例块换行...
//        mLegend.setWordWrapEnabled(true);
        mLegend_maintain.setOrientation(Legend.LegendOrientation.VERTICAL);

        mLegend_maintain.setTextColor(getResources().getColor(R.color.black));
        mLegend_maintain.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
    }

    private void initCheckPieChart() {
        mPieChart_check = (PieChart) mView.findViewById(R.id.mPieChart_check);
        mPieChart_check.setUsePercentValues(true);
        mPieChart_check.getDescription().setEnabled(false);
        mPieChart_check.setExtraOffsets(5, 10, 5, 5);
        mPieChart_check.setDragDecelerationFrictionCoef(0.95f);
        mPieChart_check.setDrawHoleEnabled(true);
        mPieChart_check.setDrawSliceText(false);
        mPieChart_check.setHoleColor(Color.WHITE);

        mPieChart_check.setTransparentCircleColor(Color.WHITE);
        mPieChart_check.setTransparentCircleAlpha(110);

        mPieChart_check.setHoleRadius(58f);
        mPieChart_check.setTransparentCircleRadius(61f);

        mPieChart_check.setDrawCenterText(true);

        mPieChart_check.setCenterTextSize(16f);
        mPieChart_check.setCenterTextColor(R.color.light_black);
        mPieChart_check.setRotationAngle(0);

        // 触摸旋转
        mPieChart_check.setRotationEnabled(false);
        mPieChart_check.setHighlightPerTapEnabled(true);
        mPieChart_check.setNoDataText(getResources().getString(R.string.no_check_data));
        mPieChart_check.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //变化监听
        mPieChart_check.setOnChartValueSelectedListener(this);
        Legend mLegend_check = mPieChart_check.getLegend();  //设置比例图
        mLegend_check.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);  //左下边显示
        mLegend_check.setFormSize(8f);//比例块字体大小
        mLegend_check.setXEntrySpace(2f);//设置距离饼图的距离，防止与饼图重合
        mLegend_check.setYEntrySpace(2f);
        //设置比例块换行...
//        mLegend.setWordWrapEnabled(true);
        mLegend_check.setOrientation(Legend.LegendOrientation.VERTICAL);

        mLegend_check.setTextColor(getResources().getColor(R.color.black));
        mLegend_check.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
    }

    float spaceForBar = 10f;

    public void setChartData(LineChart chart, List<Entry> values, List<Entry> values2) {
        LineDataSet lineDataSet = null;
        LineDataSet lineDataSet2 = null;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            if (lineDataSet != null) {
                lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
                lineDataSet.setValues(values);
            }
            if (lineDataSet2 != null) {
                lineDataSet2 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                lineDataSet2.setValues(values2);
            }
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            if (values != null) {
                lineDataSet = new LineDataSet(values, maintain);
                // 设置曲线颜色
                lineDataSet.setColors(getColors()[1]);
                lineDataSet.setValueTextSize(9f);
                lineDataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        int n = (int) value;
                        return n + "";
                    }
                });
                // 设置平滑曲线
                lineDataSet.setMode(LineDataSet.Mode.LINEAR);
                // 不显示坐标点的小圆点
                lineDataSet.setDrawCircles(true);
                // 不显示坐标点的数据
                lineDataSet.setDrawValues(true);
                // 不显示定位线
                lineDataSet.setHighlightEnabled(false);
            }
            if (values2 != null) {
                lineDataSet2 = new LineDataSet(values2, check);
                // 设置曲线颜色
                lineDataSet2.setColor(getColors()[0]);
                lineDataSet2.setValueTextSize(9f);
                lineDataSet2.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        int n = (int) value;
                        return n + "";
                    }
                });
                // 设置平滑曲线
                lineDataSet2.setMode(LineDataSet.Mode.LINEAR);
                // 不显示坐标点的小圆点
                lineDataSet2.setDrawCircles(true);
                // 不显示坐标点的数据
                lineDataSet2.setDrawValues(true);
                // 不显示定位线
                lineDataSet2.setHighlightEnabled(true);
            }
            LineData data = null;
            if (lineDataSet == null && lineDataSet2 == null) {
                data = new LineData();
            } else if (lineDataSet == null && lineDataSet2 != null) {
                data = new LineData(lineDataSet2);
            } else if (lineDataSet != null && lineDataSet2 == null) {
                data = new LineData(lineDataSet);
            } else {
                data = new LineData(lineDataSet, lineDataSet2);
            }
            chart.setData(data);
            chart.invalidate();
        }
    }

    private int[] getColors() {
        int stacksize = 2;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];
        colors[0] = ColorTemplate.MATERIAL_COLORS[0];
        colors[1] = Color.RED;
        return colors;
    }

    List<String> xValuesProcess = new ArrayList<>();

    public void notifyDataSetChanged(LineChart chart, List<Entry> values, List<Entry> values2
    ) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValuesProcess.get((int) value / 10);
            }
        });
//        chart.invalidate();
        setChartData(chart, values, values2);
    }


    private void getCheckData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            AllCheckSizeRequest allCheckSizeRequest = new AllCheckSizeRequest(userId);
            sendRequest(allCheckSizeRequest, AllCheckSizeResponse.class);
        } else {

        }
    }

    private void getMaintanceData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            AllOrderSizeRequest allOrderSizeRequest = new AllOrderSizeRequest(userId);
            sendRequest(allOrderSizeRequest, AllOrderSizeResponse.class);
        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        refreshData();
    }

    public void refreshData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            getMaintanceData();
            getCheckData();
            getMonthData();
            customProgressDialog = new CustomProgressDialog(mContext, "数据加载中", R.mipmap.jiazai, ProgressDialog.THEME_HOLO_DARK);
            customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            customProgressDialog.setCancelable(false);
            customProgressDialog.setCanceledOnTouchOutside(false);
            customProgressDialog.show();
            Point size = new Point();
            customProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            int height = size.y;
            WindowManager.LayoutParams params = customProgressDialog.getWindow().getAttributes();
            params.gravity = Gravity.CENTER;
            params.alpha = 0.8f;
//                        params.height = height/6;
            params.width = 4 * width / 5;
//                    params.dimAmount = 0f;
            customProgressDialog.getWindow().setAttributes(params);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (customProgressDialog != null && customProgressDialog.isShowing()) {
                        customProgressDialog.dismiss();
                        CommonUtils.showToast(mContext, "当前网络信号较差");
                    }
                }
            }, 5000);
        } else {
            try {
                List<OrderDb> orderDbs = db.findAll(OrderDb.class);
                List<CheckOrderDb> checkOrderDbs = db.findAll(CheckOrderDb.class);
//                long shouldRecieveCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", shouldRecieve).and("userId", "=", userId)));
//                long shouldStartCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", shouldStart).and("userId", "=", userId)));
//                long shouldDoCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", shouldDo).and("userId", "=", userId)));
//                long haveShelveCount = db.count(Selector.from(OrderDb.class).where(WhereBuilder.b("repairFlag", "=", haveShelve).and("userId", "=", userId)));
//                long shouldCount = db.count(Selector.from(CheckOrderDb.class).where(WhereBuilder.b("taskStatus", "=", should).and("userId", "=", userId)));
//                long overCount = db.count(Selector.from(CheckOrderDb.class).where(WhereBuilder.b("taskStatus", "=", over).and("userId", "=", userId)));
//                int mainCount = (int) (shouldRecieveCount + shouldStartCount + shouldDoCount);
//                int checkCount = (int) (shouldCount + overCount);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void getMonthData() {
        boolean checkNetWork = Utils.checkNetWork(mContext);
        if (checkNetWork) {
            TodaySizeRequest todaySizeRequest = new TodaySizeRequest(userId);
            sendRequest(todaySizeRequest, TodaySizeResponse.class);
        } else {

        }
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries, PieChart view) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(R.color.light_black);
        view.setData(data);
        view.highlightValues(null);
        //刷新
        view.invalidate();
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText(String count) {
        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda
        SpannableString s = new SpannableString(count);
        //s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        //s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        // s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        // s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_data_center, null);
        return mView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public <Res extends BaseReponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof AllOrderSizeRequest) {
            AllOrderSizeResponse allOrderSizeResponse = (AllOrderSizeResponse) response;
            if (allOrderSizeResponse.getData() != null) {
                AllOrderSizeResponse.DataEntity data = allOrderSizeResponse.getData();
                int recieve = data.getRecieve();
                int start = data.getStart();
                int mantance = data.getMantance();
                int finish = data.getFinish();
                int shave = data.getShave();
                int total = recieve + start + mantance + finish + shave;
                if (total != 0) {
                    //设置中间文件
                    mPieChart_maintain.setCenterText(generateCenterSpannableText(total + "\n维修统计"));
                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                    if (recieve != 0) {
                        entries.add(new PieEntry(recieve * 100 / total, "待接收"));
                    }
                    if (start != 0) {
                        entries.add(new PieEntry(start * 100 / total, "待开始"));
                    }
                    if (mantance != 0) {
                        entries.add(new PieEntry(mantance * 100 / total, "待维修"));
                    }
                    if (finish != 0) {
                        entries.add(new PieEntry(finish * 100 / total, "已维修"));
                    }
                    if (shave != 0) {
                        entries.add(new PieEntry(shave * 100 / total, "已挂起"));
                    }
                    setData(entries, mPieChart_maintain);
                }
            }
        } else if (request instanceof AllCheckSizeRequest) {
            AllCheckSizeResponse allCheckSizeResponse = (AllCheckSizeResponse) response;
            if (allCheckSizeResponse.getData() != null) {
                AllCheckSizeResponse.DataEntity data = allCheckSizeResponse.getData();
                int shouldCheck = data.getShouldCheck();
                int overCheck = data.getOverCheck();
                int doneCheck = data.getDoneCheck();
                int overFinishCheck = data.getOverFinishCheck();
                int total = shouldCheck + overCheck + doneCheck + overFinishCheck;
                if (total != 0) {
                    //设置中间文件
                    mPieChart_check.setCenterText(generateCenterSpannableText(total + "\n巡检统计"));
                    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                    if (shouldCheck != 0) {
                        entries.add(new PieEntry(shouldCheck * 100 / total, "待巡检"));
                    }
                    if (doneCheck != 0) {
                        entries.add(new PieEntry(doneCheck * 100 / total, "已完工"));
                    }
                    if (overCheck != 0) {
                        entries.add(new PieEntry(overCheck * 100 / total, "已超时"));
                    }
                    if (overFinishCheck != 0) {
                        entries.add(new PieEntry(overFinishCheck * 100 / total, "超时补录"));
                    }
                    //设置数据
                    setData(entries, mPieChart_check);
                }
            }
        } else if (request instanceof TodaySizeRequest) {
            TodaySizeResponse todaySizeResponse = (TodaySizeResponse) response;
            if (todaySizeResponse.getData() != null) {
                TodaySizeResponse.DataEntity dataEntity = todaySizeResponse.getData();
                List<TodaySizeResponse.DataEntity.RepairEntity> repair = dataEntity.getRepair();
                List<TodaySizeResponse.DataEntity.InspectEntity> inspect = dataEntity.getInspect();
                String currentTime = CommonUtils.getCurrentTime();
                currentTime = currentTime.substring(0, 10);
                int year = Integer.parseInt(currentTime.substring(0, 4));
                int month = Integer.parseInt(currentTime.substring(5, 7));
                Date endDayofMonth = CommonUtils.getSupportEndDayofMonth(year, month);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String endDate = dateFormat.format(endDayofMonth);
                String days = endDate.substring(8, 10);
                int dayCount = Integer.parseInt(days);
                for (int j = 1; j < dayCount + 1; j++) {
                    xValuesProcess.add(j + "");
                    boolean haveRepair = false;
                    for (int i = 0; i < repair.size(); i++) {
                        TodaySizeResponse.DataEntity.RepairEntity repairEntity = repair.get(i);
                        String date = repairEntity.getDate().substring(8, 10);
                        int day = Integer.parseInt(date);
                        if (j == day) {
                            haveRepair = true;
                            int repairCount = repairEntity.getCount();
                            repairList.add(new Entry((j - 1) * spaceForBar, repairCount));
                        }
                    }
                    if (!haveRepair) {
//                        repairList.add(new Entry((j - 1) * spaceForBar, 0));
                    }
                    boolean haveCheck = false;
                    for (int i = 0; i < inspect.size(); i++) {
                        TodaySizeResponse.DataEntity.InspectEntity inspectEntity = inspect.get(i);
                        String date = inspectEntity.getDate().substring(8, 10);
                        int day = Integer.parseInt(date);
                        if (j == day) {
                            haveCheck = true;
                            int checkCount = inspectEntity.getCount();
                            checkList.add(new Entry((j - 1) * spaceForBar, checkCount));
                        }
                    }
                    if (!haveCheck) {
//                        checkList.add(new Entry((j - 1) * spaceForBar, 0));
                    }
                }
//                xAxis.setLabelCount(xValuesProcess.size());
                description.setText(month + "月");
                notifyDataSetChanged(lineChart, repairList.size() == 0 ? null : repairList, checkList.size() == 0 ? null : checkList);
            }
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 500);
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
