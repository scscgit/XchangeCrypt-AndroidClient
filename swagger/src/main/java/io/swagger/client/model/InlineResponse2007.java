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
public class InlineResponse2007 {

  public enum SEnum {
    ok, error,
  }

  ;
  @SerializedName("s")
  private SEnum s = null;
  @SerializedName("errmsg")
  private String errmsg = null;

  /**
   * Gets or Sets S
   **/
  @ApiModelProperty(required = true, value = "Gets or Sets S")
  public SEnum getS() {
    return s;
  }

  public void setS(SEnum s) {
    this.s = s;
  }

  /**
   * Gets or Sets Errmsg
   **/
  @ApiModelProperty(value = "Gets or Sets Errmsg")
  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2007 inlineResponse2007 = (InlineResponse2007) o;
    return (this.s == null ? inlineResponse2007.s == null : this.s.equals(inlineResponse2007.s)) &&
      (this.errmsg == null ? inlineResponse2007.errmsg == null : this.errmsg.equals(inlineResponse2007.errmsg));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.s == null ? 0 : this.s.hashCode());
    result = 31 * result + (this.errmsg == null ? 0 : this.errmsg.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2007 {\n");

    sb.append("  s: ").append(s).append("\n");
    sb.append("  errmsg: ").append(errmsg).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
