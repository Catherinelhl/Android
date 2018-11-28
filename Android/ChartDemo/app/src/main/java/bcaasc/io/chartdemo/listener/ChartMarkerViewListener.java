package bcaasc.io.chartdemo.listener;

import bcaasc.io.chartdemo.bean.KLineBean;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/28
 * <p>
 * 设置一个监听用于监听点击当前chart显示触摸点的信息
 */
public interface ChartMarkerViewListener {
    void getKLineBean(Entry entry, Highlight highlight,KLineBean kLineBean);
    void getIndex(int index);
}
