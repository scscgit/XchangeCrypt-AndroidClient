# TradingTerminalIntegrationApi

All URIs are relative to *https://localhost/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**marksGet**](TradingTerminalIntegrationApi.md#marksGet) | **GET** /trading/marks | 
[**timescaleMarksGet**](TradingTerminalIntegrationApi.md#timescaleMarksGet) | **GET** /trading/timescale_marks | 


<a name="marksGet"></a>
# **marksGet**
> MarksArrays marksGet(symbol, resolution, from, to)



Request for bar marks (circles on top of bars). You can display custom marks only in the Trading Terminal

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingTerminalIntegrationApi;

TradingTerminalIntegrationApi apiInstance = new TradingTerminalIntegrationApi();
String symbol = "symbol_example"; // String | Symbol name or ticker
String resolution = "resolution_example"; // String | Symbol resolution. Possible resolutions are daily (`1D`, `2D` ... ), weekly (`1W`, `2W` ...), monthly (`1M`, `2M`...) and an intra-day resolution &amp;ndash; minutes(`1`, `2` ...).
Double from = 3.4D; // Double | Unix timestamp (UTC) of the leftmost required bar, including `from`.
Double to = 3.4D; // Double | Unix timestamp (UTC) of the rightmost required bar, including `to`.
try {
    MarksArrays result = apiInstance.marksGet(symbol, resolution, from, to);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingTerminalIntegrationApi#marksGet");
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

### Return type

[**MarksArrays**](MarksArrays.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="timescaleMarksGet"></a>
# **timescaleMarksGet**
> List&lt;TimescaleMark&gt; timescaleMarksGet(symbol, resolution, from, to)



Request for timescale marks (circles on the timescale). You can display custom marks only in the Trading Terminal

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingTerminalIntegrationApi;

TradingTerminalIntegrationApi apiInstance = new TradingTerminalIntegrationApi();
String symbol = "symbol_example"; // String | Symbol name or ticker
String resolution = "resolution_example"; // String | Symbol resolution. Possible resolutions are daily (`1D`, `2D` ... ), weekly (`1W`, `2W` ...), monthly (`1M`, `2M`...) and an intra-day resolution &amp;ndash; minutes(`1`, `2` ...).
Double from = 3.4D; // Double | Unix timestamp (UTC) of the leftmost required bar, including `from`.
Double to = 3.4D; // Double | Unix timestamp (UTC) of the rightmost required bar, including `to`.
try {
    List<TimescaleMark> result = apiInstance.timescaleMarksGet(symbol, resolution, from, to);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingTerminalIntegrationApi#timescaleMarksGet");
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

### Return type

[**List&lt;TimescaleMark&gt;**](TimescaleMark.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

