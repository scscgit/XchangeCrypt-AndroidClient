package bit.xchangecrypt.client.datamodel;

/**
 * Created by Peter on 22.04.2018.
 */
public class Coin extends BaseObject {
    private String symbolName;
    private String publicKey;
    private double amount;

    public Coin(int id, String symbolName, String publicKey, double amount) {
        super(id);
        this.symbolName = symbolName;
        this.publicKey = publicKey;
        this.amount = amount;
    }

    public Coin(String symbolName, String publicKey, double amount) {
        this.symbolName = symbolName;
        this.publicKey = publicKey;
        this.amount = amount;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
