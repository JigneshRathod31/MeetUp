package com.jignesh.meetup.activities.bottom_navigation.people;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jignesh.meetup.R;
import com.jignesh.meetup.adapters.PeopleAdapter;
import com.jignesh.meetup.databinding.FragmentPeopleBinding;
import com.jignesh.meetup.models.PeopleModel;
import com.jignesh.meetup.utilities.SharedPreferenceData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeopleFragment extends Fragment {

//    private FragmentPeopleBinding binding;

    RecyclerView rvPerfectMatch;
    ArrayList<PeopleModel> alPerfectMatchUsers;
    PeopleAdapter perfectMatchUserAdapter;

    RecyclerView rvSportMatch;
    ArrayList<PeopleModel> alSportMatchUsers;
    PeopleAdapter sportMatchUserAdapter;

    RecyclerView rvOther;
    ArrayList<PeopleModel> alOther;
    PeopleAdapter otherPeopleAdapter;

    FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_people, container, false);

        try {
            rvPerfectMatch = root.findViewById(R.id.rv_perfect_match);
            rvSportMatch = root.findViewById(R.id.rv_sport_match);
            rvOther = root.findViewById(R.id.rv_other_match);

            db = FirebaseFirestore.getInstance();

            // Perfect Match RecyclerView Set Up
            alPerfectMatchUsers = new ArrayList<>();
            rvPerfectMatch.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            perfectMatchUserAdapter = new PeopleAdapter(requireContext(), alPerfectMatchUsers, 1);
            rvPerfectMatch.setAdapter(perfectMatchUserAdapter);

            // Sport Match RecyclerView Set Up
            alSportMatchUsers = new ArrayList<>();
            rvSportMatch.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            sportMatchUserAdapter = new PeopleAdapter(requireContext(), alSportMatchUsers, 2);
            rvSportMatch.setAdapter(sportMatchUserAdapter);

            // Other RecyclerView Set Up
            alOther = new ArrayList<>();
            rvOther.setLayoutManager(new LinearLayoutManager(requireContext()));
            otherPeopleAdapter = new PeopleAdapter(requireContext(), alOther, 3);
            rvOther.setAdapter(otherPeopleAdapter);

        getPerfectUsers();
        getSportUsers();
            getOtherUsers();
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    private void getPerfectUsers(){
        try {
            db.collection("users")
                    .whereEqualTo("profession", "Software Developer")
                    .whereArrayContainsAny("knownLanguages", Arrays.asList("Hindi", "Gujarati"))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            try {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document: task.getResult()){
                                        int profileImg = R.drawable.avatar_four;
                                        String uid = document.get("uid").toString();
                                        String name = document.get("name").toString();
                                        String email = document.get("email").toString();
                                        String mobile = document.get("mobile").toString();
                                        boolean gender = (boolean) document.get("gender");
                                        String dob = document.get("dob").toString();
                                        String state = document.get("state").toString();
                                        List<String> languages = (List<String>) document.get("knownLanguages");
                                        List<String> sports = (List<String>) document.get("sports");
                                        List<String> eSports = (List<String>) document.get("eSports");
                                        String profession = document.get("profession").toString();
                                        List<String> hobbies = (List<String>) document.get("hobbies");

                                        alPerfectMatchUsers.add(new PeopleModel(profileImg, uid, name, email, mobile, gender, dob, state, languages, sports, eSports, profession, hobbies));
                                    }
                                    perfectMatchUserAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getSportUsers(){
        try {
            db.collection("users")
                    .whereArrayContainsAny("sports", Arrays.asList("Cricket", "Basketball"))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            try {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document: task.getResult()){
                                        int profileImg = R.drawable.avatar_four;
                                        String uid = document.get("uid").toString();
                                        String name = document.get("name").toString();
                                        String email = document.get("email").toString();
                                        String mobile = document.get("mobile").toString();
                                        boolean gender = (boolean) document.get("gender");
                                        String dob = document.get("dob").toString();
                                        String state = document.get("state").toString();
                                        List<String> languages = (List<String>) document.get("knownLanguages");
                                        List<String> sports = (List<String>) document.get("sports");
                                        List<String> eSports = (List<String>) document.get("eSports");
                                        String profession = document.get("profession").toString();
                                        List<String> hobbies = (List<String>) document.get("hobbies");

                                        alSportMatchUsers.add(new PeopleModel(profileImg, uid, name, email, mobile, gender, dob, state, languages, sports, eSports, profession, hobbies));
                                    }
                                    sportMatchUserAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getOtherUsers(){
        try {
            db.collection("users")
                .whereEqualTo("state", "Gujarat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        try {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()){
                                    int profileImg = R.drawable.avatar_four;
                                    String uid = document.get("uid").toString();
                                    String name = document.get("name").toString();
                                    String email = document.get("email").toString();
                                    String mobile = document.get("mobile").toString();
                                    boolean gender = (boolean) document.get("gender");
                                    String dob = document.get("dob").toString();
                                    String state = document.get("state").toString();
                                    List<String> languages = (List<String>) document.get("knownLanguages");
                                    List<String> sports = (List<String>) document.get("sports");
                                    List<String> eSports = (List<String>) document.get("eSports");
                                    String profession = document.get("profession").toString();
                                    List<String> hobbies = (List<String>) document.get("hobbies");

                                    alOther.add(new PeopleModel(profileImg, uid, name, email, mobile, gender, dob, state, languages, sports, eSports, profession, hobbies));
                                }
                                otherPeopleAdapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}