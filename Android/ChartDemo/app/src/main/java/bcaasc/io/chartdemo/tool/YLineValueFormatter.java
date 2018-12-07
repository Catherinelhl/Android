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
        if (value > 1000 && value < 1000000) {
            return Math.round(value / 1000) + "kil";

        } else if (value >= 1000000 && value < 1000000000) {
            return Math.round(value / 1000000) + "mli";

        } else if (value >= 1000000000 && value < 1000000000000f) {
            return Math.round(value / 1000000000) + "bli";

        } else if (value >= 1000000000000f && value < 1000000000000000f) {
            return Math.round(value / 1000000000000f) + "tri";

        }else {
            return String.valueOf(value);
        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (value > 1000 && value < 1000000) {
            return Math.round(value / 1000) + "kil";

        } else if (value >= 1000000 && value < 1000000000) {
            return Math.round(value / 1000000) + "mli";

        } else if (value >= 1000000000 && value < 1000000000000f) {
            return Math.round(value / 1000000000) + "bli";

        } else if (value >= 1000000000000f && value < 1000000000000000f) {
            return Math.round(value / 1000000000000f) + "tri";

        }else {
            return String.valueOf(value);
        }
    }
}
