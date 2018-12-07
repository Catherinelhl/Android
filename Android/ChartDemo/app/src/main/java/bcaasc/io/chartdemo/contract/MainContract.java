package bcaasc.io.chartdemo.contract;

import bcaasc.io.chartdemo.bean.FilterCurrencyListBean;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/23
 * <p>
 * CoinMarketCap 獲取幣種列表
 */
public interface MainContract {

    interface View {
        void getCompleteCurrencyListSuccess(List<FilterCurrencyListBean> filterCurrencyListBean);

        void getCompleteCurrencyListFailure(String failureInfo);

    }

    interface Presenter {

        void getCompleteCurrencyLists();
    }
}
