package bcaasc.io.chartdemo.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpApi {


    /*获取交易记录*/
    @GET("/api/v1/klines")
    Call<String> getKLines(@Query("symbol") String symbol, @Query("interval") String interval,
                           @Query("startTime") long startTime, @Query("endTime") long endTime);

    /*获取交易记录*/
    @GET("/api/v1/klines")
    Call<String> getKLines(@Query("symbol") String symbol, @Query("interval") String interval);
}
