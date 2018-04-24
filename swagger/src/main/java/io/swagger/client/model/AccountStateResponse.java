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
import java.util.*;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class AccountStateResponse {
  
  @SerializedName("balance")
  private BigDecimal balance = null;
  @SerializedName("unrealizedPl")
  private BigDecimal unrealizedPl = null;
  @SerializedName("equity")
  private BigDecimal equity = null;
  @SerializedName("amData")
  private List<List<List<String>>> amData = null;

  /**
   * Account Balance
   **/
  @ApiModelProperty(required = true, value = "Account Balance")
  public BigDecimal getBalance() {
    return balance;
  }
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  /**
   * Unrealized Profit/loss
   **/
  @ApiModelProperty(required = true, value = "Unrealized Profit/loss")
  public BigDecimal getUnrealizedPl() {
    return unrealizedPl;
  }
  public void setUnrealizedPl(BigDecimal unrealizedPl) {
    this.unrealizedPl = unrealizedPl;
  }

  /**
   * Equity
   **/
  @ApiModelProperty(value = "Equity")
  public BigDecimal getEquity() {
    return equity;
  }
  public void setEquity(BigDecimal equity) {
    this.equity = equity;
  }

  /**
   * Account Manager data. Structure of Account Manager is defined by `/config` response. Each element of this array is a table.
   **/
  @ApiModelProperty(value = "Account Manager data. Structure of Account Manager is defined by `/config` response. Each element of this array is a table.")
  public List<List<List<String>>> getAmData() {
    return amData;
  }
  public void setAmData(List<List<List<String>>> amData) {
    this.amData = amData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountStateResponse accountStateResponse = (AccountStateResponse) o;
    return (this.balance == null ? accountStateResponse.balance == null : this.balance.equals(accountStateResponse.balance)) &&
        (this.unrealizedPl == null ? accountStateResponse.unrealizedPl == null : this.unrealizedPl.equals(accountStateResponse.unrealizedPl)) &&
        (this.equity == null ? accountStateResponse.equity == null : this.equity.equals(accountStateResponse.equity)) &&
        (this.amData == null ? accountStateResponse.amData == null : this.amData.equals(accountStateResponse.amData));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.balance == null ? 0: this.balance.hashCode());
    result = 31 * result + (this.unrealizedPl == null ? 0: this.unrealizedPl.hashCode());
    result = 31 * result + (this.equity == null ? 0: this.equity.hashCode());
    result = 31 * result + (this.amData == null ? 0: this.amData.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountStateResponse {\n");
    
    sb.append("  balance: ").append(balance).append("\n");
    sb.append("  unrealizedPl: ").append(unrealizedPl).append("\n");
    sb.append("  equity: ").append(equity).append("\n");
    sb.append("  amData: ").append(amData).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
