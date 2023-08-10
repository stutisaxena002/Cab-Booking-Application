package com.example.olaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class adminLogin extends AppCompatActivity {
    EditText adminUsername,adminPassword;
    ImageView btnAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        adminUsername=findViewById(R.id.adminUsername);
        adminPassword=findViewById(R.id.adminPassword);
        btnAdminLogin=findViewById(R.id.btnAdminLogin);
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((adminUsername.getText().toString().equals("a")) && (adminPassword.getText().toString().equals("b"))){
                    Intent i = new Intent(adminLogin.this,adminHome.class);
                    startActivity(i);
                    finish();

                }else{
                    Toast.makeText(adminLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}