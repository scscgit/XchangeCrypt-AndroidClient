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
 * Timescale marks data.
 **/
@ApiModel(description = "Timescale marks data.")
public class TimescaleMark {

  @SerializedName("id")
  private String id = null;
  @SerializedName("time")
  private Double time = null;

  public enum ColorEnum {
    red, green, blue, yellow,
  }

  ;
  @SerializedName("color")
  private ColorEnum color = null;
  @SerializedName("tooltip")
  private String tooltip = null;
  @SerializedName("label")
  private String label = null;

  /**
   * Unique identifier of marks
   **/
  @ApiModelProperty(required = true, value = "Unique identifier of marks")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * bar time, unix timestamp (UTC)
   **/
  @ApiModelProperty(required = true, value = "bar time, unix timestamp (UTC)")
  public Double getTime() {
    return time;
  }

  public void setTime(Double time) {
    this.time = time;
  }

  /**
   * Mark color
   **/
  @ApiModelProperty(value = "Mark color")
  public ColorEnum getColor() {
    return color;
  }

  public void setColor(ColorEnum color) {
    this.color = color;
  }

  /**
   * Tooltip text
   **/
  @ApiModelProperty(value = "Tooltip text")
  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  /**
   * A letter to be printed on a mark. Single character
   **/
  @ApiModelProperty(required = true, value = "A letter to be printed on a mark. Single character")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimescaleMark timescaleMark = (TimescaleMark) o;
    return (this.id == null ? timescaleMark.id == null : this.id.equals(timescaleMark.id)) &&
      (this.time == null ? timescaleMark.time == null : this.time.equals(timescaleMark.time)) &&
      (this.color == null ? timescaleMark.color == null : this.color.equals(timescaleMark.color)) &&
      (this.tooltip == null ? timescaleMark.tooltip == null : this.tooltip.equals(timescaleMark.tooltip)) &&
      (this.label == null ? timescaleMark.label == null : this.label.equals(timescaleMark.label));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
    result = 31 * result + (this.time == null ? 0 : this.time.hashCode());
    result = 31 * result + (this.color == null ? 0 : this.color.hashCode());
    result = 31 * result + (this.tooltip == null ? 0 : this.tooltip.hashCode());
    result = 31 * result + (this.label == null ? 0 : this.label.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimescaleMark {\n");

    sb.append("  id: ").append(id).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("  color: ").append(color).append("\n");
    sb.append("  tooltip: ").append(tooltip).append("\n");
    sb.append("  label: ").append(label).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
