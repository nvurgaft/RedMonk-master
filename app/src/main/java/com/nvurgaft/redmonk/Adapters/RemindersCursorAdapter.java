package com.nvurgaft.redmonk.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nvurgaft.redmonk.Logic.AlarmManagerBroadcastReceiver;
import com.nvurgaft.redmonk.Model.ConnectionManager;
import com.nvurgaft.redmonk.Model.SqlAccess;
import com.nvurgaft.redmonk.R;

import java.util.Calendar;

/**
 * Created by Koby on 02-Jul-15.
 */
public class RemindersCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;
    protected AlarmManager alarmManager;
    protected AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver;
    protected Intent alarmManagerIntent;
    protected PendingIntent pendingIntent;

    SqlAccess sqlAccess;
    SQLiteDatabase db;


    public RemindersCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = LayoutInflater.from(context);
        alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.reminder_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView reminderTimeTextView = (TextView) view.findViewById(R.id.reminderTimeTextView);
        TextView reminderTodoTextView = (TextView) view.findViewById(R.id.reminderTodoTextView);
        CheckBox resolvedCheckBox = (CheckBox) view.findViewById(R.id.resolveCheckBox);
        final Context cContext = context;
        final Cursor cCursr = cursor;
        resolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                sqlAccess = new SqlAccess(cContext);
                db = ConnectionManager.getConnection(cContext);
                if (isChecked) {
                    sqlAccess.setResolved(db, cCursr.getLong(1), true);
                    buttonView.setText(buttonView.getContext().getText(R.string.resolved));
                } else {
                    sqlAccess.setResolved(db, cCursr.getLong(2), false);
                    buttonView.setText(buttonView.getContext().getText(R.string.unresolved));
                }
                db.close();
            }
        });

        long r_id = cursor.getLong(1);
        String time_hour = cursor.getString(2);
        String time_minute = cursor.getString(3);
        String todo = cursor.getString(4);
        String resolvedString = cursor.getString(5);
        boolean resolved = Boolean.valueOf(resolvedString);
        int hour = Integer.valueOf(time_hour);
        int minute = Integer.valueOf(time_minute);

        resolvedCheckBox.setChecked(resolved);
        if (resolved) {
            resolvedCheckBox.setText(context.getText(R.string.resolved));
            cancelReminder(context, (int) r_id);
        } else {
            resolvedCheckBox.setText(context.getText(R.string.unresolved));
            createReminder(context, (int) r_id, hour, minute); // it's a schedule bug
        }

        StringBuilder sb = new StringBuilder();
        sb.append(time_hour).append(":");
        if (String.valueOf(time_minute).length() == 1) {
            sb.append(0);
        }
        sb.append(time_minute);
        if (Integer.valueOf(time_hour) > 12) {
            sb.append(" (").append(hour - 12).append(":");
            sb.append(String.valueOf(time_minute).length() == 1 ? 0 : "").append(minute);
            sb.append(" PM)");
        } else {
            sb.append(" (").append(hour).append(":");
            sb.append(String.valueOf(time_minute).length() == 1 ? 0 : "").append(minute);
            sb.append(" AM)");
        }

        reminderTimeTextView.setText(sb.toString());
        reminderTodoTextView.setText(todo);
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
    }

    public void createReminder(Context context, int alarmId, int hour, int minute) {

        alarmManagerIntent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, alarmId, alarmManagerIntent, 0);
        Calendar firingCalendar = Calendar.getInstance();
        Calendar currentCalendar = Calendar.getInstance();

        firingCalendar.set(Calendar.HOUR_OF_DAY, hour);
        firingCalendar.set(Calendar.MINUTE, minute);

        if (firingCalendar.compareTo(currentCalendar) <0) {
            firingCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Long intendedTime = firingCalendar.getTimeInMillis();

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelReminder(Context context, int alarmId) {

        if (pendingIntent!=null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }
}
