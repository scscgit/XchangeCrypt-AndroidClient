# swagger-android-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-android-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-android-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-android-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.api.TradingPanelBrokerDataBridgeApi;

public class TradingPanelBrokerDataBridgeApiExample {

    public static void main(String[] args) {
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
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://localhost/api/v1*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*TradingPanelBrokerDataBridgeApi* | [**historyGet**](docs/TradingPanelBrokerDataBridgeApi.md#historyGet) | **GET** /trading/history | 
*TradingPanelBrokerDataBridgeApi* | [**streamingGet**](docs/TradingPanelBrokerDataBridgeApi.md#streamingGet) | **GET** /trading/streaming | 
*TradingPanelBrokerDataBridgeApi* | [**symbolInfoGet**](docs/TradingPanelBrokerDataBridgeApi.md#symbolInfoGet) | **GET** /trading/symbol_info | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdExecutionsGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdExecutionsGet) | **GET** /trading/accounts/{accountId}/executions | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdInstrumentsGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdInstrumentsGet) | **GET** /trading/accounts/{accountId}/instruments | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdOrdersGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersGet) | **GET** /trading/accounts/{accountId}/orders | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdOrdersHistoryGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersHistoryGet) | **GET** /trading/accounts/{accountId}/ordersHistory | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdOrdersOrderIdDelete**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersOrderIdDelete) | **DELETE** /trading/accounts/{accountId}/orders/{orderId} | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdOrdersOrderIdGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersOrderIdGet) | **GET** /trading/accounts/{accountId}/orders/{orderId} | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdOrdersOrderIdPut**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersOrderIdPut) | **PUT** /trading/accounts/{accountId}/orders/{orderId} | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdOrdersPost**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdOrdersPost) | **POST** /trading/accounts/{accountId}/orders | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdPositionsGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsGet) | **GET** /trading/accounts/{accountId}/positions | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdPositionsPositionIdDelete**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsPositionIdDelete) | **DELETE** /trading/accounts/{accountId}/positions/{positionId} | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdPositionsPositionIdGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsPositionIdGet) | **GET** /trading/accounts/{accountId}/positions/{positionId} | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdPositionsPositionIdPut**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdPositionsPositionIdPut) | **PUT** /trading/accounts/{accountId}/positions/{positionId} | 
*TradingPanelOrdersBridgeApi* | [**accountsAccountIdStateGet**](docs/TradingPanelOrdersBridgeApi.md#accountsAccountIdStateGet) | **GET** /trading/accounts/{accountId}/state | 
*TradingPanelOrdersBridgeApi* | [**accountsGet**](docs/TradingPanelOrdersBridgeApi.md#accountsGet) | **GET** /trading/accounts | 
*TradingPanelOrdersBridgeApi* | [**authorizePost**](docs/TradingPanelOrdersBridgeApi.md#authorizePost) | **POST** /trading/authorize | 
*TradingPanelOrdersBridgeApi* | [**configGet**](docs/TradingPanelOrdersBridgeApi.md#configGet) | **GET** /trading/config | 
*TradingPanelOrdersBridgeApi* | [**depthGet**](docs/TradingPanelOrdersBridgeApi.md#depthGet) | **GET** /trading/depth | 
*TradingPanelOrdersBridgeApi* | [**mappingGet**](docs/TradingPanelOrdersBridgeApi.md#mappingGet) | **GET** /trading/mapping | 
*TradingPanelOrdersBridgeApi* | [**quotesGet**](docs/TradingPanelOrdersBridgeApi.md#quotesGet) | **GET** /trading/quotes | 
*TradingTerminalIntegrationApi* | [**marksGet**](docs/TradingTerminalIntegrationApi.md#marksGet) | **GET** /trading/marks | 
*TradingTerminalIntegrationApi* | [**timescaleMarksGet**](docs/TradingTerminalIntegrationApi.md#timescaleMarksGet) | **GET** /trading/timescale_marks | 
*UserBridgeApi* | [**profile**](docs/UserBridgeApi.md#profile) | **GET** /user/profile | Receives all profile details related to an account of the authorized user.
*UserBridgeApi* | [**wallet**](docs/UserBridgeApi.md#wallet) | **GET** /user/accounts/{accountId}/wallet | Receives details of a single specific wallet of the authorized user.
*UserBridgeApi* | [**wallet_0**](docs/UserBridgeApi.md#wallet_0) | **POST** /user/accounts/{accountId}/withdraw | Requests a coin withdrawal from a specific wallet of the authorized user.
*UserBridgeApi* | [**wallets**](docs/UserBridgeApi.md#wallets) | **GET** /user/wallets | Receives details of all wallets of the authorized user.


## Documentation for Models

 - [Account](docs/Account.md)
 - [AccountFlags](docs/AccountFlags.md)
 - [AccountManagerColumn](docs/AccountManagerColumn.md)
 - [AccountManagerTable](docs/AccountManagerTable.md)
 - [AccountStateResponse](docs/AccountStateResponse.md)
 - [AuthorizationResponse](docs/AuthorizationResponse.md)
 - [BarsArrays](docs/BarsArrays.md)
 - [ConfigResponse](docs/ConfigResponse.md)
 - [Depth](docs/Depth.md)
 - [Duration](docs/Duration.md)
 - [Execution](docs/Execution.md)
 - [InlineResponse200](docs/InlineResponse200.md)
 - [InlineResponse2001](docs/InlineResponse2001.md)
 - [InlineResponse20010](docs/InlineResponse20010.md)
 - [InlineResponse20011](docs/InlineResponse20011.md)
 - [InlineResponse20012](docs/InlineResponse20012.md)
 - [InlineResponse20013](docs/InlineResponse20013.md)
 - [InlineResponse20014](docs/InlineResponse20014.md)
 - [InlineResponse2002](docs/InlineResponse2002.md)
 - [InlineResponse2003](docs/InlineResponse2003.md)
 - [InlineResponse2004](docs/InlineResponse2004.md)
 - [InlineResponse2005](docs/InlineResponse2005.md)
 - [InlineResponse2005D](docs/InlineResponse2005D.md)
 - [InlineResponse2006](docs/InlineResponse2006.md)
 - [InlineResponse2007](docs/InlineResponse2007.md)
 - [InlineResponse2008](docs/InlineResponse2008.md)
 - [InlineResponse2009](docs/InlineResponse2009.md)
 - [Instrument](docs/Instrument.md)
 - [MarksArrays](docs/MarksArrays.md)
 - [Order](docs/Order.md)
 - [OrderDuration](docs/OrderDuration.md)
 - [Position](docs/Position.md)
 - [ProfileDetails](docs/ProfileDetails.md)
 - [PullingInterval](docs/PullingInterval.md)
 - [QuotesResponse](docs/QuotesResponse.md)
 - [SingleMapping](docs/SingleMapping.md)
 - [SingleQuote](docs/SingleQuote.md)
 - [SymbolInfoArrays](docs/SymbolInfoArrays.md)
 - [SymbolMapping](docs/SymbolMapping.md)
 - [TimescaleMark](docs/TimescaleMark.md)
 - [WalletDetails](docs/WalletDetails.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### Bearer

- **Type**: API key
- **API key parameter name**: Authorization
- **Location**: HTTP header


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



