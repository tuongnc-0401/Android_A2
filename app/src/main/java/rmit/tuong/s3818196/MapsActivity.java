package rmit.tuong.s3818196;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import rmit.tuong.s3818196.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final long UPDATE_INTERVAL = 10 * 1000;
    public static final long FASTEST_INTERVAL = 2 * 1000;
    public static final int MY_PERMISSION_REQUEST_CODE = 99;
    private static final String TAG = "Maps Activity";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    protected FusedLocationProviderClient client;
    protected LocationRequest mLocationRequest;
    private ImageView ic_gps;
    private Button btnLogin;
    private List<SiteModel> listSites;
    private DatabaseHelper databaseHelper = new DatabaseHelper(MapsActivity.this);;
    private SharedPreferences sharedPreferences;
    private  String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ic_gps = findViewById(R.id.ic_gps);
        btnLogin = findViewById(R.id.btnLogInMap);
        sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        if ( !username.isEmpty()){
            btnLogin.setText("Hi, "+sharedPreferences.getString("username", ""));
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MapsActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            });
        }
        ic_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPosition(view);
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
        requestPermission();
        mMap = googleMap;
        client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(10.73, 106.69);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in RMIT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
       // databaseHelper = new DatabaseHelper(MapsActivity.this);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if ( !username.isEmpty()){
                    Intent intent = new Intent(MapsActivity.this, AddNewSite.class);
                    intent.putExtra("latitude", latLng.latitude);
                    intent.putExtra("longitude", latLng.longitude);
                    startActivity(intent);
                } else {
                    Toast.makeText(MapsActivity.this, "You need to login to create a new site", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(@NonNull Marker marker) {
//                Toast.makeText(MapsActivity.this, marker.toString(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        setUpClusterer();

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        client.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                Toast.makeText(MapsActivity.this, "(" + location.getLatitude() + ","
                        + location.getLongitude() +")", Toast.LENGTH_SHORT).show();
            }
        },null);
    }

    private void onLocationChanged(Location lastLocation) {
        String message = "Update Location" +
                Double.toString(lastLocation.getLatitude()) + " "
                + Double.toString(lastLocation.getLatitude());
        LatLng newLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(newLocation).title("New location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
        Toast.makeText(MapsActivity.this, newLocation.toString(), Toast.LENGTH_SHORT).show();

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        },MY_PERMISSION_REQUEST_CODE);
    };

    private void drawCircle(LatLng latLng){
        CircleOptions circleOptions = new CircleOptions().center(latLng)
                .radius(500).fillColor(Color.TRANSPARENT)
                .strokeColor(Color.BLUE);
        mMap.addCircle(circleOptions);
    }

    @SuppressLint("MissingPermission")
    public void getPosition(View view){
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(currentLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(180.0F))
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
                Toast.makeText(MapsActivity.this,"Get the current location!", Toast.LENGTH_SHORT).show();
            }
        });

        // startLocationUpdate();

    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()
                , vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



//    private void listAllSite() {
//        listSites = databaseHelper.getAllSite();
//        Log.d(TAG, "listAllSite: ");
//        for (int i=0; i < listSites.size();i++){
//            SiteModel site = listSites.get(i);
//            LatLng position = new LatLng(site.getLatitude(),site.getLongitude());
//            mMap.addMarker(new MarkerOptions()
//                   .position(position)
//                    .icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_site))
//              );
//
//            if(i == listSites.size()-1){
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
//            }
//        }
//    }


    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> clusterManager;

    private void setUpClusterer() {
        // Position the map.
      //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<MyItem>(MapsActivity.this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        final CustomClusterRenderer renderer = new CustomClusterRenderer(MapsActivity.this, mMap, clusterManager);
        clusterManager.setRenderer(renderer);
        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        listSites = databaseHelper.getAllSite();
        Log.d(TAG, "listAllSite: ");
        for (int i=0; i < listSites.size();i++){
            SiteModel site = listSites.get(i);
            LatLng position = new LatLng(site.getLatitude(),site.getLongitude());
            if(i == listSites.size()-1){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
            }
            MyItem offsetItem = new MyItem(site.getLatitude(), site.getLongitude(), site.getName(),site.getName());
            clusterManager.addItem(offsetItem);
        }



//        }
    }

}