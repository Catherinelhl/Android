package bcaasc.io.chartdemo.presenter;

import bcaasc.io.chartdemo.bean.CompleteCurrencyBean;
import bcaasc.io.chartdemo.bean.FilterCurrencyListBean;
import bcaasc.io.chartdemo.contract.MainContract;
import bcaasc.io.chartdemo.interactor.BcaasCInteractor;
import bcaasc.io.chartdemo.tool.LogTool;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/7
 */
public class MainPresenterImp implements MainContract.Presenter {

    private String TAG = MainPresenterImp.class.getSimpleName();

    private MainContract.View view;
    private BcaasCInteractor interactor;

    public MainPresenterImp(MainContract.View view) {
        super();
        this.view = view;
        interactor = new BcaasCInteractor();
    }

    @Override
    public void getCompleteCurrencyLists() {
        interactor.getCompleteListOfCurrency()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompleteCurrencyBean>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(CompleteCurrencyBean completeCurrencyBean) {
                                   if (completeCurrencyBean != null) {
                                       List<CompleteCurrencyBean.Data> data = completeCurrencyBean.getData();
                                       if (data != null && data.size() > 0) {
                                           List<FilterCurrencyListBean> filterCurrencyListBeans = new ArrayList<>();
                                           for (CompleteCurrencyBean.Data data1 : data) {
                                               String name = data1.getName();
                                               String slug=data1.getSlug();
                                               float value = data1.getQuote().getUSD().getPrice();
                                               float marketValue = data1.getQuote().getUSD().getMarket_cap();
                                               FilterCurrencyListBean filterCurrencyListBean = new FilterCurrencyListBean(name, value, marketValue,slug);
                                               filterCurrencyListBeans.add(filterCurrencyListBean);


                                           }

                                           if (filterCurrencyListBeans != null && filterCurrencyListBeans.size() > 0) {
                                               view.getCompleteCurrencyListSuccess(filterCurrencyListBeans);
                                           } else {
                                               //failure
                                               view.getCompleteCurrencyListFailure("");

                                           }
                                       } else {
                                           //failure
                                           view.getCompleteCurrencyListFailure("");

                                       }
                                   } else {
                                       //failure
                                       view.getCompleteCurrencyListFailure("");

                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   LogTool.e(TAG, e.getCause().toString());
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
