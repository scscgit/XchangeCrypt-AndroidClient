package io.swagger.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Peter on 05.05.2018.
 */

@ApiModel(description = "")
public class AccountWalletResponse {

  @SerializedName("coinSymbol")
  private String coinSymbol = null;
  @SerializedName("walletPublicKey")
  private String walletPublicKey = null;
  @SerializedName("balance")
  private Double balance = null;

  @ApiModelProperty(value = "")
  public String getCoinSymbol() {
    return coinSymbol;
  }

  public void setCoinSymbol(String coinSymbol) {
    this.coinSymbol = coinSymbol;
  }

  @ApiModelProperty(value = "")
  public String getWalletPublicKey() {
    return walletPublicKey;
  }

  public void setWalletPublicKey(String walletPublicKey) {
    this.walletPublicKey = walletPublicKey;
  }

  @ApiModelProperty(value = "")
  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public AccountWalletResponse() {
  }

  public AccountWalletResponse(String coinSymbol, String walletPublicKey, Double balance) {
    this.coinSymbol = coinSymbol;
    this.balance = balance;
    this.walletPublicKey = walletPublicKey;

  }


}
