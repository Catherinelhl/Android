package com.xxmassdeveloper.mpchartexample.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.xxmassdeveloper.mpchartexample.bean.KLineBean;
import com.xxmassdeveloper.mpchartexample.tool.DateFormatTool;
import com.xxmassdeveloper.mpchartexample.tool.LogTool;

import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.Date;
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
            try {
                return DateFormatTool.getUTCDateForAMPMFormat(allKLine.get((int) value).getOpenTime());
            } catch (Exception e) {
                LogTool.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        return mFormat.format(value);
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
//        if (axis instanceof XAxis) {
        if (allKLine == null || allKLine.size() == 0) {
            return mFormat.format(value);
        } else {
            try {
                return DateFormatTool.getUTCDateForAMPMFormat(allKLine.get((int) value).getOpenTime());
            } catch (Exception e) {
                LogTool.e(TAG, e.getMessage());
                e.printStackTrace();
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
        return mFormat.format(value);
    }

    public String getKLineData(float x) {
        if (allKLine == null || allKLine.size() == 0) {
            return getFormattedValue(x);
        } else {
            int value = (int) x;
            KLineBean kLineBean = allKLine.get(value);
            return "Open:" + kLineBean.getOpen() + "\nClose:" + kLineBean.getClose() + "\nHigh:" + kLineBean.getHigh() + "\nLow:" + kLineBean.getLow();
        }

    }
}
