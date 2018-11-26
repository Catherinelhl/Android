package bcaasc.io.chartdemo.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/11/23
 */
public class KLineBean implements Serializable {
    private String openTime;
    private String open;
    private String High;
    private String Low;
    private String Close;
    private String Volume;
    private String CloseTime;
    private String QuoteAssetVolume;
    private String NumberOfTrades;
    private String TakerBuyBaseAssetVolume;
    private String TakerBuyQuoteAssetVolume;
    private String Ignore;

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        Low = low;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        Close = close;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getCloseTime() {
        return CloseTime;
    }

    public void setCloseTime(String closeTime) {
        CloseTime = closeTime;
    }

    public String getQuoteAssetVolume() {
        return QuoteAssetVolume;
    }

    public void setQuoteAssetVolume(String quoteAssetVolume) {
        QuoteAssetVolume = quoteAssetVolume;
    }

    public String getNumberOfTrades() {
        return NumberOfTrades;
    }

    public void setNumberOfTrades(String numberOfTrades) {
        NumberOfTrades = numberOfTrades;
    }

    public String getTakerBuyBaseAssetVolume() {
        return TakerBuyBaseAssetVolume;
    }

    public void setTakerBuyBaseAssetVolume(String takerBuyBaseAssetVolume) {
        TakerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
    }

    public String getTakerBuyQuoteAssetVolume() {
        return TakerBuyQuoteAssetVolume;
    }

    public void setTakerBuyQuoteAssetVolume(String takerBuyQuoteAssetVolume) {
        TakerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
    }

    public String getIgnore() {
        return Ignore;
    }

    public void setIgnore(String ignore) {
        Ignore = ignore;
    }

    @Override
    public String toString() {
        return "KLineBean{" +
                "openTime='" + openTime + '\'' +
                ", open='" + open + '\'' +
                ", High='" + High + '\'' +
                ", Low='" + Low + '\'' +
                ", Close='" + Close + '\'' +
                ", Volume='" + Volume + '\'' +
                ", CloseTime='" + CloseTime + '\'' +
                ", QuoteAssetVolume='" + QuoteAssetVolume + '\'' +
                ", NumberOfTrades='" + NumberOfTrades + '\'' +
                ", TakerBuyBaseAssetVolume='" + TakerBuyBaseAssetVolume + '\'' +
                ", TakerBuyQuoteAssetVolume='" + TakerBuyQuoteAssetVolume + '\'' +
                ", Ignore='" + Ignore + '\'' +
                '}';
    }
}
