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
public class InlineResponse20013 {

  public enum SEnum {
    ok, error,
  }

  ;
  @SerializedName("s")
  private SEnum s = null;
  @SerializedName("errmsg")
  private String errmsg = null;
  @SerializedName("d")
  private Depth d = null;

  /**
   * Gets or Sets S
   **/
  @ApiModelProperty(value = "Gets or Sets S")
  public SEnum getS() {
    return s;
  }

  public void setS(SEnum s) {
    this.s = s;
  }

  /**
   * Error message
   **/
  @ApiModelProperty(value = "Error message")
  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }

  /**
   * Gets or Sets D
   **/
  @ApiModelProperty(value = "Gets or Sets D")
  public Depth getD() {
    return d;
  }

  public void setD(Depth d) {
    this.d = d;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse20013 inlineResponse20013 = (InlineResponse20013) o;
    return (this.s == null ? inlineResponse20013.s == null : this.s.equals(inlineResponse20013.s)) &&
      (this.errmsg == null ? inlineResponse20013.errmsg == null : this.errmsg.equals(inlineResponse20013.errmsg)) &&
      (this.d == null ? inlineResponse20013.d == null : this.d.equals(inlineResponse20013.d));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.s == null ? 0 : this.s.hashCode());
    result = 31 * result + (this.errmsg == null ? 0 : this.errmsg.hashCode());
    result = 31 * result + (this.d == null ? 0 : this.d.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse20013 {\n");

    sb.append("  s: ").append(s).append("\n");
    sb.append("  errmsg: ").append(errmsg).append("\n");
    sb.append("  d: ").append(d).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
