package cloud.coders.sk.xchangecrypt.listeners;

import android.os.Bundle;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public interface FragmentSwitcherInterface {

    int FRAGMENT_SPLASH = 0;
    int FRAGMENT_LOGIN = 1;
    int FRAGMENT_EXCHANGE = 2;
    int FRAGMENT_WALLET = 3;
    int FRAGMENT_SETTINGS = 4;

    public void switchToFragment(int fragmentID, Bundle args);

    public void switchToFragmentAndClear(int fragmentID, Bundle args);
}
