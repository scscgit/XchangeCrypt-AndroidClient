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
public class Instrument {

  @SerializedName("name")
  private String name = null;
  @SerializedName("description")
  private String description = null;
  @SerializedName("minQty")
  private Double minQty = null;
  @SerializedName("maxQty")
  private Double maxQty = null;
  @SerializedName("qtyStep")
  private Double qtyStep = null;
  @SerializedName("pipSize")
  private Double pipSize = null;
  @SerializedName("pipValue")
  private Double pipValue = null;
  @SerializedName("minTick")
  private Double minTick = null;
  @SerializedName("lotSize")
  private Double lotSize = null;

  /**
   * Broker instrument name
   **/
  @ApiModelProperty(required = true, value = "Broker instrument name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Instrument description
   **/
  @ApiModelProperty(required = true, value = "Instrument description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Minimum quantity for trading
   **/
  @ApiModelProperty(value = "Minimum quantity for trading")
  public Double getMinQty() {
    return minQty;
  }

  public void setMinQty(Double minQty) {
    this.minQty = minQty;
  }

  /**
   * Maximum quantity for trading
   **/
  @ApiModelProperty(value = "Maximum quantity for trading")
  public Double getMaxQty() {
    return maxQty;
  }

  public void setMaxQty(Double maxQty) {
    this.maxQty = maxQty;
  }

  /**
   * Quantity step
   **/
  @ApiModelProperty(value = "Quantity step")
  public Double getQtyStep() {
    return qtyStep;
  }

  public void setQtyStep(Double qtyStep) {
    this.qtyStep = qtyStep;
  }

  /**
   * Size of 1 pip. It is equal to minTick for non-forex symbols. For forex pairs it equals either the minTick, or the minTick multiplied by 10. For example, for EURCAD minTick is 0.00001 and pipSize is 0.0001
   **/
  @ApiModelProperty(value = "Size of 1 pip. It is equal to minTick for non-forex symbols. For forex pairs it equals either the minTick, or the minTick multiplied by 10. For example, for EURCAD minTick is 0.00001 and pipSize is 0.0001")
  public Double getPipSize() {
    return pipSize;
  }

  public void setPipSize(Double pipSize) {
    this.pipSize = pipSize;
  }

  /**
   * Value of 1 pip in account currency
   **/
  @ApiModelProperty(value = "Value of 1 pip in account currency")
  public Double getPipValue() {
    return pipValue;
  }

  public void setPipValue(Double pipValue) {
    this.pipValue = pipValue;
  }

  /**
   * Minimum price movement
   **/
  @ApiModelProperty(value = "Minimum price movement")
  public Double getMinTick() {
    return minTick;
  }

  public void setMinTick(Double minTick) {
    this.minTick = minTick;
  }

  /**
   * Size of 1 lot
   **/
  @ApiModelProperty(value = "Size of 1 lot")
  public Double getLotSize() {
    return lotSize;
  }

  public void setLotSize(Double lotSize) {
    this.lotSize = lotSize;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Instrument instrument = (Instrument) o;
    return (this.name == null ? instrument.name == null : this.name.equals(instrument.name)) &&
      (this.description == null ? instrument.description == null : this.description.equals(instrument.description)) &&
      (this.minQty == null ? instrument.minQty == null : this.minQty.equals(instrument.minQty)) &&
      (this.maxQty == null ? instrument.maxQty == null : this.maxQty.equals(instrument.maxQty)) &&
      (this.qtyStep == null ? instrument.qtyStep == null : this.qtyStep.equals(instrument.qtyStep)) &&
      (this.pipSize == null ? instrument.pipSize == null : this.pipSize.equals(instrument.pipSize)) &&
      (this.pipValue == null ? instrument.pipValue == null : this.pipValue.equals(instrument.pipValue)) &&
      (this.minTick == null ? instrument.minTick == null : this.minTick.equals(instrument.minTick)) &&
      (this.lotSize == null ? instrument.lotSize == null : this.lotSize.equals(instrument.lotSize));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
    result = 31 * result + (this.description == null ? 0 : this.description.hashCode());
    result = 31 * result + (this.minQty == null ? 0 : this.minQty.hashCode());
    result = 31 * result + (this.maxQty == null ? 0 : this.maxQty.hashCode());
    result = 31 * result + (this.qtyStep == null ? 0 : this.qtyStep.hashCode());
    result = 31 * result + (this.pipSize == null ? 0 : this.pipSize.hashCode());
    result = 31 * result + (this.pipValue == null ? 0 : this.pipValue.hashCode());
    result = 31 * result + (this.minTick == null ? 0 : this.minTick.hashCode());
    result = 31 * result + (this.lotSize == null ? 0 : this.lotSize.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Instrument {\n");

    sb.append("  name: ").append(name).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  minQty: ").append(minQty).append("\n");
    sb.append("  maxQty: ").append(maxQty).append("\n");
    sb.append("  qtyStep: ").append(qtyStep).append("\n");
    sb.append("  pipSize: ").append(pipSize).append("\n");
    sb.append("  pipValue: ").append(pipValue).append("\n");
    sb.append("  minTick: ").append(minTick).append("\n");
    sb.append("  lotSize: ").append(lotSize).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
