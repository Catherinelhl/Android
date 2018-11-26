package bcaasc.io.chartdemo.contract;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/23
 */
public interface BcaasCChartContract {

    interface View {
        void getKLineSuccess(List<List<String>> kLineData);

        void getKLineFailure(String failureInfo);
    }

    interface Presenter {
        void getKLine();
    }
}
