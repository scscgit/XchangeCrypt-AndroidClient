package cloud.coders.sk.xchangecrypt.datamodel;

        import java.util.Date;

        import cloud.coders.sk.xchangecrypt.ui.fragments.BaseFragment;

/**
 * Created by Peter on 22.04.2018.
 */

public class MyTransaction extends BaseObject {


    private OrderSide side;
    private String baseCurrency;
    private String quoteCurrency;
    private double price;
    private double amount;

    public MyTransaction( OrderSide side, String baseCurrency, String quoteCurrency, double price, double amount) {
        this.side = side;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.price = price;
        this.amount = amount;
    }

    public MyTransaction(int id, OrderSide side, String baseCurrency, String quoteCurrency, double price, double amount) {
        super(id);
        this.side = side;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.price = price;
        this.amount = amount;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
