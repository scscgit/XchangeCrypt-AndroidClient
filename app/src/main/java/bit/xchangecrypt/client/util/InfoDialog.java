package bit.xchangecrypt.client.util;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.core.Constants;

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
