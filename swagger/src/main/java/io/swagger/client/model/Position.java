/**
 * TradingView REST API Specification for Brokers
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 * <p>
 * OpenAPI spec version:
 * <p>
 * <p>
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(description = "")
public class Position {

  @SerializedName("id")
  private String id = null;
  @SerializedName("instrument")
  private String instrument = null;
  @SerializedName("qty")
  private BigDecimal qty = null;

  public enum SideEnum {
    buy, sell,
  }

  ;
  @SerializedName("side")
  private SideEnum side = null;
  @SerializedName("avgPrice")
  private BigDecimal avgPrice = null;
  @SerializedName("unrealizedPl")
  private BigDecimal unrealizedPl = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Instrument name that is used on a broker's side
   **/
  @ApiModelProperty(required = true, value = "Instrument name that is used on a broker's side")
  public String getInstrument() {
    return instrument;
  }

  public void setInstrument(String instrument) {
    this.instrument = instrument;
  }

  /**
   * Quantity
   **/
  @ApiModelProperty(required = true, value = "Quantity")
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  /**
   * Side. Possible values &ndash; \"buy\" and \"sell\".
   **/
  @ApiModelProperty(required = true, value = "Side. Possible values &ndash; \"buy\" and \"sell\".")
  public SideEnum getSide() {
    return side;
  }

  public void setSide(SideEnum side) {
    this.side = side;
  }

  /**
   * Average price of position trades
   **/
  @ApiModelProperty(required = true, value = "Average price of position trades")
  public BigDecimal getAvgPrice() {
    return avgPrice;
  }

  public void setAvgPrice(BigDecimal avgPrice) {
    this.avgPrice = avgPrice;
  }

  /**
   * Unrealized (open) profit/loss
   **/
  @ApiModelProperty(required = true, value = "Unrealized (open) profit/loss")
  public BigDecimal getUnrealizedPl() {
    return unrealizedPl;
  }

  public void setUnrealizedPl(BigDecimal unrealizedPl) {
    this.unrealizedPl = unrealizedPl;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return (this.id == null ? position.id == null : this.id.equals(position.id)) &&
      (this.instrument == null ? position.instrument == null : this.instrument.equals(position.instrument)) &&
      (this.qty == null ? position.qty == null : this.qty.equals(position.qty)) &&
      (this.side == null ? position.side == null : this.side.equals(position.side)) &&
      (this.avgPrice == null ? position.avgPrice == null : this.avgPrice.equals(position.avgPrice)) &&
      (this.unrealizedPl == null ? position.unrealizedPl == null : this.unrealizedPl.equals(position.unrealizedPl));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
    result = 31 * result + (this.instrument == null ? 0 : this.instrument.hashCode());
    result = 31 * result + (this.qty == null ? 0 : this.qty.hashCode());
    result = 31 * result + (this.side == null ? 0 : this.side.hashCode());
    result = 31 * result + (this.avgPrice == null ? 0 : this.avgPrice.hashCode());
    result = 31 * result + (this.unrealizedPl == null ? 0 : this.unrealizedPl.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Position {\n");

    sb.append("  id: ").append(id).append("\n");
    sb.append("  instrument: ").append(instrument).append("\n");
    sb.append("  qty: ").append(qty).append("\n");
    sb.append("  side: ").append(side).append("\n");
    sb.append("  avgPrice: ").append(avgPrice).append("\n");
    sb.append("  unrealizedPl: ").append(unrealizedPl).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
