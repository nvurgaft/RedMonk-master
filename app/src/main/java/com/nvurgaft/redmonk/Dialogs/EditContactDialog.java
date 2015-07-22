package com.nvurgaft.redmonk.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.nvurgaft.redmonk.Entities.Contact;
import com.nvurgaft.redmonk.R;

/**
 * Created by Koby on 29-Jun-15.
 */
public class EditContactDialog extends DialogFragment {

    protected NoticeDialogListener mListener;

    protected EditText contactNameEditText, contactRoleEditText, firstNumberEditText, secondNumberEditText, thirdNumberEditText;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, Contact contact, boolean isEdit);

        public void onDialogNegativeClick(DialogFragment dialog, Contact contact, boolean isEdit);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.contacts_dialog_fragment_view, null);

        contactNameEditText = (EditText) dialogView.findViewById(R.id.dialogContactEditText);
        contactRoleEditText = (EditText) dialogView.findViewById(R.id.dialogContactRoleEditText);
        firstNumberEditText = (EditText) dialogView.findViewById(R.id.dialogFirstNumberEditText);
        secondNumberEditText = (EditText) dialogView.findViewById(R.id.dialogSecondNumberEditText);
        thirdNumberEditText = (EditText) dialogView.findViewById(R.id.dialogThirdNumberEditText);

        boolean isEdit = false;
        String name = "";
        String role = "";
        String number1 = "";
        String number2 = "";
        String number3 = "";

        Bundle passedBundle = getArguments();
        if (passedBundle != null) {
            isEdit = passedBundle.getBoolean("isEdit", false);
            name = passedBundle.getString("name", "N/A");
            role = passedBundle.getString("role", "N/A");
            number1 = passedBundle.getString("number1", "N/A");
            number2 = passedBundle.getString("number2", "N/A");
            number3 = passedBundle.getString("number3", "N/A");
        }

        final boolean action = isEdit;
        contactNameEditText.setText(name == null ? "" : name);
        contactRoleEditText.setText(role == null ? "" : role);
        firstNumberEditText.setText(number1 == null ? "" : number1);
        secondNumberEditText.setText(number2 == null ? "" : number2);
        thirdNumberEditText.setText(number3 == null ? "" : number3);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (isEdit) {
            builder.setMessage(R.string.contacts_dialog_title_edit);
        } else {
            builder.setMessage(R.string.contacts_dialog_title_create);
        }
        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dialogContact = contactNameEditText.getText().toString();
                        String dialogContactRole = contactRoleEditText.getText().toString();
                        String dialogFirstNumber = firstNumberEditText.getText().toString();
                        String dialogSecondNumber = secondNumberEditText.getText().toString();
                        String dialogThirdNumber = thirdNumberEditText.getText().toString();

                        Contact contact = new Contact();
                        contact.setContactName(dialogContact);
                        contact.setContactRole(dialogContactRole);
                        contact.setFirstNumber(dialogFirstNumber);
                        contact.setSecondNumber(dialogSecondNumber);
                        contact.setThirdNumber(dialogThirdNumber);

                        // send the contact back to the activity to be registered
                        mListener.onDialogPositiveClick(EditContactDialog.this, contact, action);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick(EditContactDialog.this, null, false);
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
