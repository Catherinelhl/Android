package bcaasc.io.chartdemo.interactor;

import android.text.TextUtils;
import bcaasc.io.chartdemo.bean.CompleteCurrencyBean;
import bcaasc.io.chartdemo.bean.DetailOfCoinMarketCap;
import bcaasc.io.chartdemo.bean.ListOfCoinMarketCap;
import bcaasc.io.chartdemo.constants.Constants;
import bcaasc.io.chartdemo.http.HttpApi;
import bcaasc.io.chartdemo.http.retrofit.RetrofitFactory;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/13
 * <p>
 * 获取KLine数据
 */
public class BcaasCInteractor {

    public void getKLine(String symbol, String interval, long startTime, long endTime, int limit, Callback<String> callBackListener) {
        HttpApi httpApi = RetrofitFactory.getInstance().create(HttpApi.class);
        Call<String> call = httpApi.getKLines(symbol, interval, startTime, endTime);
        call.enqueue(callBackListener);
    }

    public void getKLine(String symbol, String interval, Callback<String> callBackListener) {
        HttpApi httpApi = RetrofitFactory.getInstance().create(HttpApi.class);
        Call<String> call = httpApi.getKLines(symbol, interval);
        call.enqueue(callBackListener);
    }

    //-------------CoinMarketCap--------------

    /**
     * 获取市场的所有列表信息
     */
    public Observable<ListOfCoinMarketCap> getListOfCurrency() {
        HttpApi httpApi = RetrofitFactory.getInstance().create(HttpApi.class);
        return httpApi.getCurrencyList();
    }

    /**
     * 获取市场的所有列表更完整餓信息
     */
    public Observable<CompleteCurrencyBean> getCompleteListOfCurrency() {
        HttpApi httpApi = RetrofitFactory.getInstance().create(HttpApi.class);
        return httpApi.getCompleteCurrencyList(Constants.APIKEY);
    }

    /**
     * 根据传入的coinName得到响应的信息
     *
     * @param coinName
     * @param startTime
     * @param endTime
     */
    public Observable<DetailOfCoinMarketCap> getLineOfCoinMarketCap(String coinName, String startTime, String endTime) {
        HttpApi httpApi = RetrofitFactory.getInstance().create(HttpApi.class);
        if (TextUtils.isEmpty(startTime)||TextUtils.isEmpty(endTime)){
            return httpApi.getDetailOfCurrency(coinName);
        }
        return httpApi.getDetailOfCurrency(coinName, startTime, endTime);
    }


}
