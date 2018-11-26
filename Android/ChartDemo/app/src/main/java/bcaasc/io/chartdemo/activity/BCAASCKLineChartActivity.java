package bcaasc.io.chartdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
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
        chart.setScaleYEnabled(false);
        chart.setHighlightFullBarEnabled(false);
        // draw bars behind lines
        chart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
        });
        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);

        ValueFormatter custom = new BcaasValueFormatter(kLineBeans);
        if (kLineBeans != null && kLineBeans.size() > 0) {
            xAxis.setValueFormatter(custom);
        }
        xAxis.setGranularity(1f);
        CombinedData data = new CombinedData();
        data.setData(generateKLineData());
        data.setData(generateLineData());
        data.setValueTypeface(tfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.35f);

        BcaasMarkerView mv = new BcaasMarkerView(this, custom, true);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        chart.setData(data);
        chart.invalidate();
        chart.setVisibleXRangeMinimum(5);
        chart.setAutoScaleMinMaxEnabled(true);
    }


    private void initBarChart2() {
        chartBar.setOnChartValueSelectedListener(this);

        chartBar.setDrawBarShadow(false);
        chartBar.setDrawValueAboveBar(true);
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
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        YAxis leftAxis = chartBar.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
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

        generateBarData();
    }

    private LineData generateLineData() {
        calculateLineData();
        calculateThirtyDayLineData();
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();

        for (int index = 0; index < kLineBeans.size(); index++) {
            //range是控制曲线度，start是起点
            if (index % 5 == 0) {
                if (index != 0) {
                    LogTool.d(TAG, fiveLineData.get(index / 5));
                    entries.add(new Entry(index, Float.valueOf(fiveLineData.get(index / 5))));
                }
            }
//            entries3.add(new Entry(index, c));
        }
        for (int index = 0; index < kLineBeans.size(); index++) {
            if (index % 30 == 0) {
                if (index != 0) {
                    //range是控制曲线度，start是起点
                    entries2.add(new Entry(index, Float.valueOf(thirtyLineData.get(index / 30))));
                }
            }
//            entries3.add(new Entry(index, c));
        }
        LineDataSet set = new LineDataSet(entries, "MD5");
        set.setColor(Color.RED);
        set.setLineWidth(1f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(1f);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(8f);
        set.setValueTextColor(Color.BLACK);

//        set.setAxisDependency(YAxis.AxisDependency.LEFT);


        LineDataSet set2 = new LineDataSet(entries2, "MD30");
        set2.setColor(Color.YELLOW);
        set2.setLineWidth(1f);
        set2.setCircleColor(Color.YELLOW);
        set2.setCircleRadius(1f);
        set2.setFillColor(Color.YELLOW);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(true);
        set2.setValueTextSize(8f);
        set2.setValueTextColor(Color.BLACK);

//        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

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
    private void generateBarData() {
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
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            BarData data = new BarData();
            data.addDataSet(set);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(tfLight);

            data.setBarWidth(0.9f);

            chartBar.setData(data);

        }


        chartBar.invalidate();
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
            entries.add(new CandleEntry(index + 0.4f, Float.valueOf(kLineBeans.get(index).getHigh()), Float.valueOf(kLineBeans.get(index).getLow()),
                    Float.valueOf(kLineBeans.get(index).getOpen()), Float.valueOf(kLineBeans.get(index).getClose())));
        }
        CandleDataSet set = new CandleDataSet(entries, "ETHBTC");
        //设置是否显示文字
//        set.setDrawValues(false);
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
        switch (item.getItemId()) {
        }
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
}
