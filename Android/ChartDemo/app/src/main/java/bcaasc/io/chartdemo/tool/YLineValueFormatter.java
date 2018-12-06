package bcaasc.io.chartdemo.tool;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

/**
 * Y轴Label定制
 */
public class YLineValueFormatter extends ValueFormatter {
    private String TAG = YLineValueFormatter.class.getSimpleName();
    private List<List<String>> allLine;


    public YLineValueFormatter(List<List<String>> allLine) {
        this.allLine = allLine;

    }


    @Override
    public String getFormattedValue(float value) {
        return Math.round(value / 1000) + "kil";
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return Math.round(value / 1000) + "kil";
    }
}
