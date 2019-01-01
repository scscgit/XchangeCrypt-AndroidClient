
# Instrument

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **String** | Broker instrument name | 
**description** | **String** | Instrument description | 
**minQty** | **Double** | Minimum quantity for trading |  [optional]
**maxQty** | **Double** | Maximum quantity for trading |  [optional]
**qtyStep** | **Double** | Quantity step |  [optional]
**pipSize** | **Double** | Size of 1 pip. It is equal to minTick for non-forex symbols. For forex pairs it equals either the minTick, or the minTick multiplied by 10. For example, for EURCAD minTick is 0.00001 and pipSize is 0.0001 |  [optional]
**pipValue** | **Double** | Value of 1 pip in account currency |  [optional]
**minTick** | **Double** | Minimum price movement |  [optional]
**lotSize** | **Double** | Size of 1 lot |  [optional]



