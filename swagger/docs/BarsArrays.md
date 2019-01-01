
# BarsArrays

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**s** | [**SEnum**](#SEnum) | Status code. | 
**errmsg** | **String** | Error message. Should be provided if s &#x3D; \\\&quot;error\\\&quot; |  [optional]
**nb** | **Double** | unix time of the next bar if there is no data (status code is no_data) in the requested period (optional) |  [optional]
**t** | **List&lt;Double&gt;** | bar time, unix timestamp (UTC). Daily bars should only have the date part, time should be 0. |  [optional]
**o** | **List&lt;Double&gt;** | open price |  [optional]
**h** | **List&lt;Double&gt;** | high price |  [optional]
**l** | **List&lt;Double&gt;** | low price |  [optional]
**c** | **List&lt;Double&gt;** | close price |  [optional]
**v** | **List&lt;Double&gt;** | volume |  [optional]


<a name="SEnum"></a>
## Enum: SEnum
Name | Value
---- | -----



