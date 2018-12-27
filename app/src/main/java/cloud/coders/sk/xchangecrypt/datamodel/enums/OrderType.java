package cloud.coders.sk.xchangecrypt.datamodel.enums;

/**
 * Created by Peter on 28.04.2018.
 */
public enum OrderType {
    LIMIT, STOP, MARKET, STOPLIMIT;

    public static OrderType fromString(String side) {
        return OrderType.valueOf(side.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
