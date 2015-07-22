package com.nvurgaft.redmonk.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nvurgaft.redmonk.Values;

/**
 * Created by Koby on 02-Jul-15.
 */
public class ConnectionManager {

    private static SqlAccess sqlAccess;

    public static SQLiteDatabase getConnection(Context context) {
        if (sqlAccess==null) {
            sqlAccess = new SqlAccess(context);
        }
        return sqlAccess.getWritableDatabase();
    }

    public static void closeConnection() {
        try {
            if (sqlAccess != null) {
                sqlAccess.close();
                Log.d(Values.LOG, "Closing SQLite Connection");
                sqlAccess = null;
            }
        } catch (Exception ex) {
            Log.e(Values.LOG, "Error encountered while closing SQLite connection", ex);
        }
    }

    public static void closeConnectionSilently() {
        sqlAccess.close();
        sqlAccess = null;
    }

    public static SqlAccess getSqlAccess() {
        return sqlAccess;
    }
}
