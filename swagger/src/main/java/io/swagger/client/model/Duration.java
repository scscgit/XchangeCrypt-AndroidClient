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

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

/**
 * Single duration option
 **/
@ApiModel(description = "Single duration option")
public class Duration {
  
  @SerializedName("id")
  private String id = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("hasDatePicker")
  private Boolean hasDatePicker = null;
  @SerializedName("hasTimePicker")
  private Boolean hasTimePicker = null;

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
   * Localized title
   **/
  @ApiModelProperty(required = true, value = "Localized title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Display date control in Order Ticket for this duration type
   **/
  @ApiModelProperty(value = "Display date control in Order Ticket for this duration type")
  public Boolean getHasDatePicker() {
    return hasDatePicker;
  }
  public void setHasDatePicker(Boolean hasDatePicker) {
    this.hasDatePicker = hasDatePicker;
  }

  /**
   * Display time control in Order Ticket for this duration type
   **/
  @ApiModelProperty(value = "Display time control in Order Ticket for this duration type")
  public Boolean getHasTimePicker() {
    return hasTimePicker;
  }
  public void setHasTimePicker(Boolean hasTimePicker) {
    this.hasTimePicker = hasTimePicker;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Duration duration = (Duration) o;
    return (this.id == null ? duration.id == null : this.id.equals(duration.id)) &&
        (this.title == null ? duration.title == null : this.title.equals(duration.title)) &&
        (this.hasDatePicker == null ? duration.hasDatePicker == null : this.hasDatePicker.equals(duration.hasDatePicker)) &&
        (this.hasTimePicker == null ? duration.hasTimePicker == null : this.hasTimePicker.equals(duration.hasTimePicker));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.id == null ? 0: this.id.hashCode());
    result = 31 * result + (this.title == null ? 0: this.title.hashCode());
    result = 31 * result + (this.hasDatePicker == null ? 0: this.hasDatePicker.hashCode());
    result = 31 * result + (this.hasTimePicker == null ? 0: this.hasTimePicker.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Duration {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  hasDatePicker: ").append(hasDatePicker).append("\n");
    sb.append("  hasTimePicker: ").append(hasTimePicker).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
