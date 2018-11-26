package bcaasc.io.chartdemo.http;

import bcaasc.io.chartdemo.http.retrofit.RetrofitFactory;
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
        Call<String> call = httpApi.getKLines(symbol, interval,startTime,endTime);
        call.enqueue(callBackListener);
    }
    public void getKLine(String symbol, String interval, Callback<String> callBackListener) {
        HttpApi httpApi = RetrofitFactory.getInstance().create(HttpApi.class);
        Call<String> call = httpApi.getKLines(symbol, interval);
        call.enqueue(callBackListener);
    }
}
