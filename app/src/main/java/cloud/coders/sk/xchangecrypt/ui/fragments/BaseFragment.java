package cloud.coders.sk.xchangecrypt.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ProgressBar;

import cloud.coders.sk.xchangecrypt.core.ContentProvider;
import cloud.coders.sk.xchangecrypt.core.FragmentsManager;
import cloud.coders.sk.xchangecrypt.listeners.DialogOkClickListener;
import cloud.coders.sk.xchangecrypt.listeners.FragmentSwitcherInterface;
import cloud.coders.sk.xchangecrypt.ui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements FragmentSwitcherInterface {


    protected View rootView;

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setActionBar();
        }
    }

    protected void showErrorDialog(int message) {
        ((MainActivity) getActivity()).showErrorDialog(message);
    }


    protected void hideKeyboard() {
        ((MainActivity) getActivity()).hideKeyboard();
    }

    public void switchToFragment(int fragmentID, Bundle args) {
        ((MainActivity) getActivity()).switchToFragment(fragmentID, args);
    }

    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        ((MainActivity) getActivity()).switchToFragmentAndClear(fragmentID, args);
    }

    public ContentProvider getContentProvider() {
        return ((MainActivity) getActivity()).getContentProvider();
    }
    public void getDataBeforeSwitch(int fragmentID, Bundle args){};


//    public DBHelper getDBHelper(){
//        return ((MainActivity)getActivity()).getDBHelper();
//    }

    public FragmentsManager getFragmentsManager() {
        return ((MainActivity) getActivity()).getFragmentsManager();
    }


//    public FeedManager getFeedManager(){
//        return ((MainActivity)getActivity()).getFeedManager();
//    }

    public void showProgressBar(int title, int message) {
        ((MainActivity) getActivity()).showProgressDialog(title, message);
        ;
    }

    public void showProgressBar(String message) {
        ((MainActivity) getActivity()).showProgressDialog(message);
        ;
    }

    public void hideProgressBar() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    public void showDialogWindow(int message) {
        ((MainActivity) getActivity()).showDialogWindow(message);
    }

    public void showDialogWithAction(int message, final DialogOkClickListener listener, boolean showNegativeButton) {
        ((MainActivity) getActivity()).showDialogWithAction(message, listener, showNegativeButton);
    }

//    public MenuAdapter getMenuAdapter() {
//        return ((MainActivity) getActivity()).getMenuAdapter();
//    }

    protected void disableGestures() {
        ((MainActivity) getActivity()).disableGestures();
    }

    protected void enableGestures() {
        ((MainActivity) getActivity()).enableGestures();
    }

    public void showTopFragmentInMainBS() {
        getFragmentsManager().showTopFragmentInMainBS();
    }

    public void showKeyboard(View view) {
        ((MainActivity) getActivity()).showKeyboard(view);
    }

    public void hideKeyboard(View view) {
        ((MainActivity) getActivity()).hideKeyboard(view);
    }

//    protected void changeToolbarColor(int color) {
//        ((MainActivity) getActivity()).changeToolbarColor(color);
//    }

    protected void changeStatusBarColor(int color) {
        ((MainActivity) getActivity()).changeStatusBarColor(color);
    }

    public FragmentManager getSupportFragmentManager() {
        return ((MainActivity) getActivity()).getSupportFragmentManager();
    }

    public ActionBar getSupportActionBar() {
        return ((MainActivity) getActivity()).getSupportActionBar();
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
        ((MainActivity) getActivity()).hideActionBar();
    }

    public void showActionBar() {
        ((MainActivity) getActivity()).showActionBar();
    }

    public void hideStatusBar() {
        ((MainActivity) getActivity()).hideStatusBar();
    }

    public void showStatusBar() {
        ((MainActivity) getActivity()).showStatusBar();
    }

    public void hideButtonLayout(){
        ((MainActivity) getActivity()).changeBottomNavigationVisibility(View.GONE);
    }

    public void showButtonLayout(){
        ((MainActivity) getActivity()).changeBottomNavigationVisibility(View.VISIBLE);
    }

    public void setToolbarTitle (String text){
        ((MainActivity) getActivity()).setToolbarTitle(text);
    }

    public boolean customOnBackPressed() {
        return true;
    }

    public void onBackPressed() {
        ((MainActivity) getActivity()).onBackPressed();
    }

    public void toggleDrawerButtonHamburger() {
        ((MainActivity) getActivity()).toggleDrawerButtonHamburger();
    }

    public void toggleDrawerButtonArrow() {
        ((MainActivity) getActivity()).toggleDrawerButtonArrow();
    }

}
