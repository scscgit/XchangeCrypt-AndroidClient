package cloud.coders.sk.xchangecrypt.datamodel.enums;

/**
 * Created by Peter on 28.04.2018.
 */
public enum OrderSide {
    SELL, BUY;

    public static OrderSide fromString(String side) {
        return OrderSide.valueOf(side.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
