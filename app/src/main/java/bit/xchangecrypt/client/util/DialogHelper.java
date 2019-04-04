package bit.xchangecrypt.client.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import bit.xchangecrypt.client.ui.MainActivity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class DialogHelper {
    public static void confirmationDialog(final Context context, String title, String message, final Runnable action) {
        confirmationDialog(context, title, message, action, null, null, null);
    }

    public static AlertDialog confirmationDialog(final Context context, String title, String message, final Runnable yesAction, final Runnable noAction, CharSequence yesButton, CharSequence noButton) {
        return new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_dialog_dialer)
            .setPositiveButton(yesButton == null ? context.getString(android.R.string.yes) : yesButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (whichButton == BUTTON_POSITIVE) {
                        yesAction.run();
                    }
                }
            })
            .setNegativeButton(noButton == null ? context.getString(android.R.string.no) : noButton, noAction == null ? null : new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    noAction.run();
                }
            })
            .show();
    }

    public static void alertDialog(final MainActivity context, String title, String message) {
        context.runOnUiThread(() -> {
            new AlertDialog.Builder(context)
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
