package com.jignesh.meetup.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.jignesh.meetup.R;
import com.jignesh.meetup.adapters.TagAdapter;

import java.util.ArrayList;

public class LocationLanguageFragment extends Fragment {

    public AutoCompleteTextView actState, actKnownLanguage;
    String[] states = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"};
    ArrayAdapter<String> stateArrayAdapter;

    String[] languages = {"Hindi", "English", "Bengali","Marathi","Telugu","Tamil","Gujarati","Urdu","Kannada","Odia","Malayalam","Punjabi"};
    ArrayAdapter languageArrayAdapter;

    RecyclerView rvKnownLanguageTag;
    ArrayList<String> alLanguageTag;
    TagAdapter languageTagAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LocationLanguageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationLanguageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationLanguageFragment newInstance(String param1, String param2) {
        LocationLanguageFragment fragment = new LocationLanguageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_language, container, false);

        actState = view.findViewById(R.id.act_state);
        actKnownLanguage = view.findViewById(R.id.act_known_language);
        rvKnownLanguageTag = view.findViewById(R.id.rv_known_language_tag);

        // Set up AutoCompleteTextView of State & Language
        stateArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, states);
        actState.setThreshold(1);
        actState.setAdapter(stateArrayAdapter);
        
        languageArrayAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, languages);
        actKnownLanguage.setThreshold(1);
        actKnownLanguage.setAdapter(languageArrayAdapter);

        // Set RecyclerView of Known Languages
        rvKnownLanguageTag.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        alLanguageTag = new ArrayList<>();
        languageTagAdapter = new TagAdapter(requireContext(), alLanguageTag);
        rvKnownLanguageTag.setAdapter(languageTagAdapter);

        actKnownLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String language = actKnownLanguage.getText().toString();
                alLanguageTag.add(language);
                languageTagAdapter.notifyDataSetChanged();
                actKnownLanguage.setText("");
                actKnownLanguage.requestFocus();
            }
        });

        return view;
    }
}