/**
 * XchangeCrypt REST API Specification
 * TradingView REST API Specification for Brokers (ASP.NET Core 2.0)
 * <p>
 * OpenAPI spec version: v1
 * Contact:
 * <p>
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Details of a single wallet representing a balance of a single coin of one user.
 **/
@ApiModel(description = "Details of a single wallet representing a balance of a single coin of one user.")
public class WalletDetails {

  @SerializedName("coinSymbol")
  private String coinSymbol = null;
  @SerializedName("walletPublicKey")
  private String walletPublicKey = null;
  @SerializedName("balance")
  private Double balance = null;
  @SerializedName("availableBalance")
  private Double availableBalance = null;

  /**
   * Short and unique coin name, used as a part of instrument trading pair name. Also used as account identifier.
   **/
  @ApiModelProperty(value = "Short and unique coin name, used as a part of instrument trading pair name. Also used as account identifier.")
  public String getCoinSymbol() {
    return coinSymbol;
  }

  public void setCoinSymbol(String coinSymbol) {
    this.coinSymbol = coinSymbol;
  }

  /**
   * Address to be used for receiving coins into the wallet.
   **/
  @ApiModelProperty(value = "Address to be used for receiving coins into the wallet.")
  public String getWalletPublicKey() {
    return walletPublicKey;
  }

  public void setWalletPublicKey(String walletPublicKey) {
    this.walletPublicKey = walletPublicKey;
  }

  /**
   * User balance of the wallet represented in multiplies of the lowest tradable amount, which is specified by the wallet.
   **/
  @ApiModelProperty(value = "User balance of the wallet represented in multiplies of the lowest tradable amount, which is specified by the wallet.")
  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  /**
   * User balance of the wallet, that is not reserved by open orders, and is available for trading.
   **/
  @ApiModelProperty(value = "User balance of the wallet, that is not reserved by open orders, and is available for trading.")
  public Double getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(Double availableBalance) {
    this.availableBalance = availableBalance;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WalletDetails walletDetails = (WalletDetails) o;
    return (this.coinSymbol == null ? walletDetails.coinSymbol == null : this.coinSymbol.equals(walletDetails.coinSymbol)) &&
      (this.walletPublicKey == null ? walletDetails.walletPublicKey == null : this.walletPublicKey.equals(walletDetails.walletPublicKey)) &&
      (this.balance == null ? walletDetails.balance == null : this.balance.equals(walletDetails.balance)) &&
      (this.availableBalance == null ? walletDetails.availableBalance == null : this.availableBalance.equals(walletDetails.availableBalance));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.coinSymbol == null ? 0 : this.coinSymbol.hashCode());
    result = 31 * result + (this.walletPublicKey == null ? 0 : this.walletPublicKey.hashCode());
    result = 31 * result + (this.balance == null ? 0 : this.balance.hashCode());
    result = 31 * result + (this.availableBalance == null ? 0 : this.availableBalance.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WalletDetails {\n");

    sb.append("  coinSymbol: ").append(coinSymbol).append("\n");
    sb.append("  walletPublicKey: ").append(walletPublicKey).append("\n");
    sb.append("  balance: ").append(balance).append("\n");
    sb.append("  availableBalance: ").append(availableBalance).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
