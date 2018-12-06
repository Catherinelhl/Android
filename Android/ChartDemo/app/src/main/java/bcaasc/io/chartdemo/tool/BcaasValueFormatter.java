package bcaasc.io.chartdemo.tool;

import bcaasc.io.chartdemo.bean.KLineBean;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.List;

public class BcaasValueFormatter extends ValueFormatter {
    private String TAG = BcaasValueFormatter.class.getSimpleName();
    private final DecimalFormat mFormat;
    private List<KLineBean> allKLine;

    public BcaasValueFormatter(List<KLineBean> allKLine) {
        mFormat = new DecimalFormat("##0.0");
        this.allKLine = allKLine;

    }


    @Override
    public String getFormattedValue(float value) {
        if (allKLine == null || allKLine.size() == 0) {
            return mFormat.format(value);
        } else {
            return DateFormatTool.getUTCDateForAMPMFormat(allKLine.get((int) value).getOpenTime());
        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
//        if (axis instanceof XAxis) {
        if (allKLine == null || allKLine.size() == 0) {
            return mFormat.format(value);
        } else {
            int x = (int) value;
            if (x < allKLine.size()) {
                    return DateFormatTool.getUTCDateForAMPMFormat(allKLine.get(x).getOpenTime());
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

    public String getKLineData(float x) {
        if (allKLine == null || allKLine.size() == 0) {
            return getFormattedValue(x);
        } else {
            int value = (int) x;
            KLineBean kLineBean = getKLineBean(value);
            if (kLineBean != null) {
                return "Open:" + kLineBean.getOpen() +
                        "\nClose:" + kLineBean.getClose() +
                        "\nHigh:" + kLineBean.getHigh() +
                        "\nLow:" + kLineBean.getLow() +
                        "\nVolume:" + kLineBean.getVolume();
            } else {
                return getFormattedValue(x);

            }
        }

    }

    public KLineBean getKLineBean(int index) {
        if (index < allKLine.size()) {
            return allKLine.get(index);
        }
        return null;
    }
}
