package bit.xchangecrypt.client.util;

import android.util.Base64;
import com.microsoft.identity.client.IAccount;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Peter on 05.05.2018.
 */
public class MicrosoftIdentityHelper {
    public static IAccount getUserByPolicy(List<IAccount> accounts, String policy) {
        for (int i = 0; i < accounts.size(); i++) {
            IAccount account = accounts.get(i);
            String userIdentifier = Base64UrlDecode(account.getAccountIdentifier().getIdentifier().split("\\.")[0]);
            if (userIdentifier.contains(policy.toLowerCase())) {
                return account;
            }
        }
        return null;
    }

    private static String Base64UrlDecode(String s) {
        byte[] data = Base64.decode(s, Base64.DEFAULT | Base64.URL_SAFE);
        String output = "";
        try {
            output = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return output;
    }
}
