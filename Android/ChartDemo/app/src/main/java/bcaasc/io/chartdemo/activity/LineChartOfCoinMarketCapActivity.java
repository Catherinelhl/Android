package bcaasc.io.chartdemo.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import bcaasc.io.chartdemo.R;
import bcaasc.io.chartdemo.bean.DetailOfCoinMarketCap;
import bcaasc.io.chartdemo.bean.KLineBean;
import bcaasc.io.chartdemo.bean.ListOfCoinMarketCap;
import bcaasc.io.chartdemo.constants.Constants;
import bcaasc.io.chartdemo.contract.LineOfCoinMarketCapContract;
import bcaasc.io.chartdemo.listener.ChartMarkerViewListener;
import bcaasc.io.chartdemo.presenter.LineChartOfCoinMarketCapPresenterImp;
import bcaasc.io.chartdemo.tool.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static bcaasc.io.chartdemo.tool.DateFormatTool.getPastTimeOfEndByCycleTime;
import static bcaasc.io.chartdemo.tool.DateFormatTool.getPastTimeOfStartByCycleTime;
import static bcaasc.io.chartdemo.tool.DateFormatTool.getUTCDateForAMPMFormat2;

/**
 * 多数据
 * <p>
 * 接入CoinMarketCap真实数据
 */

public class LineChartOfCoinMarketCapActivity extends DemoBase
        implements OnChartValueSelectedListener, LineOfCoinMarketCapContract.View {
    private String TAG = LineChartOfCoinMarketCapActivity.class.getSimpleName();

    @BindView(R.id.tv_value_usd)
    TextView tvValueUsd;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.tv_value_btc)
    TextView tvValueBtc;
    @BindView(R.id.tv_value_market)
    TextView tvValueMarket;
    @BindView(R.id.line_chart)
    LineChart chart;
    @BindView(R.id.rg_cycle_time)
    RadioGroup rgCycleTime;
    @BindView(R.id.chart_bar)
    BarChart chartBar;

    //用来得到数据获取之后的条数
    private int count;
    //存储当前currency市值
    private List<List<String>> valueMarket = new ArrayList<>();
    //存储当前currency交易量
    private List<List<String>> volumeUSD = new ArrayList<>();
    //存储当前currency以BTC为参考
    private List<List<String>> priceBTC = new ArrayList<>();
    //存储当前currency以USD为参照物
    private List<List<String>> priceUSD = new ArrayList<>();
    //存储当前可以切换的时间选择
    private List<Constants.CycleTime> cycleTime = new ArrayList<>();
    //存储当前new出的所有RadioButton
    private List<RadioButton> radioButtons = new ArrayList<>();
    //默认当前的币种为bitCoin
    String coinName = "bitcoin";

    private LineChartOfCoinMarketCapPresenterImp presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);

        initView();
        initListener();
    }


    private void initView() {
        setTitle("LineChartOfCoinMarketCapActivity");
        presenter = new LineChartOfCoinMarketCapPresenterImp(this);
        presenter.getLists();
        //初始化所有时间段选择
        cycleTime.add(Constants.CycleTime.oneDay);
        cycleTime.add(Constants.CycleTime.sevenDay);
        cycleTime.add(Constants.CycleTime.oneMonth);
        cycleTime.add(Constants.CycleTime.threeMonth);
        cycleTime.add(Constants.CycleTime.oneYear);
        cycleTime.add(Constants.CycleTime.YTD);//年初至今
        cycleTime.add(Constants.CycleTime.ALL);
        //初始化RadioGroup信息
        for (int i = 0; i < cycleTime.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(cycleTime.get(i).getName());
            radioButton.setGravity(Gravity.CENTER);
            radioButtons.add(radioButton);
            if (i == 0) {
                radioButton.setBackground(getResources().getDrawable(R.drawable.selector_black_left_style));
            } else if (i == cycleTime.size() - 1) {
                radioButton.setBackground(getResources().getDrawable(R.drawable.selector_black_right_style));
            } else {
                radioButton.setBackground(getResources().getDrawable(R.drawable.selector_black_middle_style));
            }
            radioButton.setButtonDrawable(null);
            rgCycleTime.addView(radioButton);
            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.CycleTime cycleTimeType = cycleTime.get(finalI);
                    long endTime = System.currentTimeMillis();
                    long startTime = 0l;

                    switch (cycleTimeType) {

                        case oneDay:
                            startTime = endTime - 24 * 60 * 60 * 1000;
                            break;
                        case sevenDay:
                            startTime = endTime - 24 * 7 * 60 * 60 * 1000;
                            break;
                        case oneMonth:
                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();

                            break;
                        case threeMonth:
                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
                            break;
                        case oneYear:
                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
                            break;
                        case YTD:// 年初至今
//                            endTime = getPastTimeOfEndByCycleTime(cycleTimeType).getTime();
//                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
                            startTime = DateFormatTool.getCurrentYearStartTime().getTime();
                            LogTool.d(TAG, "startTime:" + getUTCDateForAMPMFormat2(String.valueOf(startTime)));
                            break;
                        case ALL:
                            endTime = 0l;
                            startTime = 0l;
                            break;
                    }
                    presenter.getLineOfCoinMarketCap(coinName, startTime == 0 ? null : String.valueOf(startTime), endTime == 0 ? null : String.valueOf(endTime));
                }
            });
        }
        //默认获取所有的数据信息
        presenter.getLineOfCoinMarketCap(coinName, null, null);
        //遍历当前的所有的cycleTime选项，然后默认指向all最后一个
        for (int i = 0; i < radioButtons.size(); i++) {
            if (i == radioButtons.size() - 1) {
                radioButtons.get(i).setChecked(true);
            }
        }
    }

    /**
     * 初始化线性图表¬
     */
    private void initChart() {
//        //右下角对图表的描述信息
//        chart.getDescription().setEnabled(false);
//        // 是否绘制背景颜色
//        chart.setDrawGridBackground(false);
//        //是否绘制背景阴影
//        chart.setDrawBarShadow(false);
//        //是否绘制边线
//        chart.setDrawBorders(true);
//        //边线宽度，单位dp
//        chart.setBorderWidth(1f);
//        //边线颜色
//        chart.setBorderColor(getResources().getColor(R.color.border_color));
//        //是否不开启硬件加速
//        chart.setHardwareAccelerationEnabled(true);
//        //设置上下内边距
//        chart.setMinOffset(0f);
//        //图标周围格额外的偏移量
//        chart.setExtraOffsets(10f, 0f, 10f, 0f);
////        chartBar.setExtraLeftOffset(10f);
////        chartBar.setExtraRightOffset(10f);
//        //启用图表拖拽事件
//        chart.setDragEnabled(true);
//        //设置Y轴方向不能滑动
//        chart.setScaleYEnabled(false);
//        //是否开启highLight
//        chart.setHi(false);
//        // draw bars behind lines
//        chart.setDrawOrder(new DrawOrder[]{
//                DrawOrder.CANDLE, DrawOrder.LINE
//        });
//        //主要控制左下方的图例的
//        Legend l = chart.getLegend();
//        //是否绘制 Legend 图例
//        l.setEnabled(true);
//        l.setWordWrapEnabled(true);
//        //设置位于Chart上面
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        //设置位于Chart左边
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        // 设置在chart外部
//        l.setDrawInside(false);
//
//        //开始定制右轴
//        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setDrawGridLines(true);
//        // 设置不需要 Labels
//        rightAxis.setDrawLabels(true);
//        //设置Y轴坐标位置显示
//        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        //虚线表示Y轴上的刻度竖线(float lineLength, float spaceLength, float phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标  当setDrawGridLines为true才有用
//        rightAxis.enableGridDashedLine(10f, 10f, 0f);
//
//
//        //开始定制左轴
//        YAxis leftAxis = chart.getAxisLeft();
//        //是否绘制Grid Line
//        leftAxis.setDrawGridLines(false);
//        //是否设置坐标参数
//        leftAxis.setDrawLabels(false);
//        //设置Y轴左边参数位置
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
////        leftAxis.setLabelCount(5, false);
//
//        //X轴
//        XAxis xAxis = chart.getXAxis();
//        //设置X轴的信息显示在底部
//        xAxis.setPosition(XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        // 设置不显示label
//        xAxis.setDrawLabels(false);
//        //设置不显示X轴的下标线
//        xAxis.setDrawAxisLine(false);
//        //绘制成虚线，只有在关闭硬件加速的情况下才能使用
//        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        //自定义一个底部显示内容格式化
//        ValueFormatter custom = new BcaasValueFormatter(kLineBeans);
//        // 如果当前没有数据返回，那么就是用默认的
//        if (kLineBeans != null && kLineBeans.size() > 0) {
//            xAxis.setValueFormatter(custom);
//        }
//        //在缩放时为轴设置最小间隔。轴不允许低于那个限度。这可以用来避免缩放时的标签复制。
//        xAxis.setGranularity(1f);
//
//        //声明一个加载曲线图和K线图的容器
//        CombinedData data = new CombinedData();
//        //添加K线图
//        data.setData(generateKLineData());
//        //添加均线图
//        data.setData(generateLineData());
//        //设置字体
//        data.setValueTypeface(tfLight);
//
//        //设置内容偏移量，否则first one & last one内容吗会被截掉
//        xAxis.setAxisMaximum(data.getXMax() + 0.5f);
//        xAxis.setAxisMinimum(data.getXMin() - 0.5f);
//
//        //设置内容显示自定义数据
//////        BcaasMarkerView mv = new BcaasMarkerView(this, custom, true);
//////        mv.setChartMarkerViewListener(chartMarkerViewListener);
//////        mv.setChartView(chart); // For bounds control
//////        chart.setMarker(mv); // Set the marker to the chart
//////        chart.setData(data);
//////        chart.invalidate();
//////        // 设置X放大的最小显示个数
//////        chart.setVisibleXRangeMinimum(5);
//////        //设置可以自动scale
//////        chart.setAutoScaleMinMaxEnabled(true);

        //自定义一个底部显示内容格式化
        ValueFormatter custom = new XLineValueFormatter(priceUSD);
        ValueFormatter yLineValueFormatter = new YLineValueFormatter(priceUSD);
        {   // // Chart Style // //
            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);


            // create marker to display box when values are selected
            // Set the marker to the chart
            ValueMarkerView mv = new ValueMarkerView(this, custom);
            mv.setChartView(chart);
            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            // 如果当前没有数据返回，那么就是用默认的
            if (priceUSD != null && priceUSD.size() > 0) {
                xAxis.setValueFormatter(custom);
            }
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);
            // 如果当前没有数据返回，那么就是用默认的
            if (priceUSD != null && priceUSD.size() > 0) {
                yAxis.setValueFormatter(yLineValueFormatter);
            }
            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
//            yAxis.setAxisMaximum(200f);
//            yAxis.setAxisMinimum(-50f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
            llXAxis.setTypeface(tfRegular);

            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tfRegular);

            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);
            ll2.setTypeface(tfRegular);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);

        }
        setData(45, 180);
        // draw points over time
        chart.animateX(1500);
        chart.setScaleYEnabled(false);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }

    /**
     * 开始绘制
     *
     * @param x x
     * @param y y
     */
    private void setData(int x, float y) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

//            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, Float.valueOf(priceUSD.get(i).get(1))));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "USD");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(0.1f);
            set1.setCircleRadius(1f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setDrawValues(false);
            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    /**
     * 创建柱形图
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
            if (volumeUSD != null && volumeUSD.size() > 0) {
                for (int index = 0; index < count; index++) {
                    entries.add(new BarEntry(index, Float.valueOf(volumeUSD.get(index).get(1))));
                }
                //设置柱形图数据属性
                set = new BarDataSet(entries, "ETH");
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

        }
        return null;

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
        int index = (int) e.getX();
        if (priceUSD != null) {
            tvTimer.setText("Time: " + DateFormatTool.getUTCDateForAMPMFormat2(priceUSD.get(index).get(0)));
            tvValueUsd.setText("USD: " + priceUSD.get(index).get(1));
        }
        if (priceBTC != null) {
//            tvLow.setText("Time:" + priceBTC.get(index).get(0));
            tvValueBtc.setText("BTC:" + priceBTC.get(index).get(1));
        }

        if (volumeUSD != null) {
//            tvNumber.setText("Time:" + volumeUSD.get(index).get(0));
            tvVolume.setText("Volume:" + volumeUSD.get(index).get(1));
        }
        if (valueMarket != null) {
            tvValueMarket.setText("Market USD:" + valueMarket.get(index).get(1));

        }

        LogTool.i(TAG, "Entry selected:" + e.toString());
        LogTool.i(TAG, "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        LogTool.i(TAG, "MIN MAX xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());

    }

    @Override
    public void onNothingSelected() {

    }


    private void initListener() {
        rgCycleTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                chartBar.highlightValue(h);
                chartBar.invalidate();
            }

            @Override
            public void onNothingSelected() {
                chartBar.highlightValue(null);
            }
        });

        chartBar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                h.setDataIndex(1);
                h.setmDataSetIndex(0);
                chart.highlightValues(new Highlight[]{h});
                chart.getMarker().refreshContent(e, h);
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
                //设置联动
                chartBar.getViewPortHandler().refresh(chart.getViewPortHandler().getMatrixTouch(), chartBar, true);
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                //设置联动
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
                //设置联动
                chart.getViewPortHandler().refresh(chartBar.getViewPortHandler().getMatrixTouch(), chart, true);

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                //设置联动
                chart.getViewPortHandler().refresh(chartBar.getViewPortHandler().getMatrixTouch(), chart, true);

            }
        });
    }

    @Override
    public void getLineSuccess(DetailOfCoinMarketCap detailOfCoinMarketCap) {
        if (detailOfCoinMarketCap == null) {
            return;
        }
        valueMarket = detailOfCoinMarketCap.getMarket_cap_by_available_supply();
        if (valueMarket == null || valueMarket.size() <= 0) {
            return;
        }
        count = valueMarket.size();
        LogTool.d(TAG, "lineBean:" + valueMarket);
        priceBTC = detailOfCoinMarketCap.getPrice_btc();
        LogTool.d(TAG, "priceBTC:" + priceBTC);
        priceUSD = detailOfCoinMarketCap.getPrice_usd();
        LogTool.d(TAG, "priceUSD:" + priceUSD);
        volumeUSD = detailOfCoinMarketCap.getVolume_usd();
        LogTool.d(TAG, "volumeUSD:" + volumeUSD);
        initChart();
    }

    @Override
    public void getLineFailure(String failureInfo) {
        LogTool.e(TAG, failureInfo);
    }

    @Override
    public void getListSuccess(ListOfCoinMarketCap listOfCoinMarketCap) {
        LogTool.d(TAG, "getListSuccess:" + listOfCoinMarketCap);
    }

    @Override
    public void getListFailure(String info) {
        LogTool.e(TAG, info);

    }
}
