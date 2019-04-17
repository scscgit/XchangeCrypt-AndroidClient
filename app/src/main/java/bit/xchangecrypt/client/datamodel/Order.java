package bit.xchangecrypt.client.datamodel;

import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.datamodel.enums.OrderType;

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
    private OrderSide side;
    private OrderType type;

    // Only required for account orders (for their deletion) and account orders history (for sorting)
    private String orderId;

    public Order(Double limitPrice, Double stopPrice, Double stopLoss, Double takeProfit, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, OrderSide side, OrderType type) {
        this.limitPrice = limitPrice;
        this.stopPrice = stopPrice;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.side = side;
        this.type = type;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        //this.orderId = UUID.randomUUID().toString();
    }

    public Order(Double limitPrice, Double stopPrice, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, OrderSide side, OrderType type) {
        this(limitPrice, stopPrice, null, null, baseCurrency, baseCurrencyAmount, quoteCurrency, side, type);
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

    public String getOrderId() {
        return orderId;
    }

    public Order setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }
}
