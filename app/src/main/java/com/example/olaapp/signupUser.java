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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signupUser extends AppCompatActivity {

    EditText signupUserEmail, signupUserPassword,signupUserPhone;
    Button btnUserSignup;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_user);
        signupUserEmail=findViewById(R.id.signupUserEmail);
        signupUserPassword=findViewById(R.id.signupUserPassword);
        signupUserPhone=findViewById(R.id.signupUserPhone);
        btnUserSignup=findViewById(R.id.btnUserSignup);
        mAuth = FirebaseAuth.getInstance();
        db =FirebaseFirestore.getInstance();


        btnUserSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(signupUserEmail.getText().toString(),signupUserPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Map<String, String> user = new HashMap<>();
                        user.put("email", signupUserEmail.getText().toString());
                        user.put("password", signupUserPassword.getText().toString());
                        user.put("phone", signupUserPhone.getText().toString());



                        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(signupUser.this, "Data Added", Toast.LENGTH_SHORT).show();
                                        logOut();
                                        onBackPressed();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(signupUser.this, "Failure data entry", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Toast.makeText(signupUser.this, "Signup Successful", Toast.LENGTH_SHORT).show();


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