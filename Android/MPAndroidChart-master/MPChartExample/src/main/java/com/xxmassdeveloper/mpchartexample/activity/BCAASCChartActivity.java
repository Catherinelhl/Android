package com.xxmassdeveloper.mpchartexample.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;

/**
 * 单数据
 */
public class BCAASCChartActivity extends DemoBase {

    private CombinedChart chart;
    private final int count = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bcaasc);

        setTitle("BCAASCChartActivity");

        chart = findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        chart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
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
        data.setData(generateBarData());
        data.setData(generateCandleData());
        data.setValueTypeface(tfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        chart.setData(data);
        chart.invalidate();
    }

    private LineData generateLineData() {


        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            //range是控制曲线度，start是起点
            float a = getRandom(5, 5);
//            System.out.println("this random is:" + a);
            entries.add(new Entry(index + 0.3f, a));
        }
        LineDataSet set = new LineDataSet(entries, "Line");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(3f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData d = new LineData();
        d.addDataSet(set);

        // create a data object with the data sets
        d.setValueTextColor(Color.BLACK);
        d.setValueTextSize(11f);
        return d;
    }

    /**
     * 两个比较的
     *
     * @return
     */
    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            float a = getRandom(2, 1);
            entries1.add(new BarEntry(index + 0.3f, a));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData d = new BarData();
        d.addDataSet(set1);

        return d;
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

            float baseValue = (float) (Math.random() * 5) + 10;

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
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(0.3f);
        //设置跌的颜色为Red
        set.setDecreasingColor(Color.RED);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        //设置增长的颜色为绿色
        set.setIncreasingColor(Color.GREEN);
        //设置增长的图形style
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.BLUE);
        d.addDataSet(set);
        d.setValueTextColor(Color.BLACK);
        d.setValueTextSize(11f);

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
}
