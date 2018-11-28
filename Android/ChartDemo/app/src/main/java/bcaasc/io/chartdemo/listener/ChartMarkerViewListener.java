package bcaasc.io.chartdemo.listener;

import bcaasc.io.chartdemo.bean.KLineBean;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/28
 * <p>
 * 设置一个监听用于监听点击当前chart显示触摸点的信息
 */
public interface ChartMarkerViewListener {
    void getKLineBean(KLineBean kLineBean);
    void getIndex(int index);
}
