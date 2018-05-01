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

@ApiModel(description = "")
public class AccountManagerColumn {
  
  @SerializedName("id")
  private String id = null;
  @SerializedName("title")
  private String title = null;
  @SerializedName("tooltip")
  private String tooltip = null;
  @SerializedName("fixedWidth")
  private Boolean fixedWidth = null;
  @SerializedName("sortable")
  private Boolean sortable = null;

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
   * Localized title of a column
   **/
  @ApiModelProperty(required = true, value = "Localized title of a column")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Tooltip that is shown on mouse hover
   **/
  @ApiModelProperty(value = "Tooltip that is shown on mouse hover")
  public String getTooltip() {
    return tooltip;
  }
  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  /**
   * Set it to true if data length is frequently changed
   **/
  @ApiModelProperty(value = "Set it to true if data length is frequently changed")
  public Boolean getFixedWidth() {
    return fixedWidth;
  }
  public void setFixedWidth(Boolean fixedWidth) {
    this.fixedWidth = fixedWidth;
  }

  /**
   * Set it to false if this columns data should not be sortable
   **/
  @ApiModelProperty(value = "Set it to false if this columns data should not be sortable")
  public Boolean getSortable() {
    return sortable;
  }
  public void setSortable(Boolean sortable) {
    this.sortable = sortable;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountManagerColumn accountManagerColumn = (AccountManagerColumn) o;
    return (this.id == null ? accountManagerColumn.id == null : this.id.equals(accountManagerColumn.id)) &&
        (this.title == null ? accountManagerColumn.title == null : this.title.equals(accountManagerColumn.title)) &&
        (this.tooltip == null ? accountManagerColumn.tooltip == null : this.tooltip.equals(accountManagerColumn.tooltip)) &&
        (this.fixedWidth == null ? accountManagerColumn.fixedWidth == null : this.fixedWidth.equals(accountManagerColumn.fixedWidth)) &&
        (this.sortable == null ? accountManagerColumn.sortable == null : this.sortable.equals(accountManagerColumn.sortable));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.id == null ? 0: this.id.hashCode());
    result = 31 * result + (this.title == null ? 0: this.title.hashCode());
    result = 31 * result + (this.tooltip == null ? 0: this.tooltip.hashCode());
    result = 31 * result + (this.fixedWidth == null ? 0: this.fixedWidth.hashCode());
    result = 31 * result + (this.sortable == null ? 0: this.sortable.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountManagerColumn {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  tooltip: ").append(tooltip).append("\n");
    sb.append("  fixedWidth: ").append(fixedWidth).append("\n");
    sb.append("  sortable: ").append(sortable).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}