package bcaasc.io.chartdemo.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;
import bcaasc.io.chartdemo.R;
import bcaasc.io.chartdemo.bean.KLineBean;
import bcaasc.io.chartdemo.contract.BcaasCChartContract;
import bcaasc.io.chartdemo.listener.ChartMarkerViewListener;
import bcaasc.io.chartdemo.presenter.BcaasCChartPresenterImp;
import bcaasc.io.chartdemo.tool.*;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.tv_open)
    TextView tvOpen;
    @BindView(R.id.tv_high)
    TextView tvHigh;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_low)
    TextView tvLow;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.chart1)
    CombinedChart chart;
    @BindView(R.id.chart_bar)
    BarChart chartBar;

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initListener();

    }

    private void initView() {
        presenter = new BcaasCChartPresenterImp(this);
        presenter.getKLine();
        setTitle("BCAASCKLineChartActivity");

    }

    private void initChart() {
        chart.getDescription().setEnabled(false);//右下角对图表的描述信息
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawBorders(true);//是否绘制边线
        chart.setBorderWidth(1f);//边线宽度，单位dp
        chart.setBorderColor(getResources().getColor(R.color.border_color));//边线颜色
        chart.setHardwareAccelerationEnabled(true);//是否不开启硬件加速
        chart.setMinOffset(0f);//设置上下内边距
        chart.setExtraOffsets(10f, 0f, 10f, 0f);//图标周围格额外的偏移量
//        chartBar.setExtraLeftOffset(10f);
//        chartBar.setExtraRightOffset(10f);
        chart.setDragEnabled(true);//启用图表拖拽事件
        chart.setScaleYEnabled(false); //设置Y轴方向不能滑动
        chart.setHighlightFullBarEnabled(false);
        chart.setDrawOrder(new DrawOrder[]{// draw bars behind lines
                DrawOrder.CANDLE, DrawOrder.LINE
        });

        Legend l = chart.getLegend();//主要控制左下方的图例的
        l.setEnabled(true);//是否绘制 Legend 图例
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
        rightAxis.setDrawLabels(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.enableGridDashedLine(10f, 10f, 0f); //虚线表示Y轴上的刻度竖线(float lineLength, float spaceLength, float phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标  当setDrawGridLines为true才有用


        //开始定制左轴
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        leftAxis.setLabelCount(5, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布

        //X轴
        XAxis xAxis = chart.getXAxis();
        //设置X轴的信息显示在底部
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        // 设置不显示label
        xAxis.setDrawLabels(false);
        //设置不显示X轴的下标线
        xAxis.setDrawAxisLine(false);
        xAxis.enableGridDashedLine(10f, 10f, 0f);//绘制成虚线，只有在关闭硬件加速的情况下才能使用

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

        //设置内容显示自定义数据
        BcaasMarkerView mv = new BcaasMarkerView(this, custom, true);
        mv.setChartMarkerViewListener(chartMarkerViewListener);
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
        chartBar.setDrawBorders(true);//是否绘制边线
        chartBar.setBorderWidth(1f);//边线宽度，单位dp
        chartBar.getDescription().setEnabled(false);
        chartBar.setMinOffset(0f);//设置上下内边距
        chartBar.setExtraOffsets(10f, 0f, 10f, 10f);//图标周围格额外的偏移量


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
//        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        leftAxis.setSpaceTop(15f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawLabels(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f); //虚线表示Y轴上的刻度竖线(float lineLength, float spaceLength, float phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标  当setDrawGridLines为true才有用


//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chartBar.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        Legend l = chartBar.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);

        BcaasMarkerView mv = new BcaasMarkerView(this, custom, false);
        mv.setChartMarkerViewListener(chartMarkerViewListener);
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

    private ChartMarkerViewListener chartMarkerViewListener = new ChartMarkerViewListener() {
        @Override
        public void getKLineBean(Entry entry, Highlight highlight, KLineBean kLineBean) {

        }

        @Override
        public void getIndex(int index) {
            if (kLineBeans != null && kLineBeans.size() > 0) {
                if (index < kLineBeans.size()) {
                    KLineBean kLineBean = kLineBeans.get(index);
                    //显示当前数据
                    tvOpen.setText("Open: " + kLineBean.getOpen());
                    tvClose.setText("Close: " + kLineBean.getClose());
                    tvHigh.setText("High: " + kLineBean.getHigh());
                    tvLow.setText("Low: " + kLineBean.getLow());
                    tvVolume.setText("Volume: " + kLineBean.getVolume());
                    tvNumber.setText("Number: " + kLineBean.getNumberOfTrades());
                    tvTimer.setText("Time: " + DateFormatTool.getUTCDateForAMPMFormat(kLineBean.getOpenTime()));
                }
            }
        }
    };

    private LineData generateLineData() {
        calculateLineData();
        calculateThirtyDayLineData();
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();

        for (int index = 0; index < kLineBeans.size(); index++) {
            //range是控制曲线度，start是起点
            if (index % 5 == 0) {
                if (index != 0) {
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
        d.setHighlightEnabled(false);
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
        initChart();
        initBarChart2();

    }

    @Override
    public void getKLineFailure(String failureInfo) {

    }


    private void initListener() {
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                chartBar.highlightValue(h);
                chartBar.invalidate();
//                chartBar.getMarker().refreshContent(e,h);

//                Highlight highlight = new Highlight(h.getXIndex(), h.getValue(), h.getDataIndex(), h.getDataSetIndex());
//
//                float touchY = h.getTouchY() - mChartPrice.getHeight();
//                Highlight h1 = mChartVolume.getHighlightByTouchPoint(h.getXIndex(), touchY);
//                highlight.setTouchY(touchY);
//                if (null == h1) {
//                    highlight.setTouchYValue(0);
//                } else {
//                    highlight.setTouchYValue(h1.getTouchYValue());
//                }
//                chartBar.highlightValues(new Highlight[]{highlight});
            }

            @Override
            public void onNothingSelected() {
                chartBar.highlightValue(null);
            }
        });

        chartBar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
//                Highlight highlight = new Highlight(h.getX(),h.getY(), h.getDataIndex(), h.getDataSetIndex());
//                float touchY = h.getTouchY() + chart.getHeight();
//                Highlight h1 = chart.getHighlightByTouchPoint(h.getX(), touchY);
//                highlight.setTouchY(touchY);
//                if (null == h1) {
//                    highlight.setTouchYValue(0);
//                } else {
//                    highlight.setTouchYValue(h1.getTouchYValue());
//                }
                h.setDataIndex(1);
                h.setmDataSetIndex(0);
                chart.highlightValues(new Highlight[]{h});
                chart.getMarker().refreshContent(e,h);
                chart.notifyDataSetChanged();
                chart.invalidate();
            }

            @Override
            public void onNothingSelected() {
                chart.highlightValue(null);
            }
        });

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
