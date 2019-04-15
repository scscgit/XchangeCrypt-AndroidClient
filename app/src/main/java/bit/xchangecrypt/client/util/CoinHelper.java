package bit.xchangecrypt.client.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import bit.xchangecrypt.client.R;

public class CoinHelper {
    public static Drawable getDrawableForCoin(Context context, String coinSymbol) {
        return ContextCompat.getDrawable(context, getResourceIdCoin(coinSymbol));
    }

    public static int getResourceIdCoin(String coinSymbol) {
        switch (coinSymbol) {
            case "BTC":
                return R.drawable.coin_btc;
            case "ETH":
                return R.drawable.coin_eth;
            case "LTC":
                return R.drawable.coin_ltc;
            default:
                return R.drawable.coin_default;
        }
    }
}
