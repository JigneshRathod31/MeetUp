package com.jignesh.meetup.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jignesh.meetup.R;
import com.jignesh.meetup.utilities.Constants;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    CircleImageView civProfileImg;
    EditText etUsername, etEmail, etMobile, etDob;
    ImageButton ibDob;

    Spinner spGender;
    String[] genders = {"Male", "Female"};
    ArrayAdapter<String> genderAdapter;

    AutoCompleteTextView actState;
    String[] states = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"};
    ArrayAdapter<String> stateArrayAdapter;

    String name, email, mobile, dob, state;
    boolean gender;

    Button btnUpdateProfile;
    ProgressBar progressBar;

    FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        civProfileImg = view.findViewById(R.id.civ_profile_img);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);
        etMobile = view.findViewById(R.id.et_mobile);
        etDob = view.findViewById(R.id.et_dob);
        ibDob = view.findViewById(R.id.ib_dob);
        spGender = view.findViewById(R.id.sp_gender);
        actState = view.findViewById(R.id.act_state);
        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);
        progressBar = view.findViewById(R.id.progress_bar);

        db = FirebaseFirestore.getInstance();

        getAccountDetails();

        // get image from the device on profile click
        civProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Edit: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up Gender Spinner
        genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, genders);
        spGender.setAdapter(genderAdapter);

        // set up AutoCompleteTextView for State
        stateArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, states);
        actState.setThreshold(1);
        actState.setAdapter(stateArrayAdapter);

        // Calendar for dob
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        ibDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dateOfBirth = day + "/" + (month+1) + "/" + year;
                        etDob.setText(dateOfBirth);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        return view;
    }

    public void updateProfile(){
        if (isValidDetails()){
            loading(true);

            HashMap<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);
            user.put("gender", true);
            user.put("dob", dob);
            user.put("state", state);
            if (!mobile.isEmpty()){
                user.put("mobile", mobile);
            }

            try {
                db.collection("users")
                        .whereEqualTo("uid", Constants.USER_ID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                    doc.getId();
                                }
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getAccountDetails(){
        SharedPreferences sp = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        name = sp.getString("name", "");
        email = sp.getString("email", "");
        mobile = sp.getString("mobile", "");
        gender = sp.getBoolean("gender", true);
        dob = sp.getString("dob", "");
        state = sp.getString("state", "");

        etUsername.setText(name);
        etEmail.setText(email);
        etMobile.setText(mobile);
        spGender.setSelection(1);
        if (gender){
            spGender.setSelection(0);
        }
        etDob.setText(dob);
        actState.setText(state);
    }

    public boolean isValidDetails(){
        name = etUsername.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        gender = (spGender.getSelectedItem().toString().equals("Male")) ? true : false;
        dob = etDob.getText().toString().trim();
        state = actState.getText().toString().trim();

        if (name.isEmpty()){
            etUsername.setError("Empty Field!");
            etUsername.requestFocus();
        }else if (email.isEmpty()){
            etEmail.setError("Empty Field!");
            etEmail.requestFocus();
        }else if (dob.isEmpty()){
            etDob.setError("Empty Field!");
            etDob.requestFocus();
        }else if (state.isEmpty()){
            actState.setError("Empty Field!");
            actState.requestFocus();
        }else {
            return true;
        }

        return false;
    }

    public void loading(boolean isLoading){
        if (isLoading){
            btnUpdateProfile.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            btnUpdateProfile.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK){
                if (requestCode == 1){
                    Uri selectedImgUri = data.getData();

                    if (selectedImgUri != null){
                        civProfileImg.setImageURI(selectedImgUri);
                    }else {
                        Toast.makeText(requireContext(), "Null Uri returned!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Edit1: " + e, Toast.LENGTH_SHORT).show();
        }
    }
}