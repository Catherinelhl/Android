package bcaasc.io.chartdemo.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import bcaasc.io.chartdemo.R;
import bcaasc.io.chartdemo.bean.DetailOfCoinMarketCap;
import bcaasc.io.chartdemo.listener.ChartMarkerViewListener;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Catherine
 * <p>
 * 显示Bcaas自定义的线图信息
 */
@SuppressLint("ViewConstructor")
public class ValueMarkerView extends MarkerView {

    private final TextView tvContent;
    private final ValueFormatter valueFormatter;

    private final DecimalFormat format;

    private ChartMarkerViewListener chartMarkerViewListener;
    private DetailOfCoinMarketCap detailOfCoinMarketCap;

    public ValueMarkerView(Context context, DetailOfCoinMarketCap detailOfCoinMarketCap, ValueFormatter valueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.detailOfCoinMarketCap = detailOfCoinMarketCap;
        this.valueFormatter = valueFormatter;
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
    }

    public void setChartMarkerViewListener(ChartMarkerViewListener chartMarkerViewListener) {
        this.chartMarkerViewListener = chartMarkerViewListener;
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (chartMarkerViewListener != null) {
            this.chartMarkerViewListener.getIndex((int) e.getX());
        }
        float index = e.getX();
        if (this.detailOfCoinMarketCap == null) {
            tvContent.setText(valueFormatter.getFormattedValue(index) + "\n" + format.format(e.getY()));
        } else {
            int posiontion = (int) index;
            List<List<String>> market_cap_by_available_supply = detailOfCoinMarketCap.getMarket_cap_by_available_supply();
            if (market_cap_by_available_supply == null) {
                tvContent.setText(valueFormatter.getFormattedValue(index) + "\n" + format.format(e.getY()));

            } else {
                tvContent.setText(DateFormatTool.getUTCDateForAMPMFormat(market_cap_by_available_supply.get(posiontion).get(0))
                        + "\nMarketValue:" + market_cap_by_available_supply.get((int) index).get(1)
                        + "\nBTC Price:" + detailOfCoinMarketCap.getPrice_btc().get(posiontion).get(1)
                        + "\nUSD Price:$" + detailOfCoinMarketCap.getPrice_usd().get(posiontion).get(1)
                        + "\nVolume:" + detailOfCoinMarketCap.getVolume_usd().get(posiontion).get(1));

            }
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
