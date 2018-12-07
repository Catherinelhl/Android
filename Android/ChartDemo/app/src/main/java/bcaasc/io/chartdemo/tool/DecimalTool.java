package bcaasc.io.chartdemo.tool;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/7
 */
public class DecimalTool {

    /**
     * 轉換成顯示每三位數加逗號，小數只顯示八位
     *
     * @param decimal
     */
    public static String transferDisplay(String decimal) {
        if (TextUtils.isEmpty(decimal)) {
            decimal = "0";
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        BigDecimal bigDecimal = new BigDecimal(decimal).setScale(8, RoundingMode.FLOOR);

        return decimalFormat.format(bigDecimal);
    }
}
