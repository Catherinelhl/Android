package com.xxmassdeveloper.mpchartexample.presenter;

import com.xxmassdeveloper.mpchartexample.contract.BcaasCChartContract;
import com.xxmassdeveloper.mpchartexample.http.BcaasCInteractor;
import com.xxmassdeveloper.mpchartexample.tool.LogTool;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        interactor.getKLine(symbol, interval, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LogTool.d(TAG, response.body());
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    List<List<String>> allKLineData = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        List<String> childContent = (List<String>) jsonArray.get(i);
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
