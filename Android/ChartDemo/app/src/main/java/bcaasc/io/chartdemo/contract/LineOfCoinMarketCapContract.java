package bcaasc.io.chartdemo.contract;

import bcaasc.io.chartdemo.bean.DetailOfCoinMarketCap;
import bcaasc.io.chartdemo.bean.ListOfCoinMarketCap;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/23
 * <p>
 * CoinMarketCap
 */
public interface LineOfCoinMarketCapContract {

    interface View {
        void getLineSuccess(DetailOfCoinMarketCap detailOfCoinMarketCap);

        void getLineFailure(String failureInfo);

        void getListSuccess(ListOfCoinMarketCap listOfCoinMarketCap);
        void getListFailure(String info);
    }

    interface Presenter {
        void getLineOfCoinMarketCap(String coinName, String startTime, String endTime);

        void getLists();
    }
}
