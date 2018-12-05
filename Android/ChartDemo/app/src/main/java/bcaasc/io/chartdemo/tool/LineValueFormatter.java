package bcaasc.io.chartdemo.tool;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.List;

public class LineValueFormatter extends ValueFormatter {
    private String TAG = LineValueFormatter.class.getSimpleName();
    private final DecimalFormat mFormat;
    private List<List<String>> allLine;


    public LineValueFormatter(List<List<String>> allLine) {
        mFormat = new DecimalFormat("##0.0");
        this.allLine = allLine;

    }


    @Override
    public String getFormattedValue(float value) {
        if (allLine == null || allLine.size() == 0) {
            return mFormat.format(value);
        } else {
            return DateFormatTool.getUTCDateForAMPMFormat(allLine.get((int) value).get(0));
        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
//        if (axis instanceof XAxis) {
        if (allLine == null || allLine.size() == 0) {
            return mFormat.format(value);
        } else {
            int x = (int) value;
            if (x < allLine.size()) {
                return DateFormatTool.getUTCDateForAMPMFormat(allLine.get(x).get(0));
            } else {
                return mFormat.format(value);
            }
        }
//        } else if (value > 0) {
//            try {
//                return DateFormatTool.getUTCDateForAMPMFormat(mFormat.format(value) + "cc");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            return mFormat.format(value) + "dd";
//        }
    }
}
