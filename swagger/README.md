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

import io.swagger.client.api.TradingPanelBridgeBrokerDataOrdersApi;

public class TradingPanelBridgeBrokerDataOrdersApiExample {

    public static void main(String[] args) {
        TradingPanelBridgeBrokerDataOrdersApi apiInstance = new TradingPanelBridgeBrokerDataOrdersApi();
        String accountId = "accountId_example"; // String | The account identifier
        String instrument = "instrument_example"; // String | Broker instrument name
        BigDecimal maxCount = new BigDecimal(); // BigDecimal | Maximum count of executions to return
        try {
            InlineResponse20010 result = apiInstance.accountsAccountIdExecutionsGet(accountId, instrument, maxCount);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling TradingPanelBridgeBrokerDataOrdersApi#accountsAccountIdExecutionsGet");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://rest-demo.tradingview.com/tradingview/v1/*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdExecutionsGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdExecutionsGet) | **GET** /accounts/{accountId}/executions | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdInstrumentsGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdInstrumentsGet) | **GET** /accounts/{accountId}/instruments | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdOrdersGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdOrdersGet) | **GET** /accounts/{accountId}/orders | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdOrdersHistoryGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdOrdersHistoryGet) | **GET** /accounts/{accountId}/ordersHistory | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdOrdersOrderIdDelete**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdOrdersOrderIdDelete) | **DELETE** /accounts/{accountId}/orders/{orderId} | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdOrdersOrderIdGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdOrdersOrderIdGet) | **GET** /accounts/{accountId}/orders/{orderId} | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdOrdersOrderIdPut**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdOrdersOrderIdPut) | **PUT** /accounts/{accountId}/orders/{orderId} | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdOrdersPost**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdOrdersPost) | **POST** /accounts/{accountId}/orders | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdPositionsGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdPositionsGet) | **GET** /accounts/{accountId}/positions | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdPositionsPositionIdDelete**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdPositionsPositionIdDelete) | **DELETE** /accounts/{accountId}/positions/{positionId} | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdPositionsPositionIdGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdPositionsPositionIdGet) | **GET** /accounts/{accountId}/positions/{positionId} | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdPositionsPositionIdPut**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdPositionsPositionIdPut) | **PUT** /accounts/{accountId}/positions/{positionId} | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsAccountIdStateGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsAccountIdStateGet) | **GET** /accounts/{accountId}/state | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**accountsGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#accountsGet) | **GET** /accounts | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**authorizePost**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#authorizePost) | **POST** /authorize | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**configGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#configGet) | **GET** /config | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**depthGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#depthGet) | **GET** /depth | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**historyGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#historyGet) | **GET** /history | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**mappingGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#mappingGet) | **GET** /mapping | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**quotesGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#quotesGet) | **GET** /quotes | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**streamingGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#streamingGet) | **GET** /streaming | 
*TradingPanelBridgeBrokerDataOrdersApi* | [**symbolInfoGet**](docs/TradingPanelBridgeBrokerDataOrdersApi.md#symbolInfoGet) | **GET** /symbol_info | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdExecutionsGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdExecutionsGet) | **GET** /accounts/{accountId}/executions | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdInstrumentsGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdInstrumentsGet) | **GET** /accounts/{accountId}/instruments | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdOrdersGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdOrdersGet) | **GET** /accounts/{accountId}/orders | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdOrdersHistoryGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdOrdersHistoryGet) | **GET** /accounts/{accountId}/ordersHistory | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdOrdersOrderIdDelete**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdOrdersOrderIdDelete) | **DELETE** /accounts/{accountId}/orders/{orderId} | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdOrdersOrderIdGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdOrdersOrderIdGet) | **GET** /accounts/{accountId}/orders/{orderId} | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdOrdersOrderIdPut**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdOrdersOrderIdPut) | **PUT** /accounts/{accountId}/orders/{orderId} | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdOrdersPost**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdOrdersPost) | **POST** /accounts/{accountId}/orders | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdPositionsGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdPositionsGet) | **GET** /accounts/{accountId}/positions | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdPositionsPositionIdDelete**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdPositionsPositionIdDelete) | **DELETE** /accounts/{accountId}/positions/{positionId} | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdPositionsPositionIdGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdPositionsPositionIdGet) | **GET** /accounts/{accountId}/positions/{positionId} | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdPositionsPositionIdPut**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdPositionsPositionIdPut) | **PUT** /accounts/{accountId}/positions/{positionId} | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsAccountIdStateGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsAccountIdStateGet) | **GET** /accounts/{accountId}/state | 
*TradingPanelBridgeOrdersOnlyApi* | [**accountsGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#accountsGet) | **GET** /accounts | 
*TradingPanelBridgeOrdersOnlyApi* | [**authorizePost**](docs/TradingPanelBridgeOrdersOnlyApi.md#authorizePost) | **POST** /authorize | 
*TradingPanelBridgeOrdersOnlyApi* | [**configGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#configGet) | **GET** /config | 
*TradingPanelBridgeOrdersOnlyApi* | [**depthGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#depthGet) | **GET** /depth | 
*TradingPanelBridgeOrdersOnlyApi* | [**mappingGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#mappingGet) | **GET** /mapping | 
*TradingPanelBridgeOrdersOnlyApi* | [**quotesGet**](docs/TradingPanelBridgeOrdersOnlyApi.md#quotesGet) | **GET** /quotes | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdExecutionsGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdExecutionsGet) | **GET** /accounts/{accountId}/executions | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdInstrumentsGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdInstrumentsGet) | **GET** /accounts/{accountId}/instruments | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdOrdersGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdOrdersGet) | **GET** /accounts/{accountId}/orders | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdOrdersHistoryGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdOrdersHistoryGet) | **GET** /accounts/{accountId}/ordersHistory | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdOrdersOrderIdDelete**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdOrdersOrderIdDelete) | **DELETE** /accounts/{accountId}/orders/{orderId} | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdOrdersOrderIdGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdOrdersOrderIdGet) | **GET** /accounts/{accountId}/orders/{orderId} | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdOrdersOrderIdPut**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdOrdersOrderIdPut) | **PUT** /accounts/{accountId}/orders/{orderId} | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdOrdersPost**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdOrdersPost) | **POST** /accounts/{accountId}/orders | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdPositionsGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdPositionsGet) | **GET** /accounts/{accountId}/positions | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdPositionsPositionIdDelete**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdPositionsPositionIdDelete) | **DELETE** /accounts/{accountId}/positions/{positionId} | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdPositionsPositionIdGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdPositionsPositionIdGet) | **GET** /accounts/{accountId}/positions/{positionId} | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdPositionsPositionIdPut**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdPositionsPositionIdPut) | **PUT** /accounts/{accountId}/positions/{positionId} | 
*TradingTerminalIntegrationApi* | [**accountsAccountIdStateGet**](docs/TradingTerminalIntegrationApi.md#accountsAccountIdStateGet) | **GET** /accounts/{accountId}/state | 
*TradingTerminalIntegrationApi* | [**accountsGet**](docs/TradingTerminalIntegrationApi.md#accountsGet) | **GET** /accounts | 
*TradingTerminalIntegrationApi* | [**configGet**](docs/TradingTerminalIntegrationApi.md#configGet) | **GET** /config | 
*TradingTerminalIntegrationApi* | [**depthGet**](docs/TradingTerminalIntegrationApi.md#depthGet) | **GET** /depth | 
*TradingTerminalIntegrationApi* | [**historyGet**](docs/TradingTerminalIntegrationApi.md#historyGet) | **GET** /history | 
*TradingTerminalIntegrationApi* | [**marksGet**](docs/TradingTerminalIntegrationApi.md#marksGet) | **GET** /marks | 
*TradingTerminalIntegrationApi* | [**quotesGet**](docs/TradingTerminalIntegrationApi.md#quotesGet) | **GET** /quotes | 
*TradingTerminalIntegrationApi* | [**symbolInfoGet**](docs/TradingTerminalIntegrationApi.md#symbolInfoGet) | **GET** /symbol_info | 
*TradingTerminalIntegrationApi* | [**timescaleMarksGet**](docs/TradingTerminalIntegrationApi.md#timescaleMarksGet) | **GET** /timescale_marks | 


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
 - [DepthItem](docs/DepthItem.md)
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
 - [PullingInterval](docs/PullingInterval.md)
 - [QuotesResponse](docs/QuotesResponse.md)
 - [SingleField](docs/SingleField.md)
 - [SingleMapping](docs/SingleMapping.md)
 - [SingleQuote](docs/SingleQuote.md)
 - [Status](docs/Status.md)
 - [SymbolInfoArrays](docs/SymbolInfoArrays.md)
 - [SymbolMapping](docs/SymbolMapping.md)
 - [TimescaleMark](docs/TimescaleMark.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### oauth

- **Type**: OAuth
- **Flow**: password
- **Authorization URL**: 
- **Scopes**: 
  - general: permission to perform all requests


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



