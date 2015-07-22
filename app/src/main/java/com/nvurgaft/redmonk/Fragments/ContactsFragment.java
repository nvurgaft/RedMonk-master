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

import com.nvurgaft.redmonk.Adapters.ContactsCursorAdapter;
import com.nvurgaft.redmonk.Dialogs.ConfirmDialog;
import com.nvurgaft.redmonk.Dialogs.EditContactDialog;
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
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {
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
    private ContactsCursorAdapter contactsCursorAdapter;
    private ListView listView;
    private Button newContactButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contacts.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        sqlAccess = new SqlAccess(getActivity());
        db = ConnectionManager.getConnection(getActivity());
        contactsCursorAdapter = new ContactsCursorAdapter(getActivity(), sqlAccess.getContactsCursor(db));

        listView = (ListView) view.findViewById(R.id.contactListView);
        listView.setAdapter(contactsCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = (Cursor) parent.getItemAtPosition(position);

                //Toast.makeText(getActivity(), "clicked at contact " + data.getString(1), Toast.LENGTH_SHORT).show();
                // open a dialog to edit selected contact
                EditContactDialog editContactDialog = new EditContactDialog();

                Bundle selectedContactBundle = new Bundle();
                selectedContactBundle.putBoolean("isEdit", true);
                selectedContactBundle.putString("name", data.getString(1));
                selectedContactBundle.putString("role", data.getString(2));
                selectedContactBundle.putString("number1", data.getString(3));
                selectedContactBundle.putString("number2", data.getString(4));
                selectedContactBundle.putString("number3", data.getString(5));

                // show the edit contact dialog fragment
                editContactDialog.setArguments(selectedContactBundle);
                editContactDialog.show(getFragmentManager(), "EditContactDialog");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor data = (Cursor) parent.getItemAtPosition(position);

                //Toast.makeText(getActivity(), "long clicked at contact " + data.getString(1), Toast.LENGTH_SHORT).show();
                ConfirmDialog confirmDialog = new ConfirmDialog();
                String contactName = data.getString(1);

                Bundle confirmDialogBundle = new Bundle();
                confirmDialogBundle.putString("content", getString(R.string.confirm_delete_contact_text) + contactName);
                confirmDialogBundle.putString("identifier", contactName);
                confirmDialogBundle.putString("tag", "contactPrompt");
                confirmDialog.setArguments(confirmDialogBundle);
                confirmDialog.show(getFragmentManager(), "ConfirmDialog");
                return true;
            }
        });

        newContactButton = (Button) view.findViewById(R.id.newContactButton);
        newContactButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        db = ConnectionManager.getConnection(getActivity());
        contactsCursorAdapter.changeCursor(sqlAccess.getContactsCursor(db));
        contactsCursorAdapter.notifyDataSetChanged();
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
            case R.id.newContactButton:
                EditContactDialog editContactDialog = new EditContactDialog();
                editContactDialog.show(getFragmentManager(), "EditContactDialog");
                break;
            default:
                Log.d(Values.LOG, "Invalid value selected");
        }
    }

    public void refreshFragmentView() {
        if (contactsCursorAdapter != null) {
            db = ConnectionManager.getConnection(getActivity());
            contactsCursorAdapter.changeCursor(sqlAccess.getContactsCursor(db));
            contactsCursorAdapter.notifyDataSetChanged();
            db.close();
        }
    }

}
