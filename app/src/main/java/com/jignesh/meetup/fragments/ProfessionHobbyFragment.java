package com.jignesh.meetup.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jignesh.meetup.R;
import com.jignesh.meetup.activities.MainActivity;
import com.jignesh.meetup.adapters.TagAdapter;
import com.jignesh.meetup.utilities.Constants;
import com.jignesh.meetup.utilities.SharedPreferenceData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfessionHobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfessionHobbyFragment extends Fragment {

    AutoCompleteTextView actProfession, actHobby;

    String[] professions = {"Software Developer", "Data Scientist", "Doctor", "Nurse", "Pharmacist", "Teacher", "Accountant", "Lawyer", "Civil Engineer", "Mechanical Engineer", "Electrical Engineer", "Architect", "Dentist", "Financial Analyst", "Marketing Manager", "Project Manager", "Human Resources Manager", "Sales Manager", "Graphic Designer", "Web Developer", "Network Administrator", "Database Administrator", "IT Manager", "Psychologist", "Physical Therapist", "Occupational Therapist", "Pharmacologist", "Veterinarian", "Environmental Scientist", "Biologist", "Chemist", "Physicist", "Economist", "Journalist", "Social Worker", "Urban Planner", "Real Estate Agent", "Chef", "Pilot", "Flight Attendant", "Electrician", "Plumber", "Carpenter", "Mechanic", "Machinist", "Welder", "Bartender", "Barista", "Retail Manager", "Customer Service Representative"};
    ArrayAdapter professionArrayAdapter;

    String[] hobbies = {"Reading", "Traveling", "Cooking", "Gardening", "Drawing", "Painting", "Writing", "Playing Musical Instruments", "Listening to Music", "Photography", "Hiking", "Cycling", "Fishing", "Swimming", "Running", "Yoga", "Meditation", "Bird Watching", "Collecting (stamps, coins, etc.)", "Dancing", "Crafting", "Knitting", "Sewing", "Playing Video Games", "Watching Movies", "Baking", "Scrapbooking", "Pottery", "Woodworking", "Playing Sports"};
    ArrayAdapter hobbyArrayAdapter;

    RecyclerView rvHobbyTag;
    ArrayList<String> alHobbyTag;
    TagAdapter hobbyTagAdapter;

    Button btnFinish;

    FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfessionHobbyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfessionHobbyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfessionHobbyFragment newInstance(String param1, String param2) {
        ProfessionHobbyFragment fragment = new ProfessionHobbyFragment();
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
        View view = inflater.inflate(R.layout.fragment_profession_hobby, container, false);

        // Initializations
        actProfession = view.findViewById(R.id.act_profession);
        actHobby = view.findViewById(R.id.act_hobby);
        rvHobbyTag = view.findViewById(R.id.rv_hobby_tag);
        btnFinish = view.findViewById(R.id.btn_finish);

        db = FirebaseFirestore.getInstance();

        // Set up AutoCompleteTextView of Profession & Hobbies
        professionArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, professions);
        actProfession.setThreshold(0);
        actProfession.setAdapter(professionArrayAdapter);

        hobbyArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, hobbies);
        actHobby.setThreshold(0);
        actHobby.setAdapter(hobbyArrayAdapter);

        // Set RecyclerView of Sports & E-Sports
        rvHobbyTag.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        alHobbyTag = new ArrayList<>();
        hobbyTagAdapter = new TagAdapter(requireContext(), alHobbyTag);
        rvHobbyTag.setAdapter(hobbyTagAdapter);

        actHobby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String language = actHobby.getText().toString();
                alHobbyTag.add(language);
                hobbyTagAdapter.notifyDataSetChanged();
                actHobby.setText("");
                actHobby.requestFocus();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                storeInterests();
                startActivity(new Intent(requireContext(), MainActivity.class));
            }
        });

        return view;
    }
    
    private void storeInterests(){
        HashMap<String, Object> interests = new HashMap<>();
        interests.put("state", SharedPreferenceData.state);
        interests.put("knownLanguages", SharedPreferenceData.languages);
        interests.put("sports", SharedPreferenceData.sports);
        interests.put("eSports", SharedPreferenceData.eSports);
        interests.put("profession", SharedPreferenceData.profession);
        interests.put("hobbies", SharedPreferenceData.hobbies);
        
        db.collection("users")
            .document(Constants.USER_ID)
            .set(interests)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(requireContext(), "Interests stored successfully!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}