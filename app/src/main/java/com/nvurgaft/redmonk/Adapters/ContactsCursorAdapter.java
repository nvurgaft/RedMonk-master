package com.nvurgaft.redmonk.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nvurgaft.redmonk.R;

/**
 * Created by Koby on 28-Jun-15.
 */
public class ContactsCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public ContactsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.contact_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView contactNameTextView = (TextView) view.findViewById(R.id.contact_item_name);
        TextView contactRoleTextView = (TextView) view.findViewById(R.id.contact_item_role);
        TextView contactNumbersTextView = (TextView) view.findViewById(R.id.contact_item_numbers);

        contactNameTextView.setText(new StringBuilder().append(cursor.getString(0)).append(". ").append(cursor.getString(1)).toString());
        contactRoleTextView.setText(cursor.getString(2));

        StringBuilder sb = new StringBuilder();
        for (int i=3;i<=5; i++) {
            if (!cursor.getString(i).isEmpty()) {
                sb.append(cursor.getString(i));
                sb.append(" | ");
            }
        }

        contactNumbersTextView.setText(sb.toString());
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
    }
}
