package bcaasc.io.chartdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/5
 * 币种的详细信息
 */
public class DetailOfCoinMarketCap implements Serializable {

    // 市值数据
    private List<List<String>> market_cap_by_available_supply;
    // 该货币对应的BTC单价
    private List<List<String>> price_btc;
    // 该货币对应的USD单价
    private List<List<String>> price_usd;
    // 交易量
    private List<List<String>> volume_usd;

    public List<List<String>> getMarket_cap_by_available_supply() {
        return market_cap_by_available_supply;
    }

    public void setMarket_cap_by_available_supply(List<List<String>> market_cap_by_available_supply) {
        this.market_cap_by_available_supply = market_cap_by_available_supply;
    }

    public List<List<String>> getPrice_btc() {
        return price_btc;
    }

    public void setPrice_btc(List<List<String>> price_btc) {
        this.price_btc = price_btc;
    }

    public List<List<String>> getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(List<List<String>> price_usd) {
        this.price_usd = price_usd;
    }

    public List<List<String>> getVolume_usd() {
        return volume_usd;
    }

    public void setVolume_usd(List<List<String>> volume_usd) {
        this.volume_usd = volume_usd;
    }

    @Override
    public String toString() {
        return "DetailOfCoinMarketCap{" +
                "market_cap_by_available_supply=" + market_cap_by_available_supply +
                ", price_btc=" + price_btc +
                ", price_usd=" + price_usd +
                ", volume_usd=" + volume_usd +
                '}';
    }
}
