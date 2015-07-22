package com.nvurgaft.redmonk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.nvurgaft.redmonk.Adapters.RemindersCursorAdapter;
import com.nvurgaft.redmonk.Dialogs.ConfirmDialog;
import com.nvurgaft.redmonk.Dialogs.EditReminderDialog;
import com.nvurgaft.redmonk.Model.ConnectionManager;
import com.nvurgaft.redmonk.Model.SqlAccess;
import com.nvurgaft.redmonk.OnFragmentInteractionListener;
import com.nvurgaft.redmonk.R;
import com.nvurgaft.redmonk.Values;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RemindersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemindersFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected SQLiteDatabase db;
    private OnFragmentInteractionListener mListener;

    private SqlAccess sqlAccess;
    private RemindersCursorAdapter remindersCursorAdapter;
    private ListView listView;
    private Button newReminderButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reminders.
     */
    // TODO: Rename and change types and number of parameters
    public static RemindersFragment newInstance(String param1, String param2) {
        RemindersFragment fragment = new RemindersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RemindersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reminders, container, false);

        sqlAccess = new SqlAccess(getActivity());
        db = ConnectionManager.getConnection(getActivity());
        remindersCursorAdapter = new RemindersCursorAdapter(getActivity(), sqlAccess.getRemindersCursor(db));

        listView = (ListView) view.findViewById(R.id.remindersListView);
        listView.setAdapter(remindersCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = (Cursor) parent.getItemAtPosition(position);

                //Toast.makeText(getActivity(), "clicked at reminder " + data.getInt(1), Toast.LENGTH_SHORT).show();
                // open a dialog to edit selected reminder
                EditReminderDialog editReminderDialog = new EditReminderDialog();

                Bundle selectedReminderBundle = new Bundle();
                selectedReminderBundle.putBoolean("isEdit", true);
                selectedReminderBundle.putLong("rid", data.getLong(1));
                selectedReminderBundle.putInt("hour", data.getInt(2));
                selectedReminderBundle.putInt("minute", data.getInt(3));
                selectedReminderBundle.putString("todo", data.getString(4));
                selectedReminderBundle.putString("resolved", data.getString(5));

                CheckBox cb = (CheckBox) view.findViewById(R.id.resolveCheckBox);


                // show the edit reminder dialog fragment
                editReminderDialog.setArguments(selectedReminderBundle);
                editReminderDialog.show(getFragmentManager(), "EditReminderDialog");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = (Cursor) parent.getItemAtPosition(position);

                //Toast.makeText(getActivity(), "long clicked at reminder " + data.getString(1), Toast.LENGTH_SHORT).show();
                ConfirmDialog confirmDialog = new ConfirmDialog();
                long reminderId = data.getLong(1);

                Bundle confirmDialogBundle = new Bundle();
                confirmDialogBundle.putString("content", getString(R.string.confirm_delete_reminder_text));
                confirmDialogBundle.putString("identifier", String.valueOf(id));
                confirmDialogBundle.putString("tag", "reminderPrompt");
                confirmDialog.setArguments(confirmDialogBundle);
                confirmDialog.show(getFragmentManager(), "ConfirmDialog");
                return true;
            }
        });
        listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                db = ConnectionManager.getConnection(getActivity());
                if (remindersCursorAdapter != null) {
                    remindersCursorAdapter.changeCursor(sqlAccess.getRemindersCursor(db));
                    remindersCursorAdapter.notifyDataSetChanged();
                }
                db.close();
            }
        });

        newReminderButton = (Button) view.findViewById(R.id.newReminderButton);
        newReminderButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        db = ConnectionManager.getConnection(getActivity());
        remindersCursorAdapter.changeCursor(sqlAccess.getRemindersCursor(db));
        remindersCursorAdapter.notifyDataSetChanged();
        db.close();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newReminderButton:
                EditReminderDialog editReminderDialog = new EditReminderDialog();
                editReminderDialog.show(getFragmentManager(), "EditReminderDialog");
                break;
            default:
                Log.d(Values.LOG, "Invalid value selected");
        }
    }

    public void refreshFragmentView() {
        if (remindersCursorAdapter != null) {
            db = ConnectionManager.getConnection(getActivity());
            remindersCursorAdapter.changeCursor(sqlAccess.getRemindersCursor(db));
            remindersCursorAdapter.notifyDataSetChanged();
            db.close();
        }
    }
}
