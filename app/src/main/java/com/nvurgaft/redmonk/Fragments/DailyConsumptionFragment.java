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
import android.widget.ListView;

import com.nvurgaft.redmonk.Adapters.DailyConsumptionCursorAdapter;
import com.nvurgaft.redmonk.Dialogs.ConfirmDialog;
import com.nvurgaft.redmonk.Dialogs.EditDailyConsumptionDialog;
import com.nvurgaft.redmonk.Model.ConnectionManager;
import com.nvurgaft.redmonk.Model.SqlAccess;
import com.nvurgaft.redmonk.OnFragmentInteractionListener;
import com.nvurgaft.redmonk.R;
import com.nvurgaft.redmonk.Values;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyConsumptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyConsumptionFragment extends Fragment implements View.OnClickListener {
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
    private DailyConsumptionCursorAdapter dailyConsumptionCursorAdapter;
    private ListView listView;
    private Button newDcButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyConsumption.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyConsumptionFragment newInstance(String param1, String param2) {
        DailyConsumptionFragment fragment = new DailyConsumptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DailyConsumptionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_daily_consumption, container, false);

        sqlAccess = new SqlAccess(getActivity());
        db = ConnectionManager.getConnection(getActivity());
        dailyConsumptionCursorAdapter = new DailyConsumptionCursorAdapter(getActivity(), sqlAccess.getAllDailyConsumptionsCursor(db));

        listView = (ListView) view.findViewById(R.id.dcListView);
        listView.setAdapter(dailyConsumptionCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor data = (Cursor) parent.getItemAtPosition(position);
                // open a dialog to edit selected contact
                EditDailyConsumptionDialog editContactDialog = new EditDailyConsumptionDialog();
                Bundle selectedContactBundle = new Bundle();
                selectedContactBundle.putBoolean("isEdit", true);
                selectedContactBundle.putLong("date", data.getLong(1));
                selectedContactBundle.putInt("calories", data.getInt(2));
                selectedContactBundle.putInt("carbs", data.getInt(3));
                selectedContactBundle.putInt("proteins", data.getInt(4));
                selectedContactBundle.putInt("fats", data.getInt(5));

                // show the edit contact dialog fragment
                editContactDialog.setArguments(selectedContactBundle);
                editContactDialog.show(getFragmentManager(), "EditDailyConsumptionDialog");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor data = (Cursor) parent.getItemAtPosition(position);
                ConfirmDialog confirmDialog = new ConfirmDialog();
                long logDayDate = data.getLong(1);
                Bundle confirmDialogBundle = new Bundle();
                confirmDialogBundle.putString("content", getString(R.string.confirm_delete_contact_text) +
                        " : " + android.text.format.DateFormat.format("yyyy-MM-dd", new Date(Long.valueOf(logDayDate))));
                confirmDialogBundle.putString("identifier", String.valueOf(logDayDate));
                confirmDialogBundle.putString("tag", "consumptionPrompt");
                confirmDialog.setArguments(confirmDialogBundle);
                confirmDialog.show(getFragmentManager(), "ConfirmDialog");
                return true;
            }
        });

        newDcButton = (Button) view.findViewById(R.id.dcNewLogButton);
        newDcButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        db = ConnectionManager.getConnection(getActivity());
        dailyConsumptionCursorAdapter.changeCursor(sqlAccess.getAllDailyConsumptionsCursor(db));
        dailyConsumptionCursorAdapter.notifyDataSetChanged();
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

    public void refreshFragmentView() {
        if (dailyConsumptionCursorAdapter != null) {
            db = ConnectionManager.getConnection(getActivity());
            dailyConsumptionCursorAdapter.changeCursor(sqlAccess.getAllDailyConsumptionsCursor(db));
            dailyConsumptionCursorAdapter.notifyDataSetChanged();
            db.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dcNewLogButton:
                EditDailyConsumptionDialog editDcDialog = new EditDailyConsumptionDialog();
                editDcDialog.show(getFragmentManager(), "EditDailyConsumptionDialog");
                break;
            default:
                Log.d(Values.LOG, "Invalid value selected");
        }
    }
}
