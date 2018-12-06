package bcaasc.io.chartdemo.presenter;

import bcaasc.io.chartdemo.bean.DetailOfCoinMarketCap;
import bcaasc.io.chartdemo.bean.ListOfCoinMarketCap;
import bcaasc.io.chartdemo.contract.LineOfCoinMarketCapContract;
import bcaasc.io.chartdemo.interactor.BcaasCInteractor;
import bcaasc.io.chartdemo.tool.LogTool;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/5
 * <p>
 * CoinMarketCap
 */
public class LineChartOfCoinMarketCapPresenterImp implements LineOfCoinMarketCapContract.Presenter {

    private String TAG = LineChartOfCoinMarketCapPresenterImp.class.getSimpleName();

    private LineOfCoinMarketCapContract.View view;
    private BcaasCInteractor interactor;
    private Disposable getListOfCurrencyDisposable;
    private Disposable getDetailDisposable;

    public LineChartOfCoinMarketCapPresenterImp(LineOfCoinMarketCapContract.View view) {
        super();
        this.view = view;
        interactor = new BcaasCInteractor();
    }

    @Override
    public void getLineOfCoinMarketCap(String coinName, String startTime, String endTime) {
        interactor.getLineOfCoinMarketCap(coinName, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailOfCoinMarketCap>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   getDetailDisposable = d;

                               }

                               @Override
                               public void onNext(DetailOfCoinMarketCap detailOfCoinMarketCap) {
                                   view.getLineSuccess(detailOfCoinMarketCap);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   view.getLineFailure(e.getCause().toString());
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
//                new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        getDetailDisposable = d;
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        LogTool.d(TAG, "getLineOfCoinMarketCap response:" + s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                }
                );
    }

    @Override
    public void getLists() {
        interactor.getListOfCurrency()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListOfCoinMarketCap>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                                   getListOfCurrencyDisposable = d;
                               }

                               @Override
                               public void onNext(ListOfCoinMarketCap listOfCoinMarketCap) {
                                   view.getListSuccess(listOfCoinMarketCap);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   view.getListFailure(e.getCause().toString());
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
//                new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        getListOfCurrencyDisposable = d;
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        LogTool.d(TAG, "getLists response:" + s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                }
                );
    }
}
