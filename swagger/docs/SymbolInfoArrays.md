
# SymbolInfoArrays

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**symbol** | **List&lt;String&gt;** | It&#39;s name of a symbol. It is a string which your users will see. Also, it will be used for data requests if you are not using tickers. | 
**description** | **List&lt;String&gt;** | Description of a symbol. Will be printed in chart legend for this symbol. | 
**exchangeListed** | **List&lt;String&gt;** | Short name of exchange where this symbol is listed | 
**exchangeTraded** | **List&lt;String&gt;** | Short name of exchange where this symbol is traded | 
**minmovement** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | Minimal integer price change | 
**minmov2** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | It&#39;s a number for complex price formatting cases |  [optional]
**fractional** | **List&lt;Boolean&gt;** | Boolean showing whether this symbol wants to have complex price formatting (see minmov2) or not |  [optional]
**pricescale** | [**List&lt;BigDecimal&gt;**](BigDecimal.md) | MinimalPossiblePriceChange &#x3D; minmovement / pricescale | 
**hasIntraday** | **List&lt;Boolean&gt;** | Boolean showing whether symbol has intraday (minutes) history data. If it&#39;s false then all buttons for intradays resolutions will be disabled when this symbol is active in chart. If it is set to true, all resolutions that are supplied directly by the datafeed must be provided in intraday_multipliers array. |  [optional]
**hasNoVolume** | **List&lt;Boolean&gt;** | Boolean showing whether symbol has volume data or not |  [optional]
**type** | **List&lt;String&gt;** | Symbol type (forex/stock etc.) |  [optional]
**ticker** | **List&lt;String&gt;** | It&#39;s an unique identifier for this symbol in your symbology. If you specify this property then its value will be used for all data requests for this symbol. ticker is treated to be equal to symbol if not specified explicitly. |  [optional]
**timezone** | **List&lt;String&gt;** | Exchange timezone for this symbol. We expect to get name of time zone in olsondb format | 
**sessionRegular** | **List&lt;String&gt;** | Trading hours for this symbol. See the [Trading Sessions](https://github.com/tradingview/charting_library/wiki/Trading-Sessions) article to know more details. | 
**supportedResolutions** | [**List&lt;List&lt;String&gt;&gt;**](List.md) | An array of resolutions which should be enabled in resolutions picker for this symbol. Each item of an array is expected to be a string. |  [optional]
**hasDaily** | **List&lt;Boolean&gt;** | The boolean value showing whether datafeed has its own D resolution bars or not. If has_daily &#x3D; false then Charting Library will build respective resolutions from intraday by itself. If not, then it will request those bars from datafeed. |  [optional]
**intradayMultipliers** | [**List&lt;List&lt;String&gt;&gt;**](List.md) | It is an array containing intraday resolutions (in minutes) the datafeed wants to build by itself. E.g., if the datafeed reported he supports resolutions [\&quot;1\&quot;, \&quot;5\&quot;, \&quot;15\&quot;], but in fact it has only 1 minute bars for symbol X, it should set intraday_multipliers of X &#x3D; [1]. This will make Charting Library to build 5 and 15 resolutions by itself. |  [optional]
**hasWeeklyAndMonthly** | **List&lt;Boolean&gt;** | The boolean value showing whether datafeed has its own W and M resolution bars or not. If has_weekly_and_monthly &#x3D; false then Charting Library will build respective resolutions from D by itself. If not, then it will request those bars from datafeed. |  [optional]



