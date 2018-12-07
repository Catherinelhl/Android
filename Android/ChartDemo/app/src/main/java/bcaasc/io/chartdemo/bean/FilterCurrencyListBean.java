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
    private double currencyValue;
    /*貨幣市場總價值*/
    private double currencyMarketTotalValue;
    /*貨幣 slug*/
    private String currencySlug;
    /*货币Symbol*/
    private String symbol;


    public FilterCurrencyListBean(String currencyName, double currencyValue
            , double currencyMarketTotalValue, String currencySlug,String symbol) {
        super();
        this.currencyName = currencyName;
        this.currencyValue = currencyValue;
        this.currencyMarketTotalValue = currencyMarketTotalValue;
        this.currencySlug = currencySlug;
        this.symbol=symbol;
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

    public double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public double getCurrencyMarketTotalValue() {
        return currencyMarketTotalValue;
    }

    public void setCurrencyMarketTotalValue(double currencyMarketTotalValue) {
        this.currencyMarketTotalValue = currencyMarketTotalValue;
    }

    public void setCurrencyMarketTotalValue(float currencyMarketTotalValue) {
        this.currencyMarketTotalValue = currencyMarketTotalValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "FilterCurrencyListBean{" +
                "currencyName='" + currencyName + '\'' +
                ", currencyValue=" + currencyValue +
                ", currencyMarketTotalValue=" + currencyMarketTotalValue +
                ", currencySlug='" + currencySlug + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
