package rmit.tuong.s3818196;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
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
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ImageView ic_gps, ic_join;
    private Button btnLogin;
    private List<SiteModel> listSites;
  //  private DatabaseHelper databaseHelper = new DatabaseHelper(MapsActivity.this);
  private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private  String username;
    private AutoCompleteTextView mSearchText;

    private GeoApiContext mGeoApiContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseHelper = new DatabaseHelper(MapsActivity.this);
        ic_gps = findViewById(R.id.ic_gps);
        ic_join = findViewById(R.id.ic_login);
        btnLogin = findViewById(R.id.btnLogInMap);
        mSearchText = findViewById(R.id.input_search);
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


        ic_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int siteID = sharedPreferences.getInt("siteId", -1);
               int userID = sharedPreferences.getInt("userID", -1);

                if (!username.isEmpty()){
                    Intent intent1 = new Intent(MapsActivity.this, SiteDetailActivity.class);
                   intent1.putExtra("siteID", siteID);
                     intent1.putExtra("userID", userID);
                    startActivity(intent1);


                } else {
                    Toast.makeText(MapsActivity.this, "Please log in to join this site", Toast.LENGTH_SHORT).show();
                }


            }
        });


        boolean havingAdmin = databaseHelper.checkUsername("admin");

        if(!havingAdmin){
            databaseHelper.createUser("admin", "admin","admin");
        }


        searchFunction();


        checkChanges();
    }

    private void checkChanges() {

        int userId = sharedPreferences.getInt("userID", -100);
        String userID = userId+"";
        if(!userID.equals("")){

            List<SiteModel> siteList = databaseHelper.getAllSiteOfLeader(userID);
            if(siteList.size() > 0){

                for (SiteModel site: siteList) {
                    String siteID = site.getId()+"";
                   int previousNum = site.getNumOfVolunteer();
                   List<UserModel> userVolunteer = databaseHelper.getAllVolunteerOfOneSite(siteID);
                    int latterNum = userVolunteer.size();


                    if(previousNum < latterNum){
                         int diff = latterNum - previousNum;
                          createNotification("There are new "+ diff+" users join site "+site.getName(), siteID,MapsActivity.this, Integer.parseInt(siteID));
                          databaseHelper.updateNumVolunteer(siteID,latterNum);
                    } else if (previousNum > latterNum) {
                        int diff = previousNum - latterNum;
                        createNotification("There are "+ diff+" users leave site "+site.getName(), siteID,MapsActivity.this, Integer.parseInt(siteID));
                        databaseHelper.updateNumVolunteer(siteID,latterNum);
                    }
                }

            }

//            String a = "1, 2, 3, 4,";
//            List<String> s = Arrays.asList(a.split(","));

        //    createNotification("Hello", 1,MapsActivity.this,1);
        }

    }

    private void searchFunction() {

        List<SiteModel> siteSearch = databaseHelper.getAllSite();

        List<String> site = new ArrayList<>();
        for (SiteModel siteEach: siteSearch) {
            site.add(siteEach.getName());
        }


       // AutoCompleteTextView editText = findViewById(R.id.input_search);
        ArrayAdapter<SiteModel> adapter = new ArrayAdapter<SiteModel>(this, android.R.layout.simple_list_item_1,siteSearch);
        mSearchText.setAdapter(adapter);
        mSearchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SiteModel clickedSite = (SiteModel) adapterView.getItemAtPosition(i);
               mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(clickedSite.getLatitude(),clickedSite.getLongitude()),17f));

            }
        });



        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                ){
                    geoLocate();
                   // Toast.makeText(MapsActivity.this, mSearchText.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


    }


//    private LatLng findLocation(String name, List<SiteModel> siteSearch){
//        for (SiteModel siteEach: siteSearch) {
//            if(siteEach.getName().equals(name)){
//               LatLng tmp = new LatLng(siteEach.getLatitude(),siteEach.getLongitude());
//               return tmp;
//            }
//        }
//        return null;
//    }


    private void geoLocate(){
        Log.d(TAG, "geoLocate: geoLocating");
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        } catch (IOException e){
            Log.e(TAG, "geoLocate: IOException"+ e.getMessage());
        }
        if(list.size()>0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location" + address.toString());
       //  Toast.makeText(this, address.getLatitude()+"", Toast.LENGTH_SHORT).show();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()),15));

        }
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


                if (!username.equals("admin")){
                    if ( !username.isEmpty()){
                        Intent intent = new Intent(MapsActivity.this, AddNewSite.class);
                        intent.putExtra("latitude", latLng.latitude);
                        intent.putExtra("longitude", latLng.longitude);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MapsActivity.this, "You need to login to create a new site", Toast.LENGTH_SHORT).show();
                    }
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
    private ClusterManager<SiteModel> clusterManager;

    private void setUpClusterer() {
        // Position the map.
      //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = new ClusterManager<SiteModel>(MapsActivity.this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
       mMap.setOnMarkerClickListener(clusterManager);





        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<SiteModel>() {
            @Override
            public boolean onClusterItemClick(SiteModel item) {
                ic_join.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("siteId", item.getId());
                editor.commit();
                return false;
            }
        });



//        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<SiteModel>() {
//            @Override
//            public void onClusterItemInfoWindowClick(SiteModel item) {
//          //      Toast.makeText(MapsActivity.this, item.getLatitude()+"", Toast.LENGTH_SHORT).show();
//                calculateDirections(item.getLatitude(),item.getLongitude());
//            }
//        });


        clusterManager.getMarkerCollection()
                .setInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this), MapsActivity.this));

        mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
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
            String snippets = "Leader: "+ site.getLeaderName()+"\nNumber of volunteer: "+site.getNumOfVolunteer()
                    +"\nNumber of Tested People: "+site.getNumOfPeopleTested();

            //MyItem offsetItem = new MyItem(site.getLatitude(), site.getLongitude(), site.getName(),snippets);
            clusterManager.addItem(site);
        }



//        }
    }

    private void calculateDirections(double latitudeSite, double longitudeSite){
        Log.d(TAG, "calculateDirections: calculating directions.");

        LatLng mUserPosition = new LatLng(10.73, 106.69);

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                latitudeSite,
                longitudeSite
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        mUserPosition.latitude,
                        mUserPosition.longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }

    private NotificationManager notifManager;
    public void createNotification(String aMessage, String siteID, Context context, int idOfNotif) {
        final int NOTIFY_ID = idOfNotif; // ID of notification
        int userIDExtra = sharedPreferences.getInt("userID", -100);
        String id = context.getString(R.string.app_name); // default_channel_id
        String title = context.getString(R.string.app_name); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, SiteDetailActivity.class);
            intent.putExtra("siteID", Integer.parseInt(siteID) );
            intent.putExtra("userID", userIDExtra);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle("Changes in your site ")                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                     // required
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(aMessage))
                    .setContentText(aMessage)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, SiteDetailActivity.class);
            intent.putExtra("siteID", Integer.parseInt(siteID));
            intent.putExtra("userID", userIDExtra);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle("Changes in your site ")                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    // required
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(aMessage))
                    .setContentText(aMessage) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }



}