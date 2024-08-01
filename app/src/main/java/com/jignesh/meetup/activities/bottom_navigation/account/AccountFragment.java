package com.jignesh.meetup.activities.bottom_navigation.account;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jignesh.meetup.R;
import com.jignesh.meetup.activities.InterestActivity;
import com.jignesh.meetup.activities.MainActivity;
import com.jignesh.meetup.databinding.FragmentAccountBinding;
import com.jignesh.meetup.fragments.EditProfileFragment;
import com.jignesh.meetup.utilities.Constants;

public class AccountFragment extends Fragment {

//    private FragmentAccountBinding binding;
    
    TextView tvUserName, tvUserEmail;
    Button btnEditProfile;

    LinearLayout llUserName, llEmail, llMobile, llGender, llDOB, llState;
    TextView tvName, tvEmail, tvMobile, tvGender, tvDob, tvState;
    String name, email, mobile, gender, dob, state;
    ClipboardManager clipboardManager;

    RelativeLayout rlLanguages, rlSports, rlProfessionHobby;

    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        AccountViewModel accountViewModel =
//                new ViewModelProvider(this).get(AccountViewModel.class);
//
//        binding = FragmentAccountBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        tvUserName = root.findViewById(R.id.tv_user_name);
        tvUserEmail = root.findViewById(R.id.tv_user_email);

        btnEditProfile = root.findViewById(R.id.btn_edit_profile);

        llUserName = root.findViewById(R.id.ll_user_name);
        llEmail = root.findViewById(R.id.ll_email);
        llMobile = root.findViewById(R.id.ll_mobile);
        llGender = root.findViewById(R.id.ll_gender);
        llDOB = root.findViewById(R.id.ll_dob);
        llState = root.findViewById(R.id.ll_state);

        tvName = root.findViewById(R.id.tv_name);
        tvEmail = root.findViewById(R.id.tv_email);
        tvMobile = root.findViewById(R.id.tv_mobile);
        tvGender = root.findViewById(R.id.tv_gender);
        tvDob = root.findViewById(R.id.tv_dob);
        tvState = root.findViewById(R.id.tv_state);

        rlLanguages = root.findViewById(R.id.rl_languages);
        rlSports = root.findViewById(R.id.rl_sports);
        rlProfessionHobby = root.findViewById(R.id.rl_profession_hobby);

        db = FirebaseFirestore.getInstance();

        getAccountDetails();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(((ViewGroup)(getView().getParent())).getId(), new EditProfileFragment());
//                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        llUserName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyDetailToClipBoard("name");
                return true;
            }
        });

        llEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyDetailToClipBoard("email");
                return true;
            }
        });

        llMobile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyDetailToClipBoard("mobile");
                return true;
            }
        });

        llGender.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyDetailToClipBoard("gender");
                return true;
            }
        });

        llDOB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyDetailToClipBoard("dob");
                return true;
            }
        });

        llState.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyDetailToClipBoard("address");
                return false;
            }
        });

        rlLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), InterestActivity.class));
            }
        });

        rlSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), InterestActivity.class));
            }
        });

        rlProfessionHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), InterestActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    public void getAccountDetails(){
        try {
//            SharedPreferences sp = getActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
//            name = sp.getString("name", "");
//            email = sp.getString("email", "");
//            mobile = sp.getString("mobile", "");
//            gender = (sp.getBoolean("gender", true)) ? "Male" : "Female";
//            dob = sp.getString("dob", "");
//            state = sp.getString("state", "");
            db.collection("users")
                .whereEqualTo("uid", Constants.USER_ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            name = document.get("name").toString();
                            email = document.get("email").toString();
                            mobile = document.get("mobile").toString();
                            gender = ((boolean) document.get("gender")) ? "Male" : "Female";
                            dob = document.get("dob").toString();
                            state = document.get("state").toString();

                            tvUserName.setText(name);
                            tvUserEmail.setText(email);

                            tvName.setText(name);
                            tvEmail.setText(email);
                            tvMobile.setText(mobile);
                            tvGender.setText(gender);
                            tvDob.setText(dob);
                            tvState.setText(state);
                        }
                    }
                });



        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void copyDetailToClipBoard(String field){
        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData;

        switch (field){
            case "name":
                clipData = ClipData.newPlainText(field, "Jignesh Rathod");
                break;

            case "email":
                clipData = ClipData.newPlainText(field, "jigneshrathod@gmail.com");
                break;

            case "mobile":
                clipData = ClipData.newPlainText(field, "+91 9898294522");
                break;

            case "gender":
                clipData = ClipData.newPlainText(field, "Male");
                break;

            case "dob":
                clipData = ClipData.newPlainText(field, "31/07/2004");
                break;

            case "address":
                clipData = ClipData.newPlainText(field, "786 Mira Kutir, Sector 27, Gandhinagar, Gujarat");
                break;

            default:
                clipData = ClipData.newPlainText("email", "jigneshrathod@gmail.com");
        }

        Toast.makeText(requireContext(), field + " copied!", Toast.LENGTH_SHORT).show();
        clipboardManager.setPrimaryClip(clipData);
    }
}