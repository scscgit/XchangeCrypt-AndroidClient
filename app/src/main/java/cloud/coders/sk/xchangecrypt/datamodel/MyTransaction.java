package cloud.coders.sk.xchangecrypt.datamodel;

import java.util.Date;

import cloud.coders.sk.xchangecrypt.ui.fragments.BaseFragment;

/**
 * Created by Peter on 22.04.2018.
 */

public class MyTransaction extends BaseObject {


    private Date date;
    private boolean sell;
    private String baseCurrency;
    private String quoteCurrency;
    private float price;
    private float amount;

    public MyTransaction(Date date, boolean sell, String baseCurrency, String quoteCurrency, float price, float amount) {
        this.date = date;
        this.sell = sell;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.price = price;
        this.amount = amount;
    }

    public MyTransaction(int id, Date date, boolean sell, String baseCurrency, String quoteCurrency, float price, float amount) {
        super(id);
        this.date = date;
        this.sell = sell;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.price = price;
        this.amount = amount;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSell() {
        return sell;
    }

    public void setSell(boolean sell) {
        this.sell = sell;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
