package com.example.olaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;

public class adminHome extends AppCompatActivity implements SelectListener {
    RecyclerView recyclerView;
    ArrayList<User> list;
    FirebaseFirestore db;
    MyAdapter MyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);



        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseFirestore.getInstance();
        list=new ArrayList<User>();
        MyAdapter=new MyAdapter(adminHome.this,list,this);
        recyclerView.setAdapter(MyAdapter);
        EventChangelistener();
    }

    private  void EventChangelistener(){
        db.collection("Drivers").orderBy("number", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null){

                            return;
                        }

                        for (DocumentChange dc:value.getDocumentChanges()){
                            if (dc.getType()==DocumentChange.Type.ADDED){
                                if (dc.getDocument().getString("status").equals("0")){
                                list.add(dc.getDocument().toObject(User.class));}
                            }
                            MyAdapter.notifyDataSetChanged();


                        }

                    }
                });

    }

    @Override
    public void onItemClicked(User user) {
        db.collection("Drivers")
                .document(user.docId).update("status","1");
        Toast.makeText(this, "verified", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        email.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, "DRIVER VERIFIED ");
        email.putExtra(android.content.Intent.EXTRA_TEXT, "Dear "+user.getNumber()+",\n"+"Your vehicle has been verified by contact number"+user.getPhone()+".");
        //email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));





    }
}