package com.example.prototype2.Trust;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prototype2.HomePage.HomePage;
import com.example.prototype2.Model.User;
import com.example.prototype2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class alertMap extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FirebaseUser fuser;

    DatabaseReference reference;

    DatabaseReference help;

    private View aView;

    private MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aView=inflater.inflate(R.layout.activity_alert_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    return aView;
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

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                final String trust = user.getGuide();
                    if (trust.equals("default")) {
                        Snackbar snackbar = Snackbar.make(aView, "尚未有人設你為信賴聯絡人喔！", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        help = FirebaseDatabase.getInstance().getReference("Users").child(trust);

                        help.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                final String person = user.getUsername();
                                final String latitude_S = user.getLatitude();
                                final String longitude_S = user.getLongitude();
                                final String share_S = user.getShare();
                                final double latitude = Double.parseDouble(latitude_S);
                                final double longitude = Double.parseDouble(longitude_S);
                                final boolean share = Boolean.parseBoolean(share_S);
                                //FragmentManager fragment = getActivity().getSupportFragmentManager();
                                if (share) {
                                    // Add a marker in Sydney and move the camera
                                    LatLng guide = new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(guide).title(person + " in help!!!"));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(guide, 17));
                                } else {
//        Toasty.success(getActivity(),"聯絡人沒事喔", Toast.LENGTH_SHORT, true).show();
                                    Snackbar snackbar = Snackbar.make(aView, "聯絡人安全", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fragment = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTrans = fragment .beginTransaction();
        fragmentTrans.add(R.id.content_frame,new HomePage());
        fragmentTrans.remove(new alertMap());
        fragmentTrans.commit();
//        Toasty.success(getActivity(),"聯絡人沒事喔", Toast.LENGTH_SHORT, true).show();
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        startActivity(intent);
//        getActivity().finish();

    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
