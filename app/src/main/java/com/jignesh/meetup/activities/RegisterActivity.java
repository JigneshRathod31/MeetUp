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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jignesh.meetup.R;
import com.jignesh.meetup.utilities.Constants;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPassword, etConfirmPassword;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    Button btnRegister;
    ProgressBar progressBar;
    TextView tvGotoSignIn;

    String username, email, password, confirmPassword;
    boolean gender;

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
//            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        rgGender = findViewById(R.id.rg_gender);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        progressBar = findViewById(R.id.progress_bar);
        btnRegister = findViewById(R.id.btn_register);
        tvGotoSignIn = findViewById(R.id.tv_goto_sign_in);


        try {
            fbAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        tvGotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidDetail()){
                    loading(true);

                    fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();

                                HashMap<String, Object> user = new HashMap<>();
                                user.put("uid", Constants.USER_ID);
                                user.put("name", username);
                                user.put("email", email);
                                user.put("gender", gender);

                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(RegisterActivity.this, "User stored on firebase successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                loading(false);
                                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void loading(boolean isLoading){
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
        }
    }

    public boolean isValidDetail(){
        username = etUsername.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();

        gender = rbMale.isSelected();

        if (username.isEmpty()){
            etUsername.setError("Empty field!");
            etUsername.requestFocus();
        }else if(email.isEmpty()){
            etEmail.setError("Empty field!");
            etEmail.requestFocus();
        }else if (rgGender.getCheckedRadioButtonId() == -1){
            rgGender.requestFocus();
            rbMale.setError("Gender not selected!");
//            Toast.makeText(this, "Gender not selected!", Toast.LENGTH_SHORT).show();
        }else if (password.isEmpty()){
            etPassword.setError("Empty field!");
            etPassword.requestFocus();
        }else if (password.length() < 6){
            etPassword.setError("Password should contain at least 6 characters!");
            etPassword.requestFocus();
        }
        else if (confirmPassword.isEmpty()){
            etConfirmPassword.setError("Empty field!");
            etConfirmPassword.requestFocus();
        }else if (!password.equals(confirmPassword)){
            etConfirmPassword.setError("Password not matched!");
            etConfirmPassword.requestFocus();
        } else {
            return true;
        }

        return false;
    }
}