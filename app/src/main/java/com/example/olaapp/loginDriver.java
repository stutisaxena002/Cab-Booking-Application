package com.example.olaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class loginDriver extends AppCompatActivity {
    EditText loginDriverEmail,loginDriverPassword;
    ImageView btnDriverLogin;
    TextView newDriverSignup;


    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_driver);
        loginDriverEmail=findViewById(R.id.loginDriverEmail);
        loginDriverPassword=findViewById(R.id.loginDriverPassword);
        btnDriverLogin=findViewById(R.id.btnDriverLogin);
        newDriverSignup=findViewById(R.id.newDriverSignup);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        newDriverSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginDriver.this,signupDriver.class);
                startActivity(i);
            }
        });

        btnDriverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(loginDriverEmail.getText().toString(),loginDriverPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            db.collection("Drivers").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document=task.getResult();
                                    if (document.getString("status").equals("1")){
                                        Intent i = new Intent(loginDriver.this,mapDriver.class);
                                        startActivity(i);
                                        finish();
                                        //finish

                                    }
                                    else {
                                        mAuth.signOut();
                                        Toast.makeText(loginDriver.this, "Under Verification", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(loginDriver.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}