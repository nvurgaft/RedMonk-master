package com.nvurgaft.redmonk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nvurgaft.redmonk.OnFragmentInteractionListener;
import com.nvurgaft.redmonk.R;
import com.nvurgaft.redmonk.Values;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public SharedPreferences sharedPreferences;

    private Button saveButton;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private EditText nameEditText, heightEditText, weightEditText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StatusFragment() {
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

        View view = inflater.inflate(R.layout.fragment_status, container, false);

        saveButton = (Button) view.findViewById(R.id.fragment_status_save_button);
        saveButton.setOnClickListener(this);

        radioGroup = (RadioGroup) view.findViewById(R.id.genderRadioGroup);
        radioGroup.setOnClickListener(this);

        maleRadioButton = (RadioButton) view.findViewById(R.id.radioMale);
        maleRadioButton.setOnClickListener(this);

        femaleRadioButton = (RadioButton) view.findViewById(R.id.radioFemale);
        femaleRadioButton.setOnClickListener(this);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        heightEditText =  (EditText) view.findViewById(R.id.heightEditText);
        weightEditText =  (EditText) view.findViewById(R.id.weightEditText);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        nameEditText.setText(sharedPreferences.getString("status_username", "Anon"));
        heightEditText.setText(sharedPreferences.getString("status_height", "185"));
        weightEditText.setText(sharedPreferences.getString("status_weight", "80"));
        maleRadioButton.setChecked(sharedPreferences.getBoolean("status_isDude", false));
        femaleRadioButton.setChecked(sharedPreferences.getBoolean("status_isDudette", false));

        // Inflate the layout for this fragment
        return view;
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
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fragment_status_save_button:
                sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("status_username", nameEditText.getText().toString());
                sharedPreferences.edit().putString("status_height", heightEditText.getText().toString());
                sharedPreferences.edit().putString("status_weight", weightEditText.getText().toString());
                sharedPreferences.edit().putBoolean("status_isDude", maleRadioButton.isChecked());
                sharedPreferences.edit().putBoolean("status_isDudette", femaleRadioButton.isChecked());
                sharedPreferences.edit().apply();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d(Values.LOG, "Invalid value selected");
        }
    }
}
