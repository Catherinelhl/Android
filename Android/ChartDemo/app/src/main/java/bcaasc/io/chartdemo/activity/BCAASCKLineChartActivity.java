package bcaasc.io.chartdemo.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import bcaasc.io.chartdemo.R;
import bcaasc.io.chartdemo.bean.KLineBean;
import bcaasc.io.chartdemo.contract.BcaasCChartContract;
import bcaasc.io.chartdemo.presenter.BcaasCChartPresenterImp;
import bcaasc.io.chartdemo.tool.BcaasMarkerView;
import bcaasc.io.chartdemo.tool.BcaasValueFormatter;
import bcaasc.io.chartdemo.tool.DemoBase;
import bcaasc.io.chartdemo.tool.LogTool;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 多数据
 * <p>
 * 接入币安真实数据
 */

public class BCAASCKLineChartActivity extends DemoBase
        implements OnChartValueSelectedListener, BcaasCChartContract.View {

    private String TAG = BCAASCKLineChartActivity.class.getSimpleName();

    private CombinedChart chart;
    private BarChart chartBar;
    private int count;
    private List<KLineBean> kLineBeans = new ArrayList<>();
    private List<String> fiveLineData = new ArrayList<>();
    private List<String> thirtyLineData = new ArrayList<>();

    private BcaasCChartContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bcaasc_mutilple);

        initView();
        initListener();

    }

    private void initView() {
        presenter = new BcaasCChartPresenterImp(this);
        presenter.getKLine();
        setTitle("BCAASCKLineChartActivity");
        chart = findViewById(R.id.chart1);
        chartBar = findViewById(R.id.chart_bar);

    }

    private void initChart() {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        //设置Y轴方向不能滑动
        chart.setScaleYEnabled(false);
        chart.setHighlightFullBarEnabled(false);
        // draw bars behind lines
        chart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
        });
        //设置颜色注释方位以及信息
        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        //设置位于Chart上面
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置位于Chart左边
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        // 设置在chart外部
        l.setDrawInside(false);

        //开始定制右轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        // 设置不需要 Labels
        rightAxis.setDrawLabels(false);

        //开始定制左轴
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
//        leftAxis.setMaxWidth(50f);

        //开始定制X轴
        XAxis xAxis = chart.getXAxis();
        //设置X轴的信息显示在底部
//        xAxis.setPosition(XAxisPosition.BOTTOM);
        // 设置不显示label
//        xAxis.setDrawLabels(false);
        //设置不显示X轴的下标线
        xAxis.setDrawAxisLine(false);

        //自定义一个底部显示内容格式化
        ValueFormatter custom = new BcaasValueFormatter(kLineBeans);
        // 如果当前没有数据返回，那么就是用默认的
        if (kLineBeans != null && kLineBeans.size() > 0) {
            xAxis.setValueFormatter(custom);
        }
        //在缩放时为轴设置最小间隔。轴不允许低于那个限度。这可以用来避免缩放时的标签复制。
        xAxis.setGranularity(1f);

        //声明一个加载曲线图和K线图的容器
        CombinedData data = new CombinedData();
        //添加K线图
        data.setData(generateKLineData());
        //添加均线图
        data.setData(generateLineData());
        //设置字体
        data.setValueTypeface(tfLight);

        //设置内容偏移量，否则first one & last one内容吗会被截掉
        xAxis.setAxisMaximum(data.getXMax() + 0.5f);
        xAxis.setAxisMinimum(data.getXMin() - 0.5f);

        //设置内容显示期
        BcaasMarkerView mv = new BcaasMarkerView(this, custom, true);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        chart.setData(data);
        chart.invalidate();
        // 设置X放大的最小显示个数
        chart.setVisibleXRangeMinimum(5);
        //设置可以自动scale
        chart.setAutoScaleMinMaxEnabled(true);
    }


    private void initBarChart2() {
        //设置bar是否显示区间有阴影
        chartBar.setDrawBarShadow(false);
        chartBar.setDrawValueAboveBar(false);
        // 设置Y轴不可放大
        chartBar.setScaleYEnabled(false);

        chartBar.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        chartBar.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chartBar.setPinchZoom(false);

        chartBar.setDrawGridBackground(false);

        //设置下坐标
        XAxis xAxis = chartBar.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        ValueFormatter custom = new BcaasValueFormatter(kLineBeans);
        if (kLineBeans != null && kLineBeans.size() > 0) {
            xAxis.setValueFormatter(custom);
        }
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        YAxis leftAxis = chartBar.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setDrawGridLines(true);

//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chartBar.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        Legend l = chartBar.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);

        BcaasMarkerView mv = new BcaasMarkerView(this, custom, false);
        mv.setChartView(chartBar); // For bounds control
        chartBar.setMarker(mv); // Set the marker to the chart
        chartBar.setAutoScaleMinMaxEnabled(true);
        chartBar.setVisibleXRangeMinimum(5);

        BarData barData = generateBarData();
        //如果当前是重新生成的BarData，那么就需要重新添加以及重新设置初始数据
        if (barData != null) {
            xAxis.setAxisMaximum(barData.getXMax() + 0.5f);
            xAxis.setAxisMinimum(barData.getXMin() - 0.5f);
            chartBar.setData(barData);
        }
        chartBar.invalidate();

    }

    private LineData generateLineData() {
        calculateLineData();
        calculateThirtyDayLineData();
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();

        for (int index = 0; index < kLineBeans.size(); index++) {
            //range是控制曲线度，start是起点
            if (index % 5 == 0) {
                if (index != 0) {
                    LogTool.d(TAG, fiveLineData.get(index / 5));
                    entries.add(new Entry(index, Float.valueOf(fiveLineData.get(index / 5))));
                }
            }
        }
        for (int index = 0; index < kLineBeans.size(); index++) {
            if (index % 30 == 0) {
                if (index != 0) {
                    //range是控制曲线度，start是起点
                    entries2.add(new Entry(index, Float.valueOf(thirtyLineData.get(index / 30))));
                }
            }
        }
        LineDataSet set = new LineDataSet(entries, "MD5");
        set.setColor(Color.BLUE);
        set.setLineWidth(1f);
        set.setCircleColor(Color.BLUE);
        set.setCircleRadius(1f);
        set.setFillColor(Color.BLUE);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setValueTextSize(8f);
        set.setValueTextColor(Color.BLACK);

        LineDataSet set2 = new LineDataSet(entries2, "MD30");
        set2.setColor(Color.YELLOW);
        set2.setLineWidth(1f);
        set2.setCircleColor(Color.YELLOW);
        set2.setCircleRadius(1f);
        set2.setFillColor(Color.YELLOW);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(false);
        set2.setValueTextSize(8f);
        set2.setValueTextColor(Color.BLACK);

        LineData d = new LineData(set, set2);
        // create a data object with the data sets
        d.setValueTextColor(Color.BLACK);
        d.setValueTextSize(8f);
        return d;
    }

    /**
     * 计算当前的曲线图
     * 首先以5天为单位
     */
    private void calculateLineData() {
        float data = 0.0f;
        for (int i = 0; i < kLineBeans.size(); i++) {
            if (i % 5 == 0) {
                fiveLineData.add(String.valueOf(data / 5));
                data = 0.0f;
            }
            data += Float.valueOf(kLineBeans.get(i).getClose());
        }
    }

    /**
     * 计算当前的曲线图
     * 首先以30天为单位
     */
    private void calculateThirtyDayLineData() {
        float data = 0.0f;
        for (int i = 0; i < kLineBeans.size(); i++) {
            if (i % 30 == 0) {
                thirtyLineData.add(String.valueOf(data / 30));
                data = 0.0f;
            }
            data += Float.valueOf(kLineBeans.get(i).getClose());
        }
    }

    /**
     * 两个比较的
     *
     * @return
     */
    private BarData generateBarData() {
        BarDataSet set;
        ArrayList<BarEntry> entries = new ArrayList<>();

        if (chartBar.getData() != null && chartBar.getData().getDataSetCount() > 0) {
            set = (BarDataSet) chartBar.getData().getDataSetByIndex(0);
            set.setValues(entries);
            chartBar.getData().notifyDataChanged();
            chartBar.notifyDataSetChanged();
        } else {
            for (int index = 0; index < count; index++) {
                entries.add(new BarEntry(index, Float.valueOf(kLineBeans.get(index).getVolume())));
            }
            set = new BarDataSet(entries, "ETHBTC");
            set.setColor(Color.rgb(60, 220, 78));
            set.setValueTextColor(Color.rgb(60, 220, 78));
            set.setValueTextSize(10f);
            set.setDrawValues(false);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            BarData data = new BarData();
            data.addDataSet(set);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);
            return data;


        }
        return null;

    }


    /**
     * K 线图
     *
     * @return
     */
    private CandleData generateKLineData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            entries.add(new CandleEntry(index, Float.valueOf(kLineBeans.get(index).getHigh()), Float.valueOf(kLineBeans.get(index).getLow()),
                    Float.valueOf(kLineBeans.get(index).getOpen()), Float.valueOf(kLineBeans.get(index).getClose())));
        }
        CandleDataSet set = new CandleDataSet(entries, "ETHBTC");
        //设置是否显示文字
        set.setDrawValues(false);
        //设置candle的宽度
//        set.setBarSpace(150f);
        set.setDrawIcons(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置low or high's color
//        set.setShadowColor(Color.RED);
        //设置low or high's color same as candle
        set.setShadowColorSameAsCandle(true);
        //设置low or high's stroke's width
        set.setShadowWidth(1.5f);
        //设置跌的颜色为Red
        set.setDecreasingColor(Color.RED);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.BLUE);
        //设置增长的颜色为绿色
        set.setIncreasingColor(Color.GREEN);
        //设置增长的图形style
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        d.addDataSet(set);
        d.setValueTextColor(Color.BLACK);
        d.setValueTextSize(8f);

        return d;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void saveToGallery() { /* Intentionally left empty */ }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void getKLineSuccess(List<List<String>> kLineData) {
        for (List<String> childKLineData : kLineData) {
            KLineBean kLineBean = new KLineBean();
            kLineBean.setOpenTime(childKLineData.get(0));
            kLineBean.setOpen(childKLineData.get(1));
            kLineBean.setHigh(childKLineData.get(2));
            kLineBean.setLow(childKLineData.get(3));
            kLineBean.setClose(childKLineData.get(4));
            kLineBean.setVolume(childKLineData.get(5));
            kLineBean.setCloseTime(childKLineData.get(6));
            kLineBean.setQuoteAssetVolume(childKLineData.get(7));
            kLineBean.setNumberOfTrades(childKLineData.get(8));
            kLineBean.setTakerBuyBaseAssetVolume(childKLineData.get(9));
            kLineBean.setTakerBuyQuoteAssetVolume(childKLineData.get(10));
            kLineBean.setIgnore(childKLineData.get(11));
            kLineBeans.add(kLineBean);
        }
        count = kLineBeans.size();
        LogTool.d(TAG, count);
        initChart();
        initBarChart2();

    }

    @Override
    public void getKLineFailure(String failureInfo) {

    }


    private void initListener() {
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                chartBar.getViewPortHandler().refresh(chart.getViewPortHandler().getMatrixTouch(), chartBar, true);
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                chartBar.getViewPortHandler().refresh(chart.getViewPortHandler().getMatrixTouch(), chartBar, true);

            }
        });
        chartBar.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                chart.getViewPortHandler().refresh(chartBar.getViewPortHandler().getMatrixTouch(), chart, true);

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                chart.getViewPortHandler().refresh(chartBar.getViewPortHandler().getMatrixTouch(), chart, true);

            }
        });
    }

}
