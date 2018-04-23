package cloud.coders.sk.xchangecrypt.datamodel;

/**
 * Created by Peter on 23.04.2018.
 */

public class Offer extends BaseObject {

    private double price;
    private String baseCurrency;
    private double baseCurrencyAmount;
    private String quoteCurrency;
    private double quoteCurrencyAmount;
    private boolean sell;

    public Offer(boolean sell, double price, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount) {
        this.sell = sell;
        this.price = price;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
    }

    public Offer(boolean sell, int id, double price, String baseCurrency, double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount) {
        super(id);
        this.sell = sell;
        this.price = price;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.quoteCurrency = quoteCurrency;
        this.quoteCurrencyAmount = quoteCurrencyAmount;
    }

    public boolean isSell() {
        return sell;
    }

    public void setSell(boolean sell) {
        this.sell = sell;
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
}
