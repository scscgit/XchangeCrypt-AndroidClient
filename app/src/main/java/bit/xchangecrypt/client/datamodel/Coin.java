package bit.xchangecrypt.client.datamodel;

/**
 * Created by Peter on 22.04.2018.
 */
public class Coin extends BaseObject {
    private String symbolName;
    private double amount;

    public Coin(int id, String symbolName, double amount) {
        super(id);
        this.symbolName = symbolName;
        this.amount = amount;
    }

    public Coin(String symbolName, double amount) {
        this.symbolName = symbolName;
        this.amount = amount;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
