package com.jignesh.meetup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jignesh.meetup.R;
import com.jignesh.meetup.utilities.Constants;
import com.jignesh.meetup.utilities.SharedPreferenceData;

public class SignInActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnSignIn;
    ProgressBar progressBar;
    TextView tvGotoRegister;

    FirebaseAuth fbAuth;
    FirebaseApp app;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        app = FirebaseApp.initializeApp(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        progressBar = findViewById(R.id.progress_bar);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvGotoRegister = findViewById(R.id.tv_goto_register);

        try {
            if (app != null){
                fbAuth = FirebaseAuth.getInstance();
            }
            db = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (isValidDetail()){
                    loading(true);
                    fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                db.collection("users")
                                    .whereEqualTo("uid", Constants.USER_ID)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                                String name = document.getString("name");
                                                String email = document.getString("email");
                                                boolean gender = document.getBoolean("gender");

                                                SharedPreferenceData.storeUserDetails(getApplicationContext(), name, email, "", gender, "");

                                                loading(false);

                                                startActivity(new Intent(SignInActivity.this, InterestActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                            }else {
                                Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                loading(false);
                            }
                        }
                    });
                }
            }
        });

        tvGotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
                } catch (Exception e) {
                    Toast.makeText(SignInActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        try {
            FirebaseUser currentUser = fbAuth.getCurrentUser();

            if (currentUser != null){
                startActivity(new Intent(SignInActivity.this, InterestActivity.class));
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loading(boolean isLoading){
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    public boolean isValidDetail(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Empty field!");
            etEmail.requestFocus();
        }else if (password.isEmpty()){
            etPassword.setError("Empty field!");
            etPassword.requestFocus();
        } else {
            return true;
        }

        return false;
    }
}