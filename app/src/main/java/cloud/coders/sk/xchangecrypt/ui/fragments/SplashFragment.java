package cloud.coders.sk.xchangecrypt.ui.fragments;

import android.os.Bundle;
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
//                if (!isOnline(getContext())) {
//                    System.exit(1);
//                } else {
                switchToFragmentAndClear(FRAGMENT_LOGIN, null);
//                    }
            }
        }, 2000);
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
