package bcaasc.io.chartdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bcaasc.io.chartdemo.R;
import bcaasc.io.chartdemo.adapter.CurrencyListAdapter;
import bcaasc.io.chartdemo.bean.FilterCurrencyListBean;
import bcaasc.io.chartdemo.constants.Constants;
import bcaasc.io.chartdemo.contract.MainContract;
import bcaasc.io.chartdemo.listener.ItemSelectorListener;
import bcaasc.io.chartdemo.presenter.MainPresenterImp;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/7
 * 界面顯示Binance入口以及CoinMarketCap 獲取的市場列表
 */
public class MainActivity extends AppCompatActivity implements MainContract.View, ItemSelectorListener {

    @BindView(R.id.btn_binance)
    Button btnBinance;
    @BindView(R.id.rv_currency_list)
    RecyclerView rvCurrencyList;
    private MainContract.Presenter presenter;

    private List<FilterCurrencyListBean> filterCurrencyListBean;
    private CurrencyListAdapter currencyListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initAdapter();
        initListener();
    }

    private void initView() {
        setTitle("MainActivity");
        filterCurrencyListBean = new ArrayList<>();
        presenter = new MainPresenterImp(this);
        presenter.getCompleteCurrencyLists();
    }

    private void initAdapter() {
        currencyListAdapter = new CurrencyListAdapter(this, filterCurrencyListBean);
        currencyListAdapter.setItemSelectorListener(this);
        rvCurrencyList.setHasFixedSize(true);
        rvCurrencyList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCurrencyList.setAdapter(currencyListAdapter);
    }

    private void initListener() {
        RxView.clicks(btnBinance).throttleFirst(Constants.SleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, KLineChartOfBinanceActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getCompleteCurrencyListSuccess(List<FilterCurrencyListBean> filterCurrencyListBean) {
        if (this.filterCurrencyListBean == null) return;
        this.filterCurrencyListBean.clear();
        this.filterCurrencyListBean.addAll(filterCurrencyListBean);
        this.currencyListAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCompleteCurrencyListFailure(String failureInfo) {
        if (this.filterCurrencyListBean == null) return;
        this.filterCurrencyListBean.clear();
    }

    @Override
    public void onItemSelectorListener(int position) {
        if (this.filterCurrencyListBean == null) return;
        FilterCurrencyListBean filterCurrencyListBean = this.filterCurrencyListBean.get(position);
        if (filterCurrencyListBean == null) return;
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LineChartOfCoinMarketCapActivity.class);
        intent.putExtra("name", filterCurrencyListBean.getCurrencySlug());
        startActivity(intent);
    }
}
