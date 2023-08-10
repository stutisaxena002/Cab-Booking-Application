package com.example.olaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginUser extends AppCompatActivity {

    EditText loginUserEmail,loginUserPassword;
    ImageView btnUserLogin;
    TextView newUserSignup;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        loginUserEmail=findViewById(R.id.loginUserEmail);
        loginUserPassword=findViewById(R.id.loginUserPassword);
        btnUserLogin=findViewById(R.id.btnUserLogin);
        newUserSignup=findViewById(R.id.newUserSignup);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        newUserSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginUser.this,signupUser.class);
                startActivity(i);
            }
        });

        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(loginUserEmail.getText().toString(),loginUserPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(loginUser.this,mapUser.class);
                            startActivity(i);

                            //mAuth.signOut();


                        }
                        else {
                            Toast.makeText(loginUser.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}