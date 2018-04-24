
# Instrument

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **String** | Broker instrument name | 
**description** | **String** | Instrument description | 
**minQty** | [**BigDecimal**](BigDecimal.md) | Minimum quantity for trading |  [optional]
**maxQty** | [**BigDecimal**](BigDecimal.md) | Maximum quantity for trading |  [optional]
**qtyStep** | [**BigDecimal**](BigDecimal.md) | Quantity step |  [optional]
**pipSize** | [**BigDecimal**](BigDecimal.md) | Size of 1 pip. It is equal to minTick for non-forex symbols. For forex pairs it equals either the minTick, or the minTick multiplied by 10. For example, for EURCAD minTick is 0.00001 and pipSize is 0.0001 |  [optional]
**pipValue** | [**BigDecimal**](BigDecimal.md) | Value of 1 pip in account currency |  [optional]
**minTick** | [**BigDecimal**](BigDecimal.md) | Minimum price movement |  [optional]
**lotSize** | [**BigDecimal**](BigDecimal.md) | Size of 1 lot |  [optional]



