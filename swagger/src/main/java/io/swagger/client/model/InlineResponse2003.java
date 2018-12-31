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
public class InlineResponse2003 {

  @SerializedName("s")
  private String s = null;
  @SerializedName("errmsg")
  private String errmsg = null;
  @SerializedName("d")
  private AccountStateResponse d = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public AccountStateResponse getD() {
    return d;
  }

  public void setD(AccountStateResponse d) {
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
    InlineResponse2003 inlineResponse2003 = (InlineResponse2003) o;
    return (this.s == null ? inlineResponse2003.s == null : this.s.equals(inlineResponse2003.s)) &&
      (this.errmsg == null ? inlineResponse2003.errmsg == null : this.errmsg.equals(inlineResponse2003.errmsg)) &&
      (this.d == null ? inlineResponse2003.d == null : this.d.equals(inlineResponse2003.d));
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
    sb.append("class InlineResponse2003 {\n");

    sb.append("  s: ").append(s).append("\n");
    sb.append("  errmsg: ").append(errmsg).append("\n");
    sb.append("  d: ").append(d).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
