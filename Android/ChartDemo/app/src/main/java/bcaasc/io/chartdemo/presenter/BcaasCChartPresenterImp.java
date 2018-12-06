package bcaasc.io.chartdemo.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import bcaasc.io.chartdemo.contract.BcaasCChartContract;
import bcaasc.io.chartdemo.interactor.BcaasCInteractor;
import bcaasc.io.chartdemo.tool.LogTool;
import org.json.JSONArray;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/23
 */
public class BcaasCChartPresenterImp implements BcaasCChartContract.Presenter {
    private String TAG = BcaasCChartPresenterImp.class.getSimpleName();
    private BcaasCChartContract.View view;

    private BcaasCInteractor interactor;

    public BcaasCChartPresenterImp(BcaasCChartContract.View view) {
        this.view = view;
        interactor = new BcaasCInteractor();
    }

    @Override
    public void getKLine() {
        String symbol = "ETHBTC";
        String interval = "1h";

//        long endTime = System.currentTimeMillis();
//        long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        interactor.getKLine(symbol, interval, new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    List<List<String>> allKLineData = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray jsonArray1 = (JSONArray) jsonArray.get(i);
                        List<String> childContent = new ArrayList<>();
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            childContent.add(String.valueOf(jsonArray1.get(j)));
                        }
                        allKLineData.add(childContent);

                    }
                    view.getKLineSuccess(allKLineData);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogTool.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogTool.e(TAG, t.getMessage());
            }
        });
    }
}
