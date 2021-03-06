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

import java.io.Serializable;
import java.util.List;

/**
 * Bars data.
 **/
@ApiModel(description = "Bars data.")
public class BarsArrays implements Serializable {

  public enum SEnum {
    ok, error, no_data,
  }

  ;
  @SerializedName("s")
  private SEnum s = null;
  @SerializedName("errmsg")
  private String errmsg = null;
  @SerializedName("nb")
  private Double nb = null;
  @SerializedName("t")
  private List<Double> t = null;
  @SerializedName("o")
  private List<Double> o = null;
  @SerializedName("h")
  private List<Double> h = null;
  @SerializedName("l")
  private List<Double> l = null;
  @SerializedName("c")
  private List<Double> c = null;
  @SerializedName("v")
  private List<Double> v = null;

  /**
   * Status code.
   **/
  @ApiModelProperty(required = true, value = "Status code.")
  public SEnum getS() {
    return s;
  }

  public void setS(SEnum s) {
    this.s = s;
  }

  /**
   * Error message. Should be provided if s = \\\"error\\\"
   **/
  @ApiModelProperty(value = "Error message. Should be provided if s = \\\"error\\\"")
  public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }

  /**
   * unix time of the next bar if there is no data (status code is no_data) in the requested period (optional)
   **/
  @ApiModelProperty(value = "unix time of the next bar if there is no data (status code is no_data) in the requested period (optional)")
  public Double getNb() {
    return nb;
  }

  public void setNb(Double nb) {
    this.nb = nb;
  }

  /**
   * bar time, unix timestamp (UTC). Daily bars should only have the date part, time should be 0.
   **/
  @ApiModelProperty(value = "bar time, unix timestamp (UTC). Daily bars should only have the date part, time should be 0.")
  public List<Double> getT() {
    return t;
  }

  public void setT(List<Double> t) {
    this.t = t;
  }

  /**
   * open price
   **/
  @ApiModelProperty(value = "open price")
  public List<Double> getO() {
    return o;
  }

  public void setO(List<Double> o) {
    this.o = o;
  }

  /**
   * high price
   **/
  @ApiModelProperty(value = "high price")
  public List<Double> getH() {
    return h;
  }

  public void setH(List<Double> h) {
    this.h = h;
  }

  /**
   * low price
   **/
  @ApiModelProperty(value = "low price")
  public List<Double> getL() {
    return l;
  }

  public void setL(List<Double> l) {
    this.l = l;
  }

  /**
   * close price
   **/
  @ApiModelProperty(value = "close price")
  public List<Double> getC() {
    return c;
  }

  public void setC(List<Double> c) {
    this.c = c;
  }

  /**
   * volume
   **/
  @ApiModelProperty(value = "volume")
  public List<Double> getV() {
    return v;
  }

  public void setV(List<Double> v) {
    this.v = v;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BarsArrays barsArrays = (BarsArrays) o;
    return (this.s == null ? barsArrays.s == null : this.s.equals(barsArrays.s)) &&
      (this.errmsg == null ? barsArrays.errmsg == null : this.errmsg.equals(barsArrays.errmsg)) &&
      (this.nb == null ? barsArrays.nb == null : this.nb.equals(barsArrays.nb)) &&
      (this.t == null ? barsArrays.t == null : this.t.equals(barsArrays.t)) &&
      (this.o == null ? barsArrays.o == null : this.o.equals(barsArrays.o)) &&
      (this.h == null ? barsArrays.h == null : this.h.equals(barsArrays.h)) &&
      (this.l == null ? barsArrays.l == null : this.l.equals(barsArrays.l)) &&
      (this.c == null ? barsArrays.c == null : this.c.equals(barsArrays.c)) &&
      (this.v == null ? barsArrays.v == null : this.v.equals(barsArrays.v));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.s == null ? 0 : this.s.hashCode());
    result = 31 * result + (this.errmsg == null ? 0 : this.errmsg.hashCode());
    result = 31 * result + (this.nb == null ? 0 : this.nb.hashCode());
    result = 31 * result + (this.t == null ? 0 : this.t.hashCode());
    result = 31 * result + (this.o == null ? 0 : this.o.hashCode());
    result = 31 * result + (this.h == null ? 0 : this.h.hashCode());
    result = 31 * result + (this.l == null ? 0 : this.l.hashCode());
    result = 31 * result + (this.c == null ? 0 : this.c.hashCode());
    result = 31 * result + (this.v == null ? 0 : this.v.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BarsArrays {\n");

    sb.append("  s: ").append(s).append("\n");
    sb.append("  errmsg: ").append(errmsg).append("\n");
    sb.append("  nb: ").append(nb).append("\n");
    sb.append("  t: ").append(t).append("\n");
    sb.append("  o: ").append(o).append("\n");
    sb.append("  h: ").append(h).append("\n");
    sb.append("  l: ").append(l).append("\n");
    sb.append("  c: ").append(c).append("\n");
    sb.append("  v: ").append(v).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
