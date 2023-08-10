package com.example.olaapp;

//import com.google.firebase.firestore.IgnoreExtraProperties;

import com.google.firebase.firestore.DocumentId;


public class User {
    private String email;

    private String phone;
    private String number;

    String docId;


    public User() {
    }

    public User(String email, String phone, String number) {
        this.email = email;

        this.phone = phone;
        this.number = number;
    }

    public String getEmail() {
        return email;
    }


    public String getPhone() {
        return phone;
    }

    public String getNumber() {
        return number;
    }

    @DocumentId
    public String getDocId() {
        return docId;
    }

    @DocumentId
    public void setDocId(String docId) {
        this.docId = docId;
    }
}
