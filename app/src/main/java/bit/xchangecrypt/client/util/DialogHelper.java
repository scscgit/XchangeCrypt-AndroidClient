package bit.xchangecrypt.client.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.ui.MainActivity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class DialogHelper {
    public static void yesNoConfirmationDialog(final Context context, String title, String message, final Runnable action) {
        choiceDialog(context, title, message, action, null, null, null, false);
    }

    public static AlertDialog choiceDialog(final Context context, String title, String message, final Runnable yesAction, final Runnable neutralAction, CharSequence yesButton, CharSequence neutralButton, boolean cancelable) {
        return choiceDialogBuilder(context, title, message, yesAction, neutralAction, yesButton, neutralButton, cancelable).show();
    }

    public static AlertDialog.Builder choiceDialogBuilder(final Context context, String title, String message, final Runnable yesAction, final Runnable noAction, CharSequence yesButton, CharSequence noButton, boolean cancelable) {
        Builder builder = new Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_dialog_dialer)
            .setPositiveButton(yesButton == null ? context.getString(R.string.positive_btn) : yesButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (whichButton == BUTTON_POSITIVE) {
                        yesAction.run();
                    }
                }
            })
            .setNegativeButton(noButton == null ? context.getString(R.string.negative_btn) : noButton, noAction == null ? null : new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    noAction.run();
                }
            });
        if (cancelable) {
            builder = builder.setNeutralButton(context.getString(R.string.cancel_btn), noAction == null ? null : new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        return builder;
    }

    public static void alertDialog(final MainActivity context, String title, String message) {
        context.runOnUiThread(() -> {
            new Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
        });
    }
}
