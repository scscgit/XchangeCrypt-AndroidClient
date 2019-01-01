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
 *
 **/
@ApiModel(description = "")
public class Execution {

  @SerializedName("id")
  private String id = null;
  @SerializedName("instrument")
  private String instrument = null;
  @SerializedName("price")
  private Double price = null;
  @SerializedName("time")
  private Double time = null;
  @SerializedName("qty")
  private Double qty = null;

  public enum SideEnum {
    buy, sell,
  }

  ;
  @SerializedName("side")
  private SideEnum side = null;

  /**
   * Unique identifier
   **/
  @ApiModelProperty(required = true, value = "Unique identifier")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Instrument id
   **/
  @ApiModelProperty(required = true, value = "Instrument id")
  public String getInstrument() {
    return instrument;
  }

  public void setInstrument(String instrument) {
    this.instrument = instrument;
  }

  /**
   * Execution price
   **/
  @ApiModelProperty(required = true, value = "Execution price")
  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  /**
   * Execution time
   **/
  @ApiModelProperty(required = true, value = "Execution time")
  public Double getTime() {
    return time;
  }

  public void setTime(Double time) {
    this.time = time;
  }

  /**
   * Execution quantity
   **/
  @ApiModelProperty(required = true, value = "Execution quantity")
  public Double getQty() {
    return qty;
  }

  public void setQty(Double qty) {
    this.qty = qty;
  }

  /**
   * Side. Possible values &amp;ndash; \\\"buy\\\" and \\\"sell\\\".
   **/
  @ApiModelProperty(required = true, value = "Side. Possible values &amp;ndash; \\\"buy\\\" and \\\"sell\\\".")
  public SideEnum getSide() {
    return side;
  }

  public void setSide(SideEnum side) {
    this.side = side;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Execution execution = (Execution) o;
    return (this.id == null ? execution.id == null : this.id.equals(execution.id)) &&
      (this.instrument == null ? execution.instrument == null : this.instrument.equals(execution.instrument)) &&
      (this.price == null ? execution.price == null : this.price.equals(execution.price)) &&
      (this.time == null ? execution.time == null : this.time.equals(execution.time)) &&
      (this.qty == null ? execution.qty == null : this.qty.equals(execution.qty)) &&
      (this.side == null ? execution.side == null : this.side.equals(execution.side));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
    result = 31 * result + (this.instrument == null ? 0 : this.instrument.hashCode());
    result = 31 * result + (this.price == null ? 0 : this.price.hashCode());
    result = 31 * result + (this.time == null ? 0 : this.time.hashCode());
    result = 31 * result + (this.qty == null ? 0 : this.qty.hashCode());
    result = 31 * result + (this.side == null ? 0 : this.side.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Execution {\n");

    sb.append("  id: ").append(id).append("\n");
    sb.append("  instrument: ").append(instrument).append("\n");
    sb.append("  price: ").append(price).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  qty: ").append(qty).append("\n");
    sb.append("  side: ").append(side).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
