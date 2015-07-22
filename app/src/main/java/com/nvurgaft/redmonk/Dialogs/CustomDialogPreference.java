package com.nvurgaft.redmonk.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.nvurgaft.redmonk.Model.ConnectionManager;
import com.nvurgaft.redmonk.Model.SqlAccess;
import com.nvurgaft.redmonk.Values;

/**
 * Created by Koby on 19-Jul-15.
 */
public class CustomDialogPreference extends DialogPreference implements DialogInterface.OnClickListener {

    private String keyname, keyvalue;

    public CustomDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        for (int i=0; i<attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (attrName.equals("key")) {
                keyname = attrName;
                keyvalue = attrValue;
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which){

        if(which == DialogInterface.BUTTON_POSITIVE) {
            // do your stuff to handle positive button
            SqlAccess sqlAccess = new SqlAccess(getContext());
            SQLiteDatabase db = ConnectionManager.getConnection(getContext());

            switch (keyvalue) {
                case "pref_key_clear_contacts":
                    sqlAccess.deleteAllContacts(db);
                    Toast.makeText(getContext(), "contacts purged", Toast.LENGTH_SHORT).show();
                    break;
                case "pref_key_clear_reminders":
                    sqlAccess.deleteAllReminders(db);
                    Toast.makeText(getContext(), "reminders purged", Toast.LENGTH_SHORT).show();
                    break;
                case "pref_key_clear_dc":
                    sqlAccess.deleteAllConsumptionLogs(db);
                    Toast.makeText(getContext(), "consumption logs purged", Toast.LENGTH_SHORT).show();
                    break;
                case "pref_key_clear_all":
                    sqlAccess.deleteAllContacts(db);
                    sqlAccess.deleteAllReminders(db);
                    sqlAccess.deleteAllConsumptionLogs(db);
                    Toast.makeText(getContext(), "user data purged", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(Values.LOG, "invalid preference key supplied to custom dialog preference");
            }

            if (db!=null && db.isOpen()) {
                db.close();
            }
            if (sqlAccess!=null) {
                sqlAccess.close();
            }

        } else if(which == DialogInterface.BUTTON_NEGATIVE){
            // do your stuff to handle negative button
            Toast.makeText(getContext(), "no: " + which , Toast.LENGTH_SHORT).show();
        }
    }
}
