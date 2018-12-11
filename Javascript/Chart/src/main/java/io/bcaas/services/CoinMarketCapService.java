package io.bcaas.services;

import io.bcaas.api.HttpClient;
import io.bcaas.constants.APIURLConstants;
import org.json.JSONObject;

public class CoinMarketCapService {

    //HTTP 请求工具类
    HttpClient httpClient = new HttpClient();

    //获取某个币在某段时间的价格、市值、交易量信息
    public String getCurrencies(String coinName, String startTime, String endTime) {

        //拼接请求Url
        String apiUrl = APIURLConstants.API_CURRENCIES_URL + "/" + coinName + "/" + startTime + "/" + endTime;
        //HTTP发起请求
        String responseStr = httpClient.get(apiUrl);
        try {

            //解析返回的JSON
            JSONObject responseJson = new JSONObject(responseStr);

            //返回json格式
            return responseJson.toString();
        } catch (Exception e) {
            return responseErrorJson();
        }
    }

    //获取某个币在某段时间的价格、市值、交易量信息（不需要coinName）
    public String getCurrenciesApi(String startTime, String endTime) {

        //拼接请求Url
        String apiUrl = APIURLConstants.API_CURRENCIESAPI_URL + "/" + startTime + "/" + endTime;
        //HTTP发起请求
        String responseStr = httpClient.get(apiUrl);
        try {

            //解析返回的JSON
            JSONObject responseJson = new JSONObject(responseStr);

            //返回json格式
            return responseJson.toString();
        } catch (Exception e) {
            return responseErrorJson();
        }
    }

    //获取加密货币列表（不需coinName）
    public String getCryptoCurrency() {

        //拼接请求Url
        String apiUrl = APIURLConstants.API_CRYPTOCURRENCY_URL;
        String headerKey = "X-CMC_PRO_API_KEY";
        String headerValue = "280c9a41-9dc2-496a-8a6c-4b4063be9345";
        //HTTP发起请求
        String responseStr = httpClient.get(apiUrl,headerKey,headerValue);
        try {

            //解析返回的JSON,获取余额
            JSONObject responseJson = new JSONObject(responseStr);

            //返回json格式
            return responseJson.toString();
        } catch (Exception e) {
            return responseErrorJson();
        }
    }



    //统一返回json格式
    public String responseErrorJson() {
        return new JSONObject().put("error", "Exception").toString();
    }
}
