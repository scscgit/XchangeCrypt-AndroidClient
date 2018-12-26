package cloud.coders.sk.xchangecrypt.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.ui.MainActivity;

/**
 * Created by V3502505 on 20/09/2016.
 */
public class SplashFragment extends BaseFragment {
    public static final String TAG = SplashFragment.class.getSimpleName();

    public static SplashFragment newInstance(Bundle args) {
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        setActionBar();
        setViews();
        setViewContents();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    switchToFragmentAndClear(FRAGMENT_LOGIN, null);
                } catch (IllegalStateException e) {
                    Log.i(TAG, "Couldn't switch fragment to login, because the app is no longer visible. Exiting instead");
                    System.exit(1);
                }
            }
        }, 1000);
        return rootView;
    }

    @Override
    protected void setActionBar() {
        ((MainActivity) getActivity()).hideActionBarImmediately();
    }

    @Override
    protected void setViews() {
        hideButtonLayout();
    }

    @Override
    protected void setViewContents() {
    }
}
