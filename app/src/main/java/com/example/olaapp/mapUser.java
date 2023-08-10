package com.example.olaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.olaapp.databinding.ActivityMapUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mapUser extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private ActivityMapUserBinding binding;
    //String id;
    String email;
    EditText destinationName;

    Button btnFindDrivers;
    FloatingActionButton logout_user;
    ImageView btnDestination;
    TextView txt_user_email;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    LocationManager manager;
    LatLng latLng;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //id = mAuth.getCurrentUser().getUid();
        logout_user=findViewById(R.id.logout_user);
        btnFindDrivers=findViewById(R.id.btnFindDrivers);
        btnDestination=findViewById(R.id.btnDestination);
        destinationName=findViewById(R.id.destinationName);


        btnFindDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(mapUser.this, mapFindDriver.class);
                startActivity(i);

            }
        });


        btnDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //.makeText(mapUser.this, destinationName.getText().toString(), Toast.LENGTH_SHORT).show();

                    Geocoder geocoder = new Geocoder(mapUser.this);
                    List<Address> addressList = geocoder.getFromLocationName(destinationName.getText().toString(), 1);

                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    //Toast.makeText(mapUser.this, latLng.toString(), Toast.LENGTH_SHORT).show();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
                    mMap.animateCamera(cameraUpdate);
                    MarkerOptions mo = new MarkerOptions();
                    mo.title(destinationName.getText().toString());
                    mo.position(latLng);
                    mMap.addMarker(mo);
                } catch (Exception e) {

                }
            }
        });


       db.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                email = document.getString("email");
                txt_user_email = (TextView) findViewById(R.id.txt_user_email);
                txt_user_email.setText(email);


            }
        });

        logout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mapUser.this);
                builder.setMessage("Do you want to logout?");
                builder.setTitle("Logout");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    mAuth.signOut();
                    Intent i = new Intent(mapUser.this, loginUser.class);
                    startActivity(i);
                    finish();

                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }


        });

        ActivityCompat.requestPermissions(mapUser.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mapUser.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapUser.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 10F, (LocationListener) this);

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
        mMap = googleMap;


        if (latLng != null) {



            latLng=new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(latLng,15);
            mMap.animateCamera(camera);
            MarkerOptions mo = new MarkerOptions();
            mo.position(latLng);
            mMap.addMarker(mo);



            Map<String, String> user = new HashMap<>();
            user.put("latitude", String.valueOf(location.getLatitude()));
            user.put("longitude", String.valueOf(location.getLongitude()));

            db.collection("UserLocation").document(mAuth.getCurrentUser().getUid())
                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mapUser.this, "Location Updated", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {

            if (ActivityCompat.checkSelfPermission(mapUser.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapUser.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }



            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latLng=new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(latLng,15);
            mMap.animateCamera(camera);
            MarkerOptions mo = new MarkerOptions();
            mo.position(latLng);
            mMap.addMarker(mo);


            Map<String, String> user = new HashMap<>();
            user.put("latitude", String.valueOf(location.getLatitude()));
            user.put("longitude", String.valueOf(location.getLongitude()));

            db.collection("UserLocation").document(mAuth.getCurrentUser().getUid())
                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mapUser.this, "Location Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, "GPS is working now", Toast.LENGTH_SHORT).show();
        latLng=new LatLng(location.getLatitude(),location.getLongitude());
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(latLng,15);
        mMap.animateCamera(camera);
        MarkerOptions mo = new MarkerOptions();
        mo.position(latLng);
        mMap.addMarker(mo);

        Map<String, String> user = new HashMap<>();
        user.put("latitude", String.valueOf(location.getLatitude()));
        user.put("longitude", String.valueOf(location.getLongitude()));

        db.collection("UserLocation").document(mAuth.getCurrentUser().getUid())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(mapUser.this, "Location Updated", Toast.LENGTH_SHORT).show();
                    }
                });


    }


}