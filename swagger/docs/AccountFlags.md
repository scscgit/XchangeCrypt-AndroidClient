
# AccountFlags

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**showQuantityInsteadOfAmount** | **Boolean** | Renames Amount to Quantity in the Order Ticket |  [optional]
**supportDOM** | **Boolean** | Whether you want for DOM (Depth of market) widget to be available |  [optional]
**supportBrackets** | **Boolean** | Whether you support brackets. Deprecated. Use supportOrderBrackets and supportPositionBrackets instead. |  [optional]
**supportOrderBrackets** | **Boolean** | Whether you support adding (or modifying) stop loss and take profit to orders |  [optional]
**supportPositionBrackets** | **Boolean** | Whether you support adding (or modifying) stop loss and take profit to positions |  [optional]
**supportClosePosition** | **Boolean** | Whether you support closing of a position without a need for a user to fill an order. If it is &#x60;true&#x60; the Trading Terminal shows a confirmation dialog and sends a DELETE request instead of bringing up an order ticket. |  [optional]
**supportEditAmount** | **Boolean** | Whether you support editing orders quantity. If you set it to &#x60;false&#x60;, the quantity control in the order ticket will be disabled when modifing an order. |  [optional]
**supportLevel2Data** | **Boolean** | Whether you support Level 2 data. It is required to display DOM levels. You must implement &#x60;/streaming&#x60; to display DOM. |  [optional]
**supportMultiposition** | **Boolean** | Whether you support multiple positions at one instrument at the same time |  [optional]
**supportPLUpdate** | **Boolean** | Whether you provide &#x60;unrealizedPl&#x60; for positions. Otherwise P&amp;amp;L will be calculated automatically based on a simple algorithm |  [optional]
**supportReducePosition** | **Boolean** | Reserved for future use |  [optional]
**supportStopLimitOrders** | **Boolean** | Whether you support StopLimit orders |  [optional]
**supportOrdersHistory** | **Boolean** | Whether you support /ordersHistory request |  [optional]
**supportExecutions** | **Boolean** | Whether you support /executions request |  [optional]
**supportDigitalSignature** | **Boolean** | Whether you support Digital signature input field in the Order Ticket |  [optional]



