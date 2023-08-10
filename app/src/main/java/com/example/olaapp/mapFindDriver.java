package com.example.olaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.olaapp.databinding.ActivityMapFindDriverBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class mapFindDriver extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LinearLayout ll,ll2;
    String phone_num;
    private ActivityMapFindDriverBinding binding;
    FirebaseAuth mAuth;


    LatLng latLng;

    FirebaseFirestore db;
    String userLatitude;
    String userLongitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapFindDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ll=findViewById(R.id.ll);
        //searchd=findViewById(R.id.searchd);
        Toast.makeText(this, "Drivers within 1KM", Toast.LENGTH_SHORT).show();





    }

    private void Createpopupwindow(String markerName) {


        LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View mainpopup=inflater.inflate(R.layout.mainpopup,null);
        TextView dateText = (TextView) mainpopup.findViewById(R.id.searchd);
        dateText.setText(markerName);
        ImageView datebtn = (ImageView) mainpopup.findViewById(R.id.btnsearchd);
        db.collection("Drivers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String drivernum = (String) documentSnapshot.get("number");
                    if (drivernum.equals(markerName)){
                        phone_num= (String) documentSnapshot.get("phone");
                    }
                    }
                }





        }

        );





        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mapFindDriver.this);
                builder.setMessage("Do you want to call?");
                builder.setTitle("Confirm");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    if (phone_num.trim().isEmpty()){
                        Toast.makeText(mapFindDriver.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        callIntent.setData(Uri.parse("tel:"+phone_num));
                    }

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(mapFindDriver.this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
                        requestPermission();
                    }
                    else {
                        startActivity(callIntent);
                    }


                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();

        /*Intent itentCall=new Intent(Intent.ACTION_CALL);
        String opno=user.getPhone();*/




            }

            private void requestPermission() {
                ActivityCompat.requestPermissions(mapFindDriver.this,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        });
        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height=ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable=true;
        PopupWindow popupWindow=new PopupWindow(mainpopup,width,height,focusable);
        ll.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);



            }
        });
        ll2=findViewById(R.id.ll2);
        ll2.setBackgroundColor(00000000);
        ll.setBackgroundColor(00000000);
        mainpopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }


        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mMap = googleMap;


        db.collection("UserLocation").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                userLatitude = document.getString("latitude");
                userLongitude = document.getString("longitude");


            }
        });


        db.collection("DriverLocation").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String driverlatitude = (String) documentSnapshot.get("latitude");
                    String driverlongitude = (String) documentSnapshot.get("longitude");
                    String driverId = (String) documentSnapshot.getId();
                    float[] results = new float[1];
                    assert driverlatitude != null;
                    assert driverlongitude != null;
                    Location.distanceBetween(Double.parseDouble(userLatitude), Double.parseDouble(userLongitude), Double.parseDouble(driverlatitude), Double.parseDouble(driverlongitude), results);
                    float distanceInMeters = results[0];
                    boolean isWithin1km = distanceInMeters < 1000;





                    if (isWithin1km) {
                        double lat = Double.parseDouble(driverlatitude.trim());
                        double lon = Double.parseDouble(driverlongitude.trim());
                        latLng = new LatLng(lat, lon);
                        db.collection("Drivers").document(driverId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                String num=document.getString("number");
                                CameraUpdate camera=CameraUpdateFactory.newLatLng(latLng);
                                mMap.animateCamera(camera);
                                MarkerOptions mo=new MarkerOptions();
                                mo.position(latLng);
                                mo.title(num);
                                mMap.addMarker(mo);
                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(@NonNull Marker marker) {

                                        String markerName = marker.getTitle();
                                        Createpopupwindow(markerName);


                                        return false;

                                    }
                                //titleToBe(num,latLng);
                                //Toast.makeText(mapFindDriver.this, "fire", Toast.LENGTH_SHORT).show();
                            }
                        );
                        //Toast.makeText(mapFindDriver.this, "normal", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }}});


    }
   /* public void titleToBe(String num,LatLng latlng){
        CameraUpdate camera=CameraUpdateFactory.newLatLng(latLng);
        mMap.animateCamera(camera);
        MarkerOptions mo=new MarkerOptions();
        mo.position(latLng);
        mo.title(num);
        mMap.addMarker(mo);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                String markerName = marker.getTitle();
                Createpopupwindow(markerName);


                return false;

            }

        });*/





}