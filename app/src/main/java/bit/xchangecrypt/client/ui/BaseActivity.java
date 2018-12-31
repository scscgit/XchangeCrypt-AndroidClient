package bit.xchangecrypt.client.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.core.ContentProvider;
import bit.xchangecrypt.client.listeners.ConnectionListener;
import bit.xchangecrypt.client.listeners.DialogOkClickListener;
import bit.xchangecrypt.client.util.ApplicationStorage;
import bit.xchangecrypt.client.util.FragmentsManager;

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected RelativeLayout content;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    protected LinearLayout mDrawerView;
    protected ListView mDrawerMenu;
    protected BottomNavigationView bottomNavigationView;
    protected BroadcastReceiver networkStateReceiver;
    private static ProgressDialog progress;
    private AlertDialog alertDialog;
    private boolean isOnline;
    private ConnectionListener connectionListener;
    protected LinearLayout bottomNavigationLayout;

    public void showErrorDialog(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
            .setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDialogWindow(int message) {
        if (alertDialog != null && alertDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showDialogWithAction(int message, final DialogOkClickListener listener, boolean showNegativeButton) {
        if (alertDialog != null && alertDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(showNegativeButton ? R.string.positive_btn : R.string.ok_btn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onPositiveButtonClicked(BaseActivity.this);
                }
            });
        if (showNegativeButton)
            builder.setNegativeButton(R.string.negative_btn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        alertDialog = builder.create();
        alertDialog.show();
    }

    /*
    ProgressDialog progressDialog = null;
    public void showProgressDialog( CharSequence title, CharSequence message){
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public void closeProgressDialog( ){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
    */

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public ContentProvider getContentProvider() {
        return ContentProvider.getInstance(this);
    }

    public ApplicationStorage getAppStorage() {
        return ApplicationStorage.getInstance(this);
    }

    public FragmentsManager getFragmentsManager() {
        return FragmentsManager.getInstance(this);
    }

    public void showTopFragmentInMainBS() {
        getFragmentsManager().showTopFragmentInMainBS();
    }

    public void showProgressDialog(int title, int message) {
        if (progress != null) {
            hideProgressDialog();
        }
        progress = ProgressDialog.show(
            this, null, getResources().getString(message), true);
    }

    public void showProgressDialog(String message) {
        if (progress != null) {
            hideProgressDialog();
        }
        progress = ProgressDialog.show(this, null, message, true);
    }

    public void hideProgressDialog() {
        if (progress != null) {
            progress.dismiss();
        }
        progress = null;
    }

    public void disableGestures() {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enableGestures() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideActionBarImmediately() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.setVisibility(View.GONE);
    }

    public void hideActionBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.animate().translationY(-getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material)).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void showActionBar() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public void hideBottomNavigationView() {
        collapse(bottomNavigationLayout, 0);
    }

    public void showBottomNavigationView() {
        expand(bottomNavigationLayout, 500);
    }

    public void hideActionBarWithAnimation() {
        collapse(toolbar, 500);
    }

    public void hideActionBarWithoutAnimation() {
        collapse(toolbar, 0);
    }

    public void showActionBarWithAnimation() {
        expand(toolbar, 500);
    }

    public void showActionBarWithoutAnimation() {
        expand(toolbar, 0);
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void expand(final View v, final int duration) {
        if (duration == 0) {
            v.setVisibility(View.VISIBLE);
            return;
        }
        if (v.getVisibility() != View.VISIBLE) {
            v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();
            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration(duration);
            v.startAnimation(a);
        }
    }

    public void collapse(final View v, final int duration) {
        final int initialHeight = v.getMeasuredHeight();
        if (duration != 0) {
            if (v.getVisibility() != View.GONE) {
                Animation a = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        if (interpolatedTime == 1) {
                            v.setVisibility(View.GONE);
                        } else {
                            v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                            v.requestLayout();
                        }
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };
                // 1dp/ms
                a.setDuration(duration);
                //a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
                v.startAnimation(a);
            }
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setContentMarginOn() {
        if (content == null) {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content.getLayoutParams();
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        content.setLayoutParams(params);
    }

    public void setContentMarginOff() {
        if (content == null) {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content.getLayoutParams();
        params.topMargin = 0;
        content.setLayoutParams(params);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
