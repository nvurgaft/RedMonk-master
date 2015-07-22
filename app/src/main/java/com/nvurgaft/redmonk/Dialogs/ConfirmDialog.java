package com.nvurgaft.redmonk.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nvurgaft.redmonk.R;

/**
 * Created by Koby on 17-Jul-15.
 */
public class ConfirmDialog extends DialogFragment {

    protected NoticeDialogListener mListener;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String identifier, String tag);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.confirm_dialog_view, null);

        String message = "Confirm Action";
        String cName = "";
        String tag = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            message = bundle.getString("content", "N/A");
            cName = bundle.getString("identifier", "");
            tag = bundle.getString("tag", "");
        }

        final String identifier = cName.trim();
        final String action = tag;

        TextView msgTextView = (TextView) dialogView.findViewById(R.id.confirm_dialog_view_text);
        msgTextView.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_dialog_title)
                .setView(dialogView)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(ConfirmDialog.this, identifier, action);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick(ConfirmDialog.this);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
