# TradingPanelBrokerDataBridgeApi

All URIs are relative to *https://localhost/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**historyGet**](TradingPanelBrokerDataBridgeApi.md#historyGet) | **GET** /trading/history | 
[**streamingGet**](TradingPanelBrokerDataBridgeApi.md#streamingGet) | **GET** /trading/streaming | 
[**symbolInfoGet**](TradingPanelBrokerDataBridgeApi.md#symbolInfoGet) | **GET** /trading/symbol_info | 


<a name="historyGet"></a>
# **historyGet**
> BarsArrays historyGet(symbol, resolution, from, to, countback)



Bars request. You can find examples in the [documentation](https://github.com/tradingview/charting_library/wiki/UDF#bars).

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelBrokerDataBridgeApi;

TradingPanelBrokerDataBridgeApi apiInstance = new TradingPanelBrokerDataBridgeApi();
String symbol = "symbol_example"; // String | Symbol name or ticker
String resolution = "resolution_example"; // String | Symbol resolution. Possible resolutions are daily (`1D`, `2D` ... ), weekly (`1W`, `2W` ...), monthly (`1M`, `2M`...) and an intra-day resolution &amp;ndash; minutes(`1`, `2` ...).
Double from = 3.4D; // Double | Unix timestamp (UTC) of the leftmost required bar, including `from`.
Double to = 3.4D; // Double | Unix timestamp (UTC) of the rightmost required bar, including `to`.
Double countback = 3.4D; // Double | Number of bars (higher priority than `from`) starting with `to`. If `countback` is set, `from` should be ignorred. It is used only by tradingview.com, Trading Terminal will never use it.
try {
    BarsArrays result = apiInstance.historyGet(symbol, resolution, from, to, countback);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelBrokerDataBridgeApi#historyGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **symbol** | **String**| Symbol name or ticker |
 **resolution** | **String**| Symbol resolution. Possible resolutions are daily (&#x60;1D&#x60;, &#x60;2D&#x60; ... ), weekly (&#x60;1W&#x60;, &#x60;2W&#x60; ...), monthly (&#x60;1M&#x60;, &#x60;2M&#x60;...) and an intra-day resolution &amp;amp;ndash; minutes(&#x60;1&#x60;, &#x60;2&#x60; ...). |
 **from** | **Double**| Unix timestamp (UTC) of the leftmost required bar, including &#x60;from&#x60;. |
 **to** | **Double**| Unix timestamp (UTC) of the rightmost required bar, including &#x60;to&#x60;. |
 **countback** | **Double**| Number of bars (higher priority than &#x60;from&#x60;) starting with &#x60;to&#x60;. If &#x60;countback&#x60; is set, &#x60;from&#x60; should be ignorred. It is used only by tradingview.com, Trading Terminal will never use it. | [optional]

### Return type

[**BarsArrays**](BarsArrays.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="streamingGet"></a>
# **streamingGet**
> InlineResponse20014 streamingGet()



Stream of prices. Server constantly keeps the connection alive. If the connection is broken the server constantly tries to restore it. Transfer mode is &#39;chunked encoding&#39;. The data feed should set &#39;Transfer-Encoding: chunked&#39; and make sure that all intermediate proxies are set to use this mode. All messages are finished with &#39;\\\\n&#39;. Streaming data should contain real-time only. It shouldn&#39;t contain snapshots of data.

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelBrokerDataBridgeApi;

TradingPanelBrokerDataBridgeApi apiInstance = new TradingPanelBrokerDataBridgeApi();
try {
    InlineResponse20014 result = apiInstance.streamingGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelBrokerDataBridgeApi#streamingGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**InlineResponse20014**](InlineResponse20014.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="symbolInfoGet"></a>
# **symbolInfoGet**
> SymbolInfoArrays symbolInfoGet()



Get a list of all instruments

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelBrokerDataBridgeApi;

TradingPanelBrokerDataBridgeApi apiInstance = new TradingPanelBrokerDataBridgeApi();
try {
    SymbolInfoArrays result = apiInstance.symbolInfoGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelBrokerDataBridgeApi#symbolInfoGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SymbolInfoArrays**](SymbolInfoArrays.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

