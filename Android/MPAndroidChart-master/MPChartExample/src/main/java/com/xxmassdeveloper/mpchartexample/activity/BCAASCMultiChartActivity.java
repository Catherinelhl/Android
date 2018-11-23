package com.xxmassdeveloper.mpchartexample.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.listener.OnDrawListener;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.contract.BcaasCChartContract;
import com.xxmassdeveloper.mpchartexample.custom.DayAxisValueFormatter;
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;
import com.xxmassdeveloper.mpchartexample.custom.MyValueFormatter;
import com.xxmassdeveloper.mpchartexample.custom.XYMarkerView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;
import com.xxmassdeveloper.mpchartexample.presenter.BcaasCChartPresenterImp;
import com.xxmassdeveloper.mpchartexample.tool.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 多数据
 */

public class BCAASCMultiChartActivity extends DemoBase
        implements OnChartValueSelectedListener, BcaasCChartContract.View {

    private CombinedChart chart;
    private BarChart chartBar;
    private final int count = 24;
    private String TAG = BCAASCMultiChartActivity.class.getSimpleName();

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
        setTitle("BCAASCMultiChartActivity");

        chart = findViewById(R.id.chart1);
        chartBar = findViewById(R.id.chart_bar);

        initChart();
        initBarChart2();

    }

    // Open time
//    "0.01634790",       // Open
//            "0.80000000",       // High
//            "0.01575800",       // Low
//            "0.01577100",       // Close
//            "148976.11427815",  // Volume
//            1499644799999,      // Close time
//            "2434.19055334",    // Quote asset volume
//            308,                // Number of trades
//            "1756.87402397",    // Taker buy base asset volume
//            "28.46694368",      // Taker buy quote asset volume
//            "17928899.62484339" // Ignore.


    private String open;
    private String High;
    private String Low;
    private String Close;
    private String Volume;
    private String CloseTime;
    private String QuoteAssetVolume;
    private String NumberOfTrades;
    private String TakerBuyBaseAssetVolume;
    private String TakerBuyQuoteAssetVolume;
    private String Ignore;

    private void initChart() {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
//        chart.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                return false;
//            }
//        });

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
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months[(int) value % months.length];
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
//        data.setData(generateBarData());
        data.setData(generateCandleData());
        data.setValueTypeface(tfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.35f);
        chart.setData(data);
        chart.invalidate();
    }


    private void initBarChart2() {
        chartBar.setOnChartValueSelectedListener(this);

        chartBar.setDrawBarShadow(false);
        chartBar.setDrawValueAboveBar(true);

        chartBar.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chartBar.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chartBar.setPinchZoom(false);

        chartBar.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chartBar);

        XAxis xAxis = chartBar.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months[(int) value % months.length];
            }
        });

        ValueFormatter custom = new MyValueFormatter("$");

        YAxis leftAxis = chartBar.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chartBar.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chartBar.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(chartBar); // For bounds control
        chartBar.setMarker(mv); // Set the marker to the chart
        generateBarData();
    }

    private LineData generateLineData() {


        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            //range是控制曲线度，start是起点
            float a = getRandom(1, 1);
            float b = getRandom(2, 1);
            float c = getRandom(3, 1);
//            System.out.println("this random is:" + a);
            entries.add(new Entry(index, a));
            entries2.add(new Entry(index, b));
            entries3.add(new Entry(index, c));
        }
        LineDataSet set = new LineDataSet(entries, "Line");
        set.setColor(Color.RED);
        set.setLineWidth(1.5f);
        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(2f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(8f);
        set.setValueTextColor(Color.BLACK);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);


        LineDataSet set2 = new LineDataSet(entries2, "Line2");
        set2.setColor(Color.GREEN);
        set2.setLineWidth(1.5f);
        set2.setCircleColor(Color.BLACK);
        set2.setCircleRadius(2f);
        set2.setFillColor(Color.GREEN);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(true);
        set2.setValueTextSize(8f);
        set2.setValueTextColor(Color.BLACK);

        set2.setAxisDependency(YAxis.AxisDependency.LEFT);


//        LineDataSet set3 = new LineDataSet(entries3, "Line3");
//        set3.setColor(Color.BLUE);
//        set3.setLineWidth(1.0f);
//        set3.setCircleColor(Color.BLACK);
//        set3.setCircleRadius(2f);
//        set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        set3.setDrawCircleHole(false);
////        set2.setDrawValues(true);
//        set3.setValueTextSize(10f);
//        set3.setValueTextColor(Color.WHITE);
//
//        set3.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData d = new LineData(set, set2);

        // create a data object with the data sets
        d.setValueTextColor(Color.BLACK);
        d.setValueTextSize(8f);
        return d;
    }

    /**
     * 两个比较的
     *
     * @return
     */
    private void generateBarData() {
        BarDataSet set1, set2;
        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        if (chartBar.getData() != null && chartBar.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chartBar.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chartBar.getData().getDataSetByIndex(1);
            set1.setValues(entries1);
            set2.setValues(entries1);
            chartBar.getData().notifyDataChanged();
            chartBar.notifyDataSetChanged();
        } else {


            for (int index = 0; index < count; index++) {
                float a = getRandom(1, 1);
                float b = getRandom(2, 1);
                entries1.add(new BarEntry(0, a));
                entries2.add(new BarEntry(0, b));
                // stacked
//            entries2.add(new BarEntry(index, new float[]{getRandom(1, 1), getRandom(1, 1)}));
            }

            set1 = new BarDataSet(entries1, "Bar 1");
            set1.setColor(Color.rgb(60, 220, 78));
            set1.setValueTextColor(Color.rgb(60, 220, 78));
            set1.setValueTextSize(10f);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);

            set2 = new BarDataSet(entries2, "Bar 2");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
            set2.setColors(Color.rgb(61, 165, 255));
            set2.setValueTextColor(Color.rgb(61, 165, 255));
            set2.setValueTextSize(8f);
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);


            BarData data = new BarData(set1, set2);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(tfLight);

            chartBar.setData(data);

        }


        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.435f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        // specify the width each bar should have
        chartBar.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chartBar.getXAxis().setAxisMinimum(0);
        // make this BarData object grouped
//        data.groupBars(0, groupSpace, barSpace); // start at x = 0
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chartBar.getXAxis().setAxisMaximum(0 + chartBar.getBarData().getGroupWidth(groupSpace, barSpace) * count + 1);
        chartBar.groupBars(0, groupSpace, barSpace);
        chartBar.invalidate();
    }


    /**
     * K 线图
     *
     * @return
     */
    private CandleData generateCandleData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++) {

            float baseValue = (float) (Math.random() * 5) + 5;

            float open = (float) (Math.random() * 1);//开盘的随机数
            float close = (float) (Math.random() * 1);//收盘的随机数

            //是否跌
            boolean isFall = index % 2 == 0;

            //如果当前跌了，那么就用基数+开盘随机数，否是减去开盘随机数来得到当前的开盘数
            float openingPrice = isFall ? baseValue + open : baseValue - open;
            //如果当前跌了，那么收盘的数据一定是小于开盘的，那么就用基数-收盘随机数，否则加上收盘随机数来得到当前的收盘数
            float closingPrice = isFall ? baseValue - close : baseValue + close;

            //如果今天是跌了，那么就会采用openingPrice 为图的top，并且表现为红色；如果今天是涨了，那么就表示为绿色
            float maxOfTime = (isFall ? openingPrice : closingPrice) + (float) (Math.random() * 1);
            float minOfTime = (isFall ? closingPrice : openingPrice) - (float) (Math.random() * 1);

//            //基值
//            System.out.println("val:base:" + baseValue);
//            //时间段上升的最高值
//            System.out.println("val:max:" + maxOfTime);
//            //时间段下降的最高值
//            System.out.println("val:min:" + minOfTime);
//            System.out.println("val+openingPrice:" + isFall + openingPrice);
//            System.out.println("val+closingPrice:" + isFall + closingPrice);
            // getResources().getDrawable(R.drawable.star
            entries.add(new CandleEntry(index + 0.4f, maxOfTime, minOfTime, openingPrice, closingPrice));
        }
        CandleDataSet set = new CandleDataSet(entries, "Candle");
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
        getMenuInflater().inflate(R.menu.combined, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewGithub: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/CombinedChartActivity.java"));
                startActivity(i);
                break;
            }
            case R.id.actionToggleLineValues: {
                for (IDataSet set : chart.getData().getDataSets()) {
                    if (set instanceof LineDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionToggleBarValues: {
                for (IDataSet set : chart.getData().getDataSets()) {
                    if (set instanceof BarDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionRemoveDataSet: {
                int rnd = (int) getRandom(chart.getData().getDataSetCount(), 0);
                chart.getData().removeDataSet(chart.getData().getDataSetByIndex(rnd));
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
                chart.invalidate();
                break;
            }
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
            String open = childKLineData.get(0);
            String High = childKLineData.get(1);
            String Low = childKLineData.get(2);
            String Close = childKLineData.get(3);
            String Volume = childKLineData.get(4);
            String CloseTime = childKLineData.get(5);
            String QuoteAssetVolume = childKLineData.get(6);
            String NumberOfTrades = childKLineData.get(7);
            String TakerBuyBaseAssetVolume = childKLineData.get(8);
            String TakerBuyQuoteAssetVolume = childKLineData.get(9);
            String Ignore = childKLineData.get(10);
            LogTool.d(TAG, "open:" + open + "High:" + High +
                    "Low:" + Low + "Close:" + Close + "Volume:" + Volume +
                    "CloseTime:" + CloseTime + "QuoteAssetVolume:" + QuoteAssetVolume + "NumberOfTrades:" + NumberOfTrades +
                    "TakerBuyBaseAssetVolume:" + TakerBuyBaseAssetVolume + "TakerBuyQuoteAssetVolume:" + TakerBuyQuoteAssetVolume
                    + "Ignore:" + Ignore);
        }

    }

    @Override
    public void getKLineFailure(String failureInfo) {

    }
}
