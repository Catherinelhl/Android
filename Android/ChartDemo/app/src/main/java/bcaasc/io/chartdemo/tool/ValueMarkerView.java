package bcaasc.io.chartdemo.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import bcaasc.io.chartdemo.R;
import bcaasc.io.chartdemo.listener.ChartMarkerViewListener;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

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

    public ValueMarkerView(Context context, ValueFormatter valueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.valueFormatter =  valueFormatter;
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
        this.chartMarkerViewListener.getIndex((int) e.getX());
        tvContent.setText( valueFormatter.getFormattedValue(e.getX()) + "\n" + format.format(e.getY()));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
