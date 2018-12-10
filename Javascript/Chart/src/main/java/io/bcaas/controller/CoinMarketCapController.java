package io.bcaas.controller;

import io.bcaas.constants.Constants;
import io.bcaas.services.CoinMarketCapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoinMarketCapController {

    private CoinMarketCapService coinMarketCapService = new CoinMarketCapService();

    /**
     * 获取当前市场所有主流货币以及市场价值
     * @return
     */
    @PostMapping(value = "/getCoin")
    public String getCoin() {
        //返回jsonStr
        String response = coinMarketCapService.getCryptoCurrency();

        return response;
    }

    /**
     * 获取某货币某时间段的价格、市值、交易量
     *
     * @param coinName website_slug
     * @return
     */
    @PostMapping(value = "/getCurrencies")
    public String getCurrencies(@RequestParam("coinName") String coinName,
                                String startTime,
                                String endTime) {
        Constants.LOGGER_INFO.info("getCurrencies coinname:" + coinName);

        //非空拍断
        if(StringUtils.isEmpty(coinName) ||
                StringUtils.isEmpty(startTime) ||
                StringUtils.isEmpty(endTime)){
            coinName = "bitcoin";
            startTime = "";
            endTime = "";
        }

        //返回jsonStr
//        String response = coinMarketCapService.getCurrenciesApi(startTime,endTime);
        String response = coinMarketCapService.getCurrencies(coinName,startTime,endTime);

        return response;
    }
}
