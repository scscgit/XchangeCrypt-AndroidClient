
# BarsArrays

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**s** | [**SEnum**](#SEnum) | Status code. | 
**errmsg** | **String** | Error message. Should be provided if s &#x3D; \&quot;error\&quot; |  [optional]
**nb** | [**BigDecimal**](BigDecimal.md) | unix time of the next bar if there is no data (status code is no_data) in the requested period (optional) |  [optional]
**t** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | bar time, unix timestamp (UTC). Daily bars should only have the date part, time should be 0. |  [optional]
**o** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | open price |  [optional]
**h** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | high price |  [optional]
**l** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | low price |  [optional]
**c** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | close price |  [optional]
**v** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | volume |  [optional]


<a name="SEnum"></a>
## Enum: SEnum
Name | Value
---- | -----



