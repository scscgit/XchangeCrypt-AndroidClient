package cloud.coders.sk.xchangecrypt.datamodel;

/**
 * Created by Peter on 22.04.2018.
 */

public class Coin extends BaseObject {

    private String name;
    private double amount;


    public Coin(int id, String name, double amount) {
        super(id);
        this.name = name;
        this.amount = amount;
    }

    public Coin(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
