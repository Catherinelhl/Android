package bcaasc.io.chartdemo.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/7
 * 過濾後的幣種信息
 */
public class FilterCurrencyListBean implements Serializable {

    /* 貨幣名字*/
    private String currencyName;
    /*貨幣當前價值*/
    private float currencyValue;
    /*貨幣市場總價值*/
    private float currencyMarketTotalValue;
    /*貨幣 slug*/
    private String currencySlug;


    public FilterCurrencyListBean(String currencyName, float currencyValue, float currencyMarketTotalValue, String currencySlug) {
        super();
        this.currencyName = currencyName;
        this.currencyValue = currencyValue;
        this.currencyMarketTotalValue = currencyMarketTotalValue;
        this.currencySlug = currencySlug;
    }

    public String getCurrencySlug() {
        return currencySlug;
    }

    public void setCurrencySlug(String currencySlug) {
        this.currencySlug = currencySlug;
    }


    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public float getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(float currencyValue) {
        this.currencyValue = currencyValue;
    }

    public float getCurrencyMarketTotalValue() {
        return currencyMarketTotalValue;
    }

    public void setCurrencyMarketTotalValue(float currencyMarketTotalValue) {
        this.currencyMarketTotalValue = currencyMarketTotalValue;
    }

    @Override
    public String toString() {
        return "FilterCurrencyListBean{" +
                "currencyName='" + currencyName + '\'' +
                ", currencyValue=" + currencyValue +
                ", currencyMarketTotalValue=" + currencyMarketTotalValue +
                ", currencySlug='" + currencySlug + '\'' +
                '}';
    }
}
