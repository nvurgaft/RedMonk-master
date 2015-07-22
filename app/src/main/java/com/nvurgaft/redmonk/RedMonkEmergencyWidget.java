package com.nvurgaft.redmonk;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RedMonkEmergencyWidgetConfigureActivity RedMonkEmergencyWidgetConfigureActivity}
 */
public class RedMonkEmergencyWidget extends AppWidgetProvider {

    static int widgetId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            RedMonkEmergencyWidgetConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        widgetId = appWidgetId;

        String cName = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, appWidgetId, "_name");
        String cRole = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, appWidgetId, "_role");
        String cFirst = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, appWidgetId, "_first");
        String cSecond = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, appWidgetId, "_second");
        String cThird = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, appWidgetId, "_third");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.red_monk_emergency_widget);
        views.setTextViewText(R.id.nameAndRoleTextView, cName + " - " + cRole);
        views.setTextViewText(R.id.firstContactTextView, cFirst);
        views.setTextViewText(R.id.secondContactTextView, cSecond);
        views.setTextViewText(R.id.thirdContactTextView, cThird);

        // call intent
        Intent callIntent1 = new Intent(context, RedMonkEmergencyWidget.class);
        callIntent1.setAction("CALL_1");
        Intent callIntent2 = new Intent(context, RedMonkEmergencyWidget.class);
        callIntent2.setAction("CALL_2");
        Intent callIntent3 = new Intent(context, RedMonkEmergencyWidget.class);
        callIntent3.setAction("CALL_3");

        PendingIntent pendingCallIntent1 = PendingIntent.getBroadcast(context, 0, callIntent1, 0);
        PendingIntent pendingCallIntent2 = PendingIntent.getBroadcast(context, 0, callIntent2, 0);
        PendingIntent pendingCallIntent3 = PendingIntent.getBroadcast(context, 0, callIntent3, 0);

        views.setOnClickPendingIntent(R.id.callButton1, pendingCallIntent1);
        views.setOnClickPendingIntent(R.id.callButton2, pendingCallIntent2);
        views.setOnClickPendingIntent(R.id.callButton3, pendingCallIntent3);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String cFirst = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, widgetId, "_first");
        String cSecond = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, widgetId, "_second");
        String cThird = RedMonkEmergencyWidgetConfigureActivity.loadTitlePref(context, widgetId, "_third");

        Intent i = new Intent(Intent.ACTION_CALL);

        switch (intent.getAction()) {
            case "CALL_1":
                i.setData(Uri.parse("tel:" + cFirst));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case "CALL_3":
                i.setData(Uri.parse("tel:" + cSecond));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case "CALL_2":
                i.setData(Uri.parse("tel:" + cThird));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            default:
                Log.d(Values.LOG, "Invalid parameters to start call activity");
        }
        super.onReceive(context, intent);
    }
}
