# UserBridgeApi

All URIs are relative to *https://localhost/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**profile**](UserBridgeApi.md#profile) | **GET** /user/profile | Receives all profile details related to an account of the authorized user.
[**wallet**](UserBridgeApi.md#wallet) | **GET** /user/accounts/{accountId}/wallets/{coinSymbol} | Receives details of a single specific wallet of the authorized user.
[**walletGenerate**](UserBridgeApi.md#walletGenerate) | **PUT** /user/accounts/{accountId}/wallets/{coinSymbol}/generate | Requests a coin withdrawal from a specific wallet of the authorized user.
[**walletWithdraw**](UserBridgeApi.md#walletWithdraw) | **POST** /user/accounts/{accountId}/wallets/{coinSymbol}/withdraw | Requests a coin withdrawal from a specific wallet of the authorized user.
[**wallets**](UserBridgeApi.md#wallets) | **GET** /user/accounts/{accountId}/wallets | Receives details of all wallets of the authorized user.  &lt;param name&#x3D;\&quot;accountId\&quot;&gt;The account identifier.&lt;/param&gt;


<a name="profile"></a>
# **profile**
> ProfileDetails profile()

Receives all profile details related to an account of the authorized user.

### Example
```java
// Import classes:
//import io.swagger.client.api.UserBridgeApi;

UserBridgeApi apiInstance = new UserBridgeApi();
try {
    ProfileDetails result = apiInstance.profile();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserBridgeApi#profile");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ProfileDetails**](ProfileDetails.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="wallet"></a>
# **wallet**
> WalletDetails wallet(accountId, coinSymbol)

Receives details of a single specific wallet of the authorized user.

### Example
```java
// Import classes:
//import io.swagger.client.api.UserBridgeApi;

UserBridgeApi apiInstance = new UserBridgeApi();
String accountId = "accountId_example"; // String | The account identifier.
String coinSymbol = "coinSymbol_example"; // String | A unique symbol identification of a coin.
try {
    WalletDetails result = apiInstance.wallet(accountId, coinSymbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserBridgeApi#wallet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier. |
 **coinSymbol** | **String**| A unique symbol identification of a coin. |

### Return type

[**WalletDetails**](WalletDetails.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletGenerate"></a>
# **walletGenerate**
> String walletGenerate(accountId, coinSymbol)

Requests a coin withdrawal from a specific wallet of the authorized user.

### Example
```java
// Import classes:
//import io.swagger.client.api.UserBridgeApi;

UserBridgeApi apiInstance = new UserBridgeApi();
String accountId = "accountId_example"; // String | The account identifier.
String coinSymbol = "coinSymbol_example"; // String | A unique symbol identification of a coin.
try {
    String result = apiInstance.walletGenerate(accountId, coinSymbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserBridgeApi#walletGenerate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier. |
 **coinSymbol** | **String**| A unique symbol identification of a coin. |

### Return type

**String**

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletWithdraw"></a>
# **walletWithdraw**
> Map&lt;String, String&gt; walletWithdraw(accountId, coinSymbol, recipientPublicKey, withdrawalAmount)

Requests a coin withdrawal from a specific wallet of the authorized user.

### Example
```java
// Import classes:
//import io.swagger.client.api.UserBridgeApi;

UserBridgeApi apiInstance = new UserBridgeApi();
String accountId = "accountId_example"; // String | The account identifier.
String coinSymbol = "coinSymbol_example"; // String | A unique symbol identification of a coin.
String recipientPublicKey = "recipientPublicKey_example"; // String | Recipient address of a wallet for coins to be sent to
Double withdrawalAmount = 3.4D; // Double | Amount of balance to withdraw, represented in multiplies of the lowest tradable amount, which is specified by the wallet
try {
    Map<String, String> result = apiInstance.walletWithdraw(accountId, coinSymbol, recipientPublicKey, withdrawalAmount);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserBridgeApi#walletWithdraw");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**| The account identifier. |
 **coinSymbol** | **String**| A unique symbol identification of a coin. |
 **recipientPublicKey** | **String**| Recipient address of a wallet for coins to be sent to |
 **withdrawalAmount** | **Double**| Amount of balance to withdraw, represented in multiplies of the lowest tradable amount, which is specified by the wallet |

### Return type

**Map&lt;String, String&gt;**

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/_*+json, application/json-patch+json, application/json, text/json, application/_*+json
 - **Accept**: application/json

<a name="wallets"></a>
# **wallets**
> List&lt;WalletDetails&gt; wallets(accountId)

Receives details of all wallets of the authorized user.  &lt;param name&#x3D;\&quot;accountId\&quot;&gt;The account identifier.&lt;/param&gt;

### Example
```java
// Import classes:
//import io.swagger.client.api.UserBridgeApi;

UserBridgeApi apiInstance = new UserBridgeApi();
String accountId = "accountId_example"; // String | 
try {
    List<WalletDetails> result = apiInstance.wallets(accountId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserBridgeApi#wallets");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accountId** | **String**|  |

### Return type

[**List&lt;WalletDetails&gt;**](WalletDetails.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

