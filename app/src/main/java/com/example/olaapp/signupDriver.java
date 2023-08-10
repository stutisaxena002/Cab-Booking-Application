package com.example.olaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class signupDriver extends AppCompatActivity {
    EditText signupDriverEmail, signupDriverPassword,signupDriverNumber,signupDriverPhone;
    Button btnDriverSignup;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_driver);
        signupDriverEmail = findViewById(R.id.signupDriverEmail);
        signupDriverPassword = findViewById(R.id.signupDriverPassword);
        signupDriverNumber = findViewById(R.id.signupDriverNumber);
        signupDriverPhone = findViewById(R.id.signupDriverPhone);
        btnDriverSignup = findViewById(R.id.btnDriverSignup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        btnDriverSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(signupDriverEmail.getText().toString(), signupDriverPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(signupDriver.this, "hello", Toast.LENGTH_SHORT).show();


                        Map<String, String> user = new HashMap<>();
                        user.put("email", signupDriverEmail.getText().toString());
                        user.put("password", signupDriverPassword.getText().toString());
                        user.put("number", signupDriverNumber.getText().toString());
                        user.put("phone", signupDriverPhone.getText().toString());
                        user.put("status", "0");


                        db.collection("Drivers").document(mAuth.getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(signupDriver.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        logOut();
                                        onBackPressed();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(signupDriver.this, "Failure data entry", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Toast.makeText(signupDriver.this, "Signup Successful", Toast.LENGTH_SHORT).show();


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(signupDriver.this, "Authentication failure", Toast.LENGTH_SHORT).show();

                            }
                        });
            }


        });
    }

        public void logOut()
    {
        mAuth.signOut();
    }
}