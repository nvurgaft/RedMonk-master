package com.nvurgaft.redmonk;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nvurgaft.redmonk.Adapters.ContactsCursorAdapter;
import com.nvurgaft.redmonk.Entities.Contact;
import com.nvurgaft.redmonk.Model.ConnectionManager;
import com.nvurgaft.redmonk.Model.SqlAccess;


/**
 * The configuration screen for the {@link RedMonkEmergencyWidget RedMonkEmergencyWidget} AppWidget.
 */
public class RedMonkEmergencyWidgetConfigureActivity extends Activity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    //EditText mAppWidgetText;
    protected ListView chooseContactListView;

    protected ContactsCursorAdapter contactsCursorAdapter;
    protected SqlAccess sqlAccess;
    protected SQLiteDatabase db;

    private static final String PREFS_NAME = "com.nvurgaft.redmonk.RedMonkEmergencyWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    public RedMonkEmergencyWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.red_monk_emergency_widget_configure);
        //mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        chooseContactListView = (ListView) findViewById(R.id.selectContactListView);
        findViewById(R.id.leave_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        sqlAccess = new SqlAccess(this);
        db = ConnectionManager.getConnection(this);
        contactsCursorAdapter = new ContactsCursorAdapter(this, sqlAccess.getContactsCursor(db));

        chooseContactListView.setAdapter(contactsCursorAdapter);
        chooseContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = (Cursor) parent.getItemAtPosition(position);

                Contact contact = new Contact();
                contact.setContactName(data.getString(1));
                contact.setContactRole(data.getString(2));
                contact.setFirstNumber(data.getString(3));
                contact.setSecondNumber(data.getString(4));
                contact.setThirdNumber(data.getString(5));

                saveTitlePref(getBaseContext(), mAppWidgetId, contact);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
                RedMonkEmergencyWidget.updateAppWidget(getBaseContext(), appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });

        //mAppWidgetText.setText(loadTitlePref(RedMonkEmergencyWidgetConfigureActivity.this, mAppWidgetId));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, Contact contact) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_name", contact.getContactName());
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_role", contact.getContactRole());
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_first", contact.getFirstNumber());
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_second", contact.getSecondNumber());
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_third", contact.getThirdNumber());
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId, String suffix) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId + suffix, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}

