package cloud.coders.sk.xchangecrypt.datamodel;

/**
 * Created by Peter on 23.04.2018.
 */

public class Order extends BaseObject {

    private double price;
    private String baseCurrency;
    private double baseCurrencyAmount;
    private String quoteCurrency;
    private double quoteCurrencyAmount;
    private OrderSide side;
    private OrderType type;
    private String orderId;

    public Order(double price, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount, OrderSide side, OrderType type) {
        this.price = price;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
        this.side = side;
        this.type = type;
    }

    public Order(int id, double price, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount, OrderSide side, OrderType type) {
        super(id);
        this.price = price;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
        this.side = side;
        this.type = type;
    }

    public Order(double price, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount, OrderSide side, OrderType type, String orderId) {
        this.price = price;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
        this.side = side;
        this.type = type;
        this.orderId = orderId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
