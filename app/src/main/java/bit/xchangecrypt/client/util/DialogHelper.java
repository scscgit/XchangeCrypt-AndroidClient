package bit.xchangecrypt.client.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import bit.xchangecrypt.client.ui.MainActivity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class DialogHelper {
    public static void confirmationDialog(final Context context, String title, String message, final Runnable action) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_dialog_dialer)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (whichButton == BUTTON_POSITIVE) {
                        action.run();
                    }
                }
            })
            .setNegativeButton(android.R.string.no, null)
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
