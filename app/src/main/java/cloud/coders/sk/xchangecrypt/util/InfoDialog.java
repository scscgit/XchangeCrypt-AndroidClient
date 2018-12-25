package cloud.coders.sk.xchangecrypt.util;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.core.Constants;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
@Deprecated
public class InfoDialog extends DialogFragment {
    String mTitle, mMessage, mButtonText;

    public static InfoDialog newInstance(Bundle args) {
        InfoDialog dialog = new InfoDialog();
        dialog.setArguments(args);
        dialog.mTitle = args.getString(Constants.INFODIALOG_TITLE);
        dialog.mMessage = args.getString(Constants.INFODIALOG_MESSAGE);
        dialog.mButtonText = args.getString(Constants.INFODIALOG_BUTTON_TEXT);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (null != mTitle && !mTitle.equals("")) {
            builder.setTitle(mTitle);
        }
        builder.setMessage(mMessage);
        if (null != mButtonText && !mButtonText.equals("")) {
            builder.setPositiveButton(mButtonText, null);
        } else {
            builder.setPositiveButton(R.string.ok_btn, null);
        }
        return builder.create();
    }
}
