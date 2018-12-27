package cloud.coders.sk.xchangecrypt.datamodel;

import java.util.UUID;

import cloud.coders.sk.xchangecrypt.datamodel.enums.OrderSide;
import cloud.coders.sk.xchangecrypt.datamodel.enums.OrderType;

/**
 * Created by Peter on 23.04.2018.
 */
public class Order extends BaseObject {
    private Double limitPrice;
    private Double stopPrice;
    private Double stopLoss;
    private Double takeProfit;
    private String baseCurrency;
    private double baseCurrencyAmount;
    private String quoteCurrency;
    private double quoteCurrencyAmount;
    private OrderSide side;
    private OrderType type;
    private String orderId;

    public Order(Double limitPrice, Double stopPrice, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount, OrderSide side, OrderType type) {
        this.limitPrice = limitPrice;
        this.stopPrice = stopPrice;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
        this.side = side;
        this.type = type;
        this.orderId = UUID.randomUUID().toString();
    }

    public Order(Double limitPrice, Double stopPrice, Double stopLoss, Double takeProfit, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount, OrderSide side, OrderType type) {
        this.limitPrice = limitPrice;
        this.stopPrice = stopPrice;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
        this.side = side;
        this.type = type;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.orderId = UUID.randomUUID().toString();
    }

    public Order(Double limitPrice, Double stopPrice, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount, OrderSide side, OrderType type, String orderId) {
        this.limitPrice = limitPrice;
        this.stopPrice = stopPrice;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
        this.side = side;
        this.type = type;
        this.orderId = orderId;
    }

    public Double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(Double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public Double getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(Double takeProfit) {
        this.takeProfit = takeProfit;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(Double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public Double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(Double stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public double getBaseCurrencyAmount() {
        return baseCurrencyAmount;
    }

    public void setBaseCurrencyAmount(double baseCurrencyAmount) {
        this.baseCurrencyAmount = baseCurrencyAmount;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public double getQuoteCurrencyAmount() {
        return quoteCurrencyAmount;
    }

    public void setQuoteCurrencyAmount(double quoteCurrencyAmount) {
        this.quoteCurrencyAmount = quoteCurrencyAmount;
    }

    public String getOrderId() {
        if (orderId == null) {
            return "0";
        }
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
