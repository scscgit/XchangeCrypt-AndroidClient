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

@ApiModel(description = "")
public class InlineResponse2005D {

  @SerializedName("orderId")
  private String orderId = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2005D inlineResponse2005D = (InlineResponse2005D) o;
    return (this.orderId == null ? inlineResponse2005D.orderId == null : this.orderId.equals(inlineResponse2005D.orderId));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.orderId == null ? 0 : this.orderId.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2005D {\n");

    sb.append("  orderId: ").append(orderId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
