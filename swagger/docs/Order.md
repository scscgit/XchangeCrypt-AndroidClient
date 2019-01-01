
# Order

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Gets or Sets Id | 
**instrument** | **String** | Instrument name that is used on a broker&#39;s side | 
**qty** | **Double** | Quantity | 
**side** | [**SideEnum**](#SideEnum) | Side. Possible values &amp;amp;ndash; \\\&quot;buy\\\&quot; and \\\&quot;sell\\\&quot;. | 
**type** | [**TypeEnum**](#TypeEnum) | Type. Possible values &amp;amp;ndash; \\\&quot;market\\\&quot;, \\\&quot;stop\\\&quot;, \\\&quot;limit\\\&quot;, \\\&quot;stoplimit\\\&quot;. | 
**filledQty** | **Double** | Filled quantity. |  [optional]
**avgPrice** | **Double** | Average price of order fills. It should be provided for filled / partly filled orders. |  [optional]
**limitPrice** | **Double** | Limit Price for Limit or StopLimit order. |  [optional]
**stopPrice** | **Double** | Stop Price for Stop or StopLimit order. |  [optional]
**parentId** | **String** | Identifier of a parent order or a parent position (for position brackets) depending on &#x60;parentType&#x60;. Should be set only for bracket orders. |  [optional]
**parentType** | [**ParentTypeEnum**](#ParentTypeEnum) | Type of order&#39;s parent. Should be set only for bracket orders. |  [optional]
**duration** | [**OrderDuration**](OrderDuration.md) | Gets or Sets Duration |  [optional]
**status** | [**StatusEnum**](#StatusEnum) | String constants to describe an order status.  &#x60;Status&#x60;  | Description - -- -- -- -- -|- -- -- -- -- -- -- placing   | order is not created on a broker&#39;s side yet inactive  | bracket order is created but waiting for a base order to be filled working   | order is created but not executed yet rejected  | order is rejected for some reason filled    | order is fully executed cancelled  | order is cancelled | 


<a name="SideEnum"></a>
## Enum: SideEnum
Name | Value
---- | -----


<a name="TypeEnum"></a>
## Enum: TypeEnum
Name | Value
---- | -----


<a name="ParentTypeEnum"></a>
## Enum: ParentTypeEnum
Name | Value
---- | -----


<a name="StatusEnum"></a>
## Enum: StatusEnum
Name | Value
---- | -----



