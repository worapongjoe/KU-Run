package kusrc.worapong.preyapron.sriwan.kurun;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private boolean gpsABoolean, networkABoolean;
    private double myLatADouble, myLngADouble;
    private String[] resultStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Setup
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        resultStrings = getIntent().getStringArrayExtra("Result");


        //My Loop
        //myLoop();

    }   // Main Method

    private void myLoop() {

        Log.d("5April", "Location = " + myLatADouble + " , " + myLngADouble);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLoop();
            }
        }, 3000);

    } // myLoop

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.removeUpdates(locationListener);
        myLatADouble = 13.668066;
        myLngADouble = 100.622454;

        Location networkLocation = findLocation(LocationManager.NETWORK_PROVIDER, "Cannot Connected Internet");
        if (networkLocation != null) {
            myLatADouble = networkLocation.getLatitude();
            myLngADouble = networkLocation.getLongitude();
        }
        Location gpsLocation = findLocation(LocationManager.GPS_PROVIDER, "No Card GPS");
        if (gpsLocation != null) {
            myLatADouble = gpsLocation.getLatitude();
            myLngADouble = gpsLocation.getLongitude();
        }

    }   // onResume

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location findLocation(String strProvider,
                                 String strError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider,
                    1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("5April", strError);
        }

        return location;
    }


    //Class
    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            myLatADouble = location.getLatitude();
            myLngADouble = location.getLongitude();

        }   // onLocationChange

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        goToCenterMap();

        createAllMarker();

    }   // onMapReady

    private void createAllMarker() {

        mMap.clear();

        //for user
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(myLatADouble, myLngADouble))
                .icon(BitmapDescriptorFactory.fromResource(findIconMarker(resultStrings[7]))));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createAllMarker();
            }
        }, 3000);

    }   // createAllMarker

    private int findIconMarker(String resultString) {

        int intIcon = R.drawable.kon48;
        return intIcon;
    }

    private void goToCenterMap() {

        try {

            LatLng centerLatLng = new LatLng(myLatADouble, myLngADouble);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 16));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // toToCenterMap

}   // Main Class