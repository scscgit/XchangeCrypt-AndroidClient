package bit.xchangecrypt.client.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import bit.xchangecrypt.client.core.ContentProvider;
import bit.xchangecrypt.client.listeners.DialogOkClickListener;
import bit.xchangecrypt.client.listeners.FragmentSwitcherInterface;
import bit.xchangecrypt.client.ui.MainActivity;
import bit.xchangecrypt.client.util.FragmentsManager;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements FragmentSwitcherInterface {
    protected View rootView;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View getRootView() {
        return rootView;
    }

    protected abstract void setActionBar();

    protected abstract void setViews();

    protected abstract void setViewContents();

    public MainActivity getMainActivity() {
        return (MainActivity) context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setActionBar();
        }
    }

    protected void showErrorDialog(int message) {
        getMainActivity().showErrorDialog(message);
    }

    protected void hideKeyboard() {
        getMainActivity().hideKeyboard();
    }

    public void switchToFragment(int fragmentID, Bundle args) {
        getMainActivity().switchToFragment(fragmentID, args);
    }

    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        getMainActivity().switchToFragmentAndClear(fragmentID, args);
    }

    public ContentProvider getContentProvider() {
        return ContentProvider.getInstance(getMainActivity());
    }

    public void getDataBeforeSwitch(int fragmentID, Bundle args, boolean force) {
    }

    public FragmentsManager getFragmentsManager() {
        return getMainActivity().getFragmentsManager();
    }

    public void showProgressBar(String message) {
        getMainActivity().showProgressDialog(message);
    }

    public void hideProgressBar() {
        getMainActivity().hideProgressDialog();
    }

    public void showDialogWindow(int message) {
        getMainActivity().showDialogWindow(message);
    }

    public void showDialogWithAction(String message, final DialogOkClickListener listener, boolean showNegativeButton) {
        getMainActivity().showDialogWithAction(message, listener, showNegativeButton);
    }

    protected void disableGestures() {
        getMainActivity().disableGestures();
    }

    protected void enableGestures() {
        getMainActivity().enableGestures();
    }

    public void showTopFragmentInMainBS() {
        getFragmentsManager().showTopFragmentInMainBS();
    }

    public void showKeyboard(View view) {
        getMainActivity().showKeyboard(view);
    }

    public void hideKeyboard(View view) {
        getMainActivity().hideKeyboard(view);
    }

    protected void changeStatusBarColor(int color) {
        getMainActivity().changeStatusBarColor(color);
    }

    public FragmentManager getSupportFragmentManager() {
        return getMainActivity().getSupportFragmentManager();
    }

    public ActionBar getSupportActionBar() {
        return getMainActivity().getSupportActionBar();
    }

    public void startLoading(ProgressBar progress, View view) {
        view.animate().alpha(0f).setDuration(500);
        if (progress.getVisibility() != View.VISIBLE) {
            progress.setVisibility(View.VISIBLE);
            progress.animate().alpha(1f).setDuration(500);
        }
    }

    public void stopLoading(final ProgressBar progress, View view) {
        progress.animate().alpha(0f).setDuration(200);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setStartDelay(200).setDuration(200);
    }

    public void hideActionBar() {
        getMainActivity().hideActionBar();
    }

    public void showActionBar() {
        getMainActivity().showActionBar();
    }

    public void hideStatusBar() {
        getMainActivity().hideStatusBar();
    }

    public void showStatusBar() {
        getMainActivity().showStatusBar();
    }

    public void hideButtonLayout() {
        getMainActivity().changeBottomNavigationVisibility(View.GONE);
    }

    public void showButtonLayout() {
        getMainActivity().changeBottomNavigationVisibility(View.VISIBLE);
    }

    public void setToolbarTitle(String text) {
        getMainActivity().setToolbarTitle(text);
    }

    public boolean customOnBackPressed() {
        return true;
    }

    public void onBackPressed() {
        getMainActivity().onBackPressed();
    }

    public void toggleDrawerButtonHamburger() {
        getMainActivity().toggleDrawerButtonHamburger();
    }

    public void toggleDrawerButtonArrow() {
        getMainActivity().toggleDrawerButtonArrow();
    }

    public static String formatNumber(double number) {
        return MainActivity.formatNumber(number);
    }
}
