/**
 * TradingView REST API Specification for Brokers
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import java.math.BigDecimal;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

/**
 * Time intervals in milliseconds that Trading Terminal uses to pull data from the server
 **/
@ApiModel(description = "Time intervals in milliseconds that Trading Terminal uses to pull data from the server")
public class PullingInterval {
  
  @SerializedName("history")
  private BigDecimal history = null;
  @SerializedName("quotes")
  private BigDecimal quotes = null;
  @SerializedName("orders")
  private BigDecimal orders = null;
  @SerializedName("positions")
  private BigDecimal positions = null;
  @SerializedName("accountManager")
  private BigDecimal accountManager = null;

  /**
   * Time interval in milliseconds that Trading Terminal uses to request chart real-time bar udpates. Default value is 500 ms
   **/
  @ApiModelProperty(value = "Time interval in milliseconds that Trading Terminal uses to request chart real-time bar udpates. Default value is 500 ms")
  public BigDecimal getHistory() {
    return history;
  }
  public void setHistory(BigDecimal history) {
    this.history = history;
  }

  /**
   * Time interval in milliseconds that Trading Terminal uses to request quote udpates. Default value is 500 ms
   **/
  @ApiModelProperty(value = "Time interval in milliseconds that Trading Terminal uses to request quote udpates. Default value is 500 ms")
  public BigDecimal getQuotes() {
    return quotes;
  }
  public void setQuotes(BigDecimal quotes) {
    this.quotes = quotes;
  }

  /**
   * Time interval in milliseconds that Trading Terminal uses to request orders. Default value is 500 ms
   **/
  @ApiModelProperty(value = "Time interval in milliseconds that Trading Terminal uses to request orders. Default value is 500 ms")
  public BigDecimal getOrders() {
    return orders;
  }
  public void setOrders(BigDecimal orders) {
    this.orders = orders;
  }

  /**
   * Time interval in milliseconds that Trading Terminal uses to request positions. Default value is 1000 ms
   **/
  @ApiModelProperty(value = "Time interval in milliseconds that Trading Terminal uses to request positions. Default value is 1000 ms")
  public BigDecimal getPositions() {
    return positions;
  }
  public void setPositions(BigDecimal positions) {
    this.positions = positions;
  }

  /**
   * Time interval in milliseconds that Trading Terminal uses to update Account Manager tables. Default value is 500 ms
   **/
  @ApiModelProperty(value = "Time interval in milliseconds that Trading Terminal uses to update Account Manager tables. Default value is 500 ms")
  public BigDecimal getAccountManager() {
    return accountManager;
  }
  public void setAccountManager(BigDecimal accountManager) {
    this.accountManager = accountManager;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PullingInterval pullingInterval = (PullingInterval) o;
    return (this.history == null ? pullingInterval.history == null : this.history.equals(pullingInterval.history)) &&
        (this.quotes == null ? pullingInterval.quotes == null : this.quotes.equals(pullingInterval.quotes)) &&
        (this.orders == null ? pullingInterval.orders == null : this.orders.equals(pullingInterval.orders)) &&
        (this.positions == null ? pullingInterval.positions == null : this.positions.equals(pullingInterval.positions)) &&
        (this.accountManager == null ? pullingInterval.accountManager == null : this.accountManager.equals(pullingInterval.accountManager));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.history == null ? 0: this.history.hashCode());
    result = 31 * result + (this.quotes == null ? 0: this.quotes.hashCode());
    result = 31 * result + (this.orders == null ? 0: this.orders.hashCode());
    result = 31 * result + (this.positions == null ? 0: this.positions.hashCode());
    result = 31 * result + (this.accountManager == null ? 0: this.accountManager.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PullingInterval {\n");
    
    sb.append("  history: ").append(history).append("\n");
    sb.append("  quotes: ").append(quotes).append("\n");
    sb.append("  orders: ").append(orders).append("\n");
    sb.append("  positions: ").append(positions).append("\n");
    sb.append("  accountManager: ").append(accountManager).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
