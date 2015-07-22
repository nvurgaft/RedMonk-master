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

import com.nvurgaft.redmonk.Entities.DailyConsumption;
import com.nvurgaft.redmonk.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Koby on 18-Jul-15.
 */
public class EditDailyConsumptionDialog extends DialogFragment {

    protected NoticeDialogListener mListener;

    protected EditText caloriesEditText, carbsEditText, proteinsEditText, fatsEditText;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, DailyConsumption dailyConsumption, boolean isEdit);

        public void onDialogNegativeClick(DialogFragment dialog, DailyConsumption dailyConsumption, boolean isEdit);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.daily_consumption_dialog_fragment_view, null);

        caloriesEditText = (EditText) dialogView.findViewById(R.id.caloriesEditText);
        carbsEditText = (EditText) dialogView.findViewById(R.id.carbsEditText);
        proteinsEditText = (EditText) dialogView.findViewById(R.id.proteinsEditText);
        fatsEditText = (EditText) dialogView.findViewById(R.id.fatsEditText);

        boolean isEdit = false;
        long cDate = 0L;
        int calories = 0;
        int carbs = 0;
        int proteins = 0;
        int fats = 0;

        Bundle passedBundle = getArguments();
        if (passedBundle != null) {
            isEdit = passedBundle.getBoolean("isEdit", false);
            cDate = passedBundle.getLong("date", 0);
            calories = passedBundle.getInt("calories", 0);
            carbs = passedBundle.getInt("carbs", 0);
            proteins = passedBundle.getInt("proteins", 0);
            fats = passedBundle.getInt("fats", 0);
        }

        final boolean action = isEdit;
        caloriesEditText.setText(String.valueOf(calories ));
        carbsEditText.setText(String.valueOf(carbs));
        proteinsEditText.setText(String.valueOf(proteins));
        fatsEditText.setText(String.valueOf(fats));

        final boolean fEdit = isEdit;
        final long fDate = cDate;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (isEdit) {
            builder.setMessage(R.string.dc_enter_dc);
        } else {
            builder.setMessage(R.string.dc_edit_dc);
        }
        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String cals = caloriesEditText.getText().toString();
                        String carbs = carbsEditText.getText().toString();
                        String prots = proteinsEditText.getText().toString();
                        String fats = fatsEditText.getText().toString();

                        DailyConsumption dailyConsumption = new DailyConsumption();
                        if (!fEdit) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(System.currentTimeMillis()));
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            dailyConsumption.setDate(cal.getTimeInMillis());
                        } else {
                            dailyConsumption.setDate(fDate);
                        }
                        dailyConsumption.setCalories(Integer.valueOf(cals));
                        dailyConsumption.setCarbohydrates(Integer.valueOf(carbs));
                        dailyConsumption.setProteins(Integer.valueOf(prots));
                        dailyConsumption.setFats(Integer.valueOf(fats));

                        mListener.onDialogPositiveClick(EditDailyConsumptionDialog.this, dailyConsumption, action);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick(EditDailyConsumptionDialog.this, null, false);
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
