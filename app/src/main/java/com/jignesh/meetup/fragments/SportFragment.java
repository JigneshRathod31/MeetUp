package com.jignesh.meetup.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.jignesh.meetup.R;
import com.jignesh.meetup.adapters.TagAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportFragment extends Fragment {

    AutoCompleteTextView actSport, actESport;

    String[] sports = {"Football", "Cricket", "Basketball", "Tennis", "Field Hockey", "Volleyball", "Table Tennis", "Baseball", "Golf", "Rugby", "Badminton", "American Football", "Swimming", "Track and Field (Athletics)", "Cycling", "Boxing", "Ice Hockey", "Gymnastics", "Wrestling", "Martial Arts (Judo, Karate, Taekwondo, etc.)", "Skiing", "Snowboarding", "Surfing", "Skateboarding", "Softball", "Handball", "Lacrosse", "Squash", "Rowing", "Equestrian Sports", "Snooker/Pool/Billiards", "Darts", "Fencing", "Archery", "Sailing", "Water Polo", "Triathlon", "Figure Skating", "Racquetball", "Canoeing/Kayaking", "Rock Climbing", "Weightlifting", "Bowling", "Auto Racing (Formula 1, NASCAR, etc.)", "Motocross", "Rodeo", "Ultimate Frisbee", "Sepak Takraw", "Netball", "Gaelic Football"};
    ArrayAdapter sportArrayAdapter;

    String[] eSports = {"League of Legends", "Dota 2", "Counter-Strike: Global Offensive", "Fortnite", "Overwatch", "Call of Duty", "PUBG (PlayerUnknown's Battlegrounds)", "Valorant", "Rocket League", "Rainbow Six Siege"};
    ArrayAdapter eSportArrayAdapter;

    RecyclerView rvSportTag;
    ArrayList<String> alSportTag;
    TagAdapter sportTagAdapter;

    RecyclerView rvESportTag;
    ArrayList<String> alESportTag;
    TagAdapter eSportTagAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportFragment newInstance(String param1, String param2) {
        SportFragment fragment = new SportFragment();
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
        View view = inflater.inflate(R.layout.fragment_sport, container, false);

        actSport = view.findViewById(R.id.act_sport);
        rvSportTag = view.findViewById(R.id.rv_sport_tag);
        actESport = view.findViewById(R.id.act_e_sport);
        rvESportTag = view.findViewById(R.id.rv_e_sport_tag);

        // Set up AutoCompleteTextView of Sports & E-Sports
        sportArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, sports);
        actSport.setThreshold(0);
        actSport.setAdapter(sportArrayAdapter);

        eSportArrayAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, eSports);
        actESport.setThreshold(0);
        actESport.setAdapter(eSportArrayAdapter);

        // Set RecyclerView of Sports & E-Sports
        rvSportTag.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        alSportTag = new ArrayList<>();
        sportTagAdapter = new TagAdapter(requireContext(), alSportTag);
        rvSportTag.setAdapter(sportTagAdapter);

        rvESportTag.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        alESportTag = new ArrayList<>();
        eSportTagAdapter = new TagAdapter(requireContext(), alESportTag);
        rvESportTag.setAdapter(eSportTagAdapter);

        actSport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String language = actSport.getText().toString();
                alSportTag.add(language);
                sportTagAdapter.notifyDataSetChanged();
                actSport.setText("");
                actSport.requestFocus();
            }
        });

        actESport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String language = actESport.getText().toString();
                alESportTag.add(language);
                eSportTagAdapter.notifyDataSetChanged();
                actESport.setText("");
                actESport.requestFocus();
            }
        });

        return view;
    }
}