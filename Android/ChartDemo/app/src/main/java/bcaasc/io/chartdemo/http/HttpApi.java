package bcaasc.io.chartdemo.http;

import bcaasc.io.chartdemo.bean.DetailOfCoinMarketCap;
import bcaasc.io.chartdemo.bean.ListOfCoinMarketCap;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpApi {


    /**
     * Binance
     * 获取交易记录
     *
     * @param symbol
     * @param interval
     * @param startTime
     * @param endTime
     * @return
     */
    @GET("/api/v1/klines")
    Call<String> getKLines(@Query("symbol") String symbol, @Query("interval") String interval,
                           @Query("startTime") long startTime, @Query("endTime") long endTime);

    /**
     * 获取交易记录
     * Binance
     *
     * @param symbol
     * @param interval
     * @return
     */
    @GET("/api/v1/klines")
    Call<String> getKLines(@Query("symbol") String symbol, @Query("interval") String interval);


    //--------------------CoinMarketCap----------------------

    /**
     * 获取加密货币列表
     *
     * @return
     */
    @GET("https://api.coinmarketcap.com/v2/listings")
    Observable<ListOfCoinMarketCap> getCurrencyList();


    /**
     * 获取加密货币的价格、市值、交易量（折线图）
     *
     * @param coinName
     * @param startTime
     * @param endTime
     * @return
     */
    @GET("https://graphs2.coinmarketcap.com/currencies/{coinName}/{startTime}/{endTime}")
    Observable<DetailOfCoinMarketCap> getDetailOfCurrency(@Path("coinName") String coinName,
                                                          @Path("startTime") String startTime,
                                                          @Path("endTime") String endTime);

    /**
     * 获取加密货币的价格、市值、交易量（折线图）
     *
     * @param coinName
     * @return
     */
    @GET("https://graphs2.coinmarketcap.com/currencies/{coinName}")
    Observable<DetailOfCoinMarketCap> getDetailOfCurrency(@Path("coinName") String coinName);
}
