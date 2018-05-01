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

import java.util.*;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class InlineResponse2008 {
  
  @SerializedName("s")
  private String s = null;
  @SerializedName("errmsg")
  private String errmsg = null;
  @SerializedName("d")
  private List<Position> d = null;

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
  public List<Position> getD() {
    return d;
  }
  public void setD(List<Position> d) {
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
    InlineResponse2008 inlineResponse2008 = (InlineResponse2008) o;
    return (this.s == null ? inlineResponse2008.s == null : this.s.equals(inlineResponse2008.s)) &&
        (this.errmsg == null ? inlineResponse2008.errmsg == null : this.errmsg.equals(inlineResponse2008.errmsg)) &&
        (this.d == null ? inlineResponse2008.d == null : this.d.equals(inlineResponse2008.d));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.s == null ? 0: this.s.hashCode());
    result = 31 * result + (this.errmsg == null ? 0: this.errmsg.hashCode());
    result = 31 * result + (this.d == null ? 0: this.d.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2008 {\n");
    
    sb.append("  s: ").append(s).append("\n");
    sb.append("  errmsg: ").append(errmsg).append("\n");
    sb.append("  d: ").append(d).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}