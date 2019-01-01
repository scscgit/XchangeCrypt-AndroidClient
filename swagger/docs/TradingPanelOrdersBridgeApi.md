# TradingPanelOrdersBridgeApi

All URIs are relative to *https://localhost/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**accountsAccountIdExecutionsGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdExecutionsGet) | **GET** /trading/accounts/{accountId}/executions | 
[**accountsAccountIdInstrumentsGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdInstrumentsGet) | **GET** /trading/accounts/{accountId}/instruments | 
[**accountsAccountIdOrdersGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersGet) | **GET** /trading/accounts/{accountId}/orders | 
[**accountsAccountIdOrdersHistoryGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersHistoryGet) | **GET** /trading/accounts/{accountId}/ordersHistory | 
[**accountsAccountIdOrdersOrderIdDelete**](TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersOrderIdDelete) | **DELETE** /trading/accounts/{accountId}/orders/{orderId} | 
[**accountsAccountIdOrdersOrderIdGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersOrderIdGet) | **GET** /trading/accounts/{accountId}/orders/{orderId} | 
[**accountsAccountIdOrdersOrderIdPut**](TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersOrderIdPut) | **PUT** /trading/accounts/{accountId}/orders/{orderId} | 
[**accountsAccountIdOrdersPost**](TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersPost) | **POST** /trading/accounts/{accountId}/orders | 
[**accountsAccountIdPositionsGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsGet) | **GET** /trading/accounts/{accountId}/positions | 
[**accountsAccountIdPositionsPositionIdDelete**](TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsPositionIdDelete) | **DELETE** /trading/accounts/{accountId}/positions/{positionId} | 
[**accountsAccountIdPositionsPositionIdGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsPositionIdGet) | **GET** /trading/accounts/{accountId}/positions/{positionId} | 
[**accountsAccountIdPositionsPositionIdPut**](TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsPositionIdPut) | **PUT** /trading/accounts/{accountId}/positions/{positionId} | 
[**accountsAccountIdStateGet**](TradingPanelOrdersBridgeApi.md#accountsAccountIdStateGet) | **GET** /trading/accounts/{accountId}/state | 
[**accountsGet**](TradingPanelOrdersBridgeApi.md#accountsGet) | **GET** /trading/accounts | 
[**authorizePost**](TradingPanelOrdersBridgeApi.md#authorizePost) | **POST** /trading/authorize | 
[**configGet**](TradingPanelOrdersBridgeApi.md#configGet) | **GET** /trading/config | 
[**depthGet**](TradingPanelOrdersBridgeApi.md#depthGet) | **GET** /trading/depth | 
[**mappingGet**](TradingPanelOrdersBridgeApi.md#mappingGet) | **GET** /trading/mapping | 
[**quotesGet**](TradingPanelOrdersBridgeApi.md#quotesGet) | **GET** /trading/quotes | 


<a name="accountsAccountIdExecutionsGet"></a>
# **accountsAccountIdExecutionsGet**
> InlineResponse20010 accountsAccountIdExecutionsGet(accountId, instrument, maxCount)



Get a list of executions (i.e. fills or trades) for an account and an instrument. Executions are displayed on a chart

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String instrument = "instrument_example"; // String | Broker instrument name
Integer maxCount = 56; // Integer | Maximum count of executions to return
try {
    InlineResponse20010 result = apiInstance.accountsAccountIdExecutionsGet(accountId, instrument, maxCount);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdExecutionsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **instrument** | **String**| Broker instrument name |
 **maxCount** | **Integer**| Maximum count of executions to return | [optional]

### Return type

[**InlineResponse20010**](InlineResponse20010.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdInstrumentsGet"></a>
# **accountsAccountIdInstrumentsGet**
> InlineResponse20011 accountsAccountIdInstrumentsGet(accountId)



Get a list of tradeable instruments that are available for trading with the account specified

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
try {
    InlineResponse20011 result = apiInstance.accountsAccountIdInstrumentsGet(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdInstrumentsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |

### Return type

[**InlineResponse20011**](InlineResponse20011.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdOrdersGet"></a>
# **accountsAccountIdOrdersGet**
> InlineResponse2004 accountsAccountIdOrdersGet(accountId)



Get pending orders for an account.

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
try {
    InlineResponse2004 result = apiInstance.accountsAccountIdOrdersGet(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdOrdersGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |

### Return type

[**InlineResponse2004**](InlineResponse2004.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdOrdersHistoryGet"></a>
# **accountsAccountIdOrdersHistoryGet**
> InlineResponse2004 accountsAccountIdOrdersHistoryGet(accountId, maxCount)



Get order history for an account. It is expected that returned orders will have a final status (rejected, filled, cancelled). This request is optional. If you don&#39;t support history of orders set &#x60;AccountFlags::supportOrdersHistory&#x60; to &#x60;false&#x60;.

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
Double maxCount = 3.4D; // Double | Maximum amount of orders to return
try {
    InlineResponse2004 result = apiInstance.accountsAccountIdOrdersHistoryGet(accountId, maxCount);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdOrdersHistoryGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **maxCount** | **Double**| Maximum amount of orders to return | [optional]

### Return type

[**InlineResponse2004**](InlineResponse2004.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdOrdersOrderIdDelete"></a>
# **accountsAccountIdOrdersOrderIdDelete**
> InlineResponse2007 accountsAccountIdOrdersOrderIdDelete(accountId, orderId)



Cancel an existing order

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String orderId = "orderId_example"; // String | Order ID
try {
    InlineResponse2007 result = apiInstance.accountsAccountIdOrdersOrderIdDelete(accountId, orderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdOrdersOrderIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **orderId** | **String**| Order ID |

### Return type

[**InlineResponse2007**](InlineResponse2007.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdOrdersOrderIdGet"></a>
# **accountsAccountIdOrdersOrderIdGet**
> InlineResponse2006 accountsAccountIdOrdersOrderIdGet(accountId, orderId)



Get an order for an account. It can be an active or historical order.

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String orderId = "orderId_example"; // String | Order ID
try {
    InlineResponse2006 result = apiInstance.accountsAccountIdOrdersOrderIdGet(accountId, orderId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdOrdersOrderIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **orderId** | **String**| Order ID |

### Return type

[**InlineResponse2006**](InlineResponse2006.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdOrdersOrderIdPut"></a>
# **accountsAccountIdOrdersOrderIdPut**
> InlineResponse2007 accountsAccountIdOrdersOrderIdPut(accountId, orderId, qty, limitPrice, stopPrice, stopLoss, takeProfit, digitalSignature)



Modify an existing order

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String orderId = "orderId_example"; // String | Order ID
Double qty = 3.4D; // Double | Number of units
Double limitPrice = 3.4D; // Double | Limit Price for Limit or StopLimit order
Double stopPrice = 3.4D; // Double | Stop Price for Stop or StopLimit order
Double stopLoss = 3.4D; // Double | StopLoss price (if supported)
Double takeProfit = 3.4D; // Double | TakeProfit price (if supported)
String digitalSignature = "digitalSignature_example"; // String | Digital signature (if supported)
try {
    InlineResponse2007 result = apiInstance.accountsAccountIdOrdersOrderIdPut(accountId, orderId, qty, limitPrice, stopPrice, stopLoss, takeProfit, digitalSignature);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdOrdersOrderIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **orderId** | **String**| Order ID |
 **qty** | **Double**| Number of units |
 **limitPrice** | **Double**| Limit Price for Limit or StopLimit order | [optional]
 **stopPrice** | **Double**| Stop Price for Stop or StopLimit order | [optional]
 **stopLoss** | **Double**| StopLoss price (if supported) | [optional]
 **takeProfit** | **Double**| TakeProfit price (if supported) | [optional]
 **digitalSignature** | **String**| Digital signature (if supported) | [optional]

### Return type

[**InlineResponse2007**](InlineResponse2007.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdOrdersPost"></a>
# **accountsAccountIdOrdersPost**
> InlineResponse2005 accountsAccountIdOrdersPost(accountId, instrument, qty, side, type, limitPrice, stopPrice, durationType, durationDateTime, stopLoss, takeProfit, digitalSignature, requestId)



Create a new order

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String instrument = "instrument_example"; // String | Instrument to open the order on
Double qty = 3.4D; // Double | The number of units to open order for
String side = "side_example"; // String | Side. Possible values &amp;ndash; `buy` and `sell`.
String type = "type_example"; // String | Type. Possible values &amp;ndash; `market`, `stop`, `limit`, `stoplimit`.
Double limitPrice = 3.4D; // Double | Limit Price for Limit or StopLimit order
Double stopPrice = 3.4D; // Double | Stop Price for Stop or StopLimit order
String durationType = "durationType_example"; // String | Duration ID (if supported)
Double durationDateTime = 3.4D; // Double | Expiration datetime UNIX timestamp (if supported by duration type)
Double stopLoss = 3.4D; // Double | StopLoss price (if supported)
Double takeProfit = 3.4D; // Double | TakeProfit price (if supported)
String digitalSignature = "digitalSignature_example"; // String | Digital signature (if supported)
String requestId = "requestId_example"; // String | Unique identifier for a request
try {
    InlineResponse2005 result = apiInstance.accountsAccountIdOrdersPost(accountId, instrument, qty, side, type, limitPrice, stopPrice, durationType, durationDateTime, stopLoss, takeProfit, digitalSignature, requestId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdOrdersPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **instrument** | **String**| Instrument to open the order on |
 **qty** | **Double**| The number of units to open order for |
 **side** | **String**| Side. Possible values &amp;amp;ndash; &#x60;buy&#x60; and &#x60;sell&#x60;. |
 **type** | **String**| Type. Possible values &amp;amp;ndash; &#x60;market&#x60;, &#x60;stop&#x60;, &#x60;limit&#x60;, &#x60;stoplimit&#x60;. |
 **limitPrice** | **Double**| Limit Price for Limit or StopLimit order | [optional]
 **stopPrice** | **Double**| Stop Price for Stop or StopLimit order | [optional]
 **durationType** | **String**| Duration ID (if supported) | [optional]
 **durationDateTime** | **Double**| Expiration datetime UNIX timestamp (if supported by duration type) | [optional]
 **stopLoss** | **Double**| StopLoss price (if supported) | [optional]
 **takeProfit** | **Double**| TakeProfit price (if supported) | [optional]
 **digitalSignature** | **String**| Digital signature (if supported) | [optional]
 **requestId** | **String**| Unique identifier for a request | [optional]

### Return type

[**InlineResponse2005**](InlineResponse2005.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdPositionsGet"></a>
# **accountsAccountIdPositionsGet**
> InlineResponse2008 accountsAccountIdPositionsGet(accountId)



Get positions for an account

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
try {
    InlineResponse2008 result = apiInstance.accountsAccountIdPositionsGet(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdPositionsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |

### Return type

[**InlineResponse2008**](InlineResponse2008.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdPositionsPositionIdDelete"></a>
# **accountsAccountIdPositionsPositionIdDelete**
> InlineResponse2007 accountsAccountIdPositionsPositionIdDelete(accountId, positionId)



Close an existing position

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String positionId = "positionId_example"; // String | Position ID
try {
    InlineResponse2007 result = apiInstance.accountsAccountIdPositionsPositionIdDelete(accountId, positionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdPositionsPositionIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **positionId** | **String**| Position ID |

### Return type

[**InlineResponse2007**](InlineResponse2007.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdPositionsPositionIdGet"></a>
# **accountsAccountIdPositionsPositionIdGet**
> InlineResponse2009 accountsAccountIdPositionsPositionIdGet(accountId, positionId)



Get a position for an account

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String positionId = "positionId_example"; // String | Position ID
try {
    InlineResponse2009 result = apiInstance.accountsAccountIdPositionsPositionIdGet(accountId, positionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdPositionsPositionIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **positionId** | **String**| Position ID |

### Return type

[**InlineResponse2009**](InlineResponse2009.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdPositionsPositionIdPut"></a>
# **accountsAccountIdPositionsPositionIdPut**
> InlineResponse2007 accountsAccountIdPositionsPositionIdPut(accountId, positionId, stopLoss, takeProfit)



Modify an existing position stop loss or take profit or both

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String positionId = "positionId_example"; // String | Position ID
Double stopLoss = 3.4D; // Double | StopLoss price
Double takeProfit = 3.4D; // Double | TakeProfit price
try {
    InlineResponse2007 result = apiInstance.accountsAccountIdPositionsPositionIdPut(accountId, positionId, stopLoss, takeProfit);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdPositionsPositionIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **positionId** | **String**| Position ID |
 **stopLoss** | **Double**| StopLoss price | [optional]
 **takeProfit** | **Double**| TakeProfit price | [optional]

### Return type

[**InlineResponse2007**](InlineResponse2007.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: text/plain, application/json, text/json

<a name="accountsAccountIdStateGet"></a>
# **accountsAccountIdStateGet**
> InlineResponse2003 accountsAccountIdStateGet(accountId, locale)



Get account information.

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String accountId = "accountId_example"; // String | The account identifier
String locale = "locale_example"; // String | Locale (language) id
try {
    InlineResponse2003 result = apiInstance.accountsAccountIdStateGet(accountId, locale);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsAccountIdStateGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier |
 **locale** | **String**| Locale (language) id |

### Return type

[**InlineResponse2003**](InlineResponse2003.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="accountsGet"></a>
# **accountsGet**
> InlineResponse2002 accountsGet()



Get a list of accounts owned by the user

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
try {
    InlineResponse2002 result = apiInstance.accountsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#accountsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**InlineResponse2002**](InlineResponse2002.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="authorizePost"></a>
# **authorizePost**
> InlineResponse200 authorizePost(login, password)



Oauth2 Password authorization

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String login = "login_example"; // String | User Login
String password = "password_example"; // String | User Password
try {
    InlineResponse200 result = apiInstance.authorizePost(login, password);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#authorizePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **login** | **String**| User Login |
 **password** | **String**| User Password |

### Return type

[**InlineResponse200**](InlineResponse200.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: text/plain, application/json, text/json

<a name="configGet"></a>
# **configGet**
> InlineResponse2001 configGet(locale)



Get localized configuration

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String locale = "locale_example"; // String | Locale (language) id
try {
    InlineResponse2001 result = apiInstance.configGet(locale);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#configGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **locale** | **String**| Locale (language) id |

### Return type

[**InlineResponse2001**](InlineResponse2001.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="depthGet"></a>
# **depthGet**
> InlineResponse20013 depthGet(symbol)



Get current depth of market for the instrument. Optional.

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String symbol = "symbol_example"; // String | instrument name
try {
    InlineResponse20013 result = apiInstance.depthGet(symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#depthGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **symbol** | **String**| instrument name |

### Return type

[**InlineResponse20013**](InlineResponse20013.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="mappingGet"></a>
# **mappingGet**
> SymbolMapping mappingGet()



Return all broker instruments with corresponding TradingView instruments. It is required to add a Broker to TradingView.com. It is not required for Trading Terminal integration. This request works without authorization!

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
try {
    SymbolMapping result = apiInstance.mappingGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#mappingGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SymbolMapping**](SymbolMapping.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="quotesGet"></a>
# **quotesGet**
> InlineResponse20012 quotesGet(symbols)



Get current prices of the instrument. You can see an example of this response [here](https://demo_feed.tradingview.com/quotes?symbols&#x3D;AAPL%2CMSFT%2CIBM%2CNasdaqNM%3AAAPL).

### Example
```java
// Import classes:
//import io.swagger.client.api.TradingPanelOrdersBridgeApi;

TradingPanelOrdersBridgeApi apiInstance = new TradingPanelOrdersBridgeApi();
String symbols = "symbols_example"; // String | comma separated symbols
try {
    InlineResponse20012 result = apiInstance.quotesGet(symbols);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TradingPanelOrdersBridgeApi#quotesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **symbols** | **String**| comma separated symbols |

### Return type

[**InlineResponse20012**](InlineResponse20012.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

